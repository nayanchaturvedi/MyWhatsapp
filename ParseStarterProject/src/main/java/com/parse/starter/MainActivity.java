/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {
    Boolean loginmodeactive = false;

    public void redirectifLogedin(){

        if (ParseUser.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
            startActivity(intent);
        }
    }

    public void toggleLoginMode(View view){
        Button loginSignupButton = (Button)findViewById(R.id.loginSignupButton);
        TextView toggleLoginModeTextView= (TextView)findViewById(R.id.toggleLoginModeTextView);


        if (loginmodeactive){
            loginmodeactive = false;
            toggleLoginModeTextView.setText("Login");
            loginSignupButton.setText("SignUp");

        }else{
            loginmodeactive = true;
            toggleLoginModeTextView.setText("SignUp");
            loginSignupButton.setText("Login");


        }
    }

  public  void signupLogin(View view){

      EditText usernameeditText = (EditText) findViewById(R.id.usernameEditText);
      EditText passwordeditText = (EditText) findViewById(R.id.passwordEditText);

      if (loginmodeactive){

          ParseUser.logInInBackground(usernameeditText.getText().toString(), passwordeditText.getText().toString(), new LogInCallback() {
              @Override
              public void done(ParseUser user, ParseException e) {
                  if (e==null){
                      Log.i("User","Loggedin");
                      redirectifLogedin();
                  }else{
                      String message = e.getMessage();
                      if (message.toLowerCase().contains("java")){

                          message = e.getMessage().substring(e.getMessage().indexOf(" "));
                      }
                      Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT).show();
                  }
              }
          });

      }else {

          ParseUser user = new ParseUser();
          user.setUsername(usernameeditText.getText().toString());
          user.setPassword(passwordeditText.getText().toString());

          user.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {
                  if (e == null) {

                      Log.i("User", "SignUp");
                      redirectifLogedin();
                  } else {
                      String message = e.getMessage();
                      if (message.toLowerCase().contains("java")){

                          message = e.getMessage().substring(e.getMessage().indexOf(" "));
                      }
                      Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT).show();}
              }
          });
      }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("My Whatsapp");
    //redirectifLogedin();

    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}