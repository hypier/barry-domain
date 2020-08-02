package fun.barryhome.rich.application;

import fun.barryhome.rich.domain.User;
import fun.barryhome.rich.domain.UserRepository;
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
        return repo.getOne(userId);
    }

    public void create(User u) {
        User user = new User(repo);
        user.create(u);
    }

    public void edit(User u) {
        User user = new User(repo);
        user.load(u.getUserId()).edit(u);
    }

    public void changePassword(String userId, String newPassword) {
        User user = new User(repo);
        user.load(userId).changePassword(newPassword);
    }

    public void lock(String userId) {
        User user = new User(repo);
        user.load(userId).lock();

    }

    public void unlock(String userId) {
        User user = new User(repo);
        user.load(userId).unlock();
    }
}
