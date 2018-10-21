package cameronjump.monocle;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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

    private String ip = MainActivity.ip;
    private String TAG = "PollActivity";
    private String id = "0";
    private String name = "";

    private Button[] buttons;

    Thread requestQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_activity);
        name = getIntent().getExtras().getString("name");

    }

    public void displayQuestion(final int type, final int numchoices) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayQuestion(type, numchoices, true);
            }
        });
    }

    public void displayQuestion(int type, final int numchoices, boolean yo) {
        LinearLayout layout = findViewById(R.id.layoutpoll);
        layout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(100,30,100,30);
        params.gravity = Gravity.CENTER;


        //Short answer
        if(type == 0) {
            TextView tvtype = new TextView(PollActivity.this);
            tvtype.setText("Short Answer");
            tvtype.setGravity(Gravity.CENTER);
            layout.addView(tvtype);
            tvtype.setTextSize(20);

            TextView text = new TextView(PollActivity.this);
            text.setTextSize(15);
            text.setGravity(Gravity.CENTER);
            text.setTextAlignment(1);
            text.setLayoutParams(params);
            text.setText(id);
            layout.addView(text);

            EditText edit = new EditText(PollActivity.this);
            edit.setId(12345679);
            layout.addView(edit);

            Button button = new Button(PollActivity.this);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText edit = findViewById(12345679);
                    String answer = edit.getText().toString();
                    sendAnswer(answer);
                }
            });
            button.setText("Submit");
            button.setBackground(getDrawable(R.color.colorPrimaryDark));
            button.setLayoutParams(params);
            layout.addView(button);
        }

        //Multiple choice
        if(type == 1) {
            TextView tvtype = new TextView(PollActivity.this);
            tvtype.setText("Multiple Choice");
            tvtype.setTextSize(20);
            tvtype.setGravity(Gravity.CENTER);
            layout.addView(tvtype);


            TextView text = new TextView(PollActivity.this);
            text.setTextSize(15);
            text.setGravity(Gravity.CENTER);
            text.setTextAlignment(1);
            text.setLayoutParams(params);
            text.setText(id);
            layout.addView(text);

            buttons = new Button[numchoices];
            for (int i=0; i<numchoices; i++) {
                Button button = new Button(PollActivity.this);
                button.setText(String.valueOf(i));
                button.setBackground(getDrawable(R.color.colorPrimaryDark));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button butt = (Button) v;
                        for (Button b: buttons) {
                            b.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                        }
                        butt.setBackgroundColor(getColor(R.color.colorPrimary));
                        sendAnswer(butt.getText().toString());


                    }
                });
                button.setLayoutParams(params);
                buttons[i] = button;
                layout.addView(button);
            }
        }
    }

    public void sendAnswer(final String answer) {
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

                        String questionRequest = CreateJSON.answerQuestion(id,name,answer);
                        output.println(questionRequest);
                        output.flush();
                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        final String st = input.readLine();
                        if(st != null) Log.d(TAG, st);
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

    public void requestQuestion() {
        requestQuestion = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(Thread.interrupted()) {
                            return;
                        }
                        Log.d(TAG, "Sending request");
                        //Replace below IP with the IP of that device in which server socket open.
                        //If you change port then change the port number in the server side code also.
                        Socket s = new Socket(ip, 1336);

                        OutputStream out = s.getOutputStream();

                        PrintWriter output = new PrintWriter(out);

                        String questionRequest = CreateJSON.getCurrentQuestion(id);
                        output.println(questionRequest);
                        output.flush();
                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        final String st = input.readLine();
                        Log.d(TAG, st);
                        try {
                            JSONObject json = new JSONObject(st);
                            JSONObject data = new JSONObject((String) json.get("data"));
                            System.out.println(json.toString());
                            int jtype = (int)data.get("type");
                            String jid = (String) data.get("id");
                            int jnumChoices = (int) data.get("numChoices");

                            if (!jid.equals(id)) {
                                PollActivity.this.displayQuestion(jtype, jnumChoices);
                            }
                            id = jid;
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        output.close();
                        out.close();
                        s.close();
                        Thread.sleep(2000);
                    } catch (IOException e) {
                        e.printStackTrace();}
                    catch (InterruptedException e) {
                        e.printStackTrace();}
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        requestQuestion.start();
    }

    protected void onPause() {
        super.onPause();
        if(requestQuestion != null) requestQuestion.interrupt();
    }

    protected void onResume() {
        super.onResume();
        requestQuestion();
    }

}
