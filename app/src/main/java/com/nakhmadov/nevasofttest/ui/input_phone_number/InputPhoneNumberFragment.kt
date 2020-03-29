package com.nakhmadov.nevasofttest.ui.input_phone_number

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog

import com.nakhmadov.nevasofttest.R
import com.nakhmadov.nevasofttest.databinding.FragmentInputPhoneNumberBinding

class InputPhoneNumberFragment : Fragment() {
    
    private lateinit var viewModel: InputPhoneNumberViewModel
    private lateinit var binding: FragmentInputPhoneNumberBinding

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
        binding = FragmentInputPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[InputPhoneNumberViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

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

        viewModel.confirmPhoneNumberMessageDialog.observe(viewLifecycleOwner, Observer {
            it?.let { phone ->
                MaterialDialog(context!!).show {
                    val message =
                        "<html>${resources.getString(R.string.confirmation_message_text_1)}<br/>" +
                                "<p><b>+${phone}</b></p><br/>" +
                                "<p>${resources.getString(R.string.confirmation_message_text_2)}</p></html>"

                    message(text = message) {
                        html()
                        viewModel.dismissConfirmPhoneNumberMessageDialog()
                    }
                    positiveButton(text = getString(R.string.ok)) { isTheNumberInDB(phone) }
                    negativeButton(text = getString(R.string.edit)) { viewModel.dismissConfirmPhoneNumberMessageDialog() }
                }.setOnCancelListener {
                    viewModel.dismissConfirmPhoneNumberMessageDialog()
                }
            }
        })

        binding.countryCodePicker.registerPhoneNumberTextView(binding.phoneNumberEditText)
        binding.nextButton.setOnClickListener {

            if (binding.countryCodePicker.isValid) {
                val phoneNumber = binding.countryCodePicker.fullNumber
                viewModel.showConfirmPhoneNumberMessageDialog(phoneNumber)
            } else {
                viewModel.showMessageDialog(text = getString(R.string.invalid_phone_number_warning))
            }
        }
    }

    private fun isTheNumberInDB(number: String) {
        // Check is the number in db
        if (number == getString(R.string.allowed_number))
            findNavController().navigate(
                InputPhoneNumberFragmentDirections.actionInputPhoneNumberFragmentToVerifyPhoneNumberFragment(number)
            )
        else
            viewModel.showMessageDialog(text = getString(R.string.no_such_number_in_db))
    }

}
