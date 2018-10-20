package cameronjump.monocle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_activity);
        displayQuestion(1, "Question 1", 5);

    }

    public void displayQuestion(int type, String id, int numchoices) {
        LinearLayout layout = findViewById(R.id.layoutpoll);
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
}
