package com.example.smarty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DeleteProductActivity extends AppCompatActivity {

    private TextView search_delete_button;
    private EditText search_product_del_text;
    private ListView list_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        search_delete_button=findViewById(R.id.search_delete_button);
        search_product_del_text=findViewById(R.id.search_product_del_text);
        list_delete=findViewById(R.id.list_delete);

        search_delete_button.setOnClickListener(x->{
            if(search_product_del_text.getText().toString().length()==0){
                search_product_del_text.setError("This field cannot be empty");
            }else{
                deleteProduct();
            }
        });

    }

    private void deleteProduct(){

    }
}