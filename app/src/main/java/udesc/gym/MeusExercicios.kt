package udesc.gym

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityMeusExerciciosBinding
import udesc.gym.models.Exercicio
import udesc.gym.repository.DBHelper

class MeusExercicios : AppCompatActivity() {

    private lateinit var binding: ActivityMeusExerciciosBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: ArrayAdapter<Exercicio>
    private val listaExercicios = ArrayList<Exercicio>() // Lista de objetos Exercicio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMeusExerciciosBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
        binding.cancelButton.setOnClickListener { finish() }

        dbHelper = DBHelper(this)

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
            intent.putExtra("ID_EXERCICIO", exercicioSelecionado.id)
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
        listaExercicios.clear() // Limpa a lista antes de carregar novamente
        listaExercicios.addAll(dbHelper.exerciciosSelectAll())
        adapter.notifyDataSetChanged()
    }
}