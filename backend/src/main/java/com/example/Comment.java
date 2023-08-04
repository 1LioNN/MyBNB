package com.example;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Comment extends Endpoint {

    /*
     * POST /comment
     * 
     * @body commenter, commentee, rating, content
     * 
     * @return 200, 400, 500
     */
    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException {
        JSONObject body = new JSONObject(Utils.convert(r.getRequestBody()));
        String[] fields = new String[] {"commentee", "rating", "content" };
        Class[] fieldClasses = new Class[] { Integer.class, BigDecimal.class, String.class };

        if (!this.validateFields(body, fields, fieldClasses)) {
            System.out.println("Invalid fields");
            this.sendStatus(r, 400);
        } else {
             List<String> cookie = r.getRequestHeaders().get("Cookie");
            if (cookie == null) {
                System.out.println("User is not logged in");
                this.sendStatus(r, 400);
                return;
            }
            Integer iduser = Integer.valueOf(cookie.get(0));
            Integer iduser2 = body.getInt("commentee");
            BigDecimal rating = body.getBigDecimal("rating");
            String comment = body.getString("content");

            if ((this.dao.getUserById(iduser) == null) || (this.dao.getUserById(iduser2) == null)) {
                System.out.println("Users do not exist");
                this.sendStatus(r, 400);
                return;
            }
            // limit contnet to 255 characters
            if (comment.length() > 255) {
                System.out.println("Comment is too long");
                this.sendStatus(r, 400);
                return;
            }
            // limit rating to 0-5
            try {
                if (rating.compareTo(new BigDecimal(5)) > 0 || rating.compareTo(new BigDecimal(0)) < 0) {
                    System.out.println("Rating is not between 0 and 5");
                    this.sendStatus(r, 400);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Rating is not a number");
                this.sendStatus(r, 400);
                return;
            }

            // limit one review per user per listing
            if (this.dao.getCommentByKey(iduser, iduser2) != null) {
                System.out.println("User has already reviewed this listing");
                this.sendStatus(r, 400);
                return;
            }

            try {
                this.dao.createComment(iduser, iduser2, rating, comment);
                System.out.println("Comment added");
                this.sendStatus(r, 200);
            } catch (Exception e) {
                System.out.println("Error adding review");
                this.sendStatus(r, 500);
            }

        }

    }

    /*
     * DELETE /comment/:cid
     * 
     * @param cid
     * 
     * @return 200, 400, 404, 500
     */

    @Override
    public void handleDelete(HttpExchange r) throws IOException, JSONException {
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        if (splitUrl.length != 3) {
            this.sendStatus(r, 400);
            return;
        }
        // check if uid given is integer, return 400 if not
        String uidString = splitUrl[2];
        int cid;
        try {
            cid = Integer.parseInt(uidString);
        } catch (Exception e) {
            e.printStackTrace();
            this.sendStatus(r, 400);
            return;
        }

        try {
            if (this.dao.getCommentById(cid) == null) {
                System.out.println("Comment does not exist");
                this.sendStatus(r, 404);
                return;
            }
        } catch (Exception e) {
            System.out.println("Error getting review");
            this.sendStatus(r, 500);
            return;
        }

        try {
            this.dao.deleteComment(cid);
            System.out.println("Comment deleted");
            this.sendStatus(r, 200);
        } catch (Exception e) {
            System.out.println("Error deleting review");
            this.sendStatus(r, 500);
        }
    }

}
