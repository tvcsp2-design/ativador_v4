package com.ativador.universal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ativador.universal.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var b: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.etUrl.setText(Prefs.panelUrl(this) ?: "")

        b.btnSave.setOnClickListener {
            val url = b.etUrl.text.toString().trim()
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                Toast.makeText(this, "URL deve começar com http:// ou https://", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            Prefs.setPanelUrl(this, url)
            Toast.makeText(this, "URL do painel salva!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }

        b.btnReset.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Resetar ativação?")
                .setMessage("O código atual será removido e você poderá ativar outro.")
                .setPositiveButton("Sim") { _, _ ->
                    Prefs.clearActivation(this)
                    Toast.makeText(this, "Ativação removida", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
