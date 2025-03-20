package africa.springCore.martbackend.infrastructure.configuration.datainitializer;

import africa.springCore.martbackend.common.enums.Role;
import africa.springCore.martbackend.core.base.domain.model.BioData;
import africa.springCore.martbackend.portfolio.admin.domain.model.Admin;
import africa.springCore.martbackend.portfolio.admin.domain.repository.AdminRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SuperAdminInitializer {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String email = "ogunsmoyin.m@gmail.com";
        Optional<Admin> existingAdmin = adminRepository.findByBioData_EmailAddress(email);
        if (existingAdmin.isEmpty()) {
            BioData bioData = new BioData();
            bioData.setEmailAddress(email);
            bioData.setPassword(passwordEncoder.encode("password"));
            bioData.setFirstName("Salone");
            bioData.setLastName("Mart");
            bioData.setRoles(Collections.singletonList(Role.SUPER_ADMIN));
            Admin admin = new Admin();
            admin.setBioData(bioData);
            adminRepository.save(admin);
            log.info("Super admin initialized with email: {}", email);
        } else {
            log.info("Super admin with email: {} already exists", email);
        }
    }
}
