package com.example.smarty.ui.updatePassword;

import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UpdatePasswordViewModel extends ViewModel {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    CollectionReference collectionReference;


    private MutableLiveData<String> mText;

    public UpdatePasswordViewModel(){
        mText=new MutableLiveData<>();
        mText.setValue("Please check your internet connection");

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Users");
    }

    public LiveData<String> getText(){return mText;}
}