/**
 *  created by: Motwkel-Idris, 17-1-2023
 * In this class, we specify the repository of our database
 * by the help of room database notations, to apply MVVM design pattern
 **/

package com.example.pharmacydrugstorage.data.classes

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.pharmacydrugstorage.data.interfaces.PharmacyDao

class PharmacyRepository(private val pharmacyDao: PharmacyDao) {

    // from dao interface
    // Pharmacy table
    val allDrugs: LiveData<List<Pharmacy>> = pharmacyDao.getAllDrugs()

    // for background coroutines work
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertDrug(pharmacy: Pharmacy) {
        pharmacyDao.insertDrug(pharmacy)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateDrug(pharmacy: Pharmacy) {
        pharmacyDao.updateDrug(pharmacy)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteDrug(pharmacy: Pharmacy) {
        pharmacyDao.deleteDrug(pharmacy)
    }
}