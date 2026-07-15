package com.ativador.universal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object Api {

    private fun request(urlStr: String, method: String = "GET", body: String? = null): String {
        val conn = (URL(urlStr).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 15000
            readTimeout = 20000
            setRequestProperty("Accept", "application/json")
            if (body != null) {
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
                OutputStreamWriter(outputStream, Charsets.UTF_8).use { it.write(body) }
            }
        }
        val stream = try { conn.inputStream } catch (e: Exception) { conn.errorStream }
        return BufferedReader(InputStreamReader(stream ?: return "")).use { it.readText() }
            .also { conn.disconnect() }
    }

    /** Busca branding do dono do código no painel. */
    suspend fun getBranding(panelUrl: String, code: String?): Branding = withContext(Dispatchers.IO) {
        try {
            val q = if (!code.isNullOrBlank()) "&code=${URLEncoder.encode(code, "UTF-8")}" else ""
            val res = request("$panelUrl/api.php?action=getBranding$q")
            val json = JSONObject(res)
            if (json.optBoolean("success")) {
                Branding.fromJson(json.optJSONObject("branding")?.toString())
            } else Branding()
        } catch (e: Exception) { Branding() }
    }

    data class ActivateResult(
        val success: Boolean,
        val message: String,
        val token: String? = null,
        val branding: Branding? = null
    )

    suspend fun activate(panelUrl: String, code: String, deviceId: String): ActivateResult =
        withContext(Dispatchers.IO) {
            try {
                val body = JSONObject().apply {
                    put("action", "activate")
                    put("activation_code", code)
                    put("device_id", deviceId)
                    put("hardware_fingerprint", deviceId)
                    put("device_os", "android")
                }.toString()
                val res = request("$panelUrl/validate.php", "POST", body)
                val o = JSONObject(res)
                val brand = o.optJSONObject("branding")?.let { Branding.fromJson(it.toString()) }
                ActivateResult(
                    success = o.optBoolean("success"),
                    message = o.optString("message"),
                    token = o.optString("license_token"),
                    branding = brand
                )
            } catch (e: Exception) {
                ActivateResult(false, "Erro de conexão: ${e.message}")
            }
        }
}
