package com.project.memoapp
import java.text.SimpleDateFormat
import java.util.Date

data class MemoData(
    val nameOfMemo: String = "",
    val nickname: String = "",
    val email: String = "",
    val isShared: Boolean = false,
    val creationTime: Long = System.currentTimeMillis() //Lisätty aika
)