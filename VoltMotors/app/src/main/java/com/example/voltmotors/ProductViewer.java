package com.example.voltmotors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voltmotors.custom.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ProductViewer extends AppCompatActivity {

    AppCompatButton add;
    TextView name,desc,price;
    ImageButton back;
    ImageView image;
    Product product;
    String from;
    int prodId;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_viewer);

        add = findViewById(R.id.prodInfoAdd);
        name = findViewById(R.id.prodInfoName);
        desc = findViewById(R.id.prodInfoDesc);
        price = findViewById(R.id.prodInfoPrice);
        back = findViewById(R.id.prodInfoBack);
        image = findViewById(R.id.prodInfoImage);
        from = getIntent().getStringExtra("from");
        prodId = getIntent().getIntExtra("id", 1);
        firestore = FirebaseFirestore.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductViewer.this, HomeActivity.class));
                finish();
            }
        });
        fillInputs();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(product);
                startActivity(new Intent(ProductViewer.this, HomeActivity.class));
                Toast.makeText(getApplicationContext(), "Item added to cart successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void addProduct(Product product) {
        CollectionReference cartReference = FirebaseFirestore.getInstance().collection(getString(R.string.users))
                .document(FirebaseAuth.getInstance().getUid())
                .collection("cart");
        cartReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            cartReference.document(String.valueOf(task.getResult().size()+1))
                                    .set(product);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There was an error while adding item to cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void fillInputs(){

        switch (from){
            case "search":
                firestore.collection(getString(R.string.products))
                        .document(String.valueOf(prodId))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(ProductViewer.this, "An error occurred while getting info on this product.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    product = task.getResult().toObject(Product.class);
                                    name.setText(product.getName());
                                    desc.setText(product.getDesc());
                                    price.setText("$"+String.valueOf(product.getPrice()));
                                    Picasso.with(ProductViewer.this)
                                            .load(product.getUrl())
                                            .into(image);
                                }
                            }
                        });
                break;

            case "cart":
                firestore.collection(getString(R.string.users))
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("cart")
                        .document(String.valueOf(prodId))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(ProductViewer.this, "An error occurred while getting info on this product.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    product = task.getResult().toObject(Product.class);
                                    name.setText(product.getName());
                                    desc.setText(product.getDesc());
                                    price.setText("$"+String.valueOf(product.getPrice()));
                                    Picasso.with(ProductViewer.this)
                                            .load(product.getUrl())
                                            .into(image);
                                }
                            }
                        });
                break;

            case "orders":
                firestore.collection(getString(R.string.users))
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("orders")
                        .document(String.valueOf(prodId))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(ProductViewer.this, "An error occurred while getting info on this product.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    product = task.getResult().toObject(Product.class);
                                    name.setText(product.getName());
                                    desc.setText(product.getDesc());
                                    price.setText("$"+String.valueOf(product.getPrice()));
                                    Picasso.with(ProductViewer.this)
                                            .load(product.getUrl())
                                            .into(image);
                                }
                            }
                        });
                break;
        }

    }
}