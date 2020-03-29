package com.nakhmadov.nevasofttest.ui.verify_number

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VerifyPhoneNumberViewModel : ViewModel() {

    companion object {
        const val countDownMillis = 1000L * 90
        const val countDownInterval = 1000L
    }

    private val _messageDialogState = MutableLiveData<String>()
    val messageDialogState: LiveData<String> = _messageDialogState

    private val _remainSeconds = MutableLiveData<String>()
    val remainSeconds: LiveData<String> = _remainSeconds

    private val _isTimerRunning = MutableLiveData<Boolean>()
    val isTimerRunning: LiveData<Boolean> = _isTimerRunning

    private var timer: CountDownTimer? = null

    init {
        startTimer()
        _isTimerRunning.value = true
    }

    fun showMessageDialog(text: String) {
        _messageDialogState.value = text
    }

    fun dismissMessageDialog() {
        _messageDialogState.value = null
    }

    fun startTimer() {
        if (_isTimerRunning.value != true) {
            timer?.cancel()
            _isTimerRunning.value = true
            timer = object : CountDownTimer(countDownMillis, countDownInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = (millisUntilFinished / 1000) % 60
                    val minutes = (millisUntilFinished / 1000) / 60
                    _remainSeconds.value = "$minutes:${if (seconds in 0..9) "0$seconds" else seconds}"
                }

                override fun onFinish() {
                    _remainSeconds.value = null
                    _isTimerRunning.value = false
                }

            }.start()
        }
    }

    fun clearTimer() {
        timer?.cancel()
        _isTimerRunning.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}
