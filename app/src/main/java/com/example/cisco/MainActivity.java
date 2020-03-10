package com.example.cisco;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cisco.adapter.MessageAdapter;
import com.example.cisco.model.ResponseMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.cisco.R.layout.activity_main;
import static com.example.cisco.R.layout.options;

public class MainActivity extends AppCompatActivity {

    ChatBarView chatBarView;
    String text;
    ImageButton imageButton;
    ImageView imageView;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<ResponseMessage> responseMessageList;
    TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);

        textToSpeech= new TextToSpeech(this, new OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });

        chatBarView = findViewById(R.id.chatbar);
        chatBarView.setSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO what you want..
                Toast.makeText(MainActivity.this, chatBarView.getMessageText(), Toast.LENGTH_SHORT).show();//Here the message will get from the user
                ResponseMessage responseMessage = new ResponseMessage(chatBarView.getMessageText(), true);//here the message will inserted to the adapter
                responseMessageList.add(responseMessage);
                ResponseMessage responseMessage2 = new ResponseMessage("Hello,Ashutosh", false);//here the message will obtained from server
                responseMessageList.add(responseMessage2);
                textToSpeech.speak(responseMessage2.getText(),TextToSpeech.QUEUE_FLUSH,null);
                messageAdapter.notifyDataSetChanged();
                if (!isLastVisible())
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });


        chatBarView.setOnMicListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO what you want..
                Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), speech.class);
                startActivityForResult(intent,100);
                return true;
            }
        });



        text = chatBarView.getMessageText();

        linearLayout=findViewById(R.id.layout_opt);

        imageButton=findViewById(R.id.option);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), option.class);
                startActivity(intent);
                //setContentView(options);
                //linearLayout.setVisibility(v.VISIBLE);
            }
        });
        imageView=findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), profile.class);
                startActivity(intent);
            }
        });
    }

    private boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent msg) {
        super.onActivityResult(requestCode, resultCode, msg);
        if (resultCode == RESULT_OK && null != msg) {
            String res =msg.getData().toString();

            Toast.makeText(this,res,Toast.LENGTH_SHORT).show();
            chatBarView.setText(res);
            //setResult(RESULT_OK, data);
        }
    }
}