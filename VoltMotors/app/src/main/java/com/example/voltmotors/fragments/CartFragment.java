package com.example.voltmotors.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.voltmotors.CheckOutActivity;
import com.example.voltmotors.HomeActivity;
import com.example.voltmotors.R;
import com.example.voltmotors.adapters.CartAdapter;
import com.example.voltmotors.adapters.SearchAdapter;
import com.example.voltmotors.custom.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    HomeActivity context;
    RecyclerView cart;
    AppCompatButton checkout;
    FirebaseFirestore firestore;
    CartAdapter adapter;
    ArrayList<Product>data = new ArrayList<>();

    public CartFragment(HomeActivity homeActivity) {
        context = homeActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cart = view.findViewById(R.id.cartRecycler);
        checkout = view.findViewById(R.id.cartButton);
        fillData();
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size()==0){
                    Toast.makeText(context, "Your cart is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, CheckOutActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        });
        return view;
    }
    private void fillData() {
        data.clear();
        firestore.collection(getString(R.string.users))
                .document(FirebaseAuth.getInstance().getUid())
                .collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                data.add(snapshot.toObject(Product.class));
                            }
                        } else {
                            Toast.makeText(context, "Could not get data.", Toast.LENGTH_SHORT).show();
                        }
                        adapter = new CartAdapter(data, context, "cart");
                        cart.setAdapter(adapter);
                        cart.setLayoutManager(new LinearLayoutManager(context));
                    }
                });
    }
}