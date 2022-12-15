package com.example.voltmotors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.voltmotors.adapters.PagerAdapter;
import com.example.voltmotors.fragments.SignInFragment;
import com.example.voltmotors.fragments.SignUpFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpSignInActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter adapter;
    SignInFragment signInFragment;
    SignUpFragment signUpFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_sign_in);

        tabLayout = findViewById(R.id.signUpSignInTabLayout);
        viewPager = findViewById(R.id.signUpSignInViewPager);
        adapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        signUpFragment = new SignUpFragment(this);
        signInFragment = new SignInFragment(this);
        adapter.AddFragment(signUpFragment, "Sign Up");
        adapter.AddFragment(signInFragment, "Sign In");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}