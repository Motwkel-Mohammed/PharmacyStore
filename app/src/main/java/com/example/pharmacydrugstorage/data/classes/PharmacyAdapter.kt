/**
 *  created by: Motwkel-Idris, 17-1-2023
 * In this class, we prepare the medicine data and send it
 * to the recyclerview to view it
 **/

package com.example.pharmacydrugstorage.data.classes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Color
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacydrugstorage.R
import com.example.pharmacydrugstorage.activities.MainActivity
import com.example.pharmacydrugstorage.activities.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_details.view.*

@Suppress("DEPRECATION")
class PharmacyAdapter(
    private var items: MutableList<Pharmacy>,
    private val listener: RowClickListener
) : RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder>() {

    private lateinit var mPharmacyViewModel: PharmacyViewModel
    private var remove = 0

    class PharmacyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        // prepare the views
        val drugName: TextView = itemView.findViewById(R.id.drug_name)
        val drugPrice: TextView = itemView.findViewById(R.id.drug_price)
        val drugNumber: TextView = itemView.findViewById(R.id.drug_number)
        val drugType: ImageView = itemView.findViewById(R.id.drug_type)
        val card: CardView = itemView.findViewById(R.id.card_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.drug_item, parent, false)
        return PharmacyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PharmacyViewHolder, position: Int) {
        val current = items[position]

        // display the result of the database in the views
        holder.drugName.text = current.drugName
        holder.drugPrice.text = "${current.drugPrice} $"
        val type = current.drugType
        // drug number comes in this schema: (quantity measurement) e.g: (100 ml)
        val number = current.drugNumber
        if (number.isNotBlank()) {
            holder.drugNumber.text = "($number)"
        } else {
            holder.drugNumber.text = "(_ _)"
        }
        holder.card.setCardBackgroundColor(Color.parseColor("#a5ffd6"))

        // setup the icon for each medicine type
        when (type) {
            "Tablet" -> {
                holder.drugType.setImageResource(R.drawable.medical_pill)
//                holder.card.setCardBackgroundColor(Color.parseColor("#61a5c2"))
            }
            "Injection" -> {
                holder.drugType.setImageResource(R.drawable.medical_needle)
//                holder.card.setCardBackgroundColor(Color.parseColor("#b5e48c"))
            }
            "Syrup" -> {
                holder.drugType.setImageResource(R.drawable.medical_bottel)
//                holder.card.setCardBackgroundColor(Color.parseColor("#d8e2dc"))
            }
            "Infusion" -> {
                holder.drugType.setImageResource(R.drawable.medical_drip)
//                holder.card.setCardBackgroundColor(Color.parseColor("#bad7f2"))
            }
            "Spray" -> {
                holder.drugType.setImageResource(R.drawable.medicine_spray)
//                holder.card.setCardBackgroundColor(Color.parseColor("#8e94f2"))
            }
            "Cream" -> {
                holder.drugType.setImageResource(R.drawable.medicine_cream)
//                holder.card.setCardBackgroundColor(Color.parseColor("#d2b7e5"))
            }
            "Powder" -> {
                holder.drugType.setImageResource(R.drawable.medicine_powder)
//                holder.card.setCardBackgroundColor(Color.parseColor("#f38375"))
            }
            "Drop" -> {
                holder.drugType.setImageResource(R.drawable.medicine_drop)
//                holder.card.setCardBackgroundColor(Color.parseColor("#caf0f8"))
            }
            "Suppository" -> {
                holder.drugType.setImageResource(R.drawable.medicine_suppository)
//                holder.card.setCardBackgroundColor(Color.parseColor("#d55d92"))
            }
            "Other" -> {
                holder.drugType.setImageResource(R.drawable.medicine_other)
//                holder.card.setCardBackgroundColor(Color.parseColor("#a5ffd6"))
            }
        }

        //onItemClickListener
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(current)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(pharmacy: List<Pharmacy>) {
        // display/update the result list
        this.items = (pharmacy) as MutableList<Pharmacy>
        notifyDataSetChanged()
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder, context: MainActivity) {
        val removedPosition = viewHolder.adapterPosition
        val currentItem = items[removedPosition]

        // delete item
        items.removeAt(removedPosition)
        notifyItemRemoved(removedPosition)

        // reload/insert the deleted item
        Snackbar.make(
            viewHolder.itemView,
            "${context.getString(R.string.delete_icon)} ${currentItem.drugName}?",
            Snackbar.LENGTH_LONG
        ).setAction(context.getString(R.string.undo_text)) {
            remove = 1
            items.add(removedPosition, currentItem)
            notifyItemInserted(removedPosition)
        }.show()

        // wait for 3sec, then delete the item; because it maybe undo when clicking snack-bar
        Handler().postDelayed({
            if (remove == 0) {
                mPharmacyViewModel = ViewModelProvider(context)[PharmacyViewModel::class.java]
                mPharmacyViewModel.deleteDrug(currentItem)
            }
        }, 3000)
    }

    fun updateItem(viewHolder: RecyclerView.ViewHolder, context: Context) {
        // open details activity to update the data
        val position = viewHolder.adapterPosition
        val current = items[position]

        val intent = Intent(context, RegisterActivity::class.java)

        val item = ArrayList<String>()
        item.add(current.id.toString())
        item.add(current.drugName)
        item.add(current.drugScientificName)
        item.add(current.drugIndustrial)
        item.add(current.drugDescription)
        item.add(current.drugPrice.toString())
        item.add(current.drugType)
        item.add(current.drugNumber)

        intent.putStringArrayListExtra("drug_item", item)
        intent.putStringArrayListExtra("drug_tag", current.drugTags)
        intent.putExtra("activity", "update")
        context.startActivity(intent)
    }

    interface RowClickListener {
        fun onItemClickListener(pharmacy: Pharmacy)
    }
}