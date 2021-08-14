package com.example.smarty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.smarty.R.layout.activity_add_new_product;

public class AddNewProductActivity extends AppCompatActivity {
    private EditText product_name;
    private EditText product_prise;
    private Spinner product_category;
    private ImageView product_image;
    private EditText product_manufacturer;
    private EditText product_description;
    private Spinner in_stock;
    private EditText product_discount;
    private ProgressBar progress_bar_new_product;

    public Uri imageUri;
    public static final int IMAGE_PICK_CODE=1000 ;
    public static final int PERMISSION_CODE = 1001;
    private static final String[] category={"product category","portable computers","smart phones","accessories"};
    private static final String[] inStock={"in stock?","yes","no"};

    public StorageReference storageReference;
    public String downloadImageUrl;
    private DocumentReference documentReference;


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
        product_discount=findViewById(R.id.product_discount);
        in_stock=findViewById(R.id.in_stock);
        TextView save_product = findViewById(R.id.save_product);
        progress_bar_new_product=findViewById(R.id.progress_bar_new_product);

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Products");

        FirebaseStorage storage = FirebaseStorage.getInstance();

        documentReference= collectionReference.document();
        storageReference= storage.getReference().child("Images/"+documentReference.getId());

        ArrayAdapter<String> adapter_category=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,category);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_category.setAdapter(adapter_category);

        ArrayAdapter<String> adapter_in_stock=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,inStock);
        adapter_in_stock.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        in_stock.setAdapter(adapter_in_stock);

        





        save_product.setOnClickListener(x->{
            if(product_name.getText().toString().length()==0){
                product_name.setError("This field cannot be empty");
            }else if(product_prise.getText().toString().length()==0){
                product_prise.setError("This field cannot be empty");
            }else if(product_category.getSelectedItem().equals(null)){
                Toast.makeText(this, "you need to select item category first", Toast.LENGTH_LONG).show();
            }else if(product_manufacturer.getText().toString().length()==0){
                product_manufacturer.setError("This field cannot be empty");
            }else if(product_description.getText().toString().length()==0){
                product_description.setError("This field cannot be empty");
            }else if(in_stock.getSelectedItem().equals(null)){
                Toast.makeText(this, "you need to select whether the product is in stock or not", Toast.LENGTH_SHORT).show();
            }else if(product_image==null) {
                Toast.makeText(this, "you need to select an image for the product", Toast.LENGTH_SHORT).show();
            }else if(product_discount.getText().toString().length()==0){
                product_discount.setError("This field cannot be empty");
            }else{
                progress_bar_new_product.setVisibility(View.VISIBLE);
                addNewProduct();
            }
        });

        product_image.setOnClickListener(x->{
            Toast.makeText(this, "choose an image for your product", Toast.LENGTH_SHORT).show();
            openGallery();

        });

    }
    private void addNewProduct() {

        final UploadTask uploadTask=storageReference.putFile(imageUri);
        Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                downloadImageUrl=storageReference.getDownloadUrl().toString();
                System.out.println(downloadImageUrl);
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        HashMap<String,Object> product=new HashMap<>();
                        product.put("ProductName",product_name.getText().toString());
                        product.put("ProductImage",downloadUri.toString());
                        product.put("ProductPrise",product_prise.getText().toString());
                        product.put("ProductCategory",product_category.getSelectedItem().toString());
                        product.put("ProductManufacturer",product_manufacturer.getText().toString());
                        product.put("ProductDescription",product_description.getText().toString());
                        product.put("InStock",in_stock.getSelectedItem().toString());
                        product.put("PID",documentReference.getId());
                        product.put("ProductDiscount",product_discount.getText().toString());
                        documentReference.set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(AddNewProductActivity.this, "product added successfully", Toast.LENGTH_SHORT).show();
                                    product_name.setText("");
                                    product_image.setImageURI(null);
                                    product_prise.setText("");
                                    product_category.setAdapter(null);
                                    in_stock.setAdapter(null);
                                    product_manufacturer.setText("");
                                    product_description.setText("");
                                    product_discount.setText("");
                                    progress_bar_new_product.setVisibility(View.INVISIBLE);
                                }else{
                                    AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
                                    builder.create();
                                    builder.setTitle("Error");
                                    builder.setMessage("failed to add a new product");
                                    builder.setPositiveButton("Okay",((dialog, which) -> dialog.dismiss()));
                                    builder.show();
                                }

                            }
                        });

                }else{
                    Toast.makeText(AddNewProductActivity.this, "failed to upload the image", Toast.LENGTH_SHORT).show();
                    progress_bar_new_product.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void openGallery(){
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
            product_image.setImageURI(imageUri);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        progress_bar_new_product.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        progress_bar_new_product.setVisibility(View.INVISIBLE);
    }
}