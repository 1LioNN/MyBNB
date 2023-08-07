package com.example;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;

public class UserSession extends Endpoint{
    
    /*
     * GET /user/me
     * 
     * @return 200, 400, 404, 500
     * 
     * Get the current session id 
     */
    @Override
    public void handleGet(HttpExchange r) throws IOException, JSONException {
        List<String> cookie = r.getRequestHeaders().get("Cookie");
            if (cookie == null) {
                System.out.println("User is not logged in");
                this.sendStatus(r, 401);
                return;
            }
            Integer uid = Integer.valueOf(cookie.get(0).replace("session_id=", ""));

            JSONObject res = new JSONObject();
            res.put("session_id", uid);
            this.sendResponse(r, res, 0);
    }
}
