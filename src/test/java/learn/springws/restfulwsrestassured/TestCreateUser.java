package learn.springws.restfulwsrestassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class TestCreateUser {

    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTEXT_PATH = "/photo-app";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8888;
    }

    @Test
    void testCreateUser() {
        Map<String, Object> shippingAddress = Map.of(
                "city", "Vancouver",
                "country", "Canada",
                "streetName", "123 Street",
                "postalCode", "12345",
                "type", "shipping"
        );
        List<Map<String, Object>> userAddresses = List.of(shippingAddress);
        Map<String, Object> userDetails = Map.of(
                "firstName", "Jane",
                "lastName", "Doe",
                "email", "jane1@example.org",
                "password", "123",
                "addresses", userAddresses
        );
        final Response response =
                given()
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(userDetails)
                .when()
                        .post(CONTEXT_PATH + "/users")
                .then()
                        .statusCode(200)
                        .contentType(APPLICATION_JSON)
                        .extract()
                        .response();
        final String userId = response.jsonPath().getString("userId");
        assertNotNull(userId);
    }
}
