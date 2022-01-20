package com.example.mvc_motors.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvc_motors.AboutContactUs;
import com.example.mvc_motors.MainActivity;
import com.example.mvc_motors.R;
import com.example.mvc_motors.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth base;
    DatabaseReference reference;

    private LoginViewModel loginViewModel;
    private String file = "motors_mvc_login.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        reference = FirebaseDatabase.getInstance().getReference("users");

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        try
        {

            EditText email =  findViewById(R.id.username);
            EditText password = findViewById(R.id.password);

            FileInputStream fin = openFileInput(file);
            DataInputStream din = new DataInputStream(fin);
            InputStreamReader isr = new InputStreamReader(din);
            BufferedReader br = new BufferedReader(isr);

            int i =0 ;
            String data[] = new String[1];
            String perLine;

            while((perLine = br.readLine()) != null)
            {
                data[i] = perLine;
                i++;
            }

            String check = email.getText().toString();

            if(!email.equals(check))
            {
                email.setText(data[0]);
            }

            fin.close();


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.pbLoading);

        TextView forgot = findViewById(R.id.forgotPassword);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    goToNext();
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });



        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetemail = new EditText(v.getContext());
                AlertDialog.Builder resetPasswordDialog = new AlertDialog.Builder(v.getContext());
                resetPasswordDialog.setTitle("Reset your password");
                resetPasswordDialog.setMessage("Please enter your email to receive a reset link");
                resetPasswordDialog.setView(resetemail);

                base = FirebaseAuth.getInstance();
                resetPasswordDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetemail.getText().toString();
                        base.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this,"Check your inbox to find your reset link!",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,"Something went wrong!" + e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                resetPasswordDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                resetPasswordDialog.create().show();
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void forgotPassword(View v){
        LinearLayout layout = findViewById(R.id.layout);
        layout.setVisibility(View.VISIBLE);
    }

    public void goToNext()
    {
        ProgressBar loadingProgressBar = findViewById(R.id.pbLoading);
        EditText email =  findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        loadingProgressBar.setVisibility(View.VISIBLE);

        base = FirebaseAuth.getInstance();
        base.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    String getEmail = email.getText().toString() +"\n";

                    try
                    {
                        FileOutputStream fout =  openFileOutput(file,0);
                        fout.write(getEmail.getBytes());

                        fout.close();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }


                    Toast.makeText(getApplicationContext(),"Sign in Successfully!",Toast.LENGTH_LONG).show();

                    Intent in = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(in);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Failed to login.Please check your email and password",Toast.LENGTH_LONG).show();
                    Intent in = new Intent(LoginActivity.this,LoginActivity.class);
                    startActivity(in);

                }
            }
        });

    }

    public void goToRegister(View v)
    {
        Intent in = new Intent(this,RegisterActivity.class);
        startActivity(in);
        finish();
    }
}

