package africa.springCore.martbackend.core.portfolio.product.domain.repository;

import africa.springCore.martbackend.common.enums.Role;
import africa.springCore.martbackend.core.base.domain.model.BioData;
import africa.springCore.martbackend.portfolio.admin.domain.model.Admin;
import africa.springCore.martbackend.portfolio.admin.domain.repository.AdminRepository;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductRepository;
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
