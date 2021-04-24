package com.example.testapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_todo.*

class MainActivity : AppCompatActivity() {

    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        todoAdapter = TodoAdapter(mutableListOf())
//
//        rvTodoItems.adapter = todoAdapter
        setupListOfDataIntoRecyclerView()

        btnAddTodo.setOnClickListener {
            val todoTitle = etTodoTitle.text.toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if(todoTitle.isNotEmpty()){
                val status = databaseHandler.addTodo(Todo(
                    0,todoTitle,false
                ))
                if (status > -1){
                    Toast.makeText(applicationContext, "Task saved", Toast.LENGTH_SHORT).show()
                    etTodoTitle.text.clear()

                    setupListOfDataIntoRecyclerView()
                }else{
                    Toast.makeText(applicationContext, "Cannot be blank", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnDeleteTodo.setOnClickListener {
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteCheckedTodo()
            if (status > -1){
                Toast.makeText(applicationContext, "Task Deleted", Toast.LENGTH_SHORT).show()
                setupListOfDataIntoRecyclerView()
            }else{
                Toast.makeText(applicationContext, "None was selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateTodoCheck(todo:Todo){
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.updateTodo(todo)
        if (status > -1){
            Toast.makeText(applicationContext, "Check changed", Toast.LENGTH_SHORT).show()
            setupListOfDataIntoRecyclerView()
        }else{
            Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListOfDataIntoRecyclerView(){
        if (getTodoList().size>0){
            rvTodoItems.layoutManager = LinearLayoutManager(this)
            itemAdapter = ItemAdapter(this, getTodoList())
            rvTodoItems.adapter = itemAdapter
        }else{
            rvTodoItems.layoutManager = LinearLayoutManager(this)
            itemAdapter = ItemAdapter(this, getTodoList())
            rvTodoItems.adapter = itemAdapter
        }

    }

    private fun getTodoList():ArrayList<Todo>{
        val databaseHandler = DatabaseHandler(this)
        val todoList: ArrayList<Todo> = databaseHandler.viewTodo()
        return todoList
    }
}