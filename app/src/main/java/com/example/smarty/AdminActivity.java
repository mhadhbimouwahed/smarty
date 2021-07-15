package com.example.smarty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.smarty.R.layout.activity_admin;

public class AdminActivity extends AppCompatActivity {

    private TextView addNewProduct;
    private TextView modifyProduct;
    private TextView deleteProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_admin);
        addNewProduct=findViewById(R.id.addNewProduct);
        modifyProduct=findViewById(R.id.modifyProduct);
        deleteProduct=findViewById(R.id.deleteProduct);


        addNewProduct.setOnClickListener(x->{
            Toast.makeText(getApplicationContext(),"adding new product",Toast.LENGTH_SHORT).show();
        });


        modifyProduct.setOnClickListener(x->{
            Toast.makeText(getApplicationContext(),"modifying a product",Toast.LENGTH_SHORT).show();
        });

        deleteProduct.setOnClickListener(x->{
            Toast.makeText(getApplicationContext(),"deleting a product",Toast.LENGTH_SHORT).show();
        });
    }
}