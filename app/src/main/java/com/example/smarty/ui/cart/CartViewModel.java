package com.example.smarty.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class CartViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CartViewModel(){
        mText=new MutableLiveData<>();
        mText.setValue("Please check your internet connection");
    }

    public LiveData<String> getText(){return mText;}
}