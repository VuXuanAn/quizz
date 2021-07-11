package vxa.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.reflect.TypeToken;
import  java.lang.reflect.Type ;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {
    private static final String FILE_NAME = "QUIZZER";
    private static final String KEY_NAME ="QUESTIONS" ;
    private RecyclerView recycleView ;
    private List<QuestionModel> bookmarksList ;


    private SharedPreferences preferences ;
    private SharedPreferences.Editor editor ;
    private Gson gson ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("bookmark");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycleView = findViewById(R.id.rv_bookmark);
        preferences = getSharedPreferences(FILE_NAME  , Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
        getBookmarks();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recycleView.setLayoutManager(layoutManager);

        BookmarkAdapter adapter = new BookmarkAdapter(bookmarksList);
        recycleView.setAdapter(adapter);




    }


    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBookmarks(){
        String json = preferences.getString(KEY_NAME,"");
        Type type = new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarksList = gson.fromJson(json , type );
        if(bookmarksList == null){
            bookmarksList = new ArrayList<>();
        }
    }

    private void storeBookmarks(){
        String json = gson.toJson(bookmarksList);
        editor.putString(KEY_NAME ,json);
        editor.commit();

    }
}