package fun.barryhome.anaemic.domain;

/**
 * Created on 2020/8/2 12:01 下午
 *
 * @author barry
 * Description:
 */
public interface UserService {

    void create(User user);
    void edit(User user);
    void changePassword(String userId, String newPassword);
    void lock(String userId);
    void unlock(String userId);

}
