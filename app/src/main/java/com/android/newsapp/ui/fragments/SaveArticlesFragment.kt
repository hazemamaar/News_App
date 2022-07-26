package com.android.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newsapp.R
import com.android.newsapp.databinding.FragmentBreakingNewsBinding
import com.android.newsapp.databinding.FragmentSaveArticlesBinding
import com.android.newsapp.ui.activities.MainActivity
import com.android.newsapp.ui.adapters.NewsAdapter
import com.android.newsapp.ui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SaveArticlesFragment : Fragment() {

    private var _binding:FragmentSaveArticlesBinding?=null
    private val binding get() = _binding!!
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel =(activity as MainActivity).newsViewModel
        setUpAdapter()

        val itemTouchHelperCallBack= object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val position =viewHolder.adapterPosition
                val article =newsAdapter.artivles[position]
                newsViewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        newsViewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }
        newsViewModel.getAllArticle().observe(viewLifecycleOwner, Observer {
            newsAdapter.artivles=it
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }
    fun setUpAdapter() {
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}