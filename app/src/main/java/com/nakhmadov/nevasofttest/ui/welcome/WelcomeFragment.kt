package com.nakhmadov.nevasofttest.ui.welcome


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.nakhmadov.nevasofttest.R
import com.nakhmadov.nevasofttest.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var viewModel: WelcomeViewModel
    private lateinit var binding: FragmentWelcomeBinding
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
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[WelcomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        phoneNumber = WelcomeFragmentArgs.fromBundle(arguments!!).phoneNumber

        binding.verifyPhoneText.text = resources.getString(R.string.welcome, phoneNumber)
        binding.popupMenu.setOnClickListener { showFilterPopupMenu() }
    }


    private fun showFilterPopupMenu() {
        val view = activity?.findViewById<View>(R.id.popup_menu) ?: return
        val popupMenu = PopupMenu(context!!, view)

        popupMenu.run {
            inflate(R.menu.menu)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.logout) {
                    findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToInputPhoneNumberFragment())
                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener false
            }
            show()
        }
    }
}
