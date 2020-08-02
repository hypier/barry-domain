package fun.barryhome.rich.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

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
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    @Transient
    private UserRepository repo;

    // 是否是持久化对象
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
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
    public User load(String userId) {
        if (repo == null) {
            throw new RuntimeException("持久化仓库不存在");
        }

        User user = repo.findById(userId).get();
        user.isRepository = true;
        user.setRepo(repo);
        return user;
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
    }

    @Override
    public void changePassword(String newPassword) {
        if (!isRepository) {
            throw new RuntimeException("用户不存在");
        }

        password = newPassword;
        repo.save(this);
    }

    @Override
    public void lock() {
        if (!isRepository) {
            throw new RuntimeException("用户不存在");
        }

        isLock = true;
        repo.save(this);
    }

    @Override
    public void unlock() {
        if (!isRepository) {
            throw new RuntimeException("用户不存在");
        }

        isLock = false;
        repo.save(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", lock=" + isLock +
                '}';
    }
}
