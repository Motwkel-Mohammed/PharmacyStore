/**
 *  created by: Motwkel-Idris, 17-1-2023
 * In this interface, we specify the data access object of our database
 * by the help of room database notations, to apply MVVM design pattern
 **/

package com.example.pharmacydrugstorage.data.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pharmacydrugstorage.data.classes.Pharmacy

@Dao
interface PharmacyDao {

    // Pharmacy table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDrug(pharmacy: Pharmacy)

    @Update
    suspend fun updateDrug(pharmacy: Pharmacy)

    @Delete
    suspend fun deleteDrug(pharmacy: Pharmacy)

    @Query("DELETE FROM pharmacy_table")
    suspend fun deleteAllDrugs()

    @Query("SELECT * FROM pharmacy_table ORDER BY drug_name ASC")
    fun getAllDrugs(): LiveData<List<Pharmacy>>

    @Query("SELECT * FROM pharmacy_table WHERE drug_name LIKE :query")
    fun getSearchDrugs(query: String): LiveData<List<Pharmacy>>

    @Query("SELECT * FROM pharmacy_table WHERE drug_name LIKE :query AND drug_type= :type")
    fun getAdvanceSearchDrugs(query: String, type: String): LiveData<List<Pharmacy>>

}
