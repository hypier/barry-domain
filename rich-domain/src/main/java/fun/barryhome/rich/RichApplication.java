package fun.barryhome.rich;

import fun.barryhome.rich.application.UserManager;
import fun.barryhome.rich.domain.User;
import fun.barryhome.rich.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class RichApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RichApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {



    }
}
