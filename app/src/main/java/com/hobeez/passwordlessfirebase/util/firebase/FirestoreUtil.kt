package com.hobeez.passwordlessfirebase.util.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hobeez.passwordlessfirebase.domain.entity.User

/**
 * The collection users/ contains the users.
 * Each document corresponds to one user.
 * The name of the document is the Firebase UID of the user.
 */
const val COLLECTION_USERS = "users"

class FirestoreUtil {
    companion object {
        val db = Firebase.firestore // <==> FirebaseFirestore.getInstance()

        fun updateUser(user: User, firebaseUid: String): Task<Void> {
            return db.collection(COLLECTION_USERS)
                .document(firebaseUid)
                .set(user)
        }

        fun updatePhoneNumber(firebaseUid: String, phoneNumber: String?) : Task<Void>? {
            if (phoneNumber == null) {
                return null
            } else {
                return db.collection(COLLECTION_USERS)
                    .document(firebaseUid)
                    .update("phone", phoneNumber)
            }
        }

        fun getUserFromUid(firebaseUid: String): Task<User?> {
            val docRef = db.collection(COLLECTION_USERS).document(firebaseUid)
            return docRef.get().continueWith {
                it.result?.toObject<User>()
            }
        }
    }
}
