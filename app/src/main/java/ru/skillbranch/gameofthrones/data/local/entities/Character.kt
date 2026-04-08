package ru.skillbranch.gameofthrones.data.local.entities

import androidx.room.*

@Entity(tableName = "characters")
data class Character(
    @PrimaryKey
    val id: String,
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String> = listOf(),
    val aliases: List<String> = listOf(),
    val father: String, //rel
    val mother: String, //rel
    val spouse: String,
    @ColumnInfo(name = "house_id")
    val houseId: HouseType, //rel
    @ColumnInfo(name = "is_bookmarked")
    var isBookmarked: Boolean

)

@DatabaseView(
    """
        SELECT id, house_id AS house, name, titles, aliases, is_bookmarked
        FROM characters
        ORDER BY name ASC
    """
)
data class CharacterItem(
    val id: String,
    val house: HouseType, //rel
    val name: String,
    val titles: List<String>,
    val aliases: List<String>,
    @ColumnInfo(name = "is_bookmarked")
    var isBookmarked: Boolean

)

data class CharacterFull(
    val id: String,
    val name: String,
    val words: String,
    val born: String,
    val died: String,
    val titles: List<String>,
    val aliases: List<String>,
    @ColumnInfo(name = "is_bookmarked")
    var isBookmarked: Boolean,
    @ColumnInfo(name = "house_id")
    val house: HouseType, //rel
    val mother: RelativeCharacter? = null,
    val father: RelativeCharacter? = null
)

data class RelativeCharacter(
    val id: String,
    val name: String,
    @ColumnInfo(name = "house_id")
    val houseId: HouseType, //rel
    @ColumnInfo(name = "is_bookmarked")
    val isBookmarked: Boolean
)