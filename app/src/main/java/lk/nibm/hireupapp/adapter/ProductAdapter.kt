package lk.nibm.hireupapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.model.Category
import lk.nibm.hireupapp.model.Hardware
import lk.nibm.hireupapp.model.HardwareProductsData

class ProductAdapter(private val productList: List<HardwareProductsData>) :RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    //private val productList: List<HardwareProductsData>,private val categoryList: List<Category>,private val hardwareList : List<Hardware>



    class ViewHolder(productView : View) : RecyclerView.ViewHolder(productView) {

        var productCard : CardView
        var productImage : ImageView
        var productName : TextView
  //      var hardwareName : TextView
//        var productPrice : TextView
//        var productQuantity : TextView
//        var productDescription : TextView
//        var productCategory : TextView

        init {
            productImage = productView.findViewById(R.id.product_image)
            productName = productView.findViewById(R.id.product_name)
            productCard = productView.findViewById(R.id.product_card)
  //          hardwareName = productView.findViewById(R.id.hardware_address_name)
//            productPrice = productView.findViewById(R.id.productPrice)
//            productQuantity = productView.findViewById(R.id.productQuantity)
//            productDescription = productView.findViewById(R.id.productDescription)
//            productCategory = productView.findViewById(R.id.productCategory)
        }
        fun bind(product : HardwareProductsData){
            Glide.with(itemView).load(product.productImage).into(productImage)
            productName.text = product.name
            //hardwareName.text = product.price

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var product = productList[position]
        holder.bind(product)

    }
}