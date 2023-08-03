package com.example;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

public class Review extends Endpoint{

    /*
     * POST /review
     * 
     * @body iduser, idlisting, rating, comment
     */
    @Override
    public void handlePost (HttpExchange r) throws IOException, JSONException{
        JSONObject body = new JSONObject(Utils.convert(r.getRequestBody()));
        String[] fields = new String[]{"iduser", "idlisting", "rating", "comment"};
        Class[] fieldClasses = new Class[]{Integer.class, Integer.class, BigDecimal.class, String.class};

        


    }

}
