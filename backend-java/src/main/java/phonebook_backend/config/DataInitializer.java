package phonebook_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import phonebook_backend.model.User;
import phonebook_backend.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createDefaultUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {

                User user = new User(
                        "admin",
                        "admin@example.com",
                        passwordEncoder.encode("admin123"),
                        "ROLE_ADMIN"
                );

                userRepository.save(user);

                System.out.println("Default admin user created.");
            }
        };
    }
}