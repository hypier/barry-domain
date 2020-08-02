package fun.barryhoe.rich.application;


import fun.barryhoe.rich.domain.User;
import fun.barryhoe.rich.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 2020/8/2 1:44 下午
 *
 * @author barry
 * Description:
 */
@Service
public class UserManager {

    @Autowired
    private UserRepository repo;

    public User findOne(String userId){
        return repo.findById(userId).get();
    }

    public void create(User u) {
        User user = new User();
        user.create(u);
        repo.save(user);
    }

    public void edit(User u) {
        User user = findOne(u.getUserId());
        user.edit(u);

        repo.save(user);
    }

    public void changePassword(String userId, String newPassword) {
        User user = findOne(userId);
        user.changePassword(newPassword);
        repo.save(user);
    }

    public void lock(String userId) {
        User user = findOne(userId);
        user.lock();
        repo.save(user);

    }

    public void unlock(String userId) {
        User user = findOne(userId);
        user.unlock();
        repo.save(user);
    }
}
