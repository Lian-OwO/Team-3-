package com.example.firebasetest.project3_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasetest.project3_android.Model.CategoryItem
import com.example.firebasetest.project3_android.R

class CategoryAdapter(private val items: List<CategoryItem>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    // 추가 시작
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(categoryItem: CategoryItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
    // 추가 끝

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        // 추가 시작
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }
        // 추가 끝

    }


    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryImageView: ImageView = itemView.findViewById(R.id.categoryImageView)
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)

        fun bind(item: CategoryItem) {
            categoryImageView.setImageResource(item.imageResId)
            categoryTextView.text = item.text
        }
    }
}