/**
 * Represents a single news item in the feed.
 *
 * @property id Unique identifier for the news item
 * @property title Headline of the news article
 * @property category Category the news belongs to (e.g., "Technology", "Sports")
 * @property content Brief content or summary of the news
 */
data class NewsItem(
    val id: Int,
    val title: String,
    val category: String,
    val content: String
)

/**
 * Represents the full details fetched asynchronously for a news item.
 *
 * @property newsId Reference to the original NewsItem id
 * @property fullContent Extended article content
 * @property author Name of the author
 * @property publishedAt Simulated publication timestamp
 */
data class NewsDetail(
    val newsId: Int,
    val fullContent: String,
    val author: String,
    val publishedAt: String
)
