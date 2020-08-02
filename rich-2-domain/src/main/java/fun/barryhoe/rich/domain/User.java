package fun.barryhoe.rich.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import java.util.UUID;

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


    @Override
    public void create(User user) {
        user.userId = UUID.randomUUID().toString();
    }

    @Override
    public void edit(User user) {
        userName = user.userName;
    }

    @Override
    public void changePassword(String newPassword) {
        password = newPassword;
    }

    @Override
    public void lock() {
        isLock = true;
    }

    @Override
    public void unlock() {
        isLock = false;
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
