import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * Service layer responsible for generating and transforming the news stream.
 *
 * Demonstrates:
 * - Flow creation with `flow { emit() }`
 * - Operators: `.filter {}`, `.map {}`, `.onEach {}`, `.catch {}`
 *
 * @param onLog Callback to send log messages to the UI
 */
class NewsFeedService(private val onLog: (String) -> Unit = {}) {

    // Simulated news database with mixed categories (10 items)
    private val newsList = listOf(
        NewsItem(1, "Kotlin 2.0 Released with Major Improvements", "Technology",
            "JetBrains announces Kotlin 2.0 with a new K2 compiler and performance boosts."),
        NewsItem(2, "Champions League Quarter-Finals Draw Announced", "Sports",
            "The UEFA Champions League quarter-final matchups have been revealed."),
        NewsItem(3, "Kotlin Flow Simplifies Async Programming", "Technology",
            "Developers are adopting Kotlin Flow for reactive stream processing."),
        NewsItem(4, "Global Markets Rally on Positive Economic Data", "Finance",
            "Stock markets worldwide see significant gains amid optimistic forecasts."),
        NewsItem(5, "Android 16 Preview Introduces New APIs", "Technology",
            "Google releases the first developer preview of Android 16 with exciting features."),
        NewsItem(6, "World Cup 2026 Venues Confirmed", "Sports",
            "FIFA confirms all 16 host cities for the 2026 World Cup."),
        NewsItem(7, "Coroutines: The Future of Concurrent Programming", "Technology",
            "Kotlin Coroutines continue to gain traction for server-side and mobile development."),
        NewsItem(8, "New Study Reveals Benefits of Mediterranean Diet", "Health",
            "Research shows significant cardiovascular benefits from the Mediterranean diet."),
        NewsItem(9, "Jetpack Compose Multiplatform Goes Stable", "Technology",
            "JetBrains announces the stable release of Compose Multiplatform for desktop and web."),
        NewsItem(10, "SpaceX Successfully Launches Starship", "Science",
            "SpaceX completes a full orbital test flight of its Starship rocket.")
    )

    /** All available categories */
    val categories: List<String> = newsList.map { it.category }.distinct()

    /**
     * FEATURE 1: News Stream (Flow)
     * Creates a Flow that emits news items every 2 seconds using flow { emit() }
     */
    fun newsFlow(): Flow<NewsItem> = flow {
        for (news in newsList) {
            delay(2000L) // emit every 2 seconds
            emit(news)
        }
    }

    /**
     * FEATURE 2: Filter by Category using .filter {} operator
     */
    fun filteredByCategory(category: String): Flow<NewsItem> {
        return newsFlow()
            .onEach { item ->
                onLog("📡 [STREAM] Received: #${item.id} \"${item.title}\" [${item.category}]")
            }
            .filter { item ->
                item.category.equals(category, ignoreCase = true)
            }
            .onEach { item ->
                onLog("🔍 [FILTER] Passed ($category): #${item.id} \"${item.title}\"")
            }
    }

    /**
     * FEATURE 1+2+3+BONUS: Full pipeline
     * Streams all items, logs via .onEach {}, filters via .filter {},
     * transforms via .map {}, handles errors via .catch {}
     *
     * Returns Pair<NewsItem, String> — the original item + the formatted string
     */
    fun formattedFeed(category: String): Flow<Pair<NewsItem, String>> {
        return newsFlow()
            .onEach { item ->
                onLog("📡 [STREAM] Received: #${item.id} \"${item.title}\" [${item.category}]")
            }
            .filter { item ->
                item.category.equals(category, ignoreCase = true)
            }
            .onEach { item ->
                onLog("🔍 [FILTER] Passed ($category): #${item.id}")
            }
            .map { item ->
                val formatted = "[${item.category.uppercase()}] Breaking: ${item.title} — ${item.content}"
                onLog("✨ [TRANSFORM] $formatted")
                Pair(item, formatted)
            }
            .catch { exception ->
                onLog("❌ [ERROR] Stream error: ${exception.message}")
            }
    }
}
