package fun.barryhome.anaemic.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 2020/8/2 4:48 下午
 *
 * @author barry
 * Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Override
    public void create(User user) {
        repo.save(user);
    }

    @Override
    public void edit(User user) {
        User dbUser = repo.findById(user.getUserId()).get();
        dbUser.setUserName(user.getUserName());
        repo.save(dbUser);
    }

    @Override
    public void changePassword(String userId, String newPassword) {
        User dbUser = repo.findById(userId).get();
        dbUser.setPassword(newPassword);
        repo.save(dbUser);
    }

    @Override
    public void lock(String userId) {
        User dbUser = repo.findById(userId).get();
        dbUser.setLock(true);
        repo.save(dbUser);
    }

    @Override
    public void unlock(String userId) {
        User dbUser = repo.findById(userId).get();
        dbUser.setLock(false);
        repo.save(dbUser);
    }
}
