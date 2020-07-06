package com.example.madlevel7example

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.firestore.FirebaseFirestore

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val FIRESTORE_TAG = "FIRESTORE"
    private val context = application.applicationContext
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var quizDocument =
        firestore.collection("quizzes").document("quiz1")

    var question: String = ""
    var answer: String = ""

    init {
        quizDocument.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    this.question = document.getString("question").toString()
                    this.answer = document.getString("answer").toString()
                } else {
                    Log.e(FIRESTORE_TAG, "No such document")
                }
            } else {
                Log.d(FIRESTORE_TAG, "Error getting documents", task.exception)
            }
        }
    }

    fun isAnswerCorrect(answer: String): Boolean {
        return answer.toLowerCase() == this.answer.toLowerCase()
    }

    fun createQuestion(question: String, answer: String) {
        this.question = question
        this.answer = answer

        // persist data to firestore
        val quiz = Quiz(question, answer).toMap()

        firestore.collection("quizzes")
            .document("quiz1")
            .set(quiz)
            .addOnSuccessListener {
                Log.e(FIRESTORE_TAG, "Quiz document update successful!")
                Toast.makeText(context, "Quiz has been updated!", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { e ->
                Log.e(FIRESTORE_TAG, "Error adding Quiz document", e)
                Toast.makeText(context, "Quiz could not be updated!", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    fun isQuestionCreated(): Boolean {
        return question.isNotBlank() && answer.isNotBlank()
    }

}