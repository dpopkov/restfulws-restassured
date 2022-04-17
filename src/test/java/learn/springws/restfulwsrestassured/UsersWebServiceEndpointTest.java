package learn.springws.restfulwsrestassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static learn.springws.restfulwsrestassured.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsersWebServiceEndpointTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = SERVER_PORT;
    }

    @Test
    void testUserLogin() {
        Map<String, Object> loginDetails = Map.of(
                "email", "jane3@example.org",
                "password", "123"
        );
        final Response response =
                    given()
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(loginDetails)
                    .when()
                        .post(CONTEXT_PATH + "/users/login")
                    .then()
                        .statusCode(STATUS_OK)
                        .extract()
                        .response();
        final String userId = response.header("UserId");
        assertNotNull(userId);
        final String authorization = response.header("Authorization");
        assertNotNull(authorization);
        assertTrue(authorization.startsWith("Bearer "));
    }
}
