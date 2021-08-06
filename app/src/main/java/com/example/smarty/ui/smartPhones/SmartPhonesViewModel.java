package com.example.smarty.ui.smartPhones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SmartPhonesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SmartPhonesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}