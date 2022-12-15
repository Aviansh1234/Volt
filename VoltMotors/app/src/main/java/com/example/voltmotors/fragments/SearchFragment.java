package com.example.voltmotors.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voltmotors.HomeActivity;
import com.example.voltmotors.R;
import com.example.voltmotors.adapters.SearchAdapter;
import com.example.voltmotors.custom.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    HomeActivity context;
    TextInputEditText search;
    TextView numResults;
    RecyclerView results;
    ArrayList<Product> data = new ArrayList<>();
    FirebaseFirestore firestore;
    SearchAdapter adapter;

    public SearchFragment(HomeActivity context) {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        search = view.findViewById(R.id.searchField);
        numResults = view.findViewById(R.id.numResults);
        results = view.findViewById(R.id.searchRecycler);
        fillData();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search((s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    private void search(String param) {
        ArrayList<Product> temp = new ArrayList<>();
        System.out.println("here");
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().toLowerCase().contains(param.toLowerCase())) {
                temp.add(data.get(i));
            }
        }
        numResults.setText(String.valueOf(temp.size()) + " results found.");
        adapter.updateReceiptsList(temp);
    }

    private void fillData() {
        data.clear();
        firestore.collection(getString(R.string.products))
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
                        adapter = new SearchAdapter(context, data);
                        results.setAdapter(adapter);
                        results.setLayoutManager(new LinearLayoutManager(context));
                        numResults.setText(String.valueOf(data.size()) + " results found.");
                    }
                });
    }

}