package com.example.quoteofthedayapp.favorite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteofthedayapp.R
import com.example.quoteofthedayapp.databinding.FavItemBinding

class FavAdapter(var favoriteQuotes: MutableSet<String>) :
    RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = FavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favoriteQuotes.size
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(favoriteQuotes.elementAt(position))
    }

    inner class FavViewHolder(private val binding: FavItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quote: String) {
            binding.text.text = quote
            binding.unFav.setOnClickListener {
                // remove the quote from sharedPreferences and update recyclerView
                removeQuote(quote)
            }

            binding.shareBtn.setOnClickListener {
                shareQuote(quote, binding.root.context)
            }
        }

        private fun removeQuote(quote: String) {
            val context = binding.root.context
            val sharedPreferences = context.getSharedPreferences("FavoriteQuotes", Context.MODE_PRIVATE)
            val quotesString = sharedPreferences.getString("quotes", "") ?: ""
            val quotesSet = quotesString.split(";").toMutableSet()
            quotesSet.remove(quote)
            sharedPreferences.edit {
                putString("quotes", quotesSet.joinToString(separator = ";"))
            }

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                favoriteQuotes.remove(quote)
                notifyItemRemoved(position)

                if (favoriteQuotes.isEmpty()) {
                    (binding.root.context as? Activity)?.findViewById<ImageView>(R.id.no_favs)?.visibility = View.VISIBLE
                }

                notifyDataSetChanged()
            }
        }

        private fun shareQuote(quote: String, context: Context) {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, quote)
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_quote)))
        }

    }
}