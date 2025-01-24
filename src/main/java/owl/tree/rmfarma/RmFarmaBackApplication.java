package owl.tree.rmfarma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class RmFarmaBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(RmFarmaBackApplication.class, args);
    }

}
