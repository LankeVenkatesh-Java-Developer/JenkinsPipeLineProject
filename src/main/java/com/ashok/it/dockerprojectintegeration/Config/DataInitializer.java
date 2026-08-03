package com.ashok.it.dockerprojectintegeration.Config;

import com.ashok.it.dockerprojectintegeration.Model.User;
import com.ashok.it.dockerprojectintegeration.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Initializing default admin user...");
            
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            
            userRepository.save(admin);
            
            log.info("Default admin user created: username=admin, password=admin123");
            
            // Create a default user as well
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@example.com");
            user.setRole(User.Role.USER);
            user.setEnabled(true);
            
            userRepository.save(user);
            
            log.info("Default user created: username=user, password=user123");
        } else {
            log.info("Users already exist, skipping initialization");
        }
    }
}
