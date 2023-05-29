package tap.youtube

import VideoItem
import Videos
import VideosViewModel
import androidx.compose.runtime.Composable

@Composable
fun SearchScreen() {
    Videos(
        YouTubeApiViewModel(),
    )
}


class YouTubeApiViewModel : VideosViewModel() {
    override suspend fun searchVideos(query: String): List<VideoItem> =
        YouTubeAPI().service
            .searchVideos(query)
            .items
            .map { video ->
                VideoItem(
                    title = video.snippet.title,
                    thumbnailUrl = video.snippet.thumbnails.default.url
                )
            }
}
