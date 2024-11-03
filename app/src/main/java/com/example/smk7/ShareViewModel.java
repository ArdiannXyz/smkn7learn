package com.example.smk7;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smk7.classdataguru;

public class ShareViewModel extends ViewModel {
    private final MutableLiveData<classdataguru> selectedData = new MutableLiveData<>();

    public void setSelectedData(classdataguru data) {
        selectedData.setValue(data);
    }

    public LiveData<classdataguru> getSelectedData() {
        return selectedData;
    }
}