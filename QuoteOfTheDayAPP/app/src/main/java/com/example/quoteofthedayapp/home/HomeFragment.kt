package com.example.quoteofthedayapp.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.quoteofthedayapp.databinding.FragmentHomeBinding
import com.example.quoteofthedayapp.favorite.FavFragment
import java.util.*
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

class HomeFragment : Fragment() {
    lateinit var viewModel: HomeViewModel
    private lateinit var favFragment: FavFragment

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        sharedPreferences =
            requireContext().getSharedPreferences("HomeFragmentPrefs", Context.MODE_PRIVATE)

        binding.favBtn.setOnClickListener {
            val quote = binding.text.text.toString()
            saveFavoriteQuote(requireContext(), quote)
            val scaleUpAnimation = ScaleAnimation(
                1f, 1.2f,
                1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            scaleUpAnimation.duration = 200
            scaleUpAnimation.fillAfter = true

            binding.favBtn.startAnimation(scaleUpAnimation)
        }

        binding.favBtn.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val scaleDownAnimation = ScaleAnimation(
                    1.2f, 1f,
                    1.2f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                scaleDownAnimation.duration = 200
                scaleDownAnimation.fillAfter = true

                binding.favBtn.startAnimation(scaleDownAnimation)
            }
            false
        }

        binding.shareBtn.setOnClickListener {
            shareQuote(binding.text.text.toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        updateQuoteIfNeeded(binding, requireContext())
        favFragment = FavFragment()
    }


    private fun shareQuote(quote: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, quote)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun updateQuoteIfNeeded(binding: FragmentHomeBinding, context: Context) {
        val sharedPreferences =
            context.getSharedPreferences("QuotePreferences", Context.MODE_PRIVATE)
        val storedQuote = sharedPreferences.getString("quote", null)
        val lastUpdateDate = sharedPreferences.getLong("lastUpdateDate", 0)

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        val isQuoteUpdateNeeded = storedQuote == null || lastUpdateDate < today

        if (isQuoteUpdateNeeded) {
            viewModel.loadQuote(
                context,
                category = "happiness",
                onDataLoaded = { quote ->
                    quote?.let {
                        sharedPreferences.edit {
                            putString("quote", it.quote)
                            putLong("lastUpdateDate", today)
                        }
                        binding.text.text = it.quote
                    }
                },
                onError = { errorMessage ->
                    Log.e("HomeFragment", "Failed to fetch quote: $errorMessage")
                }
            )
        } else {
            storedQuote?.let {
                binding.text.text = it
            }
        }
    }


    private fun saveFavoriteQuote(context: Context, quote: String) {
        val sharedPreferences = context.getSharedPreferences("FavoriteQuotes", Context.MODE_PRIVATE)
        val storedQuotesString = sharedPreferences.getString("quotes", null)
        val quotesSet: MutableSet<String> = if (storedQuotesString != null) {
            HashSet(listOf(storedQuotesString))
        } else {
            // empty set
            mutableSetOf()
        }

        quotesSet.add(quote)
        sharedPreferences.edit {
            putString(
                "quotes",
                quotesSet.joinToString(separator = ";")
            ) // storing as ';' separated string
        }
    }
}