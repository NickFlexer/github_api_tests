package helpers;

import net.minidev.json.JSONObject;

import java.util.Map;

public class JsonHelper {

    public static String generateJsonBody(Map<String, String> data) {
        JSONObject body = new JSONObject(data);

        return body.toJSONString();
    }
}
