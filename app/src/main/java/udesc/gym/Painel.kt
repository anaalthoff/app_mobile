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

        binding.setaExercicio.setOnClickListener {
            val intent = Intent(this, MeusExercicios::class.java)
            startActivity(intent)
        }
        binding.textExercicio.setOnClickListener {
            val intent = Intent(this, MeusExercicios::class.java)
            startActivity(intent)
        }
        binding.setaTreino.setOnClickListener {
            val intent = Intent(this, MeusTreinos::class.java)
            startActivity(intent)
        }
        binding.textTreino.setOnClickListener {
            val intent = Intent(this, MeusTreinos::class.java)
            startActivity(intent)
        }
    }
}