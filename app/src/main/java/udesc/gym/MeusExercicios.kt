package udesc.gym

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityMeusExerciciosBinding

class MeusExercicios : AppCompatActivity() {

    private lateinit var binding: ActivityMeusExerciciosBinding
    private val listaExercicios = ArrayList<Exercicio>() // Lista de objetos Exercicio
    private lateinit var adapter: ArrayAdapter<Exercicio>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMeusExerciciosBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
        binding.cancelButton.setOnClickListener { finish() }

        binding.buttonCadastrar.setOnClickListener {
            // intent é o recurso de mudar de tela
            // cria um objeto da classe Intent, diz a tela inicial e a final, informando a classe que implementa essa activity
            val intent = Intent(this, CadastrarExercicio::class.java)
            // chama o método abaixo e passa a intent
            startActivity(intent)
        }

        // Inicializa o adaptador
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaExercicios)
        binding.listViewExercicios.adapter = adapter

        // Carrega os dados salvos
        carregarExercicios()

        // Configura o listener de clique
        binding.listViewExercicios.setOnItemClickListener { _, _, position, _ ->
            val exercicioSelecionado = listaExercicios[position]

            // Passa os dados do exercício para a tela de edição
            val intent = Intent(this, EditarExercicio::class.java)
            intent.putExtra("NOME_EXERCICIO", exercicioSelecionado.nome)
            intent.putExtra("LINK_IMG_EXERCICIO", exercicioSelecionado.linkImagem)
            startActivity(intent)
        }
    }

    // Atualiza a tela com o novo exercício cadastrado
    override fun onResume() {
        super.onResume()
        carregarExercicios()
    }

    private fun carregarExercicios() {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val exerciciosSalvos = sharedPreferences.getString("lista_exercicios", "").orEmpty()

        listaExercicios.clear() // Limpa a lista antes de carregar novamente

        exerciciosSalvos.split("\n").filter { it.isNotEmpty() }.forEach {
            val partes = it.split(" - ")
            val nome = partes.getOrNull(0) ?: ""
            val linkImagem = partes.getOrNull(1) ?: ""
            listaExercicios.add(Exercicio(nome, linkImagem))
        }

        adapter.notifyDataSetChanged()
    }
}