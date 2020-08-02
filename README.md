# 领域建模的贫血模型与充血模型

标签（空格分隔）： 领域建模

---

领域建模是通过识别领域对象与行为来连接与现实世界业务主体与操作的映射关系。对象与行为的组织设计原则更体现面向对象设计的思想，通过聚合、解耦、抽象、组合等多种设计方式达到系统可复用，可维护，易扩展的能力。

在实际程序代码设计中，由于语言、结构、技术的不一样对领域建模代码落地也有所不同，且各有优缺点。

## 一、贫血模型

此种模型下领域对象的作用很简单，只有所有属性的get/set方式，以及少量简单的属性值转换，不包含任何业务逻辑，不关系对象持久化，只是用来做为数据对象的承载和传递的介质。

```java
@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String userId;
    private String userName;
    private String password;
    private boolean isLock;
}

```

而真正的业务逻辑则由领域服务负责实现，此服务引入持久化仓库，在业务逻辑完成之后持久化到仓库中，并在此可以发布领域事件(Domain Event)

``` java
public interface UserService {

    void create(User user);
    void edit(User user);
    void changePassword(String userId, String newPassword);
    void lock(String userId);
    void unlock(String userId);

}

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Override
    public void edit(User user) {
        User dbUser = repo.findById(user.getUserId()).get();
        dbUser.setUserName(user.getUserName());
        repo.save(dbUser);
        // 发布领域事件 ...
    }

    @Override
    public void lock(String userId) {
        User dbUser = repo.findById(userId).get();
        dbUser.setLock(true);
        repo.save(dbUser);
        // 发布领域事件 ...
    }
    
    // ... 省略完整代码
}

```

**优点：** 结构简单，职责单一，相互隔离性好，使用单例模型提高运行性能
**缺点：** 对象状态与行为分离，不能直观地描述领域对象。行为的设计主要考虑参数的输入和输出而非行为本身，不太具有面向对象设计的思考方式。行为间关联性较小，更像是面向过程式的方法，可复用性也较小。

> SpringBoot 采用单例模式，尽量不手动创建对象，对象无状态化，故较推荐使用贫血模型

## 二、 充血模型

此种模型下领域对象作用此领域相关行为，包含此领域相关的业务逻辑，同时也包含对领域对象的持久化操作。

```java
@Entity
@Data
@Builder
@AllArgsConstructor
public class User implements UserService {

    @Id
    private String userId;
    private String userName;
    private String password;
    private boolean isLock;

    // 持久化仓库
    @Transient
    private UserRepository repo;

    // 是否是持久化对象
    @Transient
    private boolean isRepository;

    @PostLoad
    public void per() {
        isRepository = true;
    }

    public User() {
    }

    public User(UserRepository repo) {
        this.repo = repo;
    }

     @Override
    public void create(User user) {
        repo.save(user);
    }

    @Override
    public void edit(User user) {
        if (!isRepository) {
            throw new RuntimeException("用户不存在");
        }

        userName = user.userName;
        repo.save(this);
        // 发布领域事件 ...
    }


    @Override
    public void lock() {
        if (!isRepository) {
            throw new RuntimeException("用户不存在");
        }

        isLock = true;
        repo.save(this);
        // 发布领域事件 ...
    }

}

```

**优点：** 对象自洽程度很高，表达能力很强，因此非常适合于复杂的企业业务逻辑的实现，以及可复用程度比较高，更符合面向对象设计思想
**缺点：** 对象属性中掺杂持久化仓库，不够纯粹，持久化操作是否属于业务逻辑有待求证。但由于持久化仅需暴露接口，对业务逻辑与持久化操作的耦合度有一定降低。
**说明：** 有人认为对象中的`Create()`，是新建对象方法不应该属于对象本身，应由其它对象产生或static方法产生。我的理解是不能把业务对象中的新建和程序对象上的新建混淆。业务对象的新建是指的是业务行为操作得出的结果，理应属于对象本身行为。而程序里的新建则是对象初始化过程`New()`，这是程序构建逻辑不是业务概念，不能相等对待。

> 在领域对象行为逻辑较复杂的情况下，需要多个行为共享对象状态的时候，充血模型表现力更强

## 三、充血模型2
为了解决业务逻辑不纯粹问题，也有将持久化操作移出业务逻辑的作法。
```java
@Entity
@Data
@Builder
@AllArgsConstructor
public class User implements UserService {

    @Id
    private String userId;
    private String userName;
    private String password;
    private boolean isLock;

    // 是否是持久化对象
    @Transient
    private boolean isRepository;

    @Override
    public void create(User user) {
        user.userId = UUID.randomUUID().toString();
    }

    @Override
    public void edit(User user) {
        userName = user.userName;
    }


    @Override
    public void lock() {
        isLock = true;
    }

}


@Service
public class UserManager {

    @Autowired
    private UserRepository repo;

    public User findOne(String userId){
        return repo.findById(userId).get();
    }
    
    public void edit(User u) {
        User user = findOne(u.getUserId());
        user.edit(u);

        repo.save(user);
        // 发布领域事件 ...
    }

    public void lock(String userId) {
        User user = findOne(userId);
        user.lock();
        repo.save(user);
        // 发布领域事件 ...
    }
}

```

**优点：** 保持了业务逻辑的纯粹性，去掉了持久化的入侵
**缺点：** 降低了领域服务的自治性，破坏了行为逻辑的完整性，部分逻辑混入了`application`层，尤其是领域事件的发布

> 此种方式是前两种方式的折中，充分地做到了解耦，但也牺牲了部分内聚

## 四、总结

架构设计是一项持续性演进性的工作，不是一成不变的。架构的选择并没有好坏只有适合，每一种都有自己的使用场景。如何选择需要自身理论支持，保持相对方向性统一，并持续审视是否符合预期目标。

## 五、 源码
[https://gitee.com/hypier/barry-domain][1]


![此处输入图片的描述][2]


  [1]: https://gitee.com/hypier/barry-domain
  [2]: https://oscimg.oschina.net/oscnet/up-8969dabd3beeba071b59e61139a2bb8b22f.JPEG

