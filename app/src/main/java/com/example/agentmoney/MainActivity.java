package com.example.agentmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;
    private FirebaseAuth mAuth;
    private boolean loginCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btn_login) {
                    String email = etAccount.getText().toString();
                    String password = etPassword.getText().toString();
                    if (email.isEmpty() || password.isEmpty()){
                        Toast.makeText(MainActivity.this, "帳號或密碼為空", Toast.LENGTH_SHORT).show();
                    } else {
                        signIn(email, password);
                        if (loginCheck) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, homeactivity.class);
                            MainActivity.this.startActivity(intent);
                        }
                    }

                } else if (view.getId() == R.id.btn_signup) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, signupactivity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        };
        btnLogin.setOnClickListener(listener);
        btnSignup.setOnClickListener(listener);
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginCheck = true;
                } else {
                    //登入失敗，顯示錯誤訊息
                    Toast.makeText(MainActivity.this, "登入失敗，請檢查帳號與密碼", Toast.LENGTH_SHORT).show();
                    loginCheck = false;
                }
            }
        });
    }

}