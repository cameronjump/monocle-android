package cameronjump.monocle;

import android.app.Activity;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class Request {

    private String tag = "REQUEST DEBUG";

    public static void sendRequest(final String surl, final String requestType, final JSONObject json) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(surl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod(requestType);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    //DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(json.toString()));
                    //os.writeBytes(json.toString());
                    //os.write("hey man lets go to bed soon");

                    //os.flush();
                    //os.close();

                    Log.d(TAG, String.valueOf(conn.getResponseCode()));
                    Log.d(TAG, conn.getResponseMessage());
                    InputStream is = conn.getInputStream();
                    Scanner s = new Scanner(is).useDelimiter("\\A");
                    String result = s.hasNext() ? s.next() : "";
                    Log.d(TAG, result);
                    conn.disconnect();
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        });
        thread.start();
    }
}
