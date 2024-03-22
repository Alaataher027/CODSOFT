package com.example.quoteofthedayapp.favorite

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quoteofthedayapp.R
import com.example.quoteofthedayapp.databinding.FragmentFavBinding

class FavFragment : Fragment() {

    private lateinit var viewBinding: FragmentFavBinding
    private lateinit var adapter: FavAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentFavBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FavAdapter(getFavoriteQuotes(requireContext()).toMutableSet())

        viewBinding.RVPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavFragment.adapter
        }

        updateEmptyViewVisibility()

        adapter.favoriteQuotes.forEach { Log.d("FavFragment", "Quote: $it") }

    }


    private fun getFavoriteQuotes(context: Context): Set<String> {
        val noFavsImageView = (context as? Activity)?.findViewById<ImageView>(R.id.no_favs)
        val sharedPreferences = context.getSharedPreferences("FavoriteQuotes", Context.MODE_PRIVATE)
        val quotesString = sharedPreferences.getString("quotes", null)

        val favoriteQuotes = if (quotesString != null) {
            noFavsImageView?.visibility = View.INVISIBLE
            // if quotesString is not null >>  convert it to a set
            HashSet(quotesString.split(";").filter { it.isNotBlank() })
        } else {
            // return an empty set
            mutableSetOf()
        }
        return favoriteQuotes
    }

    private fun updateEmptyViewVisibility() {
        val noFavsImageView = requireActivity().findViewById<ImageView>(R.id.no_favs)
        if (adapter.itemCount == 0 || adapter.favoriteQuotes.isEmpty()) {
            noFavsImageView?.visibility = View.VISIBLE
        } else {
            noFavsImageView?.visibility = View.INVISIBLE
        }
    }

}