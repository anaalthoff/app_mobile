package udesc.gym

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityCadastrarExercicioBinding

class CadastrarExercicio : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarExercicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastrarExercicioBinding.inflate(layoutInflater)
        setContentView(binding.main)

        binding.cancelButton.setOnClickListener{
            // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
            finish()
        }

        binding.buttonSalvar.setOnClickListener{
            finish()
        }
    }
}