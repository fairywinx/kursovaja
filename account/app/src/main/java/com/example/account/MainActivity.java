package com.example.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button btnRegister, btnSignIn;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    RelativeLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.register);
        btnSignIn = findViewById(R.id.signIn);
        root = findViewById(R.id.root_element);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterWindow();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInWindow();
            }
        });

    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Авторизация");
        dialog.setMessage("Введите данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(sign_in_window);

        TextInputEditText Email = sign_in_window.findViewById(R.id.email_text);
        TextInputEditText Password = sign_in_window.findViewById(R.id.pass_text);


        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

            }
        });
        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(Email.getText().toString())){
                    Snackbar.make(root,"Введите почту",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(Password.getText().toString(). length() < 5){
                    Snackbar.make(root,"Введите пароль, который более 5 символов",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(Email.getText().toString(),Password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(MainActivity.this, Main2Activity.class));
                            finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(root,"Ошибка авторизации. " + e.getMessage(),Snackbar.LENGTH_SHORT).show();

                            }
                        });
            }
        });
        dialog.show();
    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        dialog.setView(register_window);

        TextInputEditText Email = register_window.findViewById(R.id.email_text);
        TextInputEditText Password = register_window.findViewById(R.id.pass_text);
        TextInputEditText Name = register_window.findViewById(R.id.name_text);
        TextInputEditText Phone = register_window.findViewById(R.id.phone_text);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

            }
        });
            dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    if(TextUtils.isEmpty(Email.getText().toString())){
                        Snackbar.make(root,"Введите почту",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if(Password.getText().toString(). length() < 5){
                        Snackbar.make(root,"Введите пароль, который более 5 символов",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(Name.getText().toString())){
                        Snackbar.make(root,"Введите имя",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(Phone.getText().toString())){
                        Snackbar.make(root,"Введите телефон",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    //Регистрация
                    auth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    User user = new User();
                                    user.setEmail(Email.getText().toString());
                                    user.setName(Name.getText().toString());
                                    user.setPass(Password.getText().toString());
                                    user.setPhone(Phone.getText().toString());

                                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Snackbar.make(root,"Пользователь добавлен", Snackbar.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            });
                }
            });
            dialog.show();
        }
    }

