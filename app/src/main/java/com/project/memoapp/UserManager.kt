package com.project.memoapp

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class UserManager private constructor() {

    private var username: String? = null
    private var uid: String? = null
    private var email: String? = null
    private var currentDocumentID: String? = null
    private var currentSnapshot: QuerySnapshot? = null
    private var documentSnapshotList: ArrayList<DocumentSnapshot>? = null

    companion object {
        @Volatile
        private var instance: UserManager? = null

        fun getInstance(): UserManager {
            return instance ?: synchronized(this) {
                instance ?: UserManager().also { instance = it }
            }
        }
    }

    // Setter for all details at once
    fun setUserDetails(username: String?, uid: String?, email: String?, currentDocumentID: String?) {
        this.username = username
        this.uid = uid
        this.email = email
        this.currentDocumentID = currentDocumentID
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun setUid(uid: String?) {
        this.uid = uid
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun setCurrentDocumentID(currentDocumentID: String?) {
        this.currentDocumentID = currentDocumentID
    }

    fun setCurrentSnapshot(currentSnapshot: QuerySnapshot?) {
        this.currentSnapshot = currentSnapshot
    }

    fun setDocumentSnapshotList(documentSnapshotList: ArrayList<DocumentSnapshot>?) {
        this.documentSnapshotList = documentSnapshotList
    }

    fun getUsername(): String? {
        return username
    }

    fun getUid(): String? {
        return uid
    }

    fun getEmail(): String? {
        return email
    }

    fun getCurrentDocumentID(): String? {
        return currentDocumentID
    }

    fun getCurrentSnapshot(): QuerySnapshot? {
        return currentSnapshot
    }

    fun getDocumentSnapshotList(): ArrayList<DocumentSnapshot>? {
        return documentSnapshotList
    }
}