package com.example.smarty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



import java.util.ArrayList;
import java.util.Map;

public class ModifyProductActivity extends AppCompatActivity {
    private EditText search_product_text;
    private TextView search_product_button;
    private RecyclerView items_to_modify;


    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;

    ArrayList<Product> list;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_product);

        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Products/");
        documentReference=collectionReference.document();

        search_product_button=findViewById(R.id.search_product_button);
        search_product_text=findViewById(R.id.search_product_text);
        items_to_modify=findViewById(R.id.items_to_modify);

        search_product_button.setOnClickListener(x->{
            if(search_product_text.getText().toString().length()==0){
                search_product_text.setError("This field cannot be empty");
            }else{
                modifyProduct();
            }
        });
    }

    private void modifyProduct() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        items_to_modify.setHasFixedSize(true);
        items_to_modify.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list=new ArrayList<>();
        myAdapter=new MyAdapter(getApplicationContext(),list);
        items_to_modify.setAdapter(myAdapter);

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        Map<String,Object> data=documentSnapshot.getData();
                        Product product=new Product(data.get("ProductName"),
                                data.get("ProductPrise"),
                                data.get("ProductDescription"),
                                data.get("ProductCategory"),
                                data.get("ProductImage"),
                                data.get("ProductManufacturer"),
                                data.get("InStock"));
                        list.add(product);
                        myAdapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(ModifyProductActivity.this, "failed to load products", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}