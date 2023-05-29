import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onValueChange: (String) -> Unit) {
    TextField(
        value = "",
        onValueChange = onValueChange,
        placeholder = { Text(text = "Search") },
        // You can customize the TextField further
    )
}

@Composable
fun VideoCard(video: VideoItem) {
    // replace this with your actual video item UI
    Text(text = "Video: ${video.title}")
}

@Composable
fun VideoCards(videos: List<VideoItem>) {
    Column {
        videos.forEach { video ->
            VideoCard(video = video)
        }
    }
}


@Composable
fun Videos(
    vm: VideosViewModel,
) {
    val videos = vm.videos
    val isLoading = vm.isLoading

    Column {
        SearchBar(onValueChange = vm::onSearchQueryChanged)
        if (isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            VideoCards(videos = videos.value)
        }
    }
}


class PreviewVideosViewModel : VideosViewModel() {
    override suspend fun searchVideos(query: String): List<VideoItem> {
        // Replace with actual implementation
        delay(2000) // simulate network delay
        return listOf(
            VideoItem("Video 1", "https://via.placeholder.com/150"),
            VideoItem("Video 2", "https://via.placeholder.com/150"),
            VideoItem("Video 3", "https://via.placeholder.com/150"),
        )
    }
}

data class VideoItem(val title: String, val thumbnailUrl: String)

abstract class VideosViewModel : ViewModel() {
    val videos = mutableStateOf(listOf<VideoItem>())
    val isLoading = mutableStateOf(false)
    var searchQuery = mutableStateOf("")

    init {
        attemptSearchVideos()
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        attemptSearchVideos()
    }

    private fun attemptSearchVideos() {
        viewModelScope.launch {
            isLoading.value = true
            videos.value = searchVideos(searchQuery.value)
            isLoading.value = false
        }
    }

    abstract suspend fun searchVideos(query: String): List<VideoItem>
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    Videos(PreviewVideosViewModel())
}