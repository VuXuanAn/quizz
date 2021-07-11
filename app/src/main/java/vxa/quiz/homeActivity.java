package vxa.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homeActivity extends AppCompatActivity {
    private Button home_btn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_btn = findViewById(R.id.tracNghiemBtn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent categoryIntent = new Intent(homeActivity.this,MainActivity.class);
                Intent categoryIntent = new Intent(homeActivity.this,bt_tuan2.class);
                startActivity(categoryIntent);
            }
        });
    }
}