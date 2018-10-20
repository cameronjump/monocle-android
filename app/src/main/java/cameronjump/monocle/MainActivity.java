package cameronjump.monocle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean verified = true;

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

        Button pb = findViewById(R.id.poll);
        pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verified) {
                    Intent myIntent = new Intent(MainActivity.this, PollActivity.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please verify your session.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        changeVerificationState();
    }

    public void verify() {
        if (verified) {
            Toast.makeText(MainActivity.this, "User already verified.",
                    Toast.LENGTH_LONG).show();
        }
        EditText userid = findViewById(R.id.userid);
        EditText sessionid = findViewById(R.id.sessionid);


    }

    public void changeVerificationState() {
        TextView vtext = findViewById(R.id.verificationstate);
        if(verified) {
            vtext.setText("Verified? ✓");
        }
        else {
            vtext.setText("Verified? X");
        }
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
