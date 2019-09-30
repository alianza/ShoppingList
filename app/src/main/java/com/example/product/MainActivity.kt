package com.example.reminder

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.product.R

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val ADD_REMINDER_REQUEST_CODE = 100

class MainActivity : AppCompatActivity() {

    private val products = arrayListOf<Product>()
    private val productAdapter = ProductAdapter(products)
    private lateinit var productRepository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        productRepository = ProductRepository(this)

        fab.setOnClickListener {

     /*       // Code to add to the floating button onClickListener:
            val product = etReminder.text.toString()
            addReminder(product)
*/
            onSaveClick()
            etAddProduct.text = Editable.Factory.getInstance().newEditable("")
        }

        initViews()
    }



    private fun onSaveClick() {
        if (etAddProduct.text.toString().isNotBlank()) {
            val product = Product(null, etAddProduct.text.toString())
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    productRepository.insertReminder(product)
                }
                getRemindersFromDatabase()
            }
        } else {
            Toast.makeText(this,"The product cannot be empty"
                , Toast.LENGTH_SHORT).show()
        }
    }


    private fun getRemindersFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val reminders = withContext(Dispatchers.IO) {
                productRepository.getAllReminders()
            }
            this@MainActivity.products.clear()
            this@MainActivity.products.addAll(reminders)
            productAdapter.notifyDataSetChanged()
        }
    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvReminders.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rvReminders.adapter = productAdapter
        rvReminders.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        createItemTouchHelper().attachToRecyclerView(rvReminders)
        getRemindersFromDatabase()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val reminderToDelete = products[position]
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteReminder(reminderToDelete)
                    }
                    getRemindersFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }
}
