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
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import udesc.gym.databinding.ActivityCadastrarTreinoBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CadastrarTreino : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarTreinoBinding
    private val listaExercicios = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastrarTreinoBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Configura o adaptador do ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaExercicios)
        binding.listaExerciciosTreino.adapter = adapter

        // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
        binding.cancelButton.setOnClickListener { finish() }


        // Configura o botão "Adicionar"
        binding.buttonAdicionar.setOnClickListener {
            val nomeTreino = binding.textNomeTreino.text.toString().trim()
            val nomeExercicio = binding.textNomeExercicio.text.toString().trim()
            val numeroSeries = binding.textnumeroDeSeries.text.toString().trim()
            val numeroRepeticoes = binding.textnumeroDeRepeticoes.text.toString().trim()

            // Valida os campos
            if (nomeTreino.isEmpty() || nomeExercicio.isEmpty() || numeroSeries.isEmpty() || numeroRepeticoes.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos antes de adicionar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Formata a entrada do exercício
            val exercicio = "Treino: $nomeTreino, Exercício: $nomeExercicio, Séries: $numeroSeries, Repetições: $numeroRepeticoes"

            // Adiciona o exercício à lista e atualiza o adaptador
            listaExercicios.add(exercicio)
            adapter.notifyDataSetChanged()

            // Limpa os campos
            binding.textNomeTreino.setText("")
            binding.textNomeExercicio.setText("")
            binding.textnumeroDeSeries.setText("")
            binding.textnumeroDeRepeticoes.setText("")
        }

        // Inicializa o SharedPreferences
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)

        binding.buttonSalvar.setOnClickListener {
            if (listaExercicios.isNotEmpty()) {
                // Converte a lista de exercícios para um único String (usando separador, por exemplo, "\n")
                val exerciciosString = listaExercicios.joinToString(separator = "\n")

                // Salva a lista no SharedPreferences
                val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("lista_treinos", exerciciosString)
                editor.apply()

                // Exibe mensagem de sucesso e finaliza a Activity
                Toast.makeText(this, "Treinos salvos com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                // Exibe mensagem se a lista estiver vazia
                Toast.makeText(this, "A lista de exercícios está vazia. Adicione ao menos um exercício antes de salvar.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}