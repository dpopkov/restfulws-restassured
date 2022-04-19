package learn.springws.restfulwsrestassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static learn.springws.restfulwsrestassured.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersWebServiceEndpointTest {

    private static String userId;
    private static String authorization;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = SERVER_PORT;
    }

    @Order(1)
    @Test
    void testUserLogin() {
        Map<String, Object> loginDetails = Map.of(
                "email", EMAIL,
                "password", PASSWORD
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
        userId = response.header("UserId");
        assertNotNull(userId);
        authorization = response.header("Authorization");
        assertNotNull(authorization);
        assertTrue(authorization.startsWith("Bearer "));
    }

    @Order(2)
    @Test
    void testGetUser() {
        final Response response =
                given()
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .header("Authorization", authorization)
                    .pathParam("id", userId)
                .when()
                    .get(CONTEXT_PATH + "/users/{id}")
                .then()
                    .statusCode(STATUS_OK)
                    .contentType(APPLICATION_JSON)
                    .extract()
                    .response();
        String receivedUserId = response.jsonPath().getString("userId");
        assertEquals(userId, receivedUserId);
        String email = response.jsonPath().getString("email");
        assertEquals(EMAIL, email);
        String firstName = response.jsonPath().getString("firstName");
        assertEquals(USER_FIRST_NAME, firstName);
        String lastName = response.jsonPath().getString("lastName");
        assertEquals(USER_LAST_NAME, lastName);

        List<Map<String, String>> addresses = response.jsonPath().getList("addresses");
        assertNotNull(addresses);
        assertEquals(USER_ADDRESSES.size(), addresses.size());
        String addressId = addresses.get(0).get("publicId");
        assertNotNull(addressId);
        assertEquals(PUBLIC_ID_LENGTH, addressId.length());
    }

    @Order(3)
    @Test
    void testUpdateUser() {
        final String firstNameUpdated = USER_FIRST_NAME + " upd";
        final String lastNameUpdated = USER_LAST_NAME + " upd";
        Map<String, Object> userDetails = Map.of(
                "firstName", firstNameUpdated,
                "lastName", lastNameUpdated
        );
        final Response response =
                given()
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .header("Authorization", authorization)
                        .pathParam("id", userId)
                        .body(userDetails)
                .when()
                        .put(CONTEXT_PATH + "/users/{id}")
                .then()
                        .statusCode(STATUS_OK)
                        .contentType(APPLICATION_JSON)
                        .extract()
                        .response();
        String receivedUserId = response.jsonPath().getString("userId");
        assertEquals(userId, receivedUserId);
        String email = response.jsonPath().getString("email");
        assertEquals(EMAIL, email);
        String firstName = response.jsonPath().getString("firstName");
        assertEquals(firstNameUpdated, firstName);
        String lastName = response.jsonPath().getString("lastName");
        assertEquals(lastNameUpdated, lastName);

        List<Map<String, String>> addresses = response.jsonPath().getList("addresses");
        assertNotNull(addresses);
        assertEquals(USER_ADDRESSES.size(), addresses.size());
        String addressId = addresses.get(0).get("publicId");
        assertNotNull(addressId);
        assertEquals(PUBLIC_ID_LENGTH, addressId.length());
    }
}
