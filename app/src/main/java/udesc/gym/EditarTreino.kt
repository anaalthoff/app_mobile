package udesc.gym

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityEditarTreinoBinding

import android.view.View
import android.widget.Toast

class EditarTreino : AppCompatActivity() {

    private lateinit var binding: ActivityEditarTreinoBinding
    private var nomeOriginal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditarTreinoBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Cria um OnClickListener que chama finish()
        val finishListener = View.OnClickListener {
            finish()
        }

        // Atribui o mesmo listener a todos os botões
        binding.cancelButton.setOnClickListener(finishListener)
        binding.buttonSalvar.setOnClickListener(finishListener)
        binding.buttonExcluir.setOnClickListener(finishListener)

        // Recupera os dados enviados pela Intent
        nomeOriginal = intent.getStringExtra("NOME_TREINO")

        // Preenche os campos com os dados do exercício selecionado
        binding.textNomeExercicio.setText(nomeOriginal)

        // Botão Salvar
        binding.buttonSalvar.setOnClickListener {
            val novoNome = binding.textNomeExercicio.text.toString().trim()

            if (novoNome.isNotEmpty()) {
                atualizarTreino(nomeOriginal.orEmpty(), novoNome)
                Toast.makeText(this, "Treino atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "O nome do treino não pode ficar vazio.", Toast.LENGTH_SHORT).show()
            }
        }

        // Botão Excluir
        binding.buttonExcluir.setOnClickListener {
            excluirTreino(nomeOriginal.orEmpty())
            Toast.makeText(this, "Treino excluído com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    // Atualiza o exercício no SharedPreferences
    private fun atualizarTreino(nomeAntigo: String, novoNome: String) {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val treinosSalvos = sharedPreferences.getString("lista_treinos", "").orEmpty()
        val treinosAtualizados = treinosSalvos.split("\n").joinToString("\n") { linha ->
            val partes = linha.split(" - ")
            val nome = partes.getOrNull(0) ?: ""
            if (nome == nomeAntigo) "$novoNome - " else "$nome -"
        }

        val editor = sharedPreferences.edit()
        editor.putString("lista_treinos", treinosAtualizados)
        editor.apply()
    }

    // Exclui o exercício no SharedPreferences
    private fun excluirTreino(nome: String) {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val treinosSalvos = sharedPreferences.getString("lista_treinos", "").orEmpty()
        val treinosAtualizados = treinosSalvos.split("\n")
            .filter { linha ->
                val partes = linha.split(" - ")
                val nomeAtual = partes.getOrNull(0) ?: ""
                nomeAtual != nome
            }
            .joinToString("\n")

        val editor = sharedPreferences.edit()
        editor.putString("lista_treinos", treinosAtualizados)
        editor.apply()
    }
}