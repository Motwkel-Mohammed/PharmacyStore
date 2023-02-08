/**
 *  created by: Motwkel-Idris, 17-1-2023
 * In this class, we specify the view-model of our database
 * by the help of room database notations, to apply MVVM design pattern
 **/

package com.example.pharmacydrugstorage.data.classes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PharmacyViewModel(application: Application) : AndroidViewModel(application) {

    // from repository class
    private val repository: PharmacyRepository
    // livedata observer
    val allDrugs: LiveData<List<Pharmacy>>

    init {
        val pharmacyDao = PharmacyDatabase.getDatabase(application).pharmacyDao()
        repository = PharmacyRepository(pharmacyDao)
        // Pharmacy table
        allDrugs = repository.allDrugs
    }

    fun insertDrug(pharmacy: Pharmacy) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertDrug(pharmacy)
    }

    fun updateDrug(pharmacy: Pharmacy) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateDrug(pharmacy)
    }

    fun deleteDrug(pharmacy: Pharmacy) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteDrug(pharmacy)
    }

}
