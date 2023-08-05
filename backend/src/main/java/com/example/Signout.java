package com.example;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Signout extends Endpoint {

    /*
     * POST /user/signout
     * 
     * @return 200, 400, 404, 500
     */

    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException {
        List<String> cookie = r.getRequestHeaders().get("Cookie");
        System.out.println(cookie);
        if (cookie == null) {
            System.out.println("User is not logged in");
            this.sendStatus(r, 400);
            return;
        }

        List<String> updatedCookie = new ArrayList<>();
        for (String c : cookie) {
            if (!c.startsWith("session_id=")) {
                updatedCookie.add(c);
            }
        }
        r.getResponseHeaders().put("Set-Cookie", updatedCookie);
        this.sendStatus(r, 200);
    }

}
