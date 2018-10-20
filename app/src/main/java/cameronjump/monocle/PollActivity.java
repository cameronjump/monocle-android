package cameronjump.monocle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class PollActivity extends AppCompatActivity {

    private String TAG = "PollActivity";
    String id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_activity);
        displayQuestion(1, "Question 1", 5);

    }

    public void displayQuestion(int type, String id, int numchoices) {
        LinearLayout layout = findViewById(R.id.layoutpoll);
        layout.removeAllViews();
        TextView text = new TextView(PollActivity.this);
        text.setTextSize(20);
        text.setTextAlignment(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10,10,10,10);
        text.setLayoutParams(params);
        text.setText(id);
        layout.addView(text);

        //Short answer
        if(type == 0) {
            TextView tvtype = findViewById(R.id.question);
            tvtype.setText("Short Answer");
            layout.addView(tvtype);
            EditText edit = new EditText(PollActivity.this);
            layout.addView(edit);

            Button button = new Button(PollActivity.this);
            button.setText("Submit");
            button.setBackground(getDrawable(R.color.colorPrimaryDark));
            button.setLayoutParams(params);
            layout.addView(button);
        }

        //Multiple choice
        if(type == 1) {
            TextView tvtype = findViewById(R.id.question);
            tvtype.setText("Multiple Choice");

            for (int i=0; i<numchoices; i++) {
                Button button = new Button(PollActivity.this);
                button.setText(String.valueOf(i));
                button.setBackground(getDrawable(R.color.colorPrimaryDark));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button) v;
                        b.setBackgroundColor(getColor(R.color.colorPrimary));
                    }
                });
                button.setLayoutParams(params);
                layout.addView(button);
            }
        }
    }

    public requestQuestion() {
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

                    String questionRequest = CreateJSON.getCurrentQuestion(id);
                    output.println(questionRequest);
                    output.flush();
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    final String st = input.readLine();
                    Log.d(TAG,st);
                    JSONObject json = new JSONObject(st);
                    JSONObject data = json.get("data");
                    System.out.println(json.toString());
                    int jtype = json.get("type");
                    String jid = json.get("id");
                    int jnumChoices = data.get("numChoices");

                    if(!jid.equals(id)) {
                        PollActivity.this.displayQuestion(jtype, jid, jnumChoices);
                    }
                    id = jid;
                    
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
    }
}
