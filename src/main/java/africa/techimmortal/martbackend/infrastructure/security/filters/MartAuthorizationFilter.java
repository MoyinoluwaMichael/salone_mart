package africa.techimmortal.martbackend.infrastructure.security.filters;

import africa.techimmortal.martbackend.infrastructure.exception.AuthenticationException;
import africa.techimmortal.martbackend.core.utils.JwtUtility;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static africa.techimmortal.martbackend.core.utils.AppUtils.*;
import static africa.techimmortal.martbackend.core.utils.SecurityUtils.getAuthWhiteList;
import static africa.techimmortal.martbackend.core.utils.SecurityUtils.getGetUrlWhiteList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
@RequiredArgsConstructor
@Slf4j
public class MartAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        logRequestDetails(wrappedRequest);

        Set<String> authWhiteList = new HashSet<>(Arrays.asList(getAuthWhiteList()));
        Set<String> getUrlWhiteList = new HashSet<>(Arrays.asList(getGetUrlWhiteList()));
        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean isPathInAuthWhitelist =
                (authWhiteList.stream().anyMatch(pattern -> pathMatcher.match(pattern, request.getServletPath())) &&
                        request.getMethod().equals(HttpMethod.POST.name())) ||
                        (getUrlWhiteList.stream().anyMatch(pattern -> pathMatcher.match(pattern, request.getServletPath())) &&
                                request.getMethod().equals(HttpMethod.GET.name()));
        System.err.println("isPathInAuthWhitelist: "+request.getServletPath()+"> "+isPathInAuthWhitelist);
        if (isPathInAuthWhitelist) filterChain.doFilter(request, wrappedResponse);
        else authorizeRequest(request, wrappedResponse, filterChain);
        logResponseDetails(wrappedResponse);
        wrappedResponse.copyBodyToResponse();
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) throws IOException {
        StringBuilder requestDetails = new StringBuilder();
        requestDetails.append("Request Method: ").append(request.getMethod()).append("\n");
        requestDetails.append("Request URI: ").append(request.getRequestURI()).append("\n");
        requestDetails.append("Request Body: ").append(new String(request.getContentAsByteArray(), request.getCharacterEncoding()));
        log.info("Request Details: {}", requestDetails);
    }

    private void logResponseDetails(ContentCachingResponseWrapper response) throws IOException {
        StringBuilder responseDetails = new StringBuilder();
        responseDetails.append("Response Status: ").append(response.getStatus()).append("\n");
        responseDetails.append("Response Headers: ").append(response.getHeaderNames().stream()
                .map(headerName -> headerName + ": " + response.getHeader(headerName))
                .collect(Collectors.joining(", "))).append("\n");
        responseDetails.append("Response Body: ").append(new String(response.getContentAsByteArray(), response.getCharacterEncoding()));
        log.info("Response Details: {}", responseDetails);
    }

    private void authorizeRequest(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            authorize(request);
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            log.info("Mart Authorization Exception {}", exception.getMessage());
            Map<String, String> errors = new HashMap<>();
            errors.put(ERROR_VALUE, exception.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            mapper.writeValue(response.getOutputStream(), errors);
        }
    }

    private void authorize(HttpServletRequest request) throws AuthenticationException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        boolean isValidAuthorizationHeader = authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX);
        if (isValidAuthorizationHeader) {
            String token = parseTokenFrom(authorizationHeader);
            authorize(token);
        }
    }

    private String parseTokenFrom(String authorizationHeader) {
        return authorizationHeader.substring(TOKEN_PREFIX.length());
    }

    private void authorize(String token) throws AuthenticationException {
        Map<String, Claim> map = jwtUtil.extractClaimsFrom(token);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Claim claim = map.get(ROLES_VALUE);
        addClaimToUserAuthorities(authorities, claim);
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static void addClaimToUserAuthorities(List<SimpleGrantedAuthority> authorities, Claim claim) {
        for (int i = 0; i < claim.asMap().size(); i++) {
            String role = claim.asMap().get(CLAIM_VALUE + (i + 1)).toString();
            authorities.add(new SimpleGrantedAuthority(role));
        }
    }


}
