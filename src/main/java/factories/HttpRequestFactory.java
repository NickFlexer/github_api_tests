package factories;

import com.jayway.restassured.authentication.AuthenticationScheme;
import com.jayway.restassured.authentication.BasicAuthScheme;
import com.jayway.restassured.authentication.PreemptiveBasicAuthScheme;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import request.HttpRequest;
import utils.ConfigProperties;

public class HttpRequestFactory {

    private String baseUrl;
    private int port;

    public HttpRequestFactory(String baseUrl, String port) {
        this.baseUrl = baseUrl;
        this.port = Integer.parseInt(port);
    }

    public HttpRequest getRequestWithoutAuthorization() {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri("https://" + baseUrl)
                .setPort(port)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .build()
                .log()
                .all();

        return new HttpRequest(specification);
    }

    public HttpRequest getRequestWithDefaultAuthorization() {
        return getRequestWithAuthorization(ConfigProperties.getProperty("username"), ConfigProperties.getProperty("password"));
    }

    public HttpRequest getRequestWithCustomAuthorization(String username, String password) {
        return getRequestWithAuthorization(username, password);
    }

    private HttpRequest getRequestWithAuthorization(String username, String password) {
        PreemptiveBasicAuthScheme  auth = new PreemptiveBasicAuthScheme();
        auth.setUserName(username);
        auth.setPassword(password);

        RequestSpecification specification = new RequestSpecBuilder()
                .setAuth(auth)
                .setBaseUri("https://" + baseUrl)
                .setPort(port)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .build()
                .log()
                .all();

        return new HttpRequest(specification);
    }
}
