package request;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;

import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

public class HttpRequest {

    private RequestSpecification specification;

    public HttpRequest(RequestSpecification specification) {
        this.specification = specification;
    }

    public Response get(String resource) {
        Response response = given()
                .spec(specification)
                .when()
                .get(resource);

        response.then().log().all();

        return response;
    }

    public Response patch(String resource, String jsonBody)  {
        Response response = given()
                .spec(specification)
                .body(jsonBody)
                .when()
                .patch(resource);

        response.then().log().all();

        return response;
    }

    public Response post(String resource, String jsonBody) {
        Response response = given()
                .spec(specification)
                .body(jsonBody)
                .when()
                .post(resource);

        response.then().log().all();

        return response;
    }

    public Response delete(String resource) {
        Response response = given()
                .spec(specification)
                .when()
                .delete(resource);

        response.then().log().all();

        return response;
    }
}
