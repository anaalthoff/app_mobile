package udesc.gym

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityMeusTreinosBinding
import udesc.gym.databinding.ActivityMinhasExecucoesBinding

class MinhasExecucoes : AppCompatActivity() {

    private lateinit var binding: ActivityMinhasExecucoesBinding
    private val listaExecucoes = ArrayList<Execucao>() // Lista de objetos Exercicio
    private lateinit var adapter: ArrayAdapter<Execucao>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMinhasExecucoesBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
        binding.cancelButton.setOnClickListener { finish() }

        binding.buttonCadastrar.setOnClickListener {
            // intent é o recurso de mudar de tela
            // cria um objeto da classe Intent, diz a tela inicial e a final, informando a classe que implementa essa activity
            val intent = Intent(this, CadastrarExecucao::class.java)
            // chama o método abaixo e passa a intent
            startActivity(intent)
        }

        // Inicializa o adaptador
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaExecucoes)
        binding.listViewExecucoes.adapter = adapter

        // Carrega os dados salvos
        carregarExecucoes()

        // Configura o listener de clique
        binding.listViewExecucoes.setOnItemClickListener { _, _, position, _ ->
            val execucaoSelecionada = listaExecucoes[position]

            // Passa os dados do exercício para a tela de edição
            val intent = Intent(this, EditarTreino::class.java)
            intent.putExtra("NOME_EXECUCAO", execucaoSelecionada.treino.nome)
            intent.putExtra("DATAHORA_EXECUCAO", execucaoSelecionada.dataHoraExecucao)
            startActivity(intent)
        }
    }

    // Atualiza a tela com o novo exercício cadastrado
    override fun onResume() {
        super.onResume()
        carregarExecucoes()
    }

    private fun carregarExecucoes() {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val exerciciosSalvos = sharedPreferences.getString("lista_execucoes", "").orEmpty()

        listaExecucoes.clear() // Limpa a lista antes de carregar novamente

        exerciciosSalvos.split("\n").filter { it.isNotEmpty() }.forEach {
            val partes = it.split(" - ")
            val nome = partes.getOrNull(0) ?: ""
            val dataHoraExecucao = partes.getOrNull(1) ?: ""
            listaExecucoes.add(Execucao(Treino(nome), dataHoraExecucao))
        }

        adapter.notifyDataSetChanged()
    }
}