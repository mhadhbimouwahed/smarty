package com.example.smarty.ui.smartPhones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smarty.ClientAdapter;
import com.example.smarty.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SmartPhonesViewModel extends ViewModel {

    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    ArrayList<Product> list;
    ClientAdapter clientAdapter;

    private MutableLiveData<String> mText;

    public SmartPhonesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Please check your internet connection");
        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Products/");
    }

    public LiveData<String> getText() {
        return mText;
    }
}