package com.example.voltmotors.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.voltmotors.HomeActivity;
import com.example.voltmotors.ProductViewer;
import com.example.voltmotors.R;
import com.example.voltmotors.custom.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    AppCompatButton buyNow;
    HomeActivity context;
    ImageView image;

    public HomeFragment(HomeActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        buyNow = view.findViewById(R.id.homeFragBuyNow);
        image = view.findViewById(R.id.homeImage);
        fillImage();
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductViewer.class);
                intent.putExtra("from", "search");
                intent.putExtra("id", 1);
                context.startActivity(intent);
                context.finish();
            }
        });
        return view;
    }

    private void fillImage() {
        FirebaseFirestore.getInstance().collection(getString(R.string.products))
                .document("1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Picasso.with(context)
                                    .load(task.getResult().toObject(Product.class).getUrl())
                                    .into(image);
                        }
                    }
                });
    }

}