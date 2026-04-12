import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

// ─── Color Palette ──────────────────────────────────────────────────────
private val DarkBg = Color(0xFF0F172A)
private val CardBg = Color(0xFF1E293B)
private val CardBgLight = Color(0xFF334155)
private val AccentBlue = Color(0xFF3B82F6)
private val AccentPurple = Color(0xFF8B5CF6)
private val AccentGreen = Color(0xFF10B981)
private val AccentOrange = Color(0xFFF59E0B)
private val AccentPink = Color(0xFFEC4899)
private val AccentRed = Color(0xFFEF4444)
private val TextPrimary = Color(0xFFF1F5F9)
private val TextSecondary = Color(0xFF94A3B8)
private val TextMuted = Color(0xFF64748B)

private fun categoryColor(category: String): Color = when (category) {
    "Technology" -> AccentBlue
    "Sports" -> AccentGreen
    "Finance" -> AccentOrange
    "Health" -> AccentPink
    "Science" -> AccentPurple
    else -> AccentBlue
}

private fun categoryEmoji(category: String): String = when (category) {
    "Technology" -> "💻"
    "Sports" -> "⚽"
    "Finance" -> "💰"
    "Health" -> "🏥"
    "Science" -> "🔬"
    else -> "📰"
}

// ─── Main Screen ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedScreen() {
    // ── Log system ──────────────────────────────────────────────────────
    val logs = remember { mutableStateListOf<String>() }
    val addLog: (String) -> Unit = { message -> logs.add(0, message) }

    // ── Service & Repository ────────────────────────────────────────────
    val feedService = remember { NewsFeedService(onLog = addLog) }
    val repository = remember { NewsRepository(onLog = addLog) }
    val allCategories = remember { listOf("All") + feedService.categories }

    // ── State ───────────────────────────────────────────────────────────
    val readCount by repository.readCount.collectAsState()
    val selectedCategory = remember { mutableStateOf("All") }
    val expandedItemId = remember { mutableStateOf<Int?>(null) }
    val showLogs = remember { mutableStateOf(false) }

    // ── Collected news items & details ──────────────────────────────────
    val allItems = remember { mutableStateListOf<Pair<NewsItem, String>>() }
    val newsDetails = remember { mutableStateMapOf<Int, NewsDetail>() }
    val scope = rememberCoroutineScope()

    // ── Collect the full stream (all categories) ────────────────────────
    // Demonstrates: flow { emit() }, .onEach {}, .filter {}, .map {}, .catch {}
    LaunchedEffect(Unit) {
        addLog("🚀 News Feed Simulator started!")
        addLog("📡 Streaming news items every 2 seconds...")

        feedService.newsFlow()
            .onEach { item ->
                addLog("📡 [STREAM] Received: #${item.id} \"${item.title}\" [${item.category}]")
            }
            .collect { item ->
                // .map {} transformation to formatted string
                val formatted = "[${item.category.uppercase()}] Breaking: ${item.title} — ${item.content}"
                addLog("✨ [TRANSFORM] $formatted")

                allItems.add(0, Pair(item, formatted))

                // FEATURE 4: StateFlow — increment read count
                repository.incrementReadCount()

                // FEATURE 5: Async detail fetching with async/await
                scope.launch {
                    val deferred = async {
                        repository.fetchNewsDetail(item)
                    }
                    val detail = deferred.await()
                    newsDetails[item.id] = detail
                }
            }
    }

    // ── Derived: filtered items based on selected category ──────────────
    // FEATURE 2: Filter by category using .filter {}
    val filteredItems = allItems.filter { (item, _) ->
        selectedCategory.value == "All" || item.category == selectedCategory.value
    }

    // When user selects a category, log it
    LaunchedEffect(selectedCategory.value) {
        if (selectedCategory.value != "All") {
            addLog("🔍 [FILTER] User filtering by: ${selectedCategory.value}")
        }
    }

    // ── UI ───────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ── Header ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AccentBlue.copy(alpha = 0.3f),
                                DarkBg
                            )
                        )
                    )
                    .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "📰 News Feed",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                "Flow • StateFlow • Coroutines",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }

                        // Read count badge (StateFlow)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = AccentPurple.copy(alpha = 0.2f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("📊", fontSize = 16.sp)
                                Text(
                                    "$readCount",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentPurple
                                )
                                Text(
                                    "read",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Category Filter Chips ───────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allCategories.forEach { category ->
                            val isSelected = selectedCategory.value == category
                            val chipColor = if (category == "All") AccentBlue
                                else categoryColor(category)

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { selectedCategory.value = category },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) chipColor else chipColor.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (category != "All") {
                                        Text(categoryEmoji(category), fontSize = 14.sp)
                                    }
                                    Text(
                                        text = category,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else chipColor
                                    )
                                    // Show count
                                    val count = if (category == "All") allItems.size
                                        else allItems.count { it.first.category == category }
                                    if (count > 0) {
                                        Text(
                                            text = "$count",
                                            fontSize = 11.sp,
                                            color = if (isSelected) Color.White.copy(alpha = 0.7f)
                                                else chipColor.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ── News Cards List ─────────────────────────────────────
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredItems.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = AccentBlue,
                                    strokeWidth = 3.dp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Waiting for news...",
                                    fontSize = 16.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    "New items arrive every 2 seconds",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                items(filteredItems, key = { it.first.id }) { (item, formatted) ->
                    val isExpanded = expandedItemId.value == item.id
                    val detail = newsDetails[item.id]

                    NewsCard(
                        item = item,
                        formattedText = formatted,
                        detail = detail,
                        isExpanded = isExpanded,
                        onToggle = {
                            expandedItemId.value = if (isExpanded) null else item.id
                        }
                    )
                }

                // Bottom spacer for log toggle
                item { Spacer(modifier = Modifier.height(60.dp)) }
            }
        }

        // ── Floating Log Toggle Button ──────────────────────────────
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clip(CircleShape)
                .clickable { showLogs.value = !showLogs.value },
            shape = CircleShape,
            color = if (showLogs.value) AccentRed else CardBgLight,
            shadowElevation = 8.dp
        ) {
            Text(
                text = if (showLogs.value) "✕" else "📋",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp
            )
        }

        // ── Log Panel (slides up from bottom) ───────────────────────
        AnimatedVisibility(
            visible = showLogs.value,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            LogPanel(logs = logs)
        }
    }
}

// ─── News Card Component ────────────────────────────────────────────────

@Composable
fun NewsCard(
    item: NewsItem,
    formattedText: String,
    detail: NewsDetail?,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val color = categoryColor(item.category)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top row: Category badge + time indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = color.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(categoryEmoji(item.category), fontSize = 12.sp)
                        Text(
                            text = item.category.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = color,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // News ID
                Text(
                    text = "#${item.id}",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = item.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Content preview
            Text(
                text = item.content,
                fontSize = 14.sp,
                color = TextSecondary,
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            // Tap to expand hint
            if (!isExpanded) {
                Text(
                    text = "Tap to read more →",
                    fontSize = 12.sp,
                    color = color,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // ── Expanded Content ────────────────────────────────────
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Formatted output (.map {} transformation)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = color.copy(alpha = 0.08f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                "✨ Transformed Output (.map):",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = color,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = formattedText,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary.copy(alpha = 0.9f),
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Async-fetched detail
                    if (detail != null) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = AccentGreen.copy(alpha = 0.08f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    "⚡ Async-Fetched Details (async/await):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGreen,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = detail.fullContent,
                                    fontSize = 13.sp,
                                    color = TextPrimary.copy(alpha = 0.85f),
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "✍️ ${detail.author}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = AccentGreen
                                    )
                                    Text(
                                        "📅 ${detail.publishedAt}",
                                        fontSize = 12.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    } else {
                        // Loading state
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = AccentOrange.copy(alpha = 0.08f)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = AccentOrange,
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    "Fetching details via async/await...",
                                    fontSize = 13.sp,
                                    color = AccentOrange
                                )
                            }
                        }
                    }

                    // Collapse hint
                    Text(
                        text = "Tap to collapse ↑",
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

// ─── Log Panel Component ────────────────────────────────────────────────

@Composable
fun LogPanel(logs: List<String>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = Color(0xFF0D1117),
        shadowElevation = 16.dp
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF161B22))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Terminal dots
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(AccentRed, AccentOrange, AccentGreen).forEach { dotColor ->
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(dotColor)
                            )
                        }
                    }
                    Text(
                        "Live Log Console",
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Text(
                    "${logs.size} entries",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            // Log entries
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                items(logs.size) { index ->
                    val log = logs[index]
                    val logColor = when {
                        "STREAM" in log -> Color(0xFF7EE787)
                        "FILTER" in log -> Color(0xFF79C0FF)
                        "TRANSFORM" in log -> Color(0xFFFFA657)
                        "STATE" in log -> Color(0xFFD2A8FF)
                        "FETCH" in log && "⏳" in log -> Color(0xFFFFA198)
                        "FETCH" in log && "✅" in log -> Color(0xFF7EE787)
                        "ERROR" in log -> Color(0xFFFF7B72)
                        "🚀" in log -> Color(0xFF79C0FF)
                        else -> Color(0xFF8B949E)
                    }
                    Text(
                        text = log,
                        color = logColor,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(vertical = 2.dp),
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
