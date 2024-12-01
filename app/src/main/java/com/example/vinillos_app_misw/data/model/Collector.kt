package com.example.vinillos_app_misw.data.model
import androidx.room.*

//@Entity(tableName = "collectors_table")
//data class Collector(
//    val id: Int,
//    val name: String,
//    val telephone: String,
//    val email: String,
//    val comments: List<Comment>,
//    val favoritePerformers: List<Performer>,
//    val collectorAlbums: List<CollectorAlbum>
//)

@Entity(tableName = "collector_table")
data class Collector(
    @PrimaryKey val id: Int,
    val name: String,
    val telephone: String,
    val email: String
)


data class CollectorWithDetails(
    @Embedded val collector: Collector,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectorId"
    )
    val favoritePerformers: List<Performer>,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectorId"
    )
    val collectorAlbums: List<CollectorAlbum>,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectorId"
    )
    val comments: List<Comment>
)