package africa.techimmortal.martbackend.infrastructure.security;

import africa.techimmortal.martbackend.infrastructure.security.filters.MartAuthorizationFilter;
import africa.techimmortal.martbackend.infrastructure.exception.handler.CustomAuthenticationFailureHandler;
import africa.techimmortal.martbackend.infrastructure.security.filters.MartAuthenticationFilter;
import africa.techimmortal.martbackend.core.service.BioDataService;
import africa.techimmortal.martbackend.core.utils.JwtUtility;
import africa.techimmortal.martbackend.portfolio.admin.service.AdminService;
import africa.techimmortal.martbackend.portfolio.customer.service.CustomerService;
import africa.techimmortal.martbackend.portfolio.dispatchRider.service.DispatchRiderService;
import africa.techimmortal.martbackend.portfolio.vendor.service.VendorService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static africa.techimmortal.martbackend.core.domain.enums.Role.ORDINARY_ADMIN;
import static africa.techimmortal.martbackend.core.domain.enums.Role.SUPER_ADMIN;
import static africa.techimmortal.martbackend.core.domain.enums.Role.VENDOR;
import static africa.techimmortal.martbackend.core.utils.SecurityUtils.LOGIN_ENDPOINT;
import static africa.techimmortal.martbackend.core.utils.SecurityUtils.getAuthWhiteList;
import static africa.techimmortal.martbackend.core.utils.SecurityUtils.getGetUrlWhiteList;
import static africa.techimmortal.martbackend.core.utils.SecurityUtils.getPostUrlWhiteList;
import static africa.techimmortal.martbackend.core.utils.SecurityUtils.getProductsAuthUrl;
import static africa.techimmortal.martbackend.core.utils.SecurityUtils.getVendorApprovalUrl;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final JwtUtility jwtUtil;
    private final BioDataService bioDataService;
    private final DispatchRiderService dispatchRiderService;
    private final AdminService adminService;
    private final VendorService vendorService;
    private final CustomerService customerService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        UsernamePasswordAuthenticationFilter authenticationFilter = new MartAuthenticationFilter(
                authenticationManager, jwtUtil, bioDataService, dispatchRiderService,
                adminService, vendorService, customerService
        );

        authenticationFilter.setFilterProcessesUrl(LOGIN_ENDPOINT);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new MartAuthorizationFilter(jwtUtil), MartAuthenticationFilter.class)
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exceptionHandler -> exceptionHandler
                                .authenticationEntryPoint(customAuthenticationFailureHandler::onAuthenticationFailure)
                )
                .authorizeHttpRequests(c -> c
                        .requestMatchers(getAuthWhiteList())
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, getPostUrlWhiteList())
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, getGetUrlWhiteList())
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, getProductsAuthUrl()).hasAnyAuthority(VENDOR.name(), SUPER_ADMIN.name(), ORDINARY_ADMIN.name())
                        .requestMatchers(HttpMethod.POST, getVendorApprovalUrl()).hasAnyAuthority(ORDINARY_ADMIN.name(), SUPER_ADMIN.name())
                        .anyRequest()
                        .authenticated())
                .build();
    }



    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .exposedHeaders(
                                "Access-Control-Allow-Origin",
                                "Access-Control-Allow-Methods",
                                "Access-Control-Allow-Headers"
                        )
                        .allowCredentials(true);
            }
        };
    }

}
