/**
 *  created by: Motwkel-Idris, 17-1-2023
 * In this activity, we display all the medicine in the database by using
 * recycler-view and other features like:
 * Item touch (swipe left for delete, and swipe right for edit)
 * Search-view with advance dialog search, to make filter on the medicines..
**/

@file:Suppress("UNREACHABLE_CODE")
package com.example.pharmacydrugstorage.activities
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.pharmacydrugstorage.R
import com.example.pharmacydrugstorage.data.classes.Pharmacy
import com.example.pharmacydrugstorage.data.classes.PharmacyAdapter
import com.example.pharmacydrugstorage.data.classes.PharmacyDatabase
import com.example.pharmacydrugstorage.data.classes.PharmacyViewModel
import kotlinx.android.synthetic.main.about_dialog.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.filter_dialog.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(),
    PharmacyAdapter.RowClickListener {
    private lateinit var pharmacyAdapter: PharmacyAdapter
    private lateinit var mSpinnerAdapter: ArrayAdapter<String>
    private lateinit var mPharmacyViewModel: PharmacyViewModel
    private lateinit var pharmacyDatabase: PharmacyDatabase
    private lateinit var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback
    private lateinit var editSwipeIcon: Drawable
    private lateinit var deleteSwipeIcon: Drawable
    private lateinit var searchTag: String

    var editSwipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#43A047"))
    var deleteSwipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#F44336"))

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPharmacyViewModel = ViewModelProvider(this)[PharmacyViewModel::class.java]
        pharmacyDatabase = PharmacyDatabase.getDatabase(this)
        deleteSwipeIcon = ContextCompat.getDrawable(this, R.drawable.outline_delete_24)!!
        editSwipeIcon = ContextCompat.getDrawable(this, R.drawable.outline_edit_24)!!

        setupRecyclerAdapter()

        // Get all medicine
        mPharmacyViewModel.allDrugs.observe(this) { pharmacy ->
            // Check if the pharmacy object is empty or not
            if (pharmacy.isNotEmpty()) {
                // setup the data to display it on the recyclerview
                indicator.visibility = View.GONE
                indicator_txt.visibility = View.GONE
                recycler.visibility = View.VISIBLE
                count.visibility = View.VISIBLE
                // display the count of medicine
                count.text = "Found ${pharmacy.size} results"
                pharmacyAdapter.setData(pharmacy)
            } else {
                // display the empty view logic
                indicator.visibility = View.VISIBLE
                indicator_txt.visibility = View.VISIBLE
                count.visibility = View.GONE
                recycler.visibility = View.GONE
            }
        }

        filter.setOnClickListener {
            // display dialog box on clicking on the filter icon
            val dialog = MaterialDialog(this)
                .noAutoDismiss()
                .customView(R.layout.filter_dialog)

            setUpSpinner(dialog)
            // setup advance search
            // search by type of medicine
            dialog.search_btn.setOnClickListener {
                pharmacyDatabase.pharmacyDao()
                    .getAdvanceSearchDrugs("${search.query!!.trim()}%", searchTag)
                    .observe(this@MainActivity) { pharmacy ->
                        // Check if the resulting pharmacy object is empty or not
                        if (pharmacy.isNotEmpty()) {
                            // setup the data to display it on the recyclerview
                            indicator.visibility = View.GONE
                            indicator_txt.visibility = View.GONE
                            recycler.visibility = View.VISIBLE
                            count.visibility = View.VISIBLE
                            // display the count of medicine
                            count.text = "Found ${pharmacy.size} results"
                            pharmacyAdapter.setData(pharmacy)
                        } else {
                            // display the empty view logic
                            indicator.visibility = View.VISIBLE
                            indicator_txt.visibility = View.VISIBLE
                            recycler.visibility = View.GONE
                            count.visibility = View.GONE
                        }
                    }
            }
            dialog.show()
        }

        // setup the touch (swipe) logic
        setTouchCallback()
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)

        // setup the search query
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                // make a search query on text change of search-view
                if (p0!!.trim() == "") {
                    // if search field is empty, get all of the data
                    mPharmacyViewModel.allDrugs.observe(this@MainActivity) { pharmacy ->
                        indicator.visibility = View.GONE
                        indicator_txt.visibility = View.GONE
                        recycler.visibility = View.VISIBLE
                        count.visibility = View.VISIBLE
                        count.text = "Found ${pharmacy.size} results"
                        pharmacyAdapter.setData(pharmacy)
                    }
                } else {
                    // if not empty, get the result of the search by medicine name
                    pharmacyDatabase.pharmacyDao().getSearchDrugs("${p0.trim()}%")
                        .observe(this@MainActivity) { pharmacy ->
                            if (pharmacy.isNotEmpty()) {
                                // if resul is not empty, setup the resulted data
                                indicator.visibility = View.GONE
                                indicator_txt.visibility = View.GONE
                                recycler.visibility = View.VISIBLE
                                count.visibility = View.VISIBLE
                                count.text = "Found ${pharmacy.size} results"
                                pharmacyAdapter.setData(pharmacy)
                            } else {
                                // if resul is empty, setup the empty logic
                                indicator.visibility = View.VISIBLE
                                indicator_txt.visibility = View.VISIBLE
                                recycler.visibility = View.GONE
                                count.visibility = View.GONE
                            }
                        }
                }
                return true
            }
        })

        fab.setOnClickListener {
            // add new medicine
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("activity", "insert")
            startActivity(intent)
        }
    }

    // setup the touch (swipe) logic
    private fun setTouchCallback() {
        itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    // swipe left, for update
                    ItemTouchHelper.LEFT -> {
                        pharmacyAdapter.updateItem(viewHolder, this@MainActivity)
                    }
                    // swipe right, for delete
                    ItemTouchHelper.RIGHT -> {
                        pharmacyAdapter.removeItem(viewHolder, this@MainActivity)
                    }
                }
            }

            // setup the position of the delete/edit icon
            override fun onChildDraw(
                c: android.graphics.Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                // delete/edit icons
                val deleteIconMargin = (itemView.height - deleteSwipeIcon.intrinsicHeight) / 2
                val editIconMargin = (itemView.height - editSwipeIcon.intrinsicHeight) / 2

                if (dX > 0) {
                    // on swipe right, draw and position the delete icon and background
                    deleteSwipeBackground.setBounds(
                        itemView.left,
                        itemView.top,
                        dX.toInt(),
                        itemView.bottom
                    )
                    deleteSwipeIcon.setBounds(
                        itemView.left + editIconMargin,
                        itemView.top + editIconMargin,
                        itemView.left + editIconMargin + editSwipeIcon.intrinsicWidth,
                        itemView.bottom - editIconMargin
                    )
                    deleteSwipeBackground.draw(c)
                } else {
                    // on swipe left, draw and position the edit icon and background
                    editSwipeBackground.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    editSwipeIcon.setBounds(
                        itemView.right - deleteIconMargin - deleteSwipeIcon.intrinsicWidth,
                        itemView.top + deleteIconMargin,
                        itemView.right - deleteIconMargin,
                        itemView.bottom - deleteIconMargin
                    )
                    editSwipeBackground.draw(c)
                }
                c.save()

                if (dX > 0) {
                    // positioning for delete
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                } else {
                    // positioning for edit
                    c.clipRect(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                }

                if (dX > 0) {
                    // draw for delete
                    deleteSwipeIcon.draw(c)
                } else {
                    // draw for edit
                    editSwipeIcon.draw(c)
                }
                c.restore()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
    }


    private fun setupRecyclerAdapter() {
        recycler.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        // display all data
        mPharmacyViewModel.allDrugs.observe(this) { pharmacy ->
            pharmacyAdapter = PharmacyAdapter((pharmacy) as MutableList<Pharmacy>, this)
            recycler.adapter = pharmacyAdapter
        }

    }

    private fun setUpSpinner(dialog: MaterialDialog) {
        val drugTypes = resources.getStringArray(R.array.drug_type)
        mSpinnerAdapter =
            ArrayAdapter(
                this,
                R.layout.custom_text,
                drugTypes
            )
        dialog.search_spinner.adapter = mSpinnerAdapter

        dialog.search_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                searchTag = dialog.search_spinner.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                searchTag = "None"
            }
        }
    }

    override fun onItemClickListener(pharmacy: Pharmacy) {
        // open details activity
        val intent = Intent(this, DetailsActivity::class.java)
        val drugItem = ArrayList<String>()

        drugItem.add(pharmacy.drugName)
        drugItem.add(pharmacy.drugScientificName)
        drugItem.add(pharmacy.drugIndustrial)
        drugItem.add(pharmacy.drugDescription)
        drugItem.add(pharmacy.drugPrice.toString())
        drugItem.add(pharmacy.drugType)
        drugItem.add(pharmacy.drugNumber)

        intent.putExtra("drug_item", drugItem)
        intent.putExtra("drug_tag", pharmacy.drugTags)
        startActivity(intent)
    }

    // setup the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                // about dialog box
                val dialog = MaterialDialog(this)
                    .noAutoDismiss()
                    .customView(R.layout.about_dialog)

                // linkedin account
                dialog.btn_1.setOnClickListener {
                    val url = "https://www.linkedin.com/in/motwkel-idris-1b73b3159"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }
                // phone dial
                dialog.btn_2.setOnClickListener {
                    val uri = "+249990928293"
                    val i = Intent(Intent.ACTION_DIAL)
                    i.data = Uri.parse("tel:$uri")
                    startActivity(i)
                }
                dialog.show()
                true
            }
            // ToDo: backup data
            R.id.backup -> {
                Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show()
                true
            }
            // ToDo: restore data
            R.id.restore -> {
                Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // close the application
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }
}