package com.cygni.composeplaybox.presentation.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.cygni.composeplaybox.databinding.ClockFragmentBinding
import com.cygni.composeplaybox.presentation.compose.ClockScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClockComposeFragment : Fragment() {

    private var _binding: ClockFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)

        sharedElementReturnTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ClockFragmentBinding.inflate(inflater, container, false)

        binding.navigateToStart.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.clockCompose.setContent {
            ClockScreen(viewModel = viewModel())
        }
        return binding.root
    }

}