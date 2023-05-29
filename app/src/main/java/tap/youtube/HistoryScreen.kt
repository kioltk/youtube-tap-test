package tap.youtube

import VideoItem
import Videos
import VideosViewModel
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tap.youtube.modules.youtube.VideoItemDao
import javax.inject.Inject

@Composable
fun HistoryScreen() {
    Videos(hiltViewModel())
}

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val videoItemDao: VideoItemDao
) : VideosViewModel() {
    override suspend fun searchVideos(query: String): List<VideoItem> {
        return videoItemDao.searchVideos(query)
            .map { video ->
                VideoItem(
                    title = video.title,
                    thumbnailUrl = video.thumbnailUrl
                )
            }
    }
}
