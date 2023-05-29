package tap.youtube.modules.youtube

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): YoutubeDatabase {
        return Room.databaseBuilder(
            context,
            YoutubeDatabase::class.java, "database-name"
        ).build()
    }

    @Provides
    fun provideVideoItemDao(database: YoutubeDatabase): VideoItemDao {
        return database.videoItemDao()
    }
}

@Database(entities = [VideoItemDO::class], version = 1)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract fun videoItemDao(): VideoItemDao
}

@Dao
interface VideoItemDao {
    @Query("SELECT * FROM VideoItemDO WHERE title LIKE :query")
    suspend fun searchVideos(query: String): List<VideoItemDO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<VideoItemDO>)
}

@Entity
data class VideoItemDO(
    @PrimaryKey
    val videoId: String,
    val title: String,
    val thumbnailUrl: String
)