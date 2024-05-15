package com.example.weather.data.anotation

import androidx.annotation.StringDef
import com.example.weather.data.anotation.Icon.Companion.CLEAR
import com.example.weather.data.anotation.Icon.Companion.CLOUDS
import com.example.weather.data.anotation.Icon.Companion.RAIN
import com.example.weather.data.anotation.Icon.Companion.SNOW
import com.example.weather.data.anotation.Icon.Companion.SQUALL

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    CLEAR,
    RAIN,
    SNOW,
    CLOUDS,
    SQUALL
)
annotation class Icon {
    companion object {
        const val CLEAR = "Clear"
        const val RAIN = "Rain"
        const val SNOW = "Snow"
        const val CLOUDS = "Clouds"
        const val SQUALL = "Squall"
    }
}
