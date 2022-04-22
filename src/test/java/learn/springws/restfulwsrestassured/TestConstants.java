package learn.springws.restfulwsrestassured;

import java.util.List;
import java.util.Map;

public class TestConstants {
    public static final String BASE_URI = "http://localhost";
    public static final int SERVER_PORT = 8888;
    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTEXT_PATH = "/photo-app";
    public static final int STATUS_OK = 200;

    public static final String USER_FIRST_NAME = "Jane";
    public static final String USER_LAST_NAME = "Doe";
    public static final String EMAIL = "jane3@example.org";
    public static final String PASSWORD = "123";
    public static final String ADMIN_EMAIL = "admin@example.org";
    public static final String ADMIN_PASSWORD = "admin";
    public static final int PUBLIC_ID_LENGTH = 32;
    public static final int USER_ID_LENGTH = 32;

    private static final Map<String, Object> SHIPPING_ADDRESS = Map.of(
            "city", "Vancouver",
            "country", "Canada",
            "streetName", "123 Street",
            "postalCode", "12345",
            "type", "shipping"
    );
    private static final Map<String, Object> BILLING_ADDRESS = Map.of(
            "city", "Vancouver",
            "country", "Canada",
            "streetName", "123 Street",
            "postalCode", "12345",
            "type", "billing"
    );
    public static final List<Map<String, Object>> USER_ADDRESSES = List.of(SHIPPING_ADDRESS, BILLING_ADDRESS);
}
