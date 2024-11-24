package udesc.gym

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityMeusTreinosBinding

class MeusTreinos : AppCompatActivity() {

    private lateinit var binding: ActivityMeusTreinosBinding
    private val listaTreinos = ArrayList<Treino>() // Lista de objetos Exercicio
    private lateinit var adapter: ArrayAdapter<Treino>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMeusTreinosBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
        binding.cancelButton.setOnClickListener { finish() }

        binding.buttonCadastrar.setOnClickListener {
            // intent é o recurso de mudar de tela
            // cria um objeto da classe Intent, diz a tela inicial e a final, informando a classe que implementa essa activity
            val intent = Intent(this, CadastrarTreino::class.java)
            // chama o método abaixo e passa a intent
            startActivity(intent)
        }

        // Inicializa o adaptador
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaTreinos)
        binding.listViewTreinos.adapter = adapter

        // Carrega os dados salvos
        carregarTreinos()

        // Configura o listener de clique
//        binding.listViewTreinos.setOnItemClickListener { _, _, position, _ ->
//            val treinoSelecionado = listaTreinos[position]
//
//            // Passa os dados do exercício para a tela de edição
//            val intent = Intent(this, EditarTreino::class.java)
//            intent.putExtra("NOME_EXERCICIO", treinoSelecionado.nome)
//            startActivity(intent)
//        }
    }

    // Atualiza a tela com o novo exercício cadastrado
    override fun onResume() {
        super.onResume()
        carregarTreinos()
    }

    private fun carregarTreinos() {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val exerciciosSalvos = sharedPreferences.getString("lista_treinos", "").orEmpty()

        listaTreinos.clear() // Limpa a lista antes de carregar novamente

        exerciciosSalvos.split("\n").filter { it.isNotEmpty() }.forEach {
            val partes = it.split(" - ")
            val nome = partes.getOrNull(0) ?: ""
            println(nome)
            listaTreinos.add(Treino(nome, ArrayList<Exercicio>()))
        }

        adapter.notifyDataSetChanged()
    }
}