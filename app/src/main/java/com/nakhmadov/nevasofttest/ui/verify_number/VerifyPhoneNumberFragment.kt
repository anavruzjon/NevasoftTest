package com.nakhmadov.nevasofttest.ui.verify_number

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.nakhmadov.nevasofttest.R
import com.nakhmadov.nevasofttest.databinding.FragmentVerifyPhoneNumberBinding


class VerifyPhoneNumberFragment : Fragment() {

    companion object {}

    private lateinit var viewModel: VerifyPhoneNumberViewModel
    private lateinit var binding: FragmentVerifyPhoneNumberBinding
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        }
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        phoneNumber = VerifyPhoneNumberFragmentArgs.fromBundle(arguments!!).phoneNumber

        viewModel = ViewModelProvider(this)[VerifyPhoneNumberViewModel::class.java]

        val verifyPhoneText = resources.getString(R.string.verify, phoneNumber)
        val verifyingText =
            "${resources.getString(R.string.verifying_text_1)} <b>+$phoneNumber</b> ${resources.getString(R.string.verifying_text_2)}"

        binding.verifyingText.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(verifyingText, Html.FROM_HTML_MODE_COMPACT)
            else Html.fromHtml(verifyingText)


        binding.verifyPhoneText.text = verifyPhoneText
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.wrongNumberText.setOnClickListener {
            findNavController().navigate(VerifyPhoneNumberFragmentDirections.actionVerifyPhoneNumberFragmentToInputPhoneNumberFragment())
        }

        binding.pinEntryEdit.setOnPinEnteredListener {
            if (it.toString() == "111111") {
                viewModel.clearTimer()
                findNavController().navigate(
                    VerifyPhoneNumberFragmentDirections.actionVerifyPhoneNumberFragmentToWelcomeFragment(phoneNumber)
                )
            } else {
                viewModel.showMessageDialog(text = resources.getString(R.string.failed_pin_code))
                binding.pinEntryEdit.text = null
            }
        }

        viewModel.messageDialogState.observe(viewLifecycleOwner, Observer {
            it?.let {
                MaterialDialog(context!!).show {
                    message(text = it)
                    positiveButton(text = getString(R.string.ok)) { viewModel.dismissMessageDialog() }
                }.setOnCancelListener {
                    viewModel.dismissMessageDialog()
                }
            }
        })

        viewModel.isTimerRunning.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    binding.resendImage.setImageDrawable(getDrawable(context!!, R.drawable.ic_message_non_active))
                    binding.resendText.setTextColor(getColor(context!!, R.color.textGrayColorLight))
                } else {
                    binding.resendImage.setImageDrawable(getDrawable(context!!, R.drawable.ic_message_active))
                    binding.resendText.setTextColor(getColor(context!!, R.color.primaryColor))
                }
            }
        })

    }

}
