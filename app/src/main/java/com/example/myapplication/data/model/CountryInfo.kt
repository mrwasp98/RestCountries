package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(Converters::class)
data class CountryInfo(
    @PrimaryKey
    var commonName: String,
    var officialName: String,
    var languages: List<String>,
    val unMember: Boolean,
    val independent: Boolean,
    val capitals: List<String>?,
    val population: Int,
    val area: Float,
    val latlng: List<Double>?,
    var continent: String,
    val timezones: List<String>?,
    val carSide: String?,
    val currencies: List<String> = listOf(),
    var flag: ByteArray?,
    var flagURL: String?,
    var coatOfArms: ByteArray?,
    var coatOfArmsURL: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CountryInfo

        if (flag != null) {
            if (other.flag == null) return false
            if (!flag.contentEquals(other.flag)) return false
        } else if (other.flag != null) return false
        if (coatOfArms != null) {
            if (other.coatOfArms == null) return false
            if (!coatOfArms.contentEquals(other.coatOfArms)) return false
        } else if (other.coatOfArms != null) return false

        return true
    }
    override fun hashCode(): Int {
        var result = flag?.contentHashCode() ?: 0
        result = 31 * result + (coatOfArms?.contentHashCode() ?: 0)
        return result
    }
}