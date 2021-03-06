package vxa.quiz;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class QuestionActivity extends AppCompatActivity {
    public static final String FILE_NAME ="QUIZZER";
    public static final String KEY_NAME = "QUESTIONS";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    private TextView question , noIndicator ;
    private FloatingActionButton bookmarkBtn ;
    private LinearLayout optionContainer ;
    private Button shareBtn , nextBtn ;
    private int count = 0 ;
    private List<QuestionModel> list ;
    private int position = 0 ;
    private int score = 0 ;

    private int setNo ;
    private Dialog loadingDialog ;

    private String category ;
    private List<QuestionModel> bookmarksList ;


    private SharedPreferences preferences ;
    private SharedPreferences.Editor editor ;
    private Gson gson ;
    private int matchedQuestionPosition ;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"WrongViewCast", "CommitPrefEdits", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        question = findViewById(R.id.question);
        noIndicator = findViewById(R.id.no_indicator);
        bookmarkBtn =  (FloatingActionButton)findViewById(R.id.bookmark_btn);
        optionContainer = findViewById(R.id.options_container);
        shareBtn = findViewById(R.id.share_btn);
        nextBtn = findViewById(R.id.next_btn);


        preferences = getSharedPreferences(FILE_NAME  , Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
        getBookmarks();

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modelMatch() ){
                        bookmarksList.remove(matchedQuestionPosition);
                        bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                    }else{
                        bookmarksList.add(list.get(position));
                        bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark1));
                    }
            }
        });



        list = new ArrayList<>();


        category = getIntent().getStringExtra("category");
        setNo = getIntent().getIntExtra("setNo",1);


        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);


        list = new ArrayList<>();
        loadingDialog.show();
        myRef.child("Question").child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    list.add(snapshot1.getValue(QuestionModel.class));
                }
                if(list.size()>0)
                {

                    for(int i = 0 ; i<4;i++)
                    {
                        optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {

                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View v) {
                                checkAnswer((Button) v);
                            }
                        });
                    }
                    playAnim(question,0,list.get(position).getQuestion());
                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View v) {


                            nextBtn.setEnabled(false);
                            nextBtn.setAlpha(0.7f);
                            enableOption(true);
                            position++;

                            if(position == list.size())
                            {
                                Intent scoreIntent = new Intent(QuestionActivity.this , ScoreActivity.class);
                                scoreIntent.putExtra("score",score);
                                scoreIntent.putExtra("total",list.size());
                                startActivity(scoreIntent);
                                finish();
                                return ;
                            }
                            count = 0 ;
                            playAnim(question,0,list.get(position).getQuestion());

                        }
                    });
                    shareBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String body = list.get(position).getQuestion()+
                                    list.get(position).getOptionA() + "\n" +
                                    list.get(position).getOptionB() + "\n" +
                                    list.get(position).getOptionC() + "\n" +
                                    list.get(position).getOptionDs() ;
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("plain/text");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Quizzer challage");
                            shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(shareIntent,"Share via"));
                        }
                    });
                }else {
                    finish();
                    Toast.makeText(QuestionActivity.this, "NO question", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionActivity.this, "loi", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    // h??m n??y l?? h??m ch???y hi???u ???ng c???a text , c??ng ok r???i

    private void playAnim( final View view ,final int value ,final  String data )
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                if(value == 0 && count < 4)
                {
                    String option="" ;
                    if(count==0)
                    {
                       option = list.get(position).getOptionA();
                    }
                    else if(count==1){
                        option = list.get(position).getOptionB();
                    }
                    else if(count==2){
                        option = list.get(position).getOptionC();
                    }
                    else if(count==3){
                        option = list.get(position).getOptionDs();
                    }
                    playAnim(optionContainer.getChildAt(count),0,option);
                    count++;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onAnimationEnd(Animator animation) {

                if(value ==0  ){
                    try {
                        ((TextView)view).setText(data);
                        noIndicator.setText(position+1+"/"+list.size());
                        if(modelMatch()){
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark1));
                        }else{
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                        }
                    }catch (ClassCastException ex){
                        ((Button)view).setText(data);
                    }

                    view.setTag(data);
                    playAnim(view,1,data );

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    // ????y l?? h??m ki???m tra k???t qu??? nh?? !
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(Button selectedOption){
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);

        if(selectedOption.getText().toString().equals(list.get(position).getCorrectANS())){
            //correct
            score++;
                selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
        else
        {
            //incorrect
                selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctoption = (Button) optionContainer.findViewWithTag(list.get(position).getCorrectANS());
                correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
    }

    // h??m n??y ok r???i ,
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable){
        for(int i = 0 ; i<4 ; i++)
        {
            optionContainer.getChildAt(i).setEnabled(enable);
            if(enable){
                optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }

    // ham get bookmark
    private void getBookmarks(){
        String json = preferences.getString(KEY_NAME,"");
        Type type = new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarksList = gson.fromJson(json , type );
        if(bookmarksList == null){
            bookmarksList = new ArrayList<>();
        }
    }
    private boolean modelMatch(){
        boolean matched =  false ;
        int i = 0 ;
        for(QuestionModel model : bookmarksList){
                if(model.getQuestion().equals(list.get(position).getQuestion())
                        && model.getCorrectANS().equals(list.get(position).getCorrectANS())
                        && model.getSetNo()==list.get(position).getSetNo()){
                    matched = true ;
                    matchedQuestionPosition = i ;
                }
                i++ ;
        }
        return matched ;
    }
    private void storeBookmarks(){
        String json = gson.toJson(bookmarksList);
        editor.putString(KEY_NAME ,json);
        editor.commit();

    }
}