package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText edLogin, edPassword;
    private FirebaseAuth mAuth;
    private Button bStart,bSingUp,bSingIn,bSingOut;
    private TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if (cUser != null) {
            showSigned();

            String userName = "Вы вошли как: " + cUser.getEmail();
            tvUserName.setText(userName);

            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT).show();
        } else {
            notSigned();
            Toast.makeText(this, "User null", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        tvUserName = findViewById(R.id.tvUserEmail);
        bStart = findViewById(R.id.start);
        bSingUp = findViewById(R.id.registr);
        bSingIn = findViewById(R.id.vxod);
        bSingOut = findViewById(R.id.exit);
        edLogin = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickSingUp(View view) {
        if (!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                showSigned();
                                Toast.makeText(getApplicationContext(), "Регистрация успешна", Toast.LENGTH_SHORT).show();
                            } else {
                                notSigned();
                                Toast.makeText(getApplicationContext(), "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Не заполнено поле 'Email' или 'Password'", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickSingIn(View view) {
        if (!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString())) {
            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                showSigned();
                                Toast.makeText(getApplicationContext(), "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                            } else {
                                notSigned();
                                Toast.makeText(getApplicationContext(), "Ошибка входа: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Не заполнено поле 'Email' или 'Password'", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickSingOut(View view){
        FirebaseAuth.getInstance().signOut();
        notSigned();
    }
    private void showSigned(){
        bStart.setVisibility(View.VISIBLE);
        tvUserName.setVisibility(View.VISIBLE);
        bSingOut.setVisibility(View.VISIBLE);
        edLogin.setVisibility(View.GONE);
        edPassword.setVisibility(View.GONE);
        bSingIn.setVisibility(View.GONE);
        bSingUp.setVisibility(View.GONE);
    }
    private void notSigned(){
        bStart.setVisibility(View.GONE);
        tvUserName.setVisibility(View.GONE);
        bSingOut.setVisibility(View.GONE);
        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        bSingIn.setVisibility(View.VISIBLE);
        bSingUp.setVisibility(View.VISIBLE);
    }
    public void onClickStart(View view){
        Intent intent = new Intent(this, To_do_list.class);
        startActivity(intent);
    }
}
