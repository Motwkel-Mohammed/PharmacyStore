/**
 *  created by: Motwkel-Idris, 17-1-2023
 * In this class, we specify the schema of our table
 * by the help of room database notations
 **/

package com.example.pharmacydrugstorage.data.classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "pharmacy_table")
data class Pharmacy(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "drug_name")
    val drugName: String,
    @ColumnInfo(name = "drug_scientific_name")
    val drugScientificName: String,
    @ColumnInfo(name = "drug_industrial")
    val drugIndustrial: String,
    @ColumnInfo(name = "drug_description")
    val drugDescription: String,
    @ColumnInfo(name = "drug_type")
    val drugType: String,
    @ColumnInfo(name = "drug_number")
    val drugNumber: String,
    @ColumnInfo(name = "drug_price")
    val drugPrice: Long,
    @ColumnInfo(name = "drug_tags")
    val drugTags: ArrayList<String>
)

// for drugTags, to convert arraylist into json format to save it in database
class DrugTags {
    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        return Gson().toJson(list)
    }

}
