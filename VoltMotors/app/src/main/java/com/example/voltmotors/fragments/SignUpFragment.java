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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    EditText name, email, password;
    AppCompatButton signup;
    SignUpSignInActivity context;

    public SignUpFragment(SignUpSignInActivity context) {
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        name = view.findViewById(R.id.nameSignUp);
        email = view.findViewById(R.id.emailSignUp);
        password = view.findViewById(R.id.signUpPassword);
        signup = view.findViewById(R.id.signUpBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.setEnabled(false);
                if (validateInputs()){
                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        createUserInDB();
                                    }
                                    else{
                                        try {
                                            throw task.getException();
                                        }
                                        catch (FirebaseAuthWeakPasswordException e){
                                            Toast.makeText(context, "Your password is too weak.", Toast.LENGTH_SHORT).show();
                                        }
                                        catch (FirebaseAuthInvalidCredentialsException e){
                                            Toast.makeText(context, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                                        }
                                        catch (FirebaseAuthUserCollisionException e){
                                            Toast.makeText(context, "The email you tried to use already exists.", Toast.LENGTH_SHORT).show();
                                        }
                                        catch (Exception e)
                                        {
                                            System.out.println("authComplete: " + e.getMessage());
                                            Toast.makeText(context, "An error occurred, please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    signup.setEnabled(true);
                                }
                            });
                }
                else
                    signup.setEnabled(true);
            }
        });
        return view;
    }
    private void createUserInDB() {
        Map<String, String>user = new HashMap<>();
        user.put("name", name.getText().toString());
        user.put("email", email.getText().toString());
        firestore.collection(getString(R.string.users))
                .document(auth.getCurrentUser().getUid())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(context, "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Account created successfully.", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, HomeActivity.class));
                            context.finish();
                        }
                    }
                });

    }

    private boolean validateInputs(){
        boolean validated = true;
        if(name.getText().toString().isEmpty()){
            name.setError("Please enter a name");
            validated = false;
        }
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