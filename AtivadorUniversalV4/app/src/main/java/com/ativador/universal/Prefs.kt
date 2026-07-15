package com.ativador.universal

import android.content.Context
import android.content.SharedPreferences

object Prefs {
    private const val NAME = "ativador_prefs"
    const val KEY_PANEL_URL = "panel_url"
    const val KEY_ACTIVATED_CODE = "activated_code"
    const val KEY_LICENSE_TOKEN = "license_token"
    const val KEY_BRANDING_JSON = "branding_json"

    fun get(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun panelUrl(ctx: Context): String? =
        get(ctx).getString(KEY_PANEL_URL, null)?.trimEnd('/')

    fun setPanelUrl(ctx: Context, url: String) =
        get(ctx).edit().putString(KEY_PANEL_URL, url.trim().trimEnd('/')).apply()

    fun activatedCode(ctx: Context): String? =
        get(ctx).getString(KEY_ACTIVATED_CODE, null)

    fun setActivated(ctx: Context, code: String, token: String) =
        get(ctx).edit()
            .putString(KEY_ACTIVATED_CODE, code)
            .putString(KEY_LICENSE_TOKEN, token)
            .apply()

    fun clearActivation(ctx: Context) =
        get(ctx).edit().remove(KEY_ACTIVATED_CODE).remove(KEY_LICENSE_TOKEN).apply()

    fun saveBranding(ctx: Context, json: String) =
        get(ctx).edit().putString(KEY_BRANDING_JSON, json).apply()

    fun branding(ctx: Context): String? =
        get(ctx).getString(KEY_BRANDING_JSON, null)
}
