package com.example.madlevel7example

data class Quiz(
    var question: String,
    var answer: String
) {
    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result["question"] = question
        result["answer"] = answer

        return result
    }
}
