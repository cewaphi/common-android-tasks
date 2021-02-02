package com.example.datasharing.ui.export

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExportViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Export"
    }
    val text: LiveData<String> = _text
}