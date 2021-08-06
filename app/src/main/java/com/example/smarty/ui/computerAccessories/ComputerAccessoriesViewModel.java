package com.example.smarty.ui.computerAccessories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComputerAccessoriesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ComputerAccessoriesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}