package com.example.smarty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.smarty.R.layout.activity_add_new_product;

public class AddNewProductActivity extends AppCompatActivity {
    private EditText product_name;
    private EditText product_prise;
    private EditText product_category;
    private ImageView product_image;
    private EditText product_manufacturer;
    private EditText product_description;
    private EditText in_stock;
    private TextView save_product;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_add_new_product);
        product_name=findViewById(R.id.product_name);
        product_prise=findViewById(R.id.product_prise);
        product_category=findViewById(R.id.product_category);
        product_image=findViewById(R.id.product_image);
        product_manufacturer=findViewById(R.id.product_manufacturer);
        product_description=findViewById(R.id.product_description);
        in_stock=findViewById(R.id.in_stock);
        save_product=findViewById(R.id.save_product);



        save_product.setOnClickListener(x->{
            if(product_name.getText().toString().length()==0){
                product_name.setError("This field cannot be empty");
            }else if(product_prise.getText().toString().length()==0){
                product_prise.setError("This field cannot be empty");
            }else if(product_category.getText().toString().length()==0){
                product_category.setError("This Field cannot be empty");
            }else if(product_manufacturer.getText().toString().length()==0){
                product_manufacturer.setError("This field cannot be empty");
            }else if(product_description.getText().toString().length()==0){
                product_description.setError("This field cannot be empty");
            }else if(in_stock.getText().toString().length()==0){
                in_stock.setError("This field cannot be empty");
            }else{

            }
        });

    }

}