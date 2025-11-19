package com.yitimlatora.data.repository

import com.yitimlatora.data.dao.BookmarkDao
import com.yitimlatora.data.dao.HistoryDao
import com.yitimlatora.data.model.Bookmark
import com.yitimlatora.data.model.HistoryItem
import kotlinx.coroutines.flow.Flow

class YitimLatoraRepository(
    private val bookmarkDao: BookmarkDao,
    private val historyDao: HistoryDao
) {
    // Bookmarks
    val allBookmarks: Flow<List<Bookmark>> = bookmarkDao.getAllBookmarks()
    
    suspend fun addBookmark(title: String, url: String) {
        bookmarkDao.insertBookmark(Bookmark(title = title, url = url))
    }
    
    suspend fun removeBookmark(bookmark: Bookmark) {
        bookmarkDao.deleteBookmark(bookmark)
    }
    
    suspend fun removeBookmarkByUrl(url: String) {
        bookmarkDao.deleteBookmarkByUrl(url)
    }
    
    suspend fun isBookmarked(url: String): Boolean {
        return bookmarkDao.isBookmarked(url)
    }
    
    suspend fun getBookmarkByUrl(url: String): Bookmark? {
        return bookmarkDao.getBookmarkByUrl(url)
    }
    
    // History
    val recentHistory: Flow<List<HistoryItem>> = historyDao.getRecentHistory()
    
    suspend fun addToHistory(title: String, url: String) {
        historyDao.insertHistoryItem(HistoryItem(title = title, url = url))
    }
    
    suspend fun clearHistory() {
        historyDao.clearHistory()
    }
    
    suspend fun removeHistoryItem(item: HistoryItem) {
        historyDao.deleteHistoryItem(item)
    }
}

