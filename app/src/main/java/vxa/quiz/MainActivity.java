package vxa.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button startBtn,bookmarkBtn , tryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // thiet lap su kien cho button start
        startBtn = findViewById(R.id.start_btn);
        bookmarkBtn = findViewById(R.id.bookMark_btn);
        tryBtn=findViewById(R.id.tryBtn);

        // chuyển giao diện
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryIntent = new Intent(MainActivity.this,QuestionActivity.class);
                startActivity(categoryIntent);

            }
        });
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookmarkIntent = new Intent(MainActivity.this,BookmarkActivity.class);
                startActivity(bookmarkIntent);

            }
        });

        tryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thiThu = new Intent(MainActivity.this,getStartActivity.class);
                startActivity(thiThu);
            }
        });

    }
}