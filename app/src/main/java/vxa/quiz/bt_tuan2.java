package vxa.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.widget.Toast.*;

public class bt_tuan2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_tuan2);


        Button btng = (Button)findViewById(R.id.ok_button);
        btng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(bt_tuan2.this, "Xin chào !", Toast.LENGTH_SHORT).show();
                Intent categoryIntent = new Intent(bt_tuan2.this, bt_tuan1.class);
                startActivity(categoryIntent);
            }
        });
    }
}