package africa.springCore.martbackend.core.utils;

public class SecurityUtils {

    public static final String LOGIN_ENDPOINT = "/api/v1/login";
    public static final String PRODUCT_CATEGORY_ENDPOINT = "/api/v1/products/categories";

    public static String[] getAuthWhiteList() {
        return new String[]{
                "/api/v1/health-check",
                LOGIN_ENDPOINT,
                PRODUCT_CATEGORY_ENDPOINT,
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "webjars/**",
                "/swagger-ui.html"
        };
    }

    public static String[] getPostUrlWhiteList() {
        return new String[]{
                "/api/v1/customers",
                "/api/v1/dispatch-riders",
                "/api/v1/vendors"
        };
    }

    public static String[] getGetUrlWhiteList() {
        return new String[]{
                "/api/v1/products",
                "/api/v1/products/*"
        };
    }

    public static String[] getVendorApprovalUrl() {
        return new String[]{
                "/api/v1/vendors/*/approval"
        };
    }

    public static String[] getProductsAuthUrl() {
        return new String[]{
                "/api/v1/vendors/*/products"
        };
    }

}
