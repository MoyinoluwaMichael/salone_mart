package africa.techimmortal.martbackend.core.portfolio.product.domain.repository;

import africa.techimmortal.martbackend.core.domain.enums.Role;
import africa.techimmortal.martbackend.core.domain.model.BioData;
import africa.techimmortal.martbackend.portfolio.admin.domain.model.Admin;
import africa.techimmortal.martbackend.portfolio.admin.domain.repository.AdminRepository;
import africa.techimmortal.martbackend.portfolio.product.domain.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test() {
        BioData bioData = new BioData();
        bioData.setEmailAddress("ogunsmoyin.m@gmail.com");
        bioData.setPassword(passwordEncoder.encode("password"));
        bioData.setFirstName("Salone");
        bioData.setLastName("Mart");
        bioData.setRoles(Collections.singletonList(Role.SUPER_ADMIN));
        Admin admin = new Admin();
        admin.setBioData(bioData);
        System.out.println(adminRepository.save(admin));
        System.out.println(adminRepository.findAll());
    }
}
