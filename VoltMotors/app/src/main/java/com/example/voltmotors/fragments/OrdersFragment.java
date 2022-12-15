package com.example.voltmotors.fragments;

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

import com.example.voltmotors.HomeActivity;
import com.example.voltmotors.R;
import com.example.voltmotors.adapters.CartAdapter;
import com.example.voltmotors.custom.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    HomeActivity context;
    RecyclerView orders;
    FirebaseFirestore firestore;
    CartAdapter adapter;
    ArrayList<Product> data = new ArrayList<>();

    public OrdersFragment(HomeActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        orders = view.findViewById(R.id.ordersRecycler);
        fillData();
        return view;
    }

    private void fillData() {
        data.clear();
        firestore.collection(getString(R.string.users))
                .document(FirebaseAuth.getInstance().getUid())
                .collection("orders")
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
                        adapter = new CartAdapter(data, context, "orders");
                        orders.setAdapter(adapter);
                        orders.setLayoutManager(new LinearLayoutManager(context));
                    }
                });
    }
}