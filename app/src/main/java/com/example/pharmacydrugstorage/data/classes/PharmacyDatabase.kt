/**
 *  created by: Motwkel-Idris, 17-1-2023
 * In this class, we specify the schema of our database
 * by the help of room database notations
 **/

package com.example.pharmacydrugstorage.data.classes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pharmacydrugstorage.data.interfaces.PharmacyDao

@Suppress("NAME_SHADOWING")
@Database(entities = [Pharmacy::class], version = 1, exportSchema = false)
@TypeConverters(DrugTags::class)
abstract class PharmacyDatabase : RoomDatabase() {
    abstract fun pharmacyDao(): PharmacyDao

    companion object {
        //prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: PharmacyDatabase? = null

        fun getDatabase(context: Context): PharmacyDatabase {

            val instance = INSTANCE
            // if the INSTANCE is not null, then return it
            if (instance != null) {
                return instance
            }
            // if it is null, then create the database
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PharmacyDatabase::class.java,
                    "drug_storage"
                )
                    .fallbackToDestructiveMigration() // Wipes and rebuilds instead of migrating if no Migration object.
                    .build()
                INSTANCE = instance

                return instance
            }
        }
    }
}