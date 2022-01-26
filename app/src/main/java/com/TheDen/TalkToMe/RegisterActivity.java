package com.TheDen.TalkToMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    MaterialEditText username, email, password;
    Button tbn_register;
FirebaseUser firebaseUser;
    FirebaseAuth auth;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.usernamer);
        email= findViewById(R.id.Email);
        password=findViewById(R.id.Password);
        tbn_register = findViewById(R.id.btn_register);
        auth = FirebaseAuth.getInstance();


   /*     Toolbar toolbar = findViewById(R.id.tollbarr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
    }
    public void onClick(View v) {
        String txtUsername = username.getText().toString();
        String txtEmail = email.getText().toString();
        String txtPassword = password.getText().toString();

        if (TextUtils.isEmpty(txtUsername) )
        {
            Toast.makeText(RegisterActivity.this, "Fill  Username Field", Toast.LENGTH_SHORT).show();
        }

        if ( TextUtils.isEmpty(txtEmail) )
        {
            Toast.makeText(RegisterActivity.this, "Fill Email Field", Toast.LENGTH_SHORT).show();
        }

        if ( TextUtils.isEmpty(txtPassword))
        {
            Toast.makeText(RegisterActivity.this, "Fill Password Field", Toast.LENGTH_SHORT).show();
        }

        else if (txtPassword.length() < 6)
        {
            Toast.makeText(RegisterActivity.this , "Password must be at least 6 Characters", Toast.LENGTH_SHORT).show();

        }
        else register(txtUsername,txtEmail,txtPassword);
    }



    private void register (String username, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                          firebaseUser=auth.getCurrentUser();

                            assert firebaseUser != null;
                            String userid =firebaseUser.getUid();
                            ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL","default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                      finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Check details",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}