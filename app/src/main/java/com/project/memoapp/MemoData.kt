package com.project.memoapp

data class MemoData(
    val title: String = "",
    val content: String = "",
    val sharedWith: ArrayList<String> = arrayListOf(),
    val creationTime: Long = 0,
    val lastEdited: Long = 0,
    val createdBy: String = ""
)