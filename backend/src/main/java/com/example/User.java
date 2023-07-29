package com.example;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.*;

public class User extends Endpoint {
	
    /**
     * GET /user/:uid
     * @param uid
     * @return 200, 400, 404, 500
     * Get basic information of user with the given uid
     */

	@Override
	public void handleGet(HttpExchange r) throws IOException, JSONException {
		
		// make query and get required data, return 500 if error
		ResultSet rs;
		boolean resultHasNext;
		try {
			rs = this.dao.query();
			resultHasNext = rs.next();
		} 
		catch (SQLException e) {
            e.printStackTrace();
			this.sendStatus(r, 500);
			return;
		}

		// check if user was found, return 404 if not found
		if (!resultHasNext) {
			this.sendStatus(r, 404);
			return;
		}

		// get data
		int id;
		try {
            id = rs.getInt("id");
		}
		catch (SQLException e) {
            e.printStackTrace();
			this.sendStatus(r, 500);
			return;
		}

		// making the response
		JSONObject resp = new JSONObject();
		JSONObject data = new JSONObject();
        data.put("id", id);
        resp.put("data", data);

		this.sendResponse(r, resp, 200);
	}
}
