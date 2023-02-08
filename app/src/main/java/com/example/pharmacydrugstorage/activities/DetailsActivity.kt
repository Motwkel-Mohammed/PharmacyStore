/**
 *  created by: Motwkel-Idris, 17-1-2023
 * In this activity, we display the whole information about the
 * selected (clicked) medicine data
 **/

package com.example.pharmacydrugstorage.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.pharmacydrugstorage.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_details.*

@Suppress("DEPRECATION")
class DetailsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Initialize the color for the chips
        val chipColor = ArrayList<Int>()
        chipColor.add(R.color.chip_1)
        chipColor.add(R.color.chip_2)
        chipColor.add(R.color.chip_3)
        chipColor.add(R.color.chip_4)
        chipColor.add(R.color.chip_5)
        chipColor.add(R.color.chip_6)
        chipColor.add(R.color.chip_7)
        chipColor.add(R.color.chip_8)
        chipColor.add(R.color.chip_9)

        val intent = intent
        val drugItem = intent.getStringArrayListExtra("drug_item")!!
        val drugTag = intent.getStringArrayListExtra("drug_tag")!!

        // display the data on the views
        drug_name.text = drugItem[0]
        // check the scientific name and industry name
        if (drugItem[1].trim().isBlank() and drugItem[2].trim().isBlank()){
            // scientific name and industry name are empty
            drug_detail.text= "_, from _"
        }else if (drugItem[1].trim().isBlank()){
            // scientific name is empty
            drug_detail.text= "_, from ${drugItem[2]}"
        }else if (drugItem[2].trim().isBlank()){
            // industry name is empty
            drug_detail.text= "${drugItem[1]}, from _"
        }else{
            // scientific name and industry name are not-empty
            drug_detail.text= "${drugItem[1]}, from ${drugItem[2]}"
        }
        drug_description.text = drugItem[3]
        drug_price.text = "${drugItem[4]} $"
        val type = drugItem[5]
        val drugNumber = drugItem[6]
        // check the quantity and measurement of the drug
        if (drugNumber.isNotBlank()) {
            drug_number.text = "($drugNumber)"
        } else {
            drug_number.text = "(_ _)"
        }

        card_1.setCardBackgroundColor(Color.parseColor("#a5ffd6"))
        card_2.setCardBackgroundColor(Color.parseColor("#a5ffd6"))
        parent_layout.setBackgroundColor(Color.parseColor("#79A5FFD6"))

        // setup the image of the medicine type
        when (type) {
            "Tablet" -> {
                drug_image.setImageResource(R.drawable.medical_pill)
//                card_1.setCardBackgroundColor(Color.parseColor("#61a5c2"))
//                card_2.setCardBackgroundColor(Color.parseColor("#61a5c2"))
//                parent_layout.setBackgroundColor(Color.parseColor("#7A61A5C2"))
            }
            "Injection" -> {
                drug_image.setImageResource(R.drawable.medical_needle)
//                card_1.setCardBackgroundColor(Color.parseColor("#b5e48c"))
//                card_2.setCardBackgroundColor(Color.parseColor("#b5e48c"))
//                parent_layout.setBackgroundColor(Color.parseColor("#9CB5E48C"))
            }
            "Syrup" -> {
                drug_image.setImageResource(R.drawable.medical_bottel)
//                card_1.setCardBackgroundColor(Color.parseColor("#d8e2dc"))
//                card_2.setCardBackgroundColor(Color.parseColor("#d8e2dc"))
//                parent_layout.setBackgroundColor(Color.parseColor("#A9D8E2DC"))
            }
            "Infusion" -> {
                drug_image.setImageResource(R.drawable.medical_drip)
//                card_1.setCardBackgroundColor(Color.parseColor("#bad7f2"))
//                card_2.setCardBackgroundColor(Color.parseColor("#bad7f2"))
//                parent_layout.setBackgroundColor(Color.parseColor("#94BAD7F2"))
            }
            "Spray" -> {
                drug_image.setImageResource(R.drawable.medicine_spray)
//                card_1.setCardBackgroundColor(Color.parseColor("#8e94f2"))
//                card_2.setCardBackgroundColor(Color.parseColor("#8e94f2"))
//                parent_layout.setBackgroundColor(Color.parseColor("#928E94F2"))
            }
            "Cream" -> {
                drug_image.setImageResource(R.drawable.medicine_cream)
//                card_1.setCardBackgroundColor(Color.parseColor("#d2b7e5"))
//                card_2.setCardBackgroundColor(Color.parseColor("#d2b7e5"))
//                parent_layout.setBackgroundColor(Color.parseColor("#A9D2B7E5"))
            }
            "Powder" -> {
                drug_image.setImageResource(R.drawable.medicine_powder)
//                card_1.setCardBackgroundColor(Color.parseColor("#f38375"))
//                card_2.setCardBackgroundColor(Color.parseColor("#f38375"))
//                parent_layout.setBackgroundColor(Color.parseColor("#9EF38375"))
            }
            "Drop" -> {
                drug_image.setImageResource(R.drawable.medicine_drop)
//                card_1.setCardBackgroundColor(Color.parseColor("#caf0f8"))
//                card_2.setCardBackgroundColor(Color.parseColor("#caf0f8"))
//                parent_layout.setBackgroundColor(Color.parseColor("#9ACAF0F8"))
            }
            "Suppository" -> {
                drug_image.setImageResource(R.drawable.medicine_suppository)
//                card_1.setCardBackgroundColor(Color.parseColor("#d55d92"))
//                card_2.setCardBackgroundColor(Color.parseColor("#d55d92"))
//                parent_layout.setBackgroundColor(Color.parseColor("#90D55D92"))
            }
            "Other" -> {
                drug_image.setImageResource(R.drawable.medicine_other)
//                card_1.setCardBackgroundColor(Color.parseColor("#a5ffd6"))
//                card_2.setCardBackgroundColor(Color.parseColor("#a5ffd6"))
//                parent_layout.setBackgroundColor(Color.parseColor("#79A5FFD6"))
            }
        }

        // setup the chip logic
        for (item in drugTag) {
            val tagName = item.split("#,#")[0]
            val tagColor = item.split("#,#")[1].toInt()
            // create new chip
            val chip = Chip(this)
            chip.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            chip.isClickable = true
            chip.text = tagName
            // set the background color of the chip
            when (tagColor) {
                0 -> {
                    chip.setChipBackgroundColorResource(chipColor[0])
                }
                1 -> {
                    chip.setChipBackgroundColorResource(chipColor[1])
                }
                2 -> {
                    chip.setChipBackgroundColorResource(chipColor[2])
                }
                3 -> {
                    chip.setChipBackgroundColorResource(chipColor[3])
                }
                4 -> {
                    chip.setChipBackgroundColorResource(chipColor[4])
                }
                5 -> {
                    chip.setChipBackgroundColorResource(chipColor[5])
                }
                6 -> {
                    chip.setChipBackgroundColorResource(chipColor[6])
                }
                7 -> {
                    chip.setChipBackgroundColorResource(chipColor[7])
                }
                8 -> {
                    chip.setChipBackgroundColorResource(chipColor[8])
                }
            }
            // add chip to chip-group
            chip_group.addView(chip)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // back to main activity
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        super.onBackPressed()
    }
}