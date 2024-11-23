package udesc.gym

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityEditarExercicioBinding

class EditarExercicio : AppCompatActivity() {

    private lateinit var binding: ActivityEditarExercicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditarExercicioBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Cria um OnClickListener que chama finish()
        val finishListener = View.OnClickListener {
            finish()
        }

        // Atribui o mesmo listener a todos os botões
        binding.cancelButton.setOnClickListener(finishListener)
        binding.buttonSalvar.setOnClickListener(finishListener)
        binding.buttonExcluir.setOnClickListener(finishListener)
    }
}