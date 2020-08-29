package com.shouman.apps.thirdwayv.calac.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shouman.apps.thirdwayv.calac.adapter.CellGridAdapter
import com.shouman.apps.thirdwayv.calac.data.repository.MainRepository
import com.shouman.apps.thirdwayv.calac.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var mBinding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = MainFragmentBinding.inflate(inflater)

        mBinding.apply {
            cellsRecView.apply {
                adapter = CellGridAdapter(CellGridAdapter.OnCellClickListener {
                    viewModel?.removeCell(it)
                })
                setHasFixedSize(true)
            }
        }

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = MainViewModelFactory(MainRepository())
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        mBinding.lifecycleOwner = this

        mBinding.viewModel = viewModel
    }

}