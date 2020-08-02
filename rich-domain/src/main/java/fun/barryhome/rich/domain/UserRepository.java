package fun.barryhome.rich.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 2020/8/2 12:11 下午
 *
 * @author barry
 * Description:
 */
public interface UserRepository extends JpaRepository<User, String> {
}
