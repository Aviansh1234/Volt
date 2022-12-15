package com.example.voltmotors.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.voltmotors.HomeActivity;
import com.example.voltmotors.R;
import com.example.voltmotors.SignUpSignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    EditText email, password;
    AppCompatButton signin;
    SignUpSignInActivity context;

    public SignInFragment(SignUpSignInActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        email = view.findViewById(R.id.emailSignIn);
        password = view.findViewById(R.id.signInPassword);
        signin = view.findViewById(R.id.signInBtn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()){
                    auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Signed in successfully.", Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(context, HomeActivity.class));
                                        context.finish();
                                    }
                                    else{
                                        try {
                                            throw task.getException();
                                        }
                                        catch (FirebaseAuthInvalidUserException e){
                                            Toast.makeText(context, "This email does not have an account associated with it.", Toast.LENGTH_SHORT).show();
                                        }
                                        catch(FirebaseAuthInvalidCredentialsException e){
                                            Toast.makeText(context, "Your password is incorrect.", Toast.LENGTH_SHORT).show();
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                }
            }
        });
        return view;
    }
    private boolean validateInputs(){
        boolean validated = true;
        if(email.getText().toString().isEmpty()){
            email.setError("Please enter a name");
            validated = false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Please enter a name");
            validated = false;
        }
        return validated;
    }
}