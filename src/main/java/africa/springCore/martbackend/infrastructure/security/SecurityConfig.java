package africa.springCore.martbackend.infrastructure.security;

import africa.springCore.martbackend.infrastructure.security.filters.MartAuthorizationFilter;
import africa.springCore.martbackend.infrastructure.exception.handler.CustomAuthenticationFailureHandler;
import africa.springCore.martbackend.infrastructure.security.filters.MartAuthenticationFilter;
import africa.springCore.martbackend.core.base.service.BioDataService;
import africa.springCore.martbackend.common.utils.JwtUtility;
import africa.springCore.martbackend.portfolio.admin.service.AdminService;
import africa.springCore.martbackend.portfolio.customer.service.CustomerService;
import africa.springCore.martbackend.portfolio.dispatchRider.service.DispatchRiderService;
import africa.springCore.martbackend.portfolio.vendor.service.VendorService;
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

import static africa.springCore.martbackend.common.enums.Role.ORDINARY_ADMIN;
import static africa.springCore.martbackend.common.enums.Role.SUPER_ADMIN;
import static africa.springCore.martbackend.common.enums.Role.VENDOR;
import static africa.springCore.martbackend.common.utils.SecurityUtils.LOGIN_ENDPOINT;
import static africa.springCore.martbackend.common.utils.SecurityUtils.getAuthWhiteList;
import static africa.springCore.martbackend.common.utils.SecurityUtils.getGetUrlWhiteList;
import static africa.springCore.martbackend.common.utils.SecurityUtils.getPostUrlWhiteList;
import static africa.springCore.martbackend.common.utils.SecurityUtils.getProductsAuthUrl;
import static africa.springCore.martbackend.common.utils.SecurityUtils.getVendorApprovalUrl;

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
                        .requestMatchers(HttpMethod.POST, getProductsAuthUrl()).hasAnyAuthority(VENDOR.name())
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
