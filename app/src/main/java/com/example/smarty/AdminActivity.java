package com.example.smarty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.smarty.R.layout.activity_admin;

public class AdminActivity extends AppCompatActivity {

    private TextView addNewProduct;
    private TextView modifyProduct;
    private TextView deleteProduct;
    private TextView logout_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_admin);
        addNewProduct=findViewById(R.id.addNewProduct);
        modifyProduct=findViewById(R.id.modifyProduct);
        deleteProduct=findViewById(R.id.deleteProduct);
        logout_admin=findViewById(R.id.logout_admin);


        addNewProduct.setOnClickListener(x->{
            startActivity(new Intent(getApplicationContext(),AddNewProductActivity.class));
        });


        modifyProduct.setOnClickListener(x->{
            startActivity(new Intent(getApplicationContext(),ModifyProductActivity.class));
        });

        deleteProduct.setOnClickListener(x->{
            startActivity(new Intent(getApplicationContext(),DeleteProductActivity.class));
        });
        
        logout_admin.setOnClickListener(x->{
            Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });
    }

}