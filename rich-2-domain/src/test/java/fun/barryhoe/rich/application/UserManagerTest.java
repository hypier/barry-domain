package fun.barryhoe.rich.application;

import fun.barryhoe.rich.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created on 2020/8/2 6:16 下午
 *
 * @author barry
 * Description:
 */
@SpringBootTest
class UserManagerTest {

    @Autowired
    private UserManager userManager;

    @Test
    void create() {
        String userId = UUID.randomUUID().toString();
        User u = User.builder()
                .userId(userId)
                .userName("abc")
                .isLock(true)
                .build();

        userManager.create(u);
    }

    @Test
    void findOne() {
        String userId = "95902a3b-2c1a-40fd-9d25-a808f040afa0";
        System.out.println(userManager.findOne(userId));
    }

    @Test
    void lock(){
        String userId = "95902a3b-2c1a-40fd-9d25-a808f040afa0";
        userManager.lock(userId);
    }

    @Test
    void unlock(){
        String userId = "95902a3b-2c1a-40fd-9d25-a808f040afa0";
        userManager.unlock(userId);
    }
}