package com.example;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SignIn extends Endpoint {
    /**
     * POST /user/signin
     * 
     * @body username, password
     * @return 200, 400, 401, 404, 500
     *         Login a user into the system if the given information matches the
     *         information of the user in the database.
     */

    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException {
        JSONObject body = new JSONObject(Utils.convert(r.getRequestBody()));
        String[] fields = new String[] { "username", "password" };
        Class[] fieldClasses = new Class[] { String.class, String.class };

        if (!this.validateFields(body, fields, fieldClasses)) {
            this.sendStatus(r, 400);
        } else {

            ResultSet rs;
            Integer uid;

            String username = body.getString("username");
            String password = body.getString("password"); // need to hash later
            rs = this.dao.signinUser(username, password);

            if (rs == null) {
                this.sendStatus(r, 404);
                return;
            }

            try {
                uid = rs.getInt("iduser");
            } catch (SQLException var10) {
                var10.printStackTrace();
                this.sendStatus(r, 404);
                return;
            }

            Headers respHeaders = r.getResponseHeaders();
            respHeaders.add("Set-Cookie", "session_id=" + uid +"; Path =/");

            this.sendResponse(r, new JSONObject(), 200);

        }
    }
}
