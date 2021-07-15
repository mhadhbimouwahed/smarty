package com.example.smarty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.smarty.R.layout.activity_add_new_product;

public class AddNewProductActivity extends AppCompatActivity {
    private EditText product_name;
    private EditText product_prise;
    private EditText product_category;
    private EditText product_photo_url;
    private EditText product_manufacturer;
    private EditText product_description;
    private EditText in_stock;
    private TextView save_product;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_add_new_product);
        product_name=findViewById(R.id.product_name);
        product_prise=findViewById(R.id.product_prise);
        product_category=findViewById(R.id.product_category);
        product_photo_url=findViewById(R.id.product_photo_url);
        product_manufacturer=findViewById(R.id.product_manufacturer);
        product_description=findViewById(R.id.product_description);
        in_stock=findViewById(R.id.in_stock);
        save_product=findViewById(R.id.save_product);

        databaseReference= FirebaseDatabase.getInstance().getReference("Products");


        save_product.setOnClickListener(x->{
            if(product_name.getText().toString().length()==0){
                product_name.setError("This field cannot be empty");
            }else if(product_prise.getText().toString().length()==0){
                product_prise.setError("This field cannot be empty");
            }else if(product_category.getText().toString().length()==0){
                product_category.setError("This Field cannot be empty");
            }else if(product_photo_url.getText().toString().length()==0){
                product_photo_url.setError("This field cannot be empty");
            }else if(product_manufacturer.getText().toString().length()==0){
                product_manufacturer.setError("This field cannot be empty");
            }else if(product_description.getText().toString().length()==0){
                product_description.setError("This field cannot be empty");
            }else if(in_stock.getText().toString().length()==0){
                in_stock.setError("This field cannot be empty");
            }else{
                saveProduct();
            }
        });

    }
    private void saveProduct(){
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("adminpage@gmail.com")){
            Product product=new Product(product_name.getText().toString(),
                    product_prise.getText().toString(),
                    product_category.getText().toString(),
                    product_photo_url.getText().toString(),
                    product_manufacturer.getText().toString(),
                    product_description.getText().toString(),
                    in_stock.getText().toString());
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(product);
            product_name.setText("");
            product_prise.setText("");
            product_category.setText("");
            product_photo_url.setText("");
            product_manufacturer.setText("");
            product_description.setText("");
            in_stock.setText("");
            Toast.makeText(getApplicationContext(),"Product added successfully",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"You are not an admin",Toast.LENGTH_SHORT).show();
        }


    }
}