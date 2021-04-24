package com.example.testapp2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHandler (context: Context):SQLiteOpenHelper(context,DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "TodoTable"
        private val TABLE_CONTACTS = "TodoTable"

        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_ISCHECKED = "ischecked"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE "+ TABLE_CONTACTS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TITLE + " TEXT," + KEY_ISCHECKED + " INTEGER)" )
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXIST " + TABLE_CONTACTS)
        onCreate(db)
    }

    fun addTodo(todo: Todo): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, todo.title)
        contentValues.put(KEY_ISCHECKED,todo.isChecked)

        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()

        return success
    }

    fun viewTodo(): ArrayList<Todo> {

        val todoList: ArrayList<Todo> = ArrayList<Todo>()

        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS "
        val db = this.readableDatabase
        var cursor: Cursor

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var title: String
        var isChecked: Boolean

        if(cursor.moveToFirst()){
            do{
                val index = cursor.getColumnIndex(KEY_ID)
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                isChecked = cursor.getString(cursor.getColumnIndex(KEY_ISCHECKED)).equals("1")

                val todo = Todo(id, title, isChecked)
                todoList.add(todo)
                Log.d("debug","todolist.add(todo)-done")
            } while (cursor.moveToNext())
        }
        return todoList
    }

    fun updateTodo(todo: Todo):Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE,todo.title)
        contentValues.put(KEY_ISCHECKED,todo.isChecked)

        val success = db.update(TABLE_CONTACTS,contentValues, KEY_ID + "="+todo.id,null)
        db.close()

        return success
    }

    fun deleteCheckedTodo():Int{
        val db = this.writableDatabase
        val success = db.delete(TABLE_CONTACTS, KEY_ISCHECKED + "=?" , arrayOf("1"))
        db.close()
        return success
    }

}