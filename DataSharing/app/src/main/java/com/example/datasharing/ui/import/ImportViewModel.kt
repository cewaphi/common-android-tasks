package com.example.datasharing.ui.import

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImportViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Import"
    }
    val text: LiveData<String> = _text
}