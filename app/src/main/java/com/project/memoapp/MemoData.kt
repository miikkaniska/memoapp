package com.project.memoapp

data class MemoData(
    val nameOfMemo: String = "",
    val nickname: String = "",
    val email: String = "",
    val isShared: Boolean = false,
    val creationTime: Long = System.currentTimeMillis() //Lisätty aika
)