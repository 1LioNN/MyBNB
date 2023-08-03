package com.example;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;

public class Review extends Endpoint {

    /*
     * POST /review
     * 
     * @body iduser, idlisting, rating, content
     * 
     * @return 200, 400, 500
     */
    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException {
        System.out.println("Handling POST request...");
        JSONObject body = new JSONObject(Utils.convert(r.getRequestBody()));
        String[] fields = new String[] { "iduser", "idlisting", "rating", "content" };
        Class[] fieldClasses = new Class[] { Integer.class, Integer.class, BigDecimal.class, String.class };

        if (!this.validateFields(body, fields, fieldClasses)) {
            System.out.println("Invalid fields");
            this.sendStatus(r, 400);
        } else {
            Integer iduser = body.getInt("iduser");
            Integer idlisting = body.getInt("idlisting");
            BigDecimal rating = body.getBigDecimal("rating");
            String comment = body.getString("content");

            if ((this.dao.getUserById(iduser) == null) || (this.dao.getListingById(idlisting) == null)) {
                System.out.println("User or listing does not exist");
                this.sendStatus(r, 400);
                return;
            }
            // limit contnet to 255 characters
            if (comment.length() > 255) {
                System.out.println("Comment is too long");
                this.sendStatus(r, 400);
                return;
            }
            //limit rating to 0-5
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

            //limit one review per user per listing
            if (this.dao.getReviewByKey(iduser, idlisting) != null) {
                System.out.println("User has already reviewed this listing");
                this.sendStatus(r, 400);
                return;
            }

            try {
                this.dao.createReview(iduser, idlisting, rating, comment);
                System.out.println("Review added");
                this.sendStatus(r, 200);
            } catch (Exception e) {
                System.out.println("Error adding review");
                this.sendStatus(r, 500);
            }

        }

    }

    /*
     * DELETE /review/:rid
     * 
     * @param rid
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
        int rid;
        try {
            rid = Integer.parseInt(uidString);
        } catch (Exception e) {
            e.printStackTrace();
            this.sendStatus(r, 400);
            return;
        }

        try {
            if (this.dao.getReviewById(rid) == null) {
                System.out.println("Review does not exist");
                this.sendStatus(r, 404);
                return;
            }
        } catch (Exception e) {
            System.out.println("Error getting review");
            this.sendStatus(r, 500);
            return;
        }

        try {
            this.dao.deleteReview(rid);
            System.out.println("Review deleted");
            this.sendStatus(r, 200);
        } catch (Exception e) {
            System.out.println("Error deleting review");
            this.sendStatus(r, 500);
        }
    }

}
