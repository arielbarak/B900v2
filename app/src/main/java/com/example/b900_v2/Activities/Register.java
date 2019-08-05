package com.example.b900_v2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.b900_v2.Database_tables.User;
import com.example.b900_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    private EditText etEmail, etPassword, etDateOfBirth, etFullName, etUserName;
    private Spinner etGender;
    private Button  btnSignUp;
    private FirebaseAuth auth;
    private DatabaseReference reff;
    private User user;
    private String[] spinnerValueHoldValue = {"PHP", "ANDROID", "WEB-DESIGN", "PHOTOSHOP"};
    private String selected_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        btnSignUp = (Button) findViewById(R.id.bRegister);
        etGender=(Spinner) findViewById(R.id.etGender);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etDateOfBirth = (EditText) findViewById(R.id.etDateOfBirth);
        etFullName = (EditText) findViewById(R.id.etFullName);
        etUserName=(EditText) findViewById(R.id.etUsername);
        user = new User();
        reff=FirebaseDatabase.getInstance().getReference().child("Users");

        List<String> gender = new ArrayList<String>();
        gender.add("Man");
        gender.add("Woman");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_list_item_1, spinnerValueHoldValue);
        etGender.setAdapter(adapter);
        etGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selected_gender = Toast.makeText(Register.this, etGender.getSelectedItem().toString(), Toast.LENGTH_LONG).toString();
                Toast.makeText(Register.this, etGender.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }   @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String[] name = etFullName.getText().toString().split(" ");
                String username = etUserName.getText().toString().trim();
                String date_of_birth = etDateOfBirth.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name[0]) || TextUtils.isEmpty(name[1])) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(date_of_birth)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(selected_gender)) {
                    selected_gender = "Man"; //default value
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etGender.getSelectedItemPosition() < 0)
                {
                    Toast.makeText(getApplicationContext(), "No gender was checked!", Toast.LENGTH_SHORT).show();
                    return;
                }

                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                byte[] password_hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

                user.setEmail(email);
                user.setFirst_name(name[0]);
                user.setLast_name(name[1]);
                user.setGender(selected_gender);
                user.setPassword_hash(password_hash);
                user.setDate_of_birth(date_of_birth);
                user.setUsername(username);

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(Register.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
