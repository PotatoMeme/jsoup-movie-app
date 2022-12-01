package com.potatomeme.jsoupmovieapp.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.potatomeme.jsoupmovieapp.R
import com.potatomeme.jsoupmovieapp.databinding.FragmentStartBinding
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModel

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.starFragment = this
        viewModel = (activity as MainActivity).viewModel

//        viewModel.tierList.observe(viewLifecycleOwner){
//            Log.d(TAG, "tierList changed ${it}")
//        }
    }

    fun btn1Click() {
        Toast.makeText(context, "btn1 Clicked", Toast.LENGTH_LONG).show()
        val action = StartFragmentDirections.actionFragmentStartToFragmentTier()
        findNavController().navigate(action)

        //viewModel.searchTier()
    }

    fun btn2Click() {
        Toast.makeText(context, "btn2 Clicked", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object{
        private const val TAG = "StartFragment"
    }
}