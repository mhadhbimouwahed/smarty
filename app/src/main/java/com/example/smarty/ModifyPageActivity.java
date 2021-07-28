package com.example.smarty;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.Provider;

public class ModifyPageActivity extends AppCompatActivity {

    private EditText product_name_modify;
    private EditText product_prise_modify;
    private Spinner product_category_modify;
    private ImageView product_image_modify;
    private EditText product_manufacturer_modify;
    private EditText product_description_modify;
    private Spinner in_stock_modify;
    private ProgressBar progress_bar_new_product_modify;

    public Uri imageUri;
    public static final int IMAGE_PICK_CODE=1000 ;
    public static final int PERMISSION_CODE = 1001;
    private static final String[] categories={"","portable computers","smart phones","accessories"};
    private static final String[] instock_yes_no={"","yes","no"};

    public StorageReference storageReference;
    public String downloadImageUrl;
    private DocumentReference documentReference;





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
        
        
        TextView modify_product_button = findViewById(R.id.modify_product_button);
        progress_bar_new_product_modify=findViewById(R.id.progress_bar_new_product);

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Products");

        FirebaseStorage storage = FirebaseStorage.getInstance();

        documentReference= collectionReference.document();
        storageReference= storage.getReference().child("Images/"+documentReference.getId());

        ArrayAdapter<String> adapter_category=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,categories);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_category_modify.setAdapter(adapter_category);

        ArrayAdapter<String> adapter_in_stock=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,instock_yes_no);
        adapter_in_stock.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        in_stock_modify.setAdapter(adapter_in_stock);
        
        modify_product_button.setOnClickListener(x-> {
                

        });
        
        

    }
}