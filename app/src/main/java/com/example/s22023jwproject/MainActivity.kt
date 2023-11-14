package com.example.s22023jwproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.s22023jwproject.databinding.ActivityMainBinding
import java.lang.Exception
import java.sql.RowIdLifetime

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // create menu values
    private val menuAdd = Menu.FIRST+1
    private val menuEdit = Menu.FIRST+2
    private val menuDelete = Menu.FIRST+3
    // create a arraylist for person object and ids
    private var personList:MutableList<Person> = arrayListOf()
    private var idList:MutableList<Int> = arrayListOf()
    // create a DBHandler Object
    private val dbHandler = DBHandler(this,null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerForContextMenu(binding.lstView)
        // load the existing data in list view
        initAdapter()
    }

    fun initAdapter() {
        // use try catch to handle error
        try{
            // clear all values in the arraylist
            personList.clear()
            idList.clear()
            // get all the persons from DBHandler and populate arrayList with id and details
            for (person:Person in dbHandler.getALLPersons()){
                // read and add to arraylist
                personList.add(person)
                idList.add(person.id)
            }
            // create arrayadapter
            val adp = CustomAdapter(this,personList as ArrayList<Person>)
            // assign adapter to list view
            binding.lstView.adapter = adp
        }catch(e:Exception){
            // show error message
            Toast.makeText(this,"Problem:${e.message.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE,menuAdd,Menu.NONE,"ADD")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // this will start a new activity
        val goAddEdit = Intent(this,ADDEDIT::class.java)
        startActivity(goAddEdit)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View?,
        menuInfo:ContextMenu.ContextMenuInfo?
    ){
        menu.add(Menu.NONE,menuEdit,Menu.NONE,"Edit")
        menu.add(Menu.NONE,menuDelete,Menu.NONE,"Delete")
        super.onCreateContextMenu(menu,v,menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // adapter context menu info
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        // code for edit and delete
        when(item.itemId){
            menuEdit->{
                // create an Intent and pass id
                val addEdit = Intent(this,ADDEDIT::class.java)
                // add id for next activity
                addEdit.putExtra("ID",idList[info.position])
                startActivity(addEdit)
            }
            menuDelete->{
                // call delete function from dbhandler
                dbHandler.deletePerson(idList[info.position])
                // refresh the view
                initAdapter()
            }
        }
        return super.onContextItemSelected(item)
    }

}