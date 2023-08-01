package lk.nibm.hireupapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.ShopOrderMessages
import lk.nibm.hireupapp.model.Hardware
import lk.nibm.hireupapp.model.ShopOrderDetails
import lk.nibm.hireupapp.model.ShopOrders

class ShopOrdersAdapter(
    private val shopOrderList: List<ShopOrderDetails>,
    private val hardwareList: List<Hardware>,
): RecyclerView.Adapter<ShopOrdersAdapter.OrderViewHolder>() {
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {


        private val txtOrderStatus: TextView
        private val txtOrderMessage: TextView
        private val txtHardwareName: TextView
        private val txtOrderID: TextView

        init {
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus)
            txtOrderMessage = itemView.findViewById(R.id.txtOrderMessage)
            txtHardwareName = itemView.findViewById(R.id.txtHardwareName)
            txtOrderID = itemView.findViewById(R.id.txtOrderID)

        }
        fun bind(shopOrder: ShopOrderDetails, hardware: Hardware) {
            if(shopOrder.orderStatus == "Pending"){
                txtOrderStatus.text = ShopOrderMessages.placed
                txtOrderMessage.text = ShopOrderMessages.placedMessage
                txtOrderID.text = shopOrder.orderID
                txtHardwareName.text = hardware.name
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.orders_recycler_view_card, parent, false)
        return ShopOrdersAdapter.OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
       return shopOrderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val shopOrder = shopOrderList[position]
        val hardware = hardwareList[position]
        holder.bind(shopOrder,hardware)
    }
}