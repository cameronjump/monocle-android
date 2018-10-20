package cameronjump.monocle;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity ";
    private WebSocketClient mWebSocketClient;


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
                        Socket s = new Socket("10.131.220.41", 1336);

                        OutputStream out = s.getOutputStream();

                        PrintWriter output = new PrintWriter(out);

                        String checkinRequest = CreateJSON.checkin(userid, sessionid);
                        output.println(checkinRequest);
                        output.flush();
                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        final String st = input.readLine();
                        Log.d(TAG,st);
                        if(st.equals("1")) {
                            MainActivity.this.startNextActivity();
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

    public void startNextActivity() {
        Intent myIntent = new Intent(MainActivity.this, PollActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
