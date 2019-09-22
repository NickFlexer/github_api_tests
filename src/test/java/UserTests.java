import com.jayway.restassured.response.Response;
import factories.HttpRequestFactory;
import helpers.JsonHelper;
import helpers.JsonPathHelper;
import org.junit.jupiter.api.Test;
import request.HttpRequest;
import utils.ConfigProperties;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.StatusCodes.ST_OK;
import static testdata.StatusCodes.ST_UNAUTHORIZED;

class UserTests {

    private HttpRequestFactory requestFactory;

    UserTests() {
        requestFactory = new HttpRequestFactory(ConfigProperties.getProperty("baseUrl"), ConfigProperties.getProperty("port"));
    }

    @Test
    void getUserData() {
        HttpRequest request = requestFactory.getRequestWithoutAuthorization();
        Response response = request.get("/users/NickFlexer");

        assertEquals(ST_OK, response.getStatusCode(), "Неверный статус-код");
        assertEquals("NickFlexer", JsonPathHelper.read(response, "$.login"), "Неверный username");
    }

    @Test
    void getAuthorizedUserData() {
        HttpRequest request = requestFactory.getRequestWithDefaultAuthorization();
        Response response = request.get("/user");

        assertEquals(ST_OK, response.getStatusCode(), "Неверный статус-код");
    }

    @Test
    void updateUserData() {
        final String newLocation = "Podlipki-Dachny";

        HttpRequest request = requestFactory.getRequestWithDefaultAuthorization();
        Response response = request.patch(
                "/user",
                JsonHelper.generateJsonBody(new HashMap<String, String>() {
                    {
                        put("location", newLocation);
                    }
                }));

        assertEquals(ST_OK, response.getStatusCode(), "Неверный статус-код");
        assertEquals(
                newLocation,
                JsonPathHelper.read(response, "$.location"),
                "Неверное значение location");
    }

    @Test
    void wrongAuthorization() {
        HttpRequest request = requestFactory.getRequestWithCustomAuthorization("NickFlexer", "111");
        Response response = request.get("/user");

        assertEquals(ST_UNAUTHORIZED, response.getStatusCode(), "Неверный статус-код");
        assertEquals(
                "Bad credentials",
                JsonPathHelper.read(response, "$.message"),
                "Неверное сообщение об ошибке");
    }

    @Test
    void noAuthorization() {
        HttpRequest request = requestFactory.getRequestWithoutAuthorization();
        Response response = request.get("/user");

        assertEquals(ST_UNAUTHORIZED, response.getStatusCode(), "Неверный статус-код");
        assertEquals(
                "Requires authentication",
                JsonPathHelper.read(response, "$.message"),
                "Неверное сообщение об ошибке");
    }
}
