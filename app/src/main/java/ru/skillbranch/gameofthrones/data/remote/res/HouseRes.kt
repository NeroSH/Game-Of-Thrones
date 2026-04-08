package ru.skillbranch.gameofthrones.data.remote.res

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.data.local.entities.HouseType
import ru.skillbranch.gameofthrones.extensions.dropLastUntil

@Serializable
data class HouseRes(
    @SerialName(value = "url")
    val url: String,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "region")
    val region: String,
    @SerialName(value = "coatOfArms")
    val coatOfArms: String,
    @SerialName(value = "words")
    val words: String,
    @SerialName(value = "titles")
    val titles: List<String> = listOf(),
    @SerialName(value = "seats")
    val seats: List<String> = listOf(),
    @SerialName(value = "currentLord")
    val currentLord: String,
    @SerialName(value = "heir")
    val heir: String,
    @SerialName(value = "overlord")
    val overlord: String,
    @SerialName(value = "founded")
    val founded: String,
    @SerialName(value = "founder")
    val founder: String,
    @SerialName(value = "diedOut")
    val diedOut: String,
    @SerialName(value = "ancestralWeapons")
    val ancestralWeapons: List<String> = listOf(),
    @SerialName(value = "swornMembers")
    val swornMembers: List<String> = listOf()
) : IRes {
    override var isBookmarked: Boolean = false
    override val id: String
        get() = url.lastSegment()

    val shortName: String
        get() = name.split("\\s".toRegex())
            .dropLastUntil { it == "of" }

    val members: List<String>
        get() = swornMembers.map { it.lastSegment() }

    fun toHouse(): House {
        //Log.d("shortName" , "name: $name\tshortName: ${name.split("\\s".toRegex()).dropLastUntil { it == "of" }}\tsplit: ${name.split("\\s".toRegex())}")

        return House(
            HouseType.fromString(shortName),
            shortName,
            region,
            coatOfArms,
            words,
            titles,
            seats,
            currentLord,
            heir,
            overlord,
            founded,
            founder,
            diedOut,
            ancestralWeapons
        )
    }

}