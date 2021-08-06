package com.example.smarty.ui.portableComputers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PortableComputersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PortableComputersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}