package fun.barryhoe.rich.domain;

/**
 * Created on 2020/8/2 12:01 下午
 *
 * @author barry
 * Description:
 */
public interface UserService {

    void create(User user);
    void edit(User user);
    void changePassword(String newPassword);
    void lock();
    void unlock();

}
