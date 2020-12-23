package com.example.listadapter_infinityscroll

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadapter_infinityscroll.databinding.FragmentMainBinding

class MainFragment : Fragment(), MainListAdapter.LoadMoreDispatcher {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var mainListAdapter: MainListAdapter

    companion object {
        private val TAG = this.javaClass.name + " mori"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        viewModel = MainViewModel()
        return binding.apply {
            viewModel = this@MainFragment.viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                adapter = MainListAdapter(
                    viewLifecycleOwner,
                    this@MainFragment.viewModel
                ).apply {
                    mainListAdapter = this
                }
            }
        }

        mainListAdapter.setDispatcher(this@MainFragment)

        viewModel.apply {
            init()
            list.observe(viewLifecycleOwner, {
                Log.d(TAG, "list changed!, list: $it")
                mainListAdapter.submitList(it)
            })
        }
    }

    override fun onLoadMore() {
        viewModel.fetchList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainListAdapter.releaseDispatcher()
    }
}