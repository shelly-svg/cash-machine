package com.my.web.recaptcha;

import org.apache.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Util class for validate captcha
 *
 * @author Denys Tsebro
 */
public class VerifyUtils {

    public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final Logger logger = Logger.getLogger(VerifyUtils.class);

    /**
     * return true only if captcha is valid
     *
     * @param gRecaptchaResponse response from captcha
     * @return true if captcha is valid
     */
    public static boolean verify(String gRecaptchaResponse) {
        logger.debug("VerifyUtils.verify method starts");

        //if response is null or empty captcha isn`t valid
        if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
            return false;
        }
        try {
            URL verifyUrl = new URL(SITE_VERIFY_URL);
            HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();

            //Set some information in request
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Data will be sent in google server
            String postParams = "secret=" + CaptchaConstants.SECRET_KEY //
                    + "&response=" + gRecaptchaResponse;

            // Send Request
            conn.setDoOutput(true);

            // Get Output Stream, connection to the Server
            // Set data in Output Stream, send info to server
            OutputStream outStream = conn.getOutputStream();
            outStream.write(postParams.getBytes());

            outStream.flush();
            outStream.close();

            // Response code returns from server
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode=" + responseCode);

            // Get Input Stream Connection
            // for reading data from server
            InputStream is = conn.getInputStream();

            JsonReader jsonReader = Json.createReader(is);
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();

            // ==> {"success": true}
            logger.debug("Response: " + jsonObject);

            return jsonObject.getBoolean("success");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
