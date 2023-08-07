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
            this.sendStatus(r, 400);
            return;
        }

        String[] cookies = cookie.toString().split(";");
        Integer uid = null;

        for (String c : cookies) {
            String cookieName = c.replace("]", "").replace("[", "").replace(" ", "");
            if (cookieName.startsWith("session_id=")) {
                uid = Integer.valueOf(cookieName.replace("session_id=", ""));
            }
        }
        System.out.println(uid);
        if (uid == null) {
            System.out.println("User is not logged in");
            this.sendStatus(r, 400);
            return;
        }

            JSONObject res = new JSONObject();
            res.put("session_id", uid);
            this.sendResponse(r, res, 0);
    }
}
