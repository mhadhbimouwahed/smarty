package com.example.smarty.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smarty.CartItemsAdapter;
import com.example.smarty.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class CartViewModel extends ViewModel {

    FirebaseFirestore firestore;
    CollectionReference collectionReference;

    ArrayList<Product> list;
    CartItemsAdapter cartItemsAdapter;

    private MutableLiveData<String> mText;

    public CartViewModel(){
        mText=new MutableLiveData<>();
        mText.setValue("Please check your internet connection");

        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Cart");
    }

    public LiveData<String> getText(){return mText;}
}