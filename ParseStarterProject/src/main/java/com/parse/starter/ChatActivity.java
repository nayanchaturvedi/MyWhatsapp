package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    String activeuser = "";

    ArrayList<String> messages = new ArrayList();
    ArrayAdapter arrayAdapter;

    public void sendChat(View view){

        final EditText chatEditText = (EditText) findViewById(R.id.chatEditText);
        ParseObject message = new ParseObject("Message");

        final String messageContent = chatEditText.getText().toString();

        message.put("sender", ParseUser.getCurrentUser().getUsername());
        message.put("reciever",activeuser);
        message.put("message",messageContent);

        chatEditText.setText("");
        

        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                   messages.add(messageContent);
                   arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        activeuser = intent.getStringExtra("username");
        setTitle(activeuser);

        ListView chatListView = (ListView)findViewById(R.id.chatListView);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,messages);
        chatListView.setAdapter(arrayAdapter);

        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");
        query1.whereEqualTo("sender",ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("reciever",activeuser);

        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");
        query2.whereEqualTo("reciever",ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("sender",activeuser);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> query = ParseQuery.or(queries);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    if (objects.size()>0){
                        messages.clear();
                        for (ParseObject message : objects){
                            String messagecontent = message.getString("message");

                            if (!message.getString("sender").equals(ParseUser.getCurrentUser().getUsername())) {
                                messagecontent = ">>" + messagecontent;
                            }
                            messages.add(messagecontent);
                            Log.i("info",messagecontent);
                        }
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }
}
