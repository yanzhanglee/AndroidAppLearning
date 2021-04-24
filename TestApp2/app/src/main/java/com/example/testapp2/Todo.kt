package com.example.testapp2

data class Todo(
    val id: Int,
    val title: String,
    var isChecked: Boolean = false
    )