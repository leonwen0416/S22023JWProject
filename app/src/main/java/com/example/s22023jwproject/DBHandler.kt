package com.example.s22023jwproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler (context: Context,factory:SQLiteDatabase.CursorFactory?)
    :SQLiteOpenHelper(context,DATABASE_NAME,factory,DB_VERSION) {

    companion object{
        const val DATABASE_NAME = "HRManager.db"
        const val DB_VERSION = 2
    }
    private val tableName:String = "PERSON"
    private val keyID:String = "ID"
    private val keyName:String = "NAME"
    private val keyMobile:String = "MOBILE"
    private val keyEmail:String = "EMAIL"
    private val keyAddress:String = "ADDRESS"
    private val keyImageFile:String = "IMAGEFILE"

    override fun onCreate(db: SQLiteDatabase) {
        // create a sal statement
        val createTable = "CREATE TABLE $tableName ($keyID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$keyName TEXT, $keyImageFile TEXT, $keyAddress TEXT, $keyMobile TEXT, $keyEmail TEXT)"
        // execute sql statement to create a table
        db.execSQL(createTable)
        // add a sample record to database using contentValues
        val cv = ContentValues()
        cv.put(keyName, "Alok Garg")
        cv.put(keyMobile, "0401123456")
        cv.put(keyAddress, "Sydney")
        cv.put(keyImageFile, "first")
        cv.put(keyEmail, "alok.garg@tafensw.edu.au")
        // use db.insert function
        db.insert(tableName, null,cv)
    }

    override fun onUpgrade(db: SQLiteDatabase, old:Int, new: Int) {
        // drop existing table
        db.execSQL("DROP TABLE IF EXISTS $tableName")
        // recreate table
        onCreate(db)
    }

    fun getALLPersons(): ArrayList<Person>{
        // declare a arraylist to fill all records
        val personList = ArrayList<Person>()
        // create a sql query
        val selectQuery = "SELECT * FROM $tableName"
        // get readable database
        val db = this.readableDatabase
        // run query and put result in cursor
        val cursor = db.rawQuery(selectQuery,null)
        // loop through cursor to read all the records
        if(cursor.moveToFirst()){
            do{
                //create a person object
                val person = Person()
                // read the values from cursor and fill person object
                person.id = cursor.getInt(0)
                person.name = cursor.getString(1)
                person.imageFile = cursor.getString(2)
                person.address = cursor.getString(3)
                person.mobile = cursor.getString(4)
                person.email = cursor.getString(5)
                // add person object to arrayList
                personList.add(person)
            }while (cursor.moveToNext())
        }
        // close cursor and database
        cursor.close()
        // return personlist
        return personList
    }

    fun getPerson(id: Int): Person {
        // get readable database
        val db = this.readableDatabase
        // create a person object
        val person = Person()
        // create cursor based on query
        val cursor = db.query(tableName, arrayOf(keyID,keyName,keyImageFile,keyAddress,keyMobile,keyEmail),
        "$keyID=?",
        arrayOf(id.toString()),
        null,
        null,
        null)
        // check cursor is not empty
        if(cursor!=null){
            cursor.moveToFirst()
            person.id = cursor.getInt(0)
            person.name = cursor.getString(1)
            person.imageFile = cursor.getString(2)
            person.address = cursor.getString(3)
            person.mobile = cursor.getString(4)
            person.email = cursor.getString(5)
        }
        // close cursor and database
        cursor.close()
        db.close()
        // return person object
        return person
    }

    fun updatePerson(person: Person) {
        // get writable database
        val db = this.writableDatabase
        // create contentvalue to update database
        val cv = ContentValues()
        cv.put(keyName,person.name)
        cv.put(keyMobile,person.mobile)
        cv.put(keyEmail,person.email)
        cv.put(keyAddress,person.address)
        cv.put(keyImageFile,person.imageFile)
        // update function of db
        db.update(tableName,cv,"$keyID=?", arrayOf(person.id.toString()))
        // close the db
        db.close()
    }

    fun addPerson(person: Person) {
        // get writable database
        val db = this.writableDatabase
        // contentvalues onject to put values in the database
        val cv = ContentValues()
        cv.put(keyName,person.name)
        cv.put(keyMobile,person.mobile)
        cv.put(keyEmail,person.email)
        cv.put(keyAddress,person.address)
        cv.put(keyImageFile,person.imageFile)
        // call db insert function
        db.insert(tableName,null,cv)
        // close the db
        db.close()
    }

    fun deletePerson(id: Int) {
        // get writable database
        val db = this.writableDatabase
        // call db delete function
        db.delete(tableName,"$keyID=?",arrayOf(id.toString()))
        // close db
        db.close()

    }
}
