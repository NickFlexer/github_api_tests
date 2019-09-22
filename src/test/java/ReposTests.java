import com.jayway.restassured.response.Response;
import factories.HttpRequestFactory;
import helpers.JsonHelper;
import helpers.JsonPathHelper;
import org.junit.jupiter.api.Test;
import request.HttpRequest;
import utils.ConfigProperties;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.StatusCodes.*;

class ReposTests {

    private HttpRequestFactory requestFactory;

    ReposTests() {
        requestFactory = new HttpRequestFactory(ConfigProperties.getProperty("baseUrl"), ConfigProperties.getProperty("port"));
    }

    @Test
    void getUserRepos() {
        HttpRequest request = requestFactory.getRequestWithoutAuthorization();
        Response response = request.get("/users/octocat/repos");

        assertEquals(ST_OK, response.getStatusCode(), "Неверный статус-код");
        assertEquals(8, JsonPathHelper.getJsonPathResultCount(response, "$..full_name"));
    }

    @Test
    void createAndDeleteRepo() {
        HttpRequest request = requestFactory.getRequestWithDefaultAuthorization();

        // Получить количество репозиториев изначально
        Response response = request.get("/user/repos");

        assertEquals(ST_OK, response.getStatusCode(), "Неверный статус-код");

        int startReposCount = JsonPathHelper.getJsonPathResultCount(response, "$..full_name");

        // Создать новый репозиторий
        final String newRepoName = "API_test_repository";

        response = request.post(
                "/user/repos",
                JsonHelper.generateJsonBody(new HashMap<String, String>(){
                    {
                        put("owner", ConfigProperties.getProperty("username"));
                        put("name", newRepoName);
                        put("description", "Created by github api");
                        put("private", "false");
                    }
                }));

        assertEquals(ST_CREATED, response.getStatusCode(), "Неверный статус-код");

        // Проверить, что количество репозиториев увеличелось
        response = request.get("/user/repos");

        assertEquals(ST_OK, response.getStatusCode(), "Неверный статус-код");
        assertEquals(
                startReposCount + 1,
                JsonPathHelper.getJsonPathResultCount(response, "$..full_name"),
                "Неверное количество репозиториев");

        // Удалить созданый репозиторий
        response = request.delete(String.format("/repos/%s/%s", ConfigProperties.getProperty("username"), newRepoName));

        assertEquals(ST_NO_CONTENT, response.getStatusCode(), "Неверный статус-код");
    }

    @Test
    void noAuthorization() {
        HttpRequest request = requestFactory.getRequestWithoutAuthorization();
        Response response = request.get("/user/repos");

        assertEquals(ST_UNAUTHORIZED, response.getStatusCode(), "Неверный статус-код");
        assertEquals(
                "Requires authentication",
                JsonPathHelper.read(response, "$.message"),
                "Неверное сообщение об ошибке");

    }
}
