package com.potatomeme.jsoupmovieapp.ui.view

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.potatomeme.jsoupmovieapp.R
import com.potatomeme.jsoupmovieapp.data.model.DailyBoxOffice
import com.potatomeme.jsoupmovieapp.data.model.MovieTier
import com.potatomeme.jsoupmovieapp.databinding.FragmentTierBinding
import com.potatomeme.jsoupmovieapp.ui.adapter.ApiTierListAdapter
import com.potatomeme.jsoupmovieapp.ui.adapter.TierListAdapter
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class TierFragment : Fragment() {

    private var _binding: FragmentTierBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private lateinit var tierListAdapter: TierListAdapter
    private lateinit var apiTierListAdapter: ApiTierListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tier, container, false)
        return binding.root
    }

    val mFormat = SimpleDateFormat("yyyyMMdd")

    var yesterDay = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tierFragment = this
        viewModel = (activity as MainActivity).viewModel

        (binding.rvApiForm as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        (binding.rvForm as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE,-1)
        yesterDay = mFormat.format(cal.time)
        binding.tvDate.text = yesterDay

        binding.btnDateSelect.setOnClickListener {
            val dlg = DateDialog(activity as MainActivity, binding.tvDate.text.toString())
            dlg.setOnOKClickedListener { dateStr ->
                if (yesterDay.toInt() > dateStr.toInt()){
                    Toast.makeText(context as MainActivity,"오늘을 포함한 이후의 날짜로는 검색을 할수 없습니다",Toast.LENGTH_SHORT).show()
                    Toast.makeText(context as MainActivity,"검색 날짜를 $yesterDay 로 변경합니다.",Toast.LENGTH_SHORT).show()
                    binding.tvDate.text = yesterDay
                }else{
                    binding.tvDate.text = dateStr
                }
                viewModel.getRanking(binding.tvDate.text.toString())
            }
            dlg.show()
        }

        viewModel.searchApiList.observe(viewLifecycleOwner){
            Log.d(TAG, "viewModel.searchApiList.observe: ${it.boxOfficeResult.dailyBoxOfficeList}")
            apiTierListAdapter.submitList(it.boxOfficeResult.dailyBoxOfficeList)
        }
        
        
        //viewModel.searchTier(0)
        setupSpinner()
        setupRecyclerView()
        viewModel.tierList.observe(viewLifecycleOwner){
            Log.d(TAG, "tierList changed ${it}")
            tierListAdapter.submitList(it)
        }
    }

    private fun setupSpinner() {
        val sort_list = listOf("API Ranking","실시간 조회순", "평정순(현재 상영영화)", "평점순(모든영화)")
        val adapter_spinner =
            ArrayAdapter(context as MainActivity, R.layout.itme_dropdown, sort_list)
        binding.spinner.setAdapter(adapter_spinner)
        binding.spinner.onItemSelectedListener =  object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "onItemSelected: $position")
                if (position == 0) {
                    binding.rvApiForm.setHeightMatchParent()
                    binding.rvForm.setHeightZero()
                    viewModel.getRanking(date = binding.tvDate.text.toString())
                }else{
                    binding.rvApiForm.setHeightZero()
                    binding.rvForm.setHeightMatchParent()
                    viewModel.searchTier(position)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setupRecyclerView() {
        apiTierListAdapter = ApiTierListAdapter()
        binding.rvApiTierlist.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            apiTierListAdapter.setOnItemClickListener( object :ApiTierListAdapter.OnItemClickListener{
                override fun onItemClick(v: View, dailyBoxOffice: DailyBoxOffice, pos: Int) {
                    Log.d(TAG, "onItemClick: $dailyBoxOffice")
                }
            })
            adapter = apiTierListAdapter
        }


        tierListAdapter = TierListAdapter()
        binding.rvTierlist.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            tierListAdapter.setOnItemClickListener( object :TierListAdapter.OnItemClickListener{
                override fun onItemClick(v: View, movieTier: MovieTier, pos: Int) {
                    val action = TierFragmentDirections.actionFragmentTierToMovieFragment(movieTier.url)
                    findNavController().navigate(action)
                }
            })

            adapter = tierListAdapter
        }
    }

    fun LinearLayout.setHeightZero() {
        var layoutParam = this.layoutParams
        layoutParam.height = 0
        this.layoutParams = layoutParam
    }

    fun LinearLayout.setHeightMatchParent() {
        var layoutParam = this.layoutParams
        layoutParam.height = LinearLayout.LayoutParams.MATCH_PARENT
        this.layoutParams = layoutParam
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object{
        private const val TAG = "StartFragment"
    }
}