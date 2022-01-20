package com.example.mvc_motors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mvc_motors.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RegisterActivity extends AppCompatActivity {

    String  Image="";
    EditText name,surname,username,email,password,confPass,birthday;
    Button register;
    RadioGroup group;
    RadioButton male,female;
    ProgressBar loading;
    DatabaseReference databaseReference;
    FirebaseAuth base;
    private String file = "motors_mvc.txt";

    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        username = (EditText) findViewById(R.id.usernamereg);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.passwordreg);
        confPass = (EditText) findViewById(R.id.retypepassword);
        loading = (ProgressBar) findViewById(R.id.loadingreg);
        group = (RadioGroup) findViewById(R.id.gender);
        register = (Button) findViewById(R.id.register);
        male = (RadioButton) findViewById(R.id.rbMale);
        female = (RadioButton) findViewById(R.id.rbFemale);



        try
        {
            FileInputStream fin = openFileInput(file);
            DataInputStream din = new DataInputStream(fin);
            InputStreamReader isr = new InputStreamReader(din);
            BufferedReader br = new BufferedReader(isr);

            int i =0 ;
            String data[] = new String[5];
            String perLine;

            while((perLine = br.readLine()) != null)
            {
                data[i] = perLine;
                i++;
            }

            name.setText(data[0]);
            email.setText(data[1]);
            username.setText(data[2]);

            String genderId = data[3];

            int groupid = Integer.parseInt(genderId);

            if(groupid == R.id.rbMale)
            {
                male.setChecked(true);
            }
            else if(groupid == R.id.rbFemale)
            {
                female.setChecked(true);
            }
            surname.setText(data[4]);

            fin.close();


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        base = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString().trim();
                String surnameText = surname.getText().toString().trim();
                String usernameText = username.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String confirmPasswordText = confPass.getText().toString().trim();



                int choice = group.getCheckedRadioButtonId();

                if(choice == R.id.rbMale)
                {
                    gender = "Male";
                }
                else if(choice == R.id.rbFemale)
                {
                    gender = "Female";
                }

                if(validName()  && validSurname() && validEmail()  && validUsername()  && validPass() && validConfPass())
                    {   loading.setVisibility(View.VISIBLE);
                        base.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    try
                                    {
                                        FileOutputStream fout = openFileOutput(file, 0);
                                        fout.write("".getBytes());
                                        fout.close();
                                    }
                                    catch (Exception ex)
                                    {
                                        ex.printStackTrace();
                                        Toast.makeText(getApplicationContext(),"Something went wrong. " + ex.getMessage(),Toast.LENGTH_LONG).show();
                                    }

                                    Users information = new Users(
                                            nameText,
                                            surnameText,
                                            emailText,
                                            usernameText,
                                            gender,
                                            Image
                                    );
                                    FirebaseDatabase.getInstance().getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_LONG).show();
                                            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(in);
                                        }
                                    });

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        return;
                    }


            }
        });
    }

    private boolean validName()
    {
        EditText name = findViewById(R.id.name);
        String val = name.getText().toString().trim();
        if (val.isEmpty())
        {
            name.setError("Field can not be empty");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    private boolean validSurname()
    {
        EditText name = findViewById(R.id.surname);
        String val = name.getText().toString().trim();
        if (val.isEmpty())
        {
            name.setError("Field can not be empty");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    private boolean validEmail()
    {
        EditText email = findViewById(R.id.email);
        String val = email.getText().toString();

        String emailType = "[a-zA-Z0-9._-]+@{1}[a-z]+\\.[a-z]+";

        if(val.isEmpty())
        {
            email.setError("Field can not be empty");
            return false;
        }
        if(!val.matches(emailType))
        {//michalis@gmail.com
            email.setError("Email type is not correct");
            return false;
        }
        else
        {
            email.setError(null);
            return true;
        }
    }

    private boolean validUsername()
    {
        EditText username = findViewById(R.id.usernamereg);
        String val = username.getText().toString().trim();
        int count = 0;

        for(int i=0 ; i<val.length();i++)
        {
            if (Character.isDigit(val.charAt(i)))
            {
                count++;
            }
        }
        if (val.isEmpty())
        {
            username.setError("Field can not be empty");
            return false;
        }
        else if(count==0)
        {
            username.setError("Username must contains at least one number");
            return false;
        }
        else if(val.length()>=15 || val.length()<5)
        {
            username.setError("Username must contains 6-15 characters");
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean validPass()
    {
        EditText password = findViewById(R.id.passwordreg);
        String val = password.getText().toString().trim();

        String passType = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[.@!#$%^&+=])(?=\\S+$).{6,15}$";

        if(!val.matches(passType))
        {
            password.setError("Password must contains 6-15 characters,lower case,capital case,numbers and symbols");
            return false;
        }
        else if(val.isEmpty())
        {
            password.setError("Field can not be empty");
            return false;
        }
        else
        {
            return true;
        }

    }

    private boolean validConfPass()
    {
        EditText password = findViewById(R.id.passwordreg);
        String val1 = password.getText().toString().trim();
        EditText conf = findViewById(R.id.retypepassword);
        String val2 = conf.getText().toString().trim();

        if(val2.isEmpty())
        {
            conf.setError("Field can not be empty");
            return false;
        }
        else if(!val1.equals(val2))
        {
            conf.setError("Passwords do not match!");
            return false;
        }
        else
        {
            return true;
        }

    }

    public void goToLogin(View v)
    {
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        username = (EditText) findViewById(R.id.usernamereg);
        email = (EditText) findViewById(R.id.email);
        group = (RadioGroup) findViewById(R.id.gender);

        String getName = name.getText().toString() + "\n";
        String getSurname = surname.getText().toString() + "\n";
        String getUsername = username.getText().toString() + "\n";
        String getEmail = email.getText().toString() + "\n";

        int getGroupID = group.getCheckedRadioButtonId();
        String groupID = getGroupID + "\n";

        try {
            FileOutputStream fout =  openFileOutput(file,0);
            fout.write(getName.getBytes());
            fout.write(getEmail.getBytes());
            fout.write(getUsername.getBytes());
            fout.write(groupID.getBytes());
            fout.write(getSurname.getBytes());
            fout.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Something went wrong. " + ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        finish();
        Intent in  = new Intent(this,LoginActivity.class);
        startActivity(in);

    }

}

