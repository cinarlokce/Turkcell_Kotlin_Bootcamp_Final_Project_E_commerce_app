package com.works.final_exam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.works.final_exam.R
import com.works.final_exam.models.CartItem

class CartItemAdapter(private val cartItems: List<CartItem>) :
    RecyclerView.Adapter<CartItemAdapter.CartViewHolder>() {

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cartThumbnail:ImageView = itemView.findViewById(R.id.imageViewThumbnail)
        private val cartItemTitle:TextView = itemView.findViewById(R.id.textViewTitle)
        private val cartItemPrice:TextView = itemView.findViewById(R.id.textViewPrice)
        private val cartItemQuantity:TextView = itemView.findViewById(R.id.textViewQuantity)
        private val cartItemTotal:TextView = itemView.findViewById(R.id.textViewTotal)
        private val cartItemDiscount:TextView = itemView.findViewById(R.id.textViewDiscount)
        private val cartItemDiscountedPrice:TextView = itemView.findViewById(R.id.textViewDiscountedPrice)

        fun bind(cartItem: CartItem) {
            cartItemTitle.text = cartItem.title
            cartItemPrice.text = cartItem.price.toString()+" $"
            cartItemQuantity.text = "Quantity: "+cartItem.quantity.toString()
            cartItemTotal.text = "Total: "+cartItem.total.toString()+" $"
            cartItemDiscount.text = "${cartItem.discountPercentage}%"
            cartItemDiscountedPrice.text = "Discounted Price: "+cartItem.discountedPrice.toString()+" $"

            Picasso.get()
                .load(cartItem.thumbnail)
                .centerCrop()
                .resize(24,24)
                .into(cartThumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_design, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}