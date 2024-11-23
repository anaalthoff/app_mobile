package udesc.gym

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityEditarExercicioBinding
import android.net.Uri

class EditarExercicio : AppCompatActivity() {

    private lateinit var binding: ActivityEditarExercicioBinding
    private var nomeOriginal: String? = null // Declarando a variável corretamente
    private var uriImagemOriginal: Uri? = null // Guardar o URI original da imagem

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

        // Recupera os dados enviados pela Intent
        nomeOriginal = intent.getStringExtra("NOME_EXERCICIO")
        val linkImagemOriginal = intent.getStringExtra("LINK_IMG_EXERCICIO")

        // Preenche os campos com os dados do exercício selecionado
        binding.textNomeExercicio.setText(nomeOriginal)
        binding.textImagemExercicio.setText(linkImagemOriginal)

        // Se você tiver o URI da imagem salvo, exibe a imagem
        if (linkImagemOriginal != null) {
            uriImagemOriginal = Uri.parse(linkImagemOriginal)
            binding.imageView.setImageURI(uriImagemOriginal) // Exibe a imagem
        }

        // Botão Salvar
        binding.buttonSalvar.setOnClickListener {
            val novoNome = binding.textNomeExercicio.text.toString().trim()
            val novoLink = binding.textImagemExercicio.text.toString().trim()

            if (novoNome.isNotEmpty()) {
                atualizarExercicio(nomeOriginal.orEmpty(), novoNome, novoLink)
                Toast.makeText(this, "Exercício atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "O nome do exercício não pode ficar vazio.", Toast.LENGTH_SHORT).show()
            }
        }

        // Botão Excluir
        binding.buttonExcluir.setOnClickListener {
            excluirExercicio(nomeOriginal.orEmpty())
            Toast.makeText(this, "Exercício excluído com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // Atualiza o exercício no SharedPreferences
    private fun atualizarExercicio(nomeAntigo: String, novoNome: String, novoLink: String) {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val exerciciosSalvos = sharedPreferences.getString("lista_exercicios", "").orEmpty()
        val exerciciosAtualizados = exerciciosSalvos.split("\n")
            .map { linha ->
                val partes = linha.split(" - ")
                val nome = partes.getOrNull(0) ?: ""
                val link = partes.getOrNull(1) ?: ""
                if (nome == nomeAntigo) "$novoNome - $novoLink" else "$nome - $link"
            }
            .joinToString("\n")

        val editor = sharedPreferences.edit()
        editor.putString("lista_exercicios", exerciciosAtualizados)
        editor.apply()
    }

    // Exclui o exercício no SharedPreferences
    private fun excluirExercicio(nome: String) {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val exerciciosSalvos = sharedPreferences.getString("lista_exercicios", "").orEmpty()
        val exerciciosAtualizados = exerciciosSalvos.split("\n")
            .filter { linha ->
                val partes = linha.split(" - ")
                val nomeAtual = partes.getOrNull(0) ?: ""
                nomeAtual != nome
            }
            .joinToString("\n")

        val editor = sharedPreferences.edit()
        editor.putString("lista_exercicios", exerciciosAtualizados)
        editor.apply()
    }
}