package com.ativador.universal

import org.json.JSONObject

data class Branding(
    val appName: String = "Ativador",
    val primaryColor: String = "#2196F3",
    val whatsapp: String = "",
    val phone: String = "",
    val welcome: String = "Bem-vindo! Insira seu código de ativação.",
    val support: String = "",
    val logo: String = ""
) {
    fun toJson(): String = JSONObject().apply {
        put("appName", appName); put("primaryColor", primaryColor)
        put("whatsapp", whatsapp); put("phone", phone)
        put("welcome", welcome); put("support", support); put("logo", logo)
    }.toString()

    companion object {
        fun fromJson(s: String?): Branding {
            if (s.isNullOrBlank()) return Branding()
            return try {
                val o = JSONObject(s)
                Branding(
                    appName = o.optString("appName", "Ativador"),
                    primaryColor = o.optString("primaryColor", "#2196F3"),
                    whatsapp = o.optString("whatsapp", ""),
                    phone = o.optString("phone", ""),
                    welcome = o.optString("welcome", "Bem-vindo! Insira seu código de ativação."),
                    support = o.optString("support", ""),
                    logo = o.optString("logo", "")
                )
            } catch (e: Exception) { Branding() }
        }
    }
}
