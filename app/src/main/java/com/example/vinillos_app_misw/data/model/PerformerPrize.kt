package com.example.vinillos_app_misw.data.model

import androidx.room.*

//data class PerformerPrize(
//    val id: Int,
//    val premationDate: String
//)

@Entity(
    foreignKeys = [
        ForeignKey(entity = Performer::class, parentColumns = ["id"], childColumns = ["performerId"]),
    ]
)
data class PerformerPrize(
    @PrimaryKey val id: Int,
    val premationDate: String,
    val performerId: Int // Foreign key linking to Performer
)