package udesc.gym

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

        binding.cancelButton.setOnClickListener {
            // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
            finish()
        }

        // Inicializa o SharedPreferences
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)

        // Botão Salvar
        binding.buttonSalvar.setOnClickListener {
            val nomeExercicio = binding.textNomeExercicio.text.toString().trim()
            val linkImgExercicio = binding.textImagemExercicio.text.toString().trim()

            if (nomeExercicio.isNotEmpty()) {
                // Salvar os dados no SharedPreferences
                val editor = sharedPreferences.edit()
                var exerciciosSalvos = sharedPreferences.getString("lista_exercicios", "").orEmpty()

                // Adiciona o novo exercício no formato: Nome - Imagem
                if (exerciciosSalvos.isNotEmpty()) {
                    exerciciosSalvos += "\n" // Adiciona uma quebra de linha se já houver conteúdo
                }
                exerciciosSalvos += "$nomeExercicio - $linkImgExercicio"

                editor.putString("lista_exercicios", exerciciosSalvos)
                editor.apply()

                // Mostra uma mensagem de sucesso e limpa os campos
                Toast.makeText(this, "Exercício salvo com sucesso!", Toast.LENGTH_SHORT).show()
                binding.textNomeExercicio.setText("")
                binding.textImagemExercicio.setText("")
                finish() // Finaliza a Activity
            } else {
                Toast.makeText(
                    this,
                    "Preencha o nome do exercício antes de salvar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}