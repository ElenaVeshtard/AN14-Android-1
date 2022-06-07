package com.github.krottv.tmstemp.data.itunesroom

import androidx.room.Dao
import androidx.room.Query
import com.github.krottv.tmstemp.domain.TrackModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDao {

    @Query("select * from tracks where albumId = :albumId")
    fun getTracksByAlbumId(albumId: Long): Flow<List<TrackModel>>
}