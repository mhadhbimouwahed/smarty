package com.example.smarty;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DeleteProductActivity extends AppCompatActivity {

    private EditText search_product_del_text;
    private RecyclerView items_to_delete;



    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    
    ArrayList<Product> list;
    DeleteAdapter deleteAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);


        firestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        collectionReference=firestore.collection("Products/");
        documentReference=collectionReference.document();


        TextView search_delete_button = findViewById(R.id.search_product_del_button);
        search_product_del_text=findViewById(R.id.search_product_del_text);
        items_to_delete=findViewById(R.id.items_to_delete);



        search_delete_button.setOnClickListener(x->{
            if(search_product_del_text.getText().length()==0){
                search_product_del_text.setError("This field cannot be empty");
            }
        });

    }

    private void deleteProduct(){

    }

    @Override
    protected void onStart() {
        super.onStart();
        
        items_to_delete.setHasFixedSize(true);
        items_to_delete.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list=new ArrayList<>();
        deleteAdapter=new DeleteAdapter(getApplicationContext(),list);
        
        items_to_delete.setAdapter(deleteAdapter);
        collectionReference.get().addOnCompleteListener(task->{
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
                    deleteAdapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(fail->{
            Toast.makeText(this, "failed to load products", Toast.LENGTH_SHORT).show();
        });
        
    }
}
