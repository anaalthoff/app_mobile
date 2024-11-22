package udesc.gym

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityPainelBinding

class Painel : AppCompatActivity() {

    private lateinit var binding: ActivityPainelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPainelBinding.inflate(layoutInflater)
        setContentView(binding.main)

        binding.cancelButton.setOnClickListener{
            // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
            finish()
        }

        // Define o OnClickListener para múltiplos elementos
        val navigateListener = View.OnClickListener {
            val intent = Intent(this, Exercicios::class.java)
            startActivity(intent)
        }

        // Aplica o listener aos dois elementos
        binding.setaExercicio.setOnClickListener(navigateListener)
        binding.textExercicio.setOnClickListener(navigateListener)
    }
}