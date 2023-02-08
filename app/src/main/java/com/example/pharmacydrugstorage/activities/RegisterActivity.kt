/**
 *  created by: Motwkel-Idris, 17-1-2023
 * In this activity, we insert/update data on the database
 **/

package com.example.pharmacydrugstorage.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.pharmacydrugstorage.R
import com.example.pharmacydrugstorage.data.classes.Pharmacy
import com.example.pharmacydrugstorage.data.classes.PharmacyViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.chip_group
import kotlinx.android.synthetic.main.activity_register.drug_description
import kotlinx.android.synthetic.main.activity_register.drug_name
import kotlinx.android.synthetic.main.activity_register.drug_price

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    private lateinit var mPharmacyViewModel: PharmacyViewModel
    private lateinit var mSpinnerAdapter: ArrayAdapter<String>
    private lateinit var drugType: String
    private val chipColor = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize the color for the chips
        chipColor.add(R.color.chip_1)
        chipColor.add(R.color.chip_2)
        chipColor.add(R.color.chip_3)
        chipColor.add(R.color.chip_4)
        chipColor.add(R.color.chip_5)
        chipColor.add(R.color.chip_6)
        chipColor.add(R.color.chip_7)
        chipColor.add(R.color.chip_8)
        chipColor.add(R.color.chip_9)

        mPharmacyViewModel = ViewModelProvider(this)[PharmacyViewModel::class.java]
        setUpSpinner()
        // function to decide whether it is insert or update operation
        setUpIfInsertOrUpdate()
    }

    private fun setUpSpinner() {
        val drugItem = resources.getStringArray(R.array.drug_type)
        mSpinnerAdapter =
            ArrayAdapter(
                this,
                R.layout.custom_text,
                drugItem
            )
        drug_type.adapter = mSpinnerAdapter

        drug_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                drugType = drug_type.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                drugType = "Tablet"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpIfInsertOrUpdate() {
        val intent = intent
        if (intent.getStringExtra("activity") == "insert") {
            new_edit_drug.text = "New Medicine"
            insertNewDrug()
        } else if (intent.getStringExtra("activity") == "update") {
            new_edit_drug.text = "Update Medicine"
            updateDrug()
        }
    }

    private fun updateDrug() {
        // update medicine logic
        val intent = intent
        val drugItem = intent.getStringArrayListExtra("drug_item")!!
        val tagItem = intent.getStringArrayListExtra("drug_tag")!!
        val tagName = ArrayList<String>()

        var colorIndex = 0
        val id = drugItem[0].toLong()
        // setup data onto the views
        drug_name.setText(drugItem[1])
        drug_scientific_name.setText(drugItem[2])
        drug_industrial.setText(drugItem[3])
        drug_description.setText(drugItem[4])
        drug_price.setText(drugItem[5])
        val mDrugType = drugItem[6]
        // drug number with ( ) separator
        if (drugItem[5].isNotBlank()) {
            // check if drug number is not empty
            val number = drugItem[7].split(" ")
            if (number.size == 1) {
                // only drug quantity (no measurement)
                drug_number_details.setText(number[0])
            } else if (number.size == 2) {
                // drug quantity and measurement
                drug_number_details.setText(number[0])
                drug_measurement.setText(number[1])
            }
        }

        // setup the selection of the drug type
        when (mDrugType) {
            "Tablet" -> {
                drug_type.setSelection(0)
            }
            "Injection" -> {
                drug_type.setSelection(1)
            }
            "Syrup" -> {
                drug_type.setSelection(2)
            }
            "Infusion" -> {
                drug_type.setSelection(3)
            }
            "Spray" -> {
                drug_type.setSelection(4)
            }
            "Cream" -> {
                drug_type.setSelection(5)
            }
            "Powder" -> {
                drug_type.setSelection(6)
            }
            "Drop" -> {
                drug_type.setSelection(7)
            }
            "Suppository" -> {
                drug_type.setSelection(8)
            }
            "Other" -> {
                drug_type.setSelection(9)
            }
        }

        // add tags
        if (tagItem.size != 0) {
            // get the last drug count; to make the new chip color different from the previous one
            val drugCount = tagItem.last().toString().split("#,#")[1].toInt()
            if (drugCount != 8) {
                colorIndex = drugCount + 1
            }
        }

        for (item in tagItem) {
            // add the old chips-group
            val mTagName = item.split("#,#")[0]
            val tagColor = item.split("#,#")[1].toInt()
            val oldChip = Chip(this)

            tagName.add(mTagName)
            createChip(oldChip, mTagName)

            // set the background of the chip
            when (tagColor) {
                0 -> {
                    oldChip.setChipBackgroundColorResource(chipColor[0])
                }
                1 -> {
                    oldChip.setChipBackgroundColorResource(chipColor[1])
                }
                2 -> {
                    oldChip.setChipBackgroundColorResource(chipColor[2])
                }
                3 -> {
                    oldChip.setChipBackgroundColorResource(chipColor[3])
                }
                4 -> {
                    oldChip.setChipBackgroundColorResource(chipColor[4])
                }
                5 -> {
                    oldChip.setChipBackgroundColorResource(chipColor[5])
                }
                6 -> {
                    oldChip.setChipBackgroundColorResource(chipColor[6])
                }
                7 -> {
                    oldChip.setChipBackgroundColorResource(chipColor[7])
                }
                8 -> {
                    oldChip.setChipBackgroundColorResource(chipColor[8])
                }
            }

            chip_group.addView(oldChip)
            // remove on click the close icon on the chip
            oldChip.setOnCloseIconClickListener {
                val index = tagName.indexOf(oldChip.text)
                tagName.removeAt(index)
                tagItem.removeAt(index)
                chip_group.removeView(oldChip)
            }
        }

        // add new chip
        add_tag.setOnClickListener {
            val mTagName = drug_tag.text.toString()
            val newChip = Chip(this)

            if (mTagName.trim().isNotBlank()) {
                if (colorIndex == 9)
                    colorIndex = 0

                /** separator: (#,#) **/
                // drug tag comes in this schema: (name#,#color) e.g: (tag_1#,#red)
                tagItem.add("$mTagName#,#$colorIndex")
                tagName.add(mTagName)
                createChip(newChip, mTagName)

                newChip.setChipBackgroundColorResource(chipColor[colorIndex])
                colorIndex += 1
                chip_group.addView(newChip)
                // clear the edit text of adding new chip
                drug_tag.text = null

                // remove on click the close icon on the chip
                newChip.setOnCloseIconClickListener {
                    val index = tagName.indexOf(newChip.text)
                    tagName.removeAt(index)
                    tagItem.removeAt(index)
                    chip_group.removeView(newChip)
                }
            } else {
                // display this toast if edit text is empty
                Toast.makeText(this, "Add some tag first", Toast.LENGTH_SHORT).show()
            }
        }

        cancel_drug.setOnClickListener {
            // return to main activity
            val intent2 = Intent(this, MainActivity::class.java)
            startActivity(intent2)
        }

        save_drug.setOnClickListener {
            // update medicine on the database
            val drugName = drug_name.text.toString()
            val drugScientificName = drug_scientific_name.text.toString()
            val drugIndustrial = drug_industrial.text.toString()
            val drugDescription =
                drug_description.text.toString()
            /** separator: ( ) **/
            // schema: (quantity measurement), e.g: (100 ml)
            val drugNumber =
                "${drug_number_details.text} ${drug_measurement.text}"
            val drugPrice = drug_price.text.toString()

            // update it into the database if the medicine name and price are not empty
            if (drugName.trim().isNotBlank() && drugPrice.trim().isNotBlank()) {
                val pharmacy =
                    Pharmacy(
                        id,
                        drugName,
                        drugScientificName,
                        drugIndustrial,
                        drugDescription,
                        drugType,
                        drugNumber,
                        drugPrice.toLong(),
                        tagItem
                    )
                mPharmacyViewModel.updateDrug(pharmacy)

                val intent2 = Intent(this, MainActivity::class.java)
                startActivity(intent2)
            } else {
                // display toast if medicine name or price are empty
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createChip(chip: Chip, mTagName: String) {
        chip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chip.isCloseIconVisible = true
        chip.isChipIconVisible = true
        chip.isClickable = true
        chip.text = mTagName
    }

    private fun insertNewDrug() {
        // add new medicine logic
        val tagName = ArrayList<String>()
        val tagItem = ArrayList<String>()
        var colorIndex = 0

        // add new tag and assign new color to it
        add_tag.setOnClickListener {
            val mDrugTag = drug_tag.text.toString()
            val chip = Chip(this)

            if (mDrugTag.trim().isNotBlank()) {
                if (colorIndex == 9)
                    colorIndex = 0

                /** separator: (#,#) **/
                // drug tag comes in this schema: (name#,#color) e.g: (tag_1#,#red)
                tagName.add(mDrugTag)
                tagItem.add("${mDrugTag}#,#${colorIndex}")
                createChip(chip, mDrugTag)

                // set background color for the chip
                chip.setChipBackgroundColorResource(chipColor[colorIndex])
                colorIndex += 1
                chip_group.addView(chip)
                drug_tag.text = null

                // on click on close icon on the chip, it will be deleted
                chip.setOnCloseIconClickListener {
                    val index = tagName.indexOf(chip.text)
                    tagName.removeAt(index)
                    tagItem.removeAt(index)
                    chip_group.removeView(chip)
                }
            } else {
                // display this toast if edit text is empty
                Toast.makeText(this, "Add some tag first", Toast.LENGTH_SHORT).show()
            }
        }

        cancel_drug.setOnClickListener {
            // return to main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        save_drug.setOnClickListener {
            // add medicine to the database
            val drugName = drug_name.text.toString()
            val drugScientificName = drug_scientific_name.text.toString()
            val drugIndustrial = drug_industrial.text.toString()
            val drugDescription =
                drug_description.text.toString()
            /** separator: ( ) **/
            // schema: (quantity measurement), e.g: (100 ml)
            val drugNumber =
                "${drug_number_details.text} ${drug_measurement.text}"
            val drugPrice = drug_price.text.toString()

            // add it to the database if the medicine name and price are not empty
            if (drugName.trim().isNotBlank() && drugPrice.trim().isNotBlank()) {
                val pharmacy =
                    Pharmacy(
                        0,
                        drugName,
                        drugScientificName,
                        drugIndustrial,
                        drugDescription,
                        drugType,
                        drugNumber,
                        drugPrice.toLong(),
                        tagItem
                    )
                mPharmacyViewModel.insertDrug(pharmacy)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // display toast if medicine name or price are empty
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // go back to main activity
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        super.onBackPressed()
    }
}