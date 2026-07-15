package com.ativador.universal

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.ativador.universal.databinding.ActivitySuccessBinding

class SuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(b.root)

        val brand = Branding.fromJson(Prefs.branding(this))
        b.tvTitle.text = brand.appName
        b.tvCode.text = "Código: ${Prefs.activatedCode(this) ?: "-"}"
        if (brand.logo.isNotBlank()) b.ivLogo.load(brand.logo)
        try { b.tvTitle.setTextColor(Color.parseColor(brand.primaryColor)) } catch (_: Exception) {}

        b.btnWhats.visibility = if (brand.whatsapp.isNotBlank()) View.VISIBLE else View.GONE
        b.btnWhats.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${brand.whatsapp}")))
        }
        b.btnSupport.visibility = if (brand.support.isNotBlank()) View.VISIBLE else View.GONE
        b.btnSupport.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(brand.support)))
        }
        b.btnSettings.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
    }
}
