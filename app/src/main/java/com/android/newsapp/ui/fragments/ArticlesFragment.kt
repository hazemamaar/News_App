package com.android.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.newsapp.R
import com.android.newsapp.databinding.FragmentArticlesBinding
import com.android.newsapp.databinding.FragmentBreakingNewsBinding
import com.android.newsapp.ui.activities.MainActivity
import com.android.newsapp.ui.adapters.NewsAdapter
import com.android.newsapp.ui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class ArticlesFragment : Fragment() {

    lateinit var newsViewModel: NewsViewModel
    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!
    lateinit var newsAdapter: NewsAdapter
   val args: ArticlesFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel
        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
        binding.fab.setOnClickListener(View.OnClickListener {
            newsViewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        })
        binding.backIcon.setOnClickListener(View.OnClickListener {
             findNavController().popBackStack()

        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       _binding = FragmentArticlesBinding.inflate(inflater,container,false)
        return  binding.root
    }

}