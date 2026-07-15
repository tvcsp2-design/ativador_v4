package com.ativador.universal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.ativador.universal.databinding.ActivitySplashBinding
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var b: ActivitySplashBinding
    private var logoTaps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(b.root)

        val brand = Branding.fromJson(Prefs.branding(this))
        b.tvName.text = brand.appName
        if (brand.logo.isNotBlank()) b.ivLogo.load(brand.logo)

        // 5 toques na logo abrem as configurações
        b.ivLogo.setOnClickListener {
            logoTaps++
            if (logoTaps >= 5) {
                startActivity(Intent(this, SettingsActivity::class.java))
                logoTaps = 0
            }
        }

        val panel = Prefs.panelUrl(this)
        if (panel.isNullOrBlank()) {
            // Primeira execução: obriga configurar URL
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, SettingsActivity::class.java).putExtra("first", true))
                finish()
            }, 800)
            return
        }

        lifecycleScope.launch {
            val code = Prefs.activatedCode(this@SplashActivity)
            val newBrand = Api.getBranding(panel, code)
            Prefs.saveBranding(this@SplashActivity, newBrand.toJson())
            b.tvName.text = newBrand.appName
            if (newBrand.logo.isNotBlank()) b.ivLogo.load(newBrand.logo)

            Handler(Looper.getMainLooper()).postDelayed({
                if (code.isNullOrBlank()) {
                    startActivity(Intent(this@SplashActivity, ActivationActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, SuccessActivity::class.java))
                }
                finish()
            }, 1200)
        }
    }
}
