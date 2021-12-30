package com.amita.forextranding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class SignUp extends AppCompatActivity {

    TextView goToLogin;
    EditText username, email, password;
    Button signUp;
    ProgressDialog progressDialog;

    Random random;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        init();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");


        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, MainActivity.class));

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = username.getText().toString();
                String e = email.getText().toString();
                String p = password.getText().toString();

                if (n.isEmpty()) {
                    username.setError("Please enter name");
                } else if (e.isEmpty()) {
                    email.setError("Please enter email..");
                } else if (p.isEmpty()) {
                    password.setError("Password cannot be empty..!");
                } else {
                    progressDialog = new ProgressDialog(SignUp.this);
                    progressDialog.setTitle("Registering....");
                    progressDialog.setMessage("Please wait..We're creating account for you a short while");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                auth = FirebaseAuth.getInstance();
                                user = auth.getCurrentUser();

                                HashMap<String, Object> map = new HashMap<>();
                                map.put("username", n);
                                map.put("user_id", user.getUid());
                                map.put("email", e);

                                reference.child(user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(SignUp.this, MainActivity.class));
                                            Toast.makeText(SignUp.this, "Account created!!!", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUp.this, "Something went wrong " + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Unable to register.." + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void init() {
        goToLogin = findViewById(R.id.sign_in_txt);
        username = findViewById(R.id.name);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password_id);
        signUp = findViewById(R.id.sign_up);
        random = new Random();
    }
}
