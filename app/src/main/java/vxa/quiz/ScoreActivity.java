package vxa.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    private TextView total , scored ;
    private Button doneBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        total = findViewById(R.id.total);
        scored = findViewById(R.id.scored);

        scored.setText(String.valueOf(getIntent().getIntExtra("score",0))+"/"+ String.valueOf(getIntent().getIntExtra("total",0)));
        total.setText("Cần cố gắng hơn !" );

        doneBtn = findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}