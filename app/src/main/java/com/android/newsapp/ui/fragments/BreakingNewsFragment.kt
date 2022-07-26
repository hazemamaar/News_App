package com.android.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newsapp.R
import com.android.newsapp.databinding.FragmentBreakingNewsBinding
import com.android.newsapp.ui.activities.MainActivity

import com.android.newsapp.ui.adapters.NewsAdapter
import com.android.newsapp.ui.viewmodel.NewsViewModel
import com.bumptech.glide.load.engine.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*


class BreakingNewsFragment : Fragment() {
    lateinit var newsViewModel: NewsViewModel
    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel
        setUpAdapter()

        newsViewModel.breakingNewsData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is com.android.newsapp.util.Resource.Success -> {
                    hideProgress()
                    response.data?.let { newsResponse ->
                        Log.i("hnhn", "onViewCreated: " + newsResponse.articles.get(0).description)
                        newsAdapter.artivles=newsResponse.articles.toList()
                        val totalPage = newsResponse.totalResults / 20  +2
                        isLastPage = newsViewModel.breakingNewPage==totalPage
                        if(isLastPage){
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is com.android.newsapp.util.Resource.Error -> {
                    hideProgress()
                    response.message?.let { message ->
                        Log.e("hhh", "An error occured: $message")
                    }
                }
                is com.android.newsapp.util.Resource.Loading -> {
                    ShowProgress()
                }
            }
        })
        newsAdapter.setOnItemClickListener { response ->

            Toast.makeText(requireContext(), response.description, Toast.LENGTH_SHORT).show()
            val bundle = bundleOf("article" to response)
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articlesFragment, bundle
            )
        }
    }

    private fun hideProgress() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading=false
    }

    private fun ShowProgress() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading=true
    }

    var isLoading = false
    var isScrolling = false
    var isLastPage = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount


            val isNotLoadingAndNotLastPage = !isLastPage && !isLastPage
            val isAtLastPage = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >=0
            val isTotalMoreThanVisible = totalItemCount >=20

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastPage && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate) {
                newsViewModel.getBreakingNews("us")
                isScrolling =false
            }
        }
    }
    fun setUpAdapter() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
           addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

}