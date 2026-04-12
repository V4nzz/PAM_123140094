import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository layer responsible for state management and async data fetching.
 *
 * Demonstrates:
 * - MutableStateFlow / StateFlow for reactive state tracking
 * - Suspend functions with artificial delay for async simulation
 *
 * @param onLog Callback to send log messages to the UI instead of println
 */
class NewsRepository(private val onLog: (String) -> Unit = {}) {

    // ── State Management ────────────────────────────────────────────────
    // Internal mutable state tracking the number of news items "read"
    private val _readCount = MutableStateFlow(0)

    /**
     * Publicly exposed read-only StateFlow.
     * Observers can collect this to react to read-count changes.
     */
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    /**
     * Increments the read counter by one.
     * Called each time a news item is collected/consumed.
     */
    fun incrementReadCount() {
        _readCount.value++
        onLog("📊 [STATE] Read count updated → ${_readCount.value} article(s) read")
    }

    // ── Async Detail Fetching ───────────────────────────────────────────

    /** Simulated author names for detail generation */
    private val authors = listOf(
        "Alice Johnson", "Bob Smith", "Carol Lee",
        "David Kim", "Eva Martinez", "Frank Chen"
    )

    /**
     * Suspend function that simulates fetching full news details from a remote
     * source. The artificial delay demonstrates non-blocking async behavior.
     *
     * @param newsItem The news item to fetch details for
     * @return A [NewsDetail] with extended information
     */
    suspend fun fetchNewsDetail(newsItem: NewsItem): NewsDetail {
        onLog("⏳ [FETCH] Fetching details for #${newsItem.id} \"${newsItem.title}\"...")
        delay(800L + (200L * newsItem.id)) // variable delay to show concurrency
        val detail = NewsDetail(
            newsId = newsItem.id,
            fullContent = "${newsItem.content} [Full article with ${(500..2000).random()} words]",
            author = authors[newsItem.id % authors.size],
            publishedAt = "2026-04-${10 + newsItem.id}T${8 + newsItem.id}:00:00Z"
        )
        onLog("✅ [FETCH] Details ready for #${newsItem.id} by ${detail.author}")
        return detail
    }
}
