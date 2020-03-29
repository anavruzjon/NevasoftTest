package com.nakhmadov.nevasofttest.ui.input_phone_number

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InputPhoneNumberViewModel : ViewModel() {

    private val _messageDialogState = MutableLiveData<String>()
    val messageDialogState: LiveData<String> = _messageDialogState

    private val _confirmPhoneNumberMessageDialog = MutableLiveData<String>()
    val confirmPhoneNumberMessageDialog: LiveData<String> = _confirmPhoneNumberMessageDialog

    fun showMessageDialog(text: String) {
        _messageDialogState.value = text
    }

    fun dismissMessageDialog() {
        _messageDialogState.value = null
    }

    fun showConfirmPhoneNumberMessageDialog(text: String) {
        _confirmPhoneNumberMessageDialog.value = text
    }

    fun dismissConfirmPhoneNumberMessageDialog() {
        _confirmPhoneNumberMessageDialog.value = null
    }
}
