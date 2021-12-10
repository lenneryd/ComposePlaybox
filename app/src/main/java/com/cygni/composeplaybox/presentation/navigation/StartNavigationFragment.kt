package com.cygni.composeplaybox.presentation.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.cygni.composeplaybox.databinding.StartNavigationFragmentBinding
import com.cygni.composeplaybox.presentation.compose.ComposeActivity
import com.cygni.composeplaybox.presentation.compose.ComposeYuActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartNavigationFragment : Fragment() {
    private var _binding: StartNavigationFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.i(StartNavigationFragment::class.simpleName, "Creating StartNavigationFragment")
        _binding = StartNavigationFragmentBinding.inflate(inflater, container, false)

        val extras = FragmentNavigatorExtras(binding.image to "image")
        binding.navigateToClock.setOnClickListener {
            findNavController().navigate(
                directions = StartNavigationFragmentDirections.actionStartNavigationFragmentToClockComposeFragment(),
                navigatorExtras = extras
            )
        }

        binding.navigateToCompose.setOnClickListener {
            startActivity(Intent(requireContext(), ComposeActivity::class.java))
        }

        binding.navigateToComposeYu.setOnClickListener {
            startActivity(Intent(requireContext(), ComposeYuActivity::class.java))
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}