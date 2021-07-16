package com.example.smarty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ModifyProductActivity extends AppCompatActivity {
    private EditText search_product_text;
    private TextView search_product_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_product);
        search_product_button=findViewById(R.id.search_product_button);
        search_product_text=findViewById(R.id.search_product_text);

        search_product_button.setOnClickListener(x->{
            if(search_product_text.getText().toString().length()==0){
                search_product_text.setError("This field cannot be empty");
            }else{

            }
        });
    }

}