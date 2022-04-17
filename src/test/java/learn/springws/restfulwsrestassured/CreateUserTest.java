package learn.springws.restfulwsrestassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static learn.springws.restfulwsrestassured.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreateUserTest {

    private static final int PUBLIC_ID_EXPECTED_LENGTH = 32;
    private static final int USER_ID_EXPECTED_LENGTH = 32;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = SERVER_PORT;
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
        Map<String, Object> billingAddress = Map.of(
                "city", "Vancouver",
                "country", "Canada",
                "streetName", "123 Street",
                "postalCode", "12345",
                "type", "billing"
        );
        List<Map<String, Object>> userAddresses = List.of(shippingAddress, billingAddress);
        Map<String, Object> userDetails = Map.of(
                "firstName", "Jane",
                "lastName", "Doe",
                "email", "jane3@example.org",
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
                        .statusCode(STATUS_OK)
                        .contentType(APPLICATION_JSON)
                        .extract()
                        .response();
        final String userId = response.jsonPath().getString("userId");
        assertNotNull(userId);
        assertEquals(USER_ID_EXPECTED_LENGTH, userId.length());

        String bodyString = response.body().asString();
        try {
            JSONObject responseBodyJson = new JSONObject(bodyString);
            JSONArray addresses = responseBodyJson.getJSONArray("addresses");
            assertNotNull(addresses);
            assertEquals(userAddresses.size(), addresses.length());
            String addressPublicId = addresses.getJSONObject(0).getString("publicId");
            assertNotNull(addressPublicId);
            assertEquals(PUBLIC_ID_EXPECTED_LENGTH, addressPublicId.length());
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }
}
