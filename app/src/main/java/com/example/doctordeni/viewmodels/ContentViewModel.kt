package com.example.doctordeni.viewmodels

import androidx.lifecycle.ViewModel
import com.example.doctordeni.models.responses.Article
import com.example.doctordeni.utils.SessionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContentViewModel(
    private val db: DatabaseReference, private val sessionManager: SessionManager
) : ViewModel() {

    private val _content = MutableStateFlow<List<Article?>?>(null)
    val content: StateFlow<List<Article?>?> = _content.asStateFlow()

    private val _selected = MutableStateFlow<Article?>(Article())
    val selected: StateFlow<Article?> = _selected.asStateFlow()

    private val _dbError = MutableStateFlow<DatabaseError?>(null)
    val dbError: StateFlow<DatabaseError?> = _dbError.asStateFlow()

    init {
        getContent()
    }

    fun selectArticle(article: Article) {
//        article.content += faker.lorem().paragraph()
        sessionManager.saveArticle(article)
    }

    fun getSelectedArticle() {
        _selected.value = sessionManager.fetchSelectedArticle()
    }

    fun getContent() {
        val content = db.child("news")
        content.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = snapshot.children.mapNotNull { it.getValue(Article::class.java) }
                _content.value = dataList
            }

            override fun onCancelled(error: DatabaseError) {
                _dbError.value = error
            }

        })
    }
}