package udesc.gym

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import udesc.gym.databinding.ActivityCadastrarTreinoBinding

class CadastrarTreino : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarTreinoBinding
    private val listaExercicios = listOf(
        Exercicio("Supino", "uri_supino"),
        Exercicio("Agachamento", "uri_agachamento"),
        Exercicio("Flexão", "uri_flexao")
    )
    private val listaTreinoEspecifico = mutableListOf<TreinoEspecifico>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarTreinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar AutoCompleteTextView
        val adapterExercicios = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaExercicios.map { it.nome })
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.textNomeExercicio)
        autoCompleteTextView.setAdapter(adapterExercicios)

        // Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTreinoEspecifico)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TreinoEspecificoAdapter(listaTreinoEspecifico)
        recyclerView.adapter = adapter

        // Botão Adicionar
        binding.buttonAdicionar.setOnClickListener {
            val nomeTreino = binding.textNomeTreino.text.toString()
            val nomeExercicio = binding.textNomeExercicio.text.toString()
            val numeroSeries = binding.textNumeroSeries.text.toString().toInt()
            val numeroRepeticoes = binding.textNumeroRepeticoes.text.toString().toInt()
            val imagemUri = listaExercicios.find { it.nome == nomeExercicio }?.imagemUri ?: ""

            val novoTreino = TreinoEspecifico(nomeTreino, nomeExercicio, numeroSeries, numeroRepeticoes, imagemUri)
            listaTreinoEspecifico.add(novoTreino)
            adapter.notifyDataSetChanged()
        }

        // Botão Salvar
        binding.buttonSalvar.setOnClickListener {
            // Salve a listaTreinoEspecifico em algum lugar (SharedPreferences, banco de dados, etc.)
        }
    }
}
