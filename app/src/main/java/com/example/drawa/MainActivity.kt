package com.example.drawa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.drawa.Daos.UserDao
import com.example.drawa.DrawableItems.*
import com.example.drawa.Utils.Utils.Companion.userId
import com.example.drawa.adapter.OnItemClickListener
import com.example.drawa.adapter.ProductRecyclerAdapter
import com.example.drawa.databinding.ActivityMainBinding
import com.example.drawa.modal.ProductModal
import com.example.drawa.userI.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding : ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    private val userDao = UserDao()
    private var dataBase : FirebaseDatabase? = null
    private lateinit var productList : ArrayList<ProductModal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = FirebaseDatabase.getInstance()
        setSupportActionBar(binding.homeMain.myCustomToolBar)


        // Setting the User Information in Drawable Header
        userDao.getUserData(userId){user->

            binding.naView.getHeaderView(0).findViewById<TextView>(R.id.userName).text = user.username
            binding.naView.getHeaderView(0).findViewById<TextView>(R.id.textView).text = user.userType
            val image = binding.naView.getHeaderView(0).findViewById<ImageView>(R.id.imageView)
            Glide.with(this@MainActivity).load(user.imageUrl).into(image)
        }

        // setting RecyclerView with ProductList
        productList = ArrayList()
        binding.homeMain.productRecyclerItem.layoutManager = LinearLayoutManager(this)

        dataBase!!.getReference("product")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    productList.clear()
                    for (snapshot1 in snapshot.children){
                        val product = snapshot1.getValue(ProductModal::class.java)
                        if (product != null){
                            productList.add(product)
                            binding.homeMain.progressBar.visibility = View.GONE
                        }
                    }
                    binding.homeMain.productRecyclerItem.adapter = ProductRecyclerAdapter(this@MainActivity, this@MainActivity, productList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        // Setting Toggle Bar
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.naView.setNavigationItemSelectedListener {
            it.isChecked = true

             when(it.itemId){
                R.id.home -> replaceFragment(Home(), it.title.toString())
                R.id.add_product -> replaceFragment(add_product_items(), it.title.toString())
                R.id.profile -> replaceFragment(Profile_Activity(), it.title.toString())
                R.id.help -> replaceFragment(Help(), it.title.toString())
                R.id.contact -> replaceFragment(Contact(), it.title.toString())
                R.id.rate -> replaceFragment(Rating(), it.title.toString())
                R.id.share -> shareApp()
                R.id.logout -> logout()

            }
            true
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Inventory Management")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "App is not available for public")

        startActivity(Intent.createChooser(shareIntent, "Share using"))

    }

    private fun replaceFragment(fragment : Fragment, title : String){
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frameLayout, fragment)
        fragmentTransition.commit()
        binding.drawerLayout.closeDrawers()
        setTitle(title)
    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Log Out?")
        builder.setMessage("Are you sure you want to log Out?")
        builder.setPositiveButton("Yes") { dialog, which ->

            val user = FirebaseAuth.getInstance().currentUser
            if (user != null){
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, Login::class.java))
                finish()
                Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(id: String): Boolean {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure you want to delete this item?")
        builder.setPositiveButton("delete") { dialog, which ->
            deleteItem(id)
        }
        builder.setNegativeButton("cancel") { dialog, which ->
            dialog.dismiss()
        }
        
        val dialog = builder.create()
        dialog.show()

       return true
    }

    private fun deleteItem(id: String) {
        val docRef = userDao.postCollection.child("product").child(id)
        docRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "successfully deleted!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "error deleting $e", Toast.LENGTH_LONG).show()
            }
    }
}