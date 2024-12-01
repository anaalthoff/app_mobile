package udesc.gym

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityCadastrarExercicioBinding
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import udesc.gym.databinding.ActivityCadastrarExecucaoBinding
import udesc.gym.databinding.ActivityCadastrarTreinoBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CadastrarExecucao : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarExecucaoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastrarExecucaoBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
        binding.cancelButton.setOnClickListener { finish() }

        // Inicializa o SharedPreferences
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)

        // Botão para salvar os dados
        binding.buttonSalvar.setOnClickListener {
            val nomeExercicio = binding.textNomeExecucao.text.toString().trim()


            // Validação simples: o nome do exercício é obrigatório
            if (nomeExercicio.isNotEmpty()) {
                // Recupera os exercícios já salvos
                var treinosSalvos = sharedPreferences.getString("lista_execucoes", "").orEmpty()

                // Adiciona o novo exercício no formato: "Nome - "
                if (treinosSalvos.isNotEmpty()) {
                    treinosSalvos += "\n" // Adiciona uma quebra de linha entre os exercícios
                }

                val execucao = Execucao(Treino(nomeExercicio))

                // Adiciona apenas o nome do exercício se o link estiver vazio
                treinosSalvos += execucao.treino.nome
                if (execucao.dataHoraExecucao.isNotEmpty()) {
                    treinosSalvos += " - ${execucao.dataHoraExecucao}"
                }

                // Salva os exercícios atualizados no SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("lista_execucoes", treinosSalvos)
                editor.apply()

                // Exibe mensagem de sucesso e limpa os campos
                Toast.makeText(this, "Execução salva com sucesso!", Toast.LENGTH_SHORT).show()
                binding.textNomeExecucao.setText("")
                finish() // Finaliza a Activity e volta para a lista
            } else {
                // Exibe uma mensagem de erro se o campo nome estiver vazio
                Toast.makeText(
                    this,
                    "Por favor, preencha os dados da Execução antes de salvar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}