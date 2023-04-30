package com.example.drawa.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.drawa.DrawableItems.Edit_Product
import com.example.drawa.R
import com.example.drawa.databinding.ProductItemBinding
import com.example.drawa.modal.ProductModal

 class ProductRecyclerAdapter(private val context: Context, private val listener: OnItemClickListener,
                                      private val productItem : ArrayList<ProductModal>) : RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding : ProductItemBinding = ProductItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))

        return view
    }

    override fun getItemCount(): Int {
        return productItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productItem[position]
        Glide.with(context).load(product.productImageUrl).listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                holder.binding.progressBar.visibility = View.GONE
               return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                holder.binding.progressBar.visibility = View.GONE
                return false
            }

        }) .into(holder.binding.productImageView)
        holder.binding.productNameTextView.text = product.productName
        holder.binding.priceTextView.text = product.productPrice
        holder.binding.descriptionTextView.text = product.productDescription
        holder.binding.quantityTextView.text = product.productQuantity
        holder.itemView.setOnLongClickListener {
            listener.onItemClick(product.productId.toString())
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, Edit_Product::class.java)
            intent.putExtra("productId", product.productId)
            intent.putExtra("productQuantity", product.productQuantity)
            intent.putExtra("productPrice", product.productPrice)
            intent.putExtra("productName", product.productName)
            intent.putExtra("productDescription", product.productDescription)
            intent.putExtra("productImageUrl", product.productImageUrl)
            context.startActivity(intent)
        }
    }

}

interface OnItemClickListener {
    fun onItemClick(id: String):Boolean
}