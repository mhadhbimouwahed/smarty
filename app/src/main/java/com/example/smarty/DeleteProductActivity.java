package com.example.smarty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class DeleteProductActivity extends AppCompatActivity {

    private TextView search_delete_button;
    private EditText search_product_del_text;
    private RecyclerView items_to_delete;



    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    CollectionReference collectionReference;
    DocumentReference documentReference;

    ArrayList<Product> list;
    MyAdapter myAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);


        firestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        collectionReference=firestore.collection("Products/");
        documentReference=collectionReference.document();


        search_delete_button=findViewById(R.id.search_product_del_button);
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
        myAdapter=new MyAdapter(getApplicationContext(),list);
        items_to_delete.setAdapter(myAdapter);

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot dataSnapshot:task.getResult()){
                        Map<String, Object> data = dataSnapshot.getData();
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

                }else{
                    Toast.makeText(getApplicationContext(), "failed to load products", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
