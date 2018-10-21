package cameronjump.monocle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    public static final String ip = "35.238.254.108";

    private String TAG = "MainActivity ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button vb = findViewById(R.id.verify);
        vb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }

    public void verify() {
        EditText userid = findViewById(R.id.userid);
        String useridstring = userid.getText().toString();
        EditText sessionid = findViewById(R.id.sessionid);
        String sessionidstring = sessionid.getText().toString();
        sendVerifyRequest(useridstring, sessionidstring);
    }

    public void sendVerifyRequest(final String userid, final String sessionid) {
            final Handler handler = new Handler();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "Sending request");
                        //Replace below IP with the IP of that device in which server socket open.
                        //If you change port then change the port number in the server side code also.
                        Socket s = new Socket(ip, 1336);

                        OutputStream out = s.getOutputStream();

                        PrintWriter output = new PrintWriter(out);

                        String checkinRequest = CreateJSON.checkin(userid, sessionid);
                        output.println(checkinRequest);
                        output.flush();
                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        final String st = input.readLine();
                        Log.d(TAG,st);
                        try {
                            JSONObject json = new JSONObject(st);
                            String data = (String) json.get("data");
                            if(data.equals("0")) {
                                MainActivity.this.startNextActivity(userid);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        output.close();
                        out.close();
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
    }

    public void startNextActivity(String userid) {
        Intent myIntent = new Intent(MainActivity.this, PollActivity.class);
        myIntent.putExtra("name", userid); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "Made at Hack K-State 2018", Toast.LENGTH_SHORT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
