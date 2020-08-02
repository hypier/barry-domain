package fun.barryhome.anaemic.application;


import fun.barryhome.anaemic.domain.User;
import fun.barryhome.anaemic.domain.UserRepository;
import fun.barryhome.anaemic.domain.UserService;
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
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public User findOne(String userId){
        return userRepository.getOne(userId);
    }

    public void create(User u) {
        userService.create(u);
    }

    public void edit(User u) {
        userService.edit(u);
    }

    public void changePassword(String userId, String newPassword) {
        userService.changePassword(userId, newPassword);
    }

    public void lock(String userId) {
        userService.lock(userId);
    }

    public void unlock(String userId) {
        userService.unlock(userId);
    }
}
