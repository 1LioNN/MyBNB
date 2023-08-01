package com.example;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.*;

public class User extends Endpoint {

	/**
	 * GET /user/:uid
	 * 
	 * @param uid
	 * @return 200, 400, 404, 500
	 *         Get basic information of user with the given uid
	 */

	@Override
	public void handleGet(HttpExchange r) throws IOException, JSONException {

		// check if request url isn't malformed
		String[] splitUrl = r.getRequestURI().getPath().split("/");
		if (splitUrl.length != 3) {
			this.sendStatus(r, 400);
			return;
		}

		// check if uid given is integer, return 400 if not
		String uidString = splitUrl[2];
		int uid;
		try {
			uid = Integer.parseInt(uidString);
		} catch (Exception e) {
			e.printStackTrace();
			this.sendStatus(r, 400);
			return;
		}

		// make query and get required data, return 500 if error
		ResultSet rs;
		boolean resultHasNext;
		try {
			rs = this.dao.getUserById(uid);
			resultHasNext = rs.next();
		} catch (SQLException e) {
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
		String name;
		String email;
		String address;
		String birthday;
		String occupation;
		Integer SIN;
		String userType;
		try {
			name = rs.getString("name");
			email = rs.getString("email");
			address = rs.getString("address");
			birthday = rs.getString("birthday");
			occupation = rs.getString("occupation");
			SIN = rs.getInt("SIN");
			userType = rs.getString("userType");
		} catch (SQLException e) {
			e.printStackTrace();
			this.sendStatus(r, 500);
			return;
		}

		// making the response
		JSONObject resp = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("uid", uid);
		data.put("name", name);
		data.put("email", email);
		data.put("address", address);
		data.put("birthday", birthday);
		data.put("occupation", occupation);
		data.put("SIN", SIN);
		data.put("userType", userType);
		resp.put("data", data);
		this.sendResponse(r, resp, 200);
	}

}
