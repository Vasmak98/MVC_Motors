package com.example.mvc_motors;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment1 extends Fragment {

    private CircleImageView profileImage;
    private static final int PICK_IMAGE = 1;

    FirebaseAuth base;
    FirebaseUser user;
    DatabaseReference reference;

    private String userID;
    String _NAME,_SURNAME;
    String _USERNAME, _EMAIL, _IMAGE ;
    private ImageView profilePic;
    public Uri imageUri;
    private   FirebaseStorage storage;
    private StorageReference storageReference;
    int counter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_profile_first, container, false);
        EditText etName = root.findViewById(R.id.etName);
        EditText etSurname = root.findViewById(R.id.etSurname);
        profilePic = root.findViewById(R.id.profileImage);

        EditText etUsername = root.findViewById(R.id.etUsername);
        EditText etEmail = root.findViewById(R.id.etEmail);

        TextView forgot = root.findViewById(R.id.forgotPass);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();


        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);

                if (userProfile != null){
                    _NAME = userProfile.Name;
                    _SURNAME = userProfile.Surname;
                    _USERNAME = userProfile.Username;
                    _EMAIL = userProfile.Email;
                    _IMAGE = userProfile.Image;

                    etUsername.setText(_USERNAME);
                    etEmail.setText(_EMAIL);
                    etName.setText(_NAME);
                    etSurname.setText(_SURNAME);

                    if (_IMAGE != null) {
                        Picasso.get().load(_IMAGE).into(profilePic);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext().getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetemail = new EditText(v.getContext());
                resetemail.setText(_EMAIL);
                resetemail.setEnabled(false);
                AlertDialog.Builder resetPasswordDialog = new AlertDialog.Builder(v.getContext());
                resetPasswordDialog.setTitle("Reset your password");
                resetPasswordDialog.setMessage("Click \"YES\" if you want to get an email to change your password");
                resetPasswordDialog.setView(resetemail);

                base = FirebaseAuth.getInstance();
                resetPasswordDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetemail.getText().toString();
                        base.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(),"Check your inbox to find your reset link!",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Something went wrong!" + e.getMessage(),Toast.LENGTH_LONG).show();
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

        etUsername.setEnabled(false);
        etEmail.setEnabled(false);
        etName.setEnabled(false);
        etSurname.setEnabled(false);
        int color = etUsername.getCurrentTextColor();

        FloatingActionButton fab = root.findViewById(R.id.fabAdd);
            fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view){


                        if(counter == 0) {
                            profilePic = root.findViewById(R.id.profileImage);
                            storage = FirebaseStorage.getInstance();
                            storageReference = storage.getReference();

                            profilePic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                                    in.setType("image/*");
                                    startActivityForResult(Intent.createChooser(in, "Select Picture"), PICK_IMAGE);
                                }
                            });
                            etName.setEnabled(true);
                            etSurname.setEnabled(true);
                            etUsername.setEnabled(true);
                            profilePic.setEnabled(true);
                            etUsername.setTextColor(Color.parseColor("#000000"));
                            etName.setTextColor(Color.parseColor("#000000"));
                            etSurname.setTextColor(Color.parseColor("#000000"));
                            counter = 1;

                        }
                        else
                        {
                            etName.setEnabled(false);
                            etSurname.setEnabled(false);
                            etUsername.setEnabled(false);
                            profilePic.setEnabled(false);
                            etUsername.setTextColor(color);
                            etName.setTextColor(color);
                            etSurname.setTextColor(color);
                            counter = 0;
                        }

                }

            });



        Button update = root.findViewById(R.id.btnUpdate);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean usernameNull = etUsername.getText().toString().equals("");
                Boolean nameNull = etName.getText().toString().equals("");
                Boolean surnameNull = etSurname.getText().toString().equals("");
                int username=0;
                int name=0;
                int surname=0;
                int empty1=0;
                int empty2=0;
                int empty3=0;

                if(!usernameNull) {
                    if (isusernamechanged()) {
                        username=1;
                    }
                }

                else {
                    empty1 = 1;
                }

                if(!nameNull) {
                    if (isnamechanged()) {
                        name = 1;
                    }


                }
                else {
                    empty2 = 1;
                }

                if(!surnameNull){
                    if (issurnnamechanged()) {
                        surname=1;
                }


                }
                else {
                    empty3 = 1;
                }


                if(username ==1 && name==1 && surname ==1)
                {
                    Toast.makeText(getContext().getApplicationContext(), "Username, Name and Surname updated successfully", Toast.LENGTH_LONG).show();
                    username = 0;
                    name = 0;
                    surname=0;
                }
                else if(name ==1 && surname==1 )
                {
                    Toast.makeText(getContext().getApplicationContext(), "Name and Surname updated successfully", Toast.LENGTH_LONG).show();
                    username = 0;
                    name = 0;
                    surname=0;
                }
                else if(username ==1 && surname==1 )
                {
                    Toast.makeText(getContext().getApplicationContext(), "Username and Surname updated successfully", Toast.LENGTH_LONG).show();
                    username = 0;
                    name = 0;
                    surname=0;
                }
                else if(username ==1 && name==1 )
                {
                    Toast.makeText(getContext().getApplicationContext(), "Username and Name updated successfully", Toast.LENGTH_LONG).show();
                    username = 0;
                    name = 0;
                    surname=0;
                }
                else if(username == 1)
                {
                    Toast.makeText(getContext().getApplicationContext(), "Username updated successfully", Toast.LENGTH_LONG).show();
                    username = 0;
                    name = 0;
                    surname=0;

                }
                else if(name == 1)
                {
                    Toast.makeText(getContext().getApplicationContext(), "Name updated successfully", Toast.LENGTH_LONG).show();
                    username = 0;
                    name = 0;
                    surname=0;
                }
                else  if(surname == 1)
                {
                    Toast.makeText(getContext().getApplicationContext(), "Surname updated successfully", Toast.LENGTH_LONG).show();
                    username = 0;
                    name = 0;
                    surname=0;
                }
                else if(surname==0 && name ==0 && username==0 && empty1==0 && empty2==0 && empty3==0){
                    Toast.makeText(getContext().getApplicationContext(), "Υou did not make any changes", Toast.LENGTH_LONG).show();

                }


                if(empty1 ==1 && empty2==1 && empty3 ==1)
                {
                    Toast.makeText(getContext().getApplicationContext(), "Username, Name and Surname cannot be empty", Toast.LENGTH_LONG).show();
                    empty1=0;
                    empty2=0;
                    empty3=0;
                }
                else if(empty2 ==1 && empty3==1 )
                {
                    Toast.makeText(getContext().getApplicationContext(), "Name and Surname cannot be empty", Toast.LENGTH_LONG).show();
                    empty1=0;
                    empty2=0;
                    empty3=0;
                }
                else if(empty1 ==1 && empty3==1 )
                {
                    Toast.makeText(getContext().getApplicationContext(), "Username and Surname cannot be empty", Toast.LENGTH_LONG).show();
                    empty1=0;
                    empty2=0;
                    empty3=0;
                }
                else if(empty1 ==1 && empty2==1 )
                {
                    Toast.makeText(getContext().getApplicationContext(), "Username and Name cannot be empty", Toast.LENGTH_LONG).show();
                    empty1=0;
                    empty2=0;
                    empty3=0;
                }
                else if(empty1 == 1)
                {
                    Toast.makeText(getContext().getApplicationContext(), "Username cannot be empty", Toast.LENGTH_LONG).show();
                    empty1=0;
                    empty2=0;
                    empty3=0;
                }
                else if(empty2 == 1)
                {
                    Toast.makeText(getContext().getApplicationContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
                    empty1=0;
                    empty2=0;
                    empty3=0;
                }
                else  if(empty3 == 1)
                {
                    Toast.makeText(getContext().getApplicationContext(), "Surname cannot be empty", Toast.LENGTH_LONG).show();
                    empty1=0;
                    empty2=0;
                    empty3=0;
                }

            }


            private boolean issurnnamechanged() {
                if(!_SURNAME.equals(etSurname.getText().toString())){
                    reference.child(userID).child("Surname").setValue(etSurname.getText().toString());
                    _SURNAME = etSurname.getText().toString();
                    return true;
                }else{
                    return false;
                }
            }

            private boolean isnamechanged() {
                if(!_NAME.equals(etName.getText().toString())){
                    reference.child(userID).child("Name").setValue(etName.getText().toString());
                    _NAME = etName.getText().toString();
                    return true;
                }else{
                    return false;
                }
            }

            private boolean isusernamechanged() {
                if(!_USERNAME.equals(etUsername.getText().toString())){
                    reference.child(userID).child("Username").setValue(etUsername.getText().toString());
                    _USERNAME = etUsername.getText().toString();
                    return true;
                }else{
                    return false;
                }
            }



        });

        // Inflate the layout for this fragment
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadPicture();

        }
    }

    private void uploadPicture()
    {
       ProgressDialog pd = new ProgressDialog(getActivity());
       pd.setTitle("Uploading Image...");
       pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);


        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                reference.child(userID).child("Image").setValue(String.valueOf(uri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext().getApplicationContext(), "Image Updated", Toast.LENGTH_LONG ).show();

                                    }
                                });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getContext().getApplicationContext(), "Failed", Toast.LENGTH_LONG ).show();

                    }
                });
    }
}