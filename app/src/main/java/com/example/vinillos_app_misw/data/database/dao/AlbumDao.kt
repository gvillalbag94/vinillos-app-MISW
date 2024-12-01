package com.example.vinillos_app_misw.data.database.dao

import androidx.room.*
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.model.AlbumWithDetails
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.model.Comment
import com.example.vinillos_app_misw.data.model.Performer
import com.example.vinillos_app_misw.data.model.Track
import org.json.JSONObject

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: Track)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerformer(performer: Performer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)

    @Query("SELECT * FROM album_table")
    suspend fun getAlbums(): List<AlbumWithDetails>

    @Transaction
    @Query("SELECT * FROM album_table WHERE id = :albumId")
    suspend fun getAlbum(albumId: Int): AlbumWithDetails?

    @Transaction
    suspend fun insertAlbumWithDetails(albumWithDetails: AlbumWithDetails) {
        insertAlbum(albumWithDetails.album)

        albumWithDetails.tracks.forEach {
            insertTrack(it)
        }
        albumWithDetails.performers.forEach {
            insertPerformer(it)
        }
        albumWithDetails.comments.forEach {
            insertComment(it)
        }
    }

    @Transaction
    suspend fun insertAlbumsWithDetails(albumsWithDetails: List<AlbumWithDetails>) {
        for (albumWithDetails in albumsWithDetails) {
            insertAlbumWithDetails(albumWithDetails)
        }
    }

    @Query("DELETE FROM album_table")
    suspend fun clearTable()
}