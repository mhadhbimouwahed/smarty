package com.example.smarty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.Provider;
import java.util.HashMap;
import java.util.Map;

public class ModifyPageActivity extends AppCompatActivity {

    private EditText product_name_modify;
    private EditText product_prise_modify;
    private Spinner product_category_modify;
    private ImageView product_image_modify;
    private EditText product_manufacturer_modify;
    private EditText product_description_modify;
    private EditText product_discount_modify;
    private Spinner in_stock_modify;
    private ProgressBar progress_bar_new_product_modify;

    public Uri imageUri;
    public static final int IMAGE_PICK_CODE=1000 ;
    public static final int PERMISSION_CODE = 1001;
    private static final String[] categories={"product category","portable computers","smart phones","accessories"};
    private static final String[] instock_yes_no={"in stock?","yes","no"};

    public StorageReference storageReference;
    public FirebaseFirestore firestore;
    public CollectionReference collectionReference;
    public DocumentReference documentReference;
    public String downloadImageUrl;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_page);
        product_name_modify=findViewById(R.id.product_name_modify);
        product_prise_modify=findViewById(R.id.product_prise_modify);
        product_category_modify=findViewById(R.id.product_category_modify);
        product_image_modify=findViewById(R.id.product_image_modify);
        product_manufacturer_modify=findViewById(R.id.product_manufacturer_modify);
        product_description_modify=findViewById(R.id.product_description_modify);
        in_stock_modify=findViewById(R.id.in_stock_modify);
        product_discount_modify=findViewById(R.id.product_discount_modify);
        
        
        TextView modify_product_button = findViewById(R.id.modify_product_button);
        progress_bar_new_product_modify=findViewById(R.id.progress_bar_modify);
        firestore=FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("Products");

        FirebaseStorage storage = FirebaseStorage.getInstance();

        
        storageReference= storage.getReference().child("Images/"+getIntent().getStringExtra("ID"));

        ArrayAdapter<String> adapter_category=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,categories);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_category_modify.setAdapter(adapter_category);

        ArrayAdapter<String> adapter_in_stock=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,instock_yes_no);
        adapter_in_stock.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        in_stock_modify.setAdapter(adapter_in_stock);
        
        modify_product_button.setOnClickListener(x-> {


            if(product_name_modify.getText().toString().equals("")){
                product_name_modify.setError("This field cannot be empty");
            }
            else if(product_prise_modify.getText().toString().equals("")){
                product_prise_modify.setError("This field cannot be empty");
            }else if(product_category_modify.getSelectedItem().equals(null)){
                Toast.makeText(this, "select item category before you modify", Toast.LENGTH_SHORT).show();
            }else if(product_manufacturer_modify.getText().toString().equals("")){
                product_manufacturer_modify.setError("This field cannot be empty");
            }else if(product_description_modify.getText().toString().equals("")){
                product_description_modify.setError("This field cannot be empty");
            }else if(in_stock_modify.getSelectedItem().equals(null)){
                Toast.makeText(this, "you need to specify if the product is in stock or not", Toast.LENGTH_SHORT).show();
            }else if(product_discount_modify.getText().toString().length()==0){
                product_discount_modify.setError("This field cannot be empty");
            }else{
                modify_product();
            }
        });

        product_image_modify.setOnClickListener(x->{
            Toast.makeText(this, "choose an image from gallery", Toast.LENGTH_SHORT).show();
            openGallery();

        });

    }

    private void modify_product() {
        if (imageUri==null){
            collectionReference.document(getIntent().getStringExtra("ID")).get().addOnCompleteListener(task->{
                HashMap<String,Object> product_modify=new HashMap<String, Object>(task.getResult().getData());
                String imm=  product_modify.get("ProductImage").toString();
                product_modify.put("ProductName",product_name_modify.getText().toString());
                product_modify.put("ProductImage",imm);
                product_modify.put("ProductPrise",product_prise_modify.getText().toString());
                product_modify.put("ProductCategory",product_category_modify.getSelectedItem().toString());
                product_modify.put("ProductManufacturer",product_manufacturer_modify.getText().toString());
                product_modify.put("ProductDescription",product_description_modify.getText().toString());
                product_modify.put("InStock",in_stock_modify.getSelectedItem().toString());
                product_modify.put("PID",getIntent().getStringExtra("ID"));
                product_modify.put("ProductDiscount",product_discount_modify.getText().toString());
                collectionReference.document(getIntent().getStringExtra("ID")).update(product_modify).addOnCompleteListener(tsk->{
                    if(tsk.isSuccessful()){
                        Toast.makeText(this, "product updated successfully", Toast.LENGTH_SHORT).show();
                        product_name_modify.setText("");
                        Glide.with(getApplicationContext()).load("").into(product_image_modify);
                        product_prise_modify.setText("");
                        product_category_modify.setAdapter(null);
                        in_stock_modify.setAdapter(null);
                        product_manufacturer_modify.setText("");
                        product_description_modify.setText("");
                    }else{
                        Toast.makeText(this, "there was a problem", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(failure->{
                    Toast.makeText(this, "there was a problem", Toast.LENGTH_SHORT).show();
                });

            }).addOnFailureListener(failure->{
                Toast.makeText(this, "you need to select an image", Toast.LENGTH_SHORT).show();
            });
        }else{
            HashMap<String,Object> product_modify=new HashMap<>();
            product_modify.put("ProductName",product_name_modify.getText().toString());
            product_modify.put("ProductImage",downloadImageUrl);
            product_modify.put("ProductPrise",product_prise_modify.getText().toString());
            product_modify.put("ProductCategory",product_category_modify.getSelectedItem().toString());
            product_modify.put("ProductManufacturer",product_manufacturer_modify.getText().toString());
            product_modify.put("ProductDescription",product_description_modify.getText().toString());
            product_modify.put("InStock",in_stock_modify.getSelectedItem().toString());
            product_modify.put("PID",documentReference.getId());
            product_modify.put("ProductDiscount",product_discount_modify.getText().toString());

            collectionReference.document(getIntent().getStringExtra("ID")).update(product_modify).addOnCompleteListener(task->{
                if(task.isSuccessful()){
                    Toast.makeText(this, "updated product successfully", Toast.LENGTH_SHORT).show();
                    product_name_modify.setText("");
                    Glide.with(getApplicationContext()).load("").into(product_image_modify);
                    product_prise_modify.setText("");
                    product_category_modify.setAdapter(null);
                    in_stock_modify.setAdapter(null);
                    product_manufacturer_modify.setText("");
                    product_description_modify.setText("");
                    product_discount_modify.setText("");
                }
            }).addOnFailureListener(failure->{
                Toast.makeText(this, "failed to update the product", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_PICK_CODE);

            }
        }
        else {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {

                openGallery();
            } else {

                Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri=data.getData();
            product_image_modify.setImageURI(imageUri);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        progress_bar_new_product_modify.setVisibility(View.INVISIBLE);
        String id=getIntent().getStringExtra("ID");
        collectionReference.document(id).get().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                HashMap<String,Object> data=new HashMap<String, Object>(task.getResult().getData());
                product_name_modify.setText(data.get("ProductName").toString());
                product_prise_modify.setText(data.get("ProductPrise").toString());
                product_manufacturer_modify.setText(data.get("ProductPrise").toString());
                product_description_modify.setText(data.get("ProductDescription").toString());
                product_discount_modify.setText(data.get("ProductDiscount").toString());
                

                Glide.with(getApplicationContext()).load(data.get("ProductImage")).into(product_image_modify);
                progress_bar_new_product_modify.setVisibility(View.INVISIBLE);
            }else{
                try {
                    throw task.getException();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }).addOnFailureListener(fail->{
            progress_bar_new_product_modify.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "failed to load product", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        progress_bar_new_product_modify.setVisibility(View.INVISIBLE);
    }

}