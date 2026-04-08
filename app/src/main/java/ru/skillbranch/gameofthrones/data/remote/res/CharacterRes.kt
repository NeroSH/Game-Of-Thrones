package ru.skillbranch.gameofthrones.data.remote.res

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.HouseType

@Serializable
data class CharacterRes(
    @SerialName(value = "url")
    val url: String,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "gender")
    val gender: String,
    @SerialName(value = "culture")
    val culture: String,
    @SerialName(value = "born")
    val born: String,
    @SerialName(value = "died")
    val died: String,
    @SerialName(value = "titles")
    val titles: List<String> = listOf(),
    @SerialName(value = "aliases")
    val aliases: List<String> = listOf(),
    @SerialName(value = "father")
    val father: String,
    @SerialName(value = "mother")
    val mother: String,
    @SerialName(value = "spouse")
    val spouse: String,
    @SerialName(value = "allegiances")
    val allegiances: List<String> = listOf(),
    @SerialName(value = "books")
    val books: List<String> = listOf(),
    @SerialName(value = "tvSeries")
    val tvSeries: List<String> = listOf(),
    @SerialName(value = "playedBy")
    val playedBy: List<String> = listOf(),
    @Transient val houseId: String = ""
//    @SerialName(value = "isBookmarked")
//    var isBookmarked: Boolean = false
) : IRes {
    override val id: String get() = url.lastSegment()
    override var isBookmarked: Boolean = false
    val fatherId get() = father.lastSegment()
    val motherId get() = mother.lastSegment()

    fun toCharacter(): Character {
        return Character(
            id = id,
            name = name,
            gender = gender,
            culture = culture,
            born = born,
            died = died,
            titles = titles,
            aliases = aliases,
            father = fatherId,
            mother = motherId,
            spouse = spouse,
            houseId = HouseType.fromString(houseId),
            isBookmarked = isBookmarked
        )
    }
}
