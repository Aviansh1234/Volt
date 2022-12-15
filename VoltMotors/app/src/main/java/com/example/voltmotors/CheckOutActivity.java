package com.example.voltmotors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voltmotors.custom.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CheckOutActivity extends AppCompatActivity {

    AppCompatButton change,pay;
    EditText card,cvv;
    TextView total, checkoutaddr;
    ImageButton back;
    DocumentReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        change = findViewById(R.id.checkOutChange);
        ref = FirebaseFirestore.getInstance()
                .collection(getString(R.string.users))
                .document(FirebaseAuth.getInstance().getUid());
        pay = findViewById(R.id.checkOutPay);
        card = findViewById(R.id.checkOutCard);
        checkoutaddr = findViewById(R.id.checkOutAddress);
        cvv = findViewById(R.id.checkOutCVV);
        back = findViewById(R.id.checkOutBack);
        total = findViewById(R.id.checkOutTotal);
        getTotal();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d =new Dialog(CheckOutActivity.this);
                d.setContentView(R.layout.change_address);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                EditText editText = d.findViewById(R.id.addressChange);
                editText.setText(checkoutaddr.getText().toString());
                d.findViewById(R.id.saveAddr).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkoutaddr.setText(editText.getText().toString());
                        d.dismiss();
                        Toast.makeText(CheckOutActivity.this, "Address Changed.", Toast.LENGTH_SHORT).show();
                    }
                });
                d.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckOutActivity.this, HomeActivity.class));
                finish();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shiftToOrders();
                Toast.makeText(CheckOutActivity.this, "Ordering...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CheckOutActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
    private void getTotal(){
        ref.collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            long total = 0;
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                total+=snapshot.toObject(Product.class).getPrice();
                            }
                            CheckOutActivity.this.total.setText("$"+String.valueOf(total));
                        }
                    }
                });
    }
    private void shiftToOrders(){
        ref.collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            ref.collection("orders")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                            int size = task2.getResult().size()+1;
                                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                                ref.collection("orders")
                                                        .document(String.valueOf(size++))
                                                        .set(snapshot.getData());
                                            }
                                        }
                                    });
                            ref.collection("cart")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()){
                                                for (int i = 0; i < task.getResult().size(); i++) {
                                                    ref.collection("cart")
                                                            .document(String.valueOf(i+1))
                                                            .delete();
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}