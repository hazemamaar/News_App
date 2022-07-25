package com.android.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.newsapp.databinding.FragmentBreakingNewsBinding
import com.android.newsapp.databinding.FragmentSearchBinding
import com.android.newsapp.ui.activities.MainActivity
import com.android.newsapp.ui.adapters.NewsAdapter
import com.android.newsapp.ui.viewmodel.NewsViewModel
import com.android.newsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    lateinit var newsViewModel: NewsViewModel
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel
        setUpAdapter()
        var job: Job? = null

        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                         newsViewModel.getSearchingNews(editable.toString())
                    }

                }
            }
        }
//        binding.etSearch.addTextChangedListener{
//            job?.cancel()
//
//        }
        newsViewModel.searchingNewsData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                        newsAdapter.artivles = newsResponse.articles
                    }
                }

                else -> {}
            }


        })

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    fun setUpAdapter() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }
}