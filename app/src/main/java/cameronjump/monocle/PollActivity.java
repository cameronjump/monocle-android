package cameronjump.monocle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_activity);
        displayQuestion(0, "Question 1", 5);

    }

    public void displayQuestion(int type, String id, int numchoices) {
        LinearLayout layout = findViewById(R.id.layoutpoll);
        TextView text = new TextView(PollActivity.this);
        text.setText(id);
        layout.addView(text);

        //Short answer
        if(type == 0) {
            TextView tvtype = new TextView(PollActivity.this);
            tvtype.setText("Short Answer");
            layout.addView(tvtype);
            EditText edit = new EditText(PollActivity.this);
            layout.addView(edit);
        }

        //Multiple choice
        if(type == 1) {
            TextView tvtype = new TextView(PollActivity.this);
            tvtype.setText("Short Answer");
            layout.addView(tvtype);

            for (int i=0; i<numchoices; i++) {
                Button button = new Button(PollActivity.this);
                button.setText(String.valueOf(i));
                layout.addView(button);
            }
        }
    }
}
