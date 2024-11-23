package udesc.gym

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityExerciciosBinding

class Exercicios : AppCompatActivity() {

    private lateinit var binding: ActivityExerciciosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExerciciosBinding.inflate(layoutInflater)
        setContentView(binding.main)

        binding.cancelButton.setOnClickListener {
            // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
            finish()
        }

        binding.buttonCadastrar.setOnClickListener {
            // intent é o recurso de mudar de tela
            // cria um objeto da classe Intent, diz a tela inicial e a final, informando a classe que implementa essa activity
            val intent = Intent(this, CadastrarExercicio::class.java)
            // chama o método abaixo e passa a intent
            startActivity(intent)
        }

        // Inicializa o ListView
        val listView: ListView = binding.listViewExercicios
        // Inicializa o SharedPreferences
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        // Recupera os dados salvos
        val exerciciosSalvos = sharedPreferences.getString("lista_exercicios", "").orEmpty()
        // Converte os dados salvos em uma lista
        val exerciciosList = exerciciosSalvos.split("\n").filter { it.isNotEmpty() }
        // Usando ArrayAdapter diretamente
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, exerciciosList)
        // Configura o ListView com o ArrayAdapter
        listView.adapter = adapter

        // Adiciona um listener de clique para os itens da lista
        listView.setOnItemClickListener { _, _, position, _ ->
            // Recupera o exercício clicado
            val exercicioSelecionado = exerciciosList[position]

            // Divide o exercício no formato "Nome - Imagem"
            val partes = exercicioSelecionado.split(" - ")
            val nomeExercicio = partes.getOrNull(0) ?: ""
            val linkImgExercicio = partes.getOrNull(1) ?: ""

            // Cria a Intent para a tela de edição
            val intent = Intent(this, EditarExercicio::class.java)

            // Passa os dados do exercício para a tela de edição
            intent.putExtra("NOME_EXERCICIO", nomeExercicio)
            intent.putExtra("LINK_IMG_EXERCICIO", linkImgExercicio)

            // Inicia a Activity de edição
            startActivity(intent)
        }
    }

    // Para que os exercícios cadastrados apareçam imediatamente na em Exercicios após salvar, precisa atualizar com onResume.
    override fun onResume() {
        super.onResume()

        // Inicializa o SharedPreferences e atualiza os dados
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        // Recupera os dados salvos
        val exerciciosSalvos = sharedPreferences.getString("lista_exercicios", "").orEmpty()
        // Exibe os exercícios no TextView
        // binding.textExercicio3.text = exerciciosSalvos

        val exerciciosList = exerciciosSalvos.split("\n").filter { it.isNotEmpty() }
            .map { it.split(" - ")[0] } // Pega apenas o nome do exercício

        val listView: ListView = binding.listViewExercicios
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, exerciciosList)
        listView.adapter = adapter
    }
}