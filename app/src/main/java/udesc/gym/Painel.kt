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
        val navigateExerciciosListener = View.OnClickListener {
            val intent = Intent(this, MeusExercicios::class.java)
            startActivity(intent)
        }

        val navigateTreinosListener = View.OnClickListener {
            val intent = Intent(this, MeusTreinos::class.java)
            startActivity(intent)
        }

        // Aplica o listener aos dois elementos
        binding.setaExercicio.setOnClickListener(navigateExerciciosListener)
        binding.textExercicio.setOnClickListener(navigateExerciciosListener)

        binding.setaTreino.setOnClickListener(navigateTreinosListener)
        binding.textTreino.setOnClickListener(navigateTreinosListener)
    }
}