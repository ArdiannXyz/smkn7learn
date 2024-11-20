package com.example.smk7;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {
    private final MutableLiveData<ClassDataGuru> selectedData = new MutableLiveData<>();

    public void setSelectedData(ClassDataGuru data) {
        selectedData.setValue(data);
    }

    public LiveData<ClassDataGuru> getSelectedData() {
        return selectedData;
    }
}