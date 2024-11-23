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

        // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
        binding.cancelButton.setOnClickListener { finish() }

        // Inicializa o SharedPreferences
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)

        // Botão para salvar os dados
        binding.buttonSalvar.setOnClickListener {
            val nomeExercicio = binding.textNomeExercicio.text.toString().trim()
            val linkImgExercicio = binding.textImagemExercicio.text.toString().trim()

            // Validação simples: o nome do exercício é obrigatório
            if (nomeExercicio.isNotEmpty()) {
                // Recupera os exercícios já salvos
                var exerciciosSalvos = sharedPreferences.getString("lista_exercicios", "").orEmpty()

                // Adiciona o novo exercício no formato: "Nome - LinkImagem"
                if (exerciciosSalvos.isNotEmpty()) {
                    exerciciosSalvos += "\n" // Adiciona uma quebra de linha entre os exercícios
                }
                exerciciosSalvos += "$nomeExercicio - $linkImgExercicio"

                // Salva os exercícios atualizados no SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("lista_exercicios", exerciciosSalvos)
                editor.apply()

                // Exibe mensagem de sucesso e limpa os campos
                Toast.makeText(this, "Exercício salvo com sucesso!", Toast.LENGTH_SHORT).show()
                binding.textNomeExercicio.setText("")
                binding.textImagemExercicio.setText("")
                finish() // Finaliza a Activity e volta para a lista
            } else {
                // Exibe uma mensagem de erro se o campo nome estiver vazio
                Toast.makeText(
                    this,
                    "Por favor, preencha o nome do exercício antes de salvar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}