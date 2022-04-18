package learn.springws.restfulwsrestassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static learn.springws.restfulwsrestassured.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreateUserTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = SERVER_PORT;
    }

    @Test
    void testCreateUser() {
        Map<String, Object> userDetails = Map.of(
                "firstName", USER_FIRST_NAME,
                "lastName", USER_LAST_NAME,
                "email", EMAIL,
                "password", PASSWORD,
                "addresses", USER_ADDRESSES
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
        assertEquals(USER_ID_LENGTH, userId.length());

        String bodyString = response.body().asString();
        try {
            JSONObject responseBodyJson = new JSONObject(bodyString);
            JSONArray addresses = responseBodyJson.getJSONArray("addresses");
            assertNotNull(addresses);
            assertEquals(USER_ADDRESSES.size(), addresses.length());
            String addressPublicId = addresses.getJSONObject(0).getString("publicId");
            assertNotNull(addressPublicId);
            assertEquals(PUBLIC_ID_LENGTH, addressPublicId.length());
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }
}
