package com.ativador.universal

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.ativador.universal.databinding.ActivityActivationBinding
import kotlinx.coroutines.launch

class ActivationActivity : AppCompatActivity() {

    private lateinit var b: ActivityActivationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityActivationBinding.inflate(layoutInflater)
        setContentView(b.root)

        val brand = Branding.fromJson(Prefs.branding(this))
        applyBranding(brand)

        b.btnActivate.setOnClickListener {
            val code = b.etCode.text.toString().trim()
            if (code.length < 4) { b.tvStatus.text = "Digite um código válido"; return@setOnClickListener }
            val panel = Prefs.panelUrl(this) ?: run {
                startActivity(Intent(this, SettingsActivity::class.java)); return@setOnClickListener
            }
            b.btnActivate.isEnabled = false
            b.tvStatus.text = "Validando..."
            val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"

            lifecycleScope.launch {
                val r = Api.activate(panel, code, deviceId)
                b.btnActivate.isEnabled = true
                if (r.success) {
                    Prefs.setActivated(this@ActivationActivity, code, r.token ?: "")
                    r.branding?.let { Prefs.saveBranding(this@ActivationActivity, it.toJson()) }
                    startActivity(Intent(this@ActivationActivity, SuccessActivity::class.java))
                    finish()
                } else {
                    b.tvStatus.text = r.message
                }
            }
        }

        b.btnWhats.setOnClickListener {
            val wa = brand.whatsapp
            if (wa.isBlank()) return@setOnClickListener
            val url = "https://wa.me/$wa"
            startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)))
        }

        b.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun applyBranding(brand: Branding) {
        b.tvTitle.text = brand.appName
        b.tvWelcome.text = brand.welcome
        if (brand.logo.isNotBlank()) b.ivLogo.load(brand.logo)
        try {
            val color = Color.parseColor(brand.primaryColor)
            b.btnActivate.setBackgroundColor(color)
            b.tvTitle.setTextColor(color)
        } catch (_: Exception) {}
        b.btnWhats.visibility = if (brand.whatsapp.isNotBlank()) View.VISIBLE else View.GONE
        b.tvPhone.text = brand.phone
        b.tvPhone.visibility = if (brand.phone.isNotBlank()) View.VISIBLE else View.GONE
    }
}
