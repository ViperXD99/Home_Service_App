package lk.nibm.hireupapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lk.nibm.hireupapp.model.CartItem
import lk.nibm.hireupapp.databinding.ItemCartBinding

class CartItemAdapter(private val cartItems: List<CartItem>) :
    RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            // Bind the data to the views in the item_cart.xml layout
            binding.productName.text = cartItem.productName
            binding.productPrice.text = cartItem.productPrice
            binding.productQuantity.text = cartItem.quantity.toString()

            // Load the product image using Glide or any other image loading library
            Glide.with(binding.productImage.context)
                .load(cartItem.productImage)
                .into(binding.productImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
