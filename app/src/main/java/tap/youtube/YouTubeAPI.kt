package tap.youtube

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class YouTubeAPI {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/youtube/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(YouTubeApiService::class.java)
}

interface YouTubeApiService {
    @GET("search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("key") apiKey: String = "AIzaSyB9PdtQsK9-COudepsEPPEoNW7eAqLkkak",
        @Query("type") type: String = "video",
        @Query("part") part: String = "snippet",
    ): YouTubeApiResponse
}


data class YouTubeApiResponse(
    @SerializedName("items")
    val items: List<YouTubeVideo>
)

data class YouTubeVideo(
    @SerializedName("id")
    val id: VideoId,
    @SerializedName("snippet")
    val snippet: VideoSnippet
)

data class VideoId(
    @SerializedName("videoId")
    val videoId: String
)

data class VideoSnippet(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thumbnails")
    val thumbnails: VideoThumbnails
)

data class VideoThumbnails(
    @SerializedName("default")
    val default: ThumbnailDetails,
    @SerializedName("medium")
    val medium: ThumbnailDetails,
    @SerializedName("high")
    val high: ThumbnailDetails
)

data class ThumbnailDetails(
    @SerializedName("url")
    val url: String
)