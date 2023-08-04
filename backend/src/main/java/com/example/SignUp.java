package com.example;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SignUp extends Endpoint {

    /**
     * POST /user/signup
     * @body name, email, password
     * @return 200, 400, 500
     * Register a user into the system using the given information.
     */

    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException {
        JSONObject body = new JSONObject(Utils.convert(r.getRequestBody()));
        String[] fields = new String[]{"name", "email", "password", "address", "birthday","occupation","SIN"};
        Class[] fieldClasses = new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, Integer.class};
        ResultSet rs;
        Integer uid;

        if (!this.validateFields(body, fields, fieldClasses)) {
            System.out.println("Invalid fields");
            this.sendStatus(r, 400);
        } else {
            String name = body.getString("name");
            String email = body.getString("email");
            String password = body.getString("password");
            String address = body.getString("address");
            String birthday = body.getString("birthday");
            String occupation = body.getString("occupation");
            Integer SIN = body.getInt("SIN");
            String creditcard = null;
            String creditPass = null;
            try {
                creditcard = body.getString("credit_number");
            } catch (JSONException e) {
                creditcard = null;
            }
            try{
                creditPass = body.getString("credit_password");
            } catch (JSONException e) {
                creditPass = null;
            }
           if ((this.dao.getUserbyEmail(email) != null) || (this.dao.getUserbySIN(SIN) != null)){
                System.out.println("User already exists");
                this.sendStatus(r, 400);
                return;
            }
            if (creditcard != null && creditPass != null){
                rs = this.dao.signupRenter(email, password, name, address, birthday, occupation, SIN, creditcard, creditPass);
                System.out.println("Renter created");
            }else{
                rs = this.dao.signupHost(email, password, name, address, birthday, occupation, SIN);
                System.out.println("Host created");
            }   

            try {
                uid = rs.getInt("iduser");
            } catch (SQLException var10) {
                var10.printStackTrace();
                this.sendStatus(r, 404);
                return;
            }

            Headers respHeaders = r.getResponseHeaders();

            respHeaders.add("Set-Cookie", uid +"; Path =/");

            

            this.sendResponse(r, new JSONObject(), 200);

        }
    }

}

