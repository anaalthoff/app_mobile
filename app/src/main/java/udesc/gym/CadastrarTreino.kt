package udesc.gym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
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

data class TreinoEspecifico(
    val nomeTreino: String,
    val nomeExercicio: String,
    val numeroSeries: Int,
    val numeroRepeticoes: Int,
    val imagemUri: String
)

class TreinoEspecificoAdapter(private val treinos: List<TreinoEspecifico>) :
    RecyclerView.Adapter<TreinoEspecificoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_treino_especifico, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(treinos[position])
    }

    override fun getItemCount(): Int = treinos.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNomeTreino: TextView = itemView.findViewById(R.id.textViewNomeTreino)
        private val textViewNomeExercicio: TextView = itemView.findViewById(R.id.textViewNomeExercicio)
        private val textViewSeries: TextView = itemView.findViewById(R.id.textViewSeries)
        private val textViewRepeticoes: TextView = itemView.findViewById(R.id.textViewRepeticoes)
        private val imageViewExercicio: ImageView = itemView.findViewById(R.id.imageViewExercicio)

        fun bind(treino: TreinoEspecifico) {
            textViewNomeTreino.text = treino.nomeTreino
            textViewNomeExercicio.text = treino.nomeExercicio
            textViewSeries.text = "Séries: ${treino.numeroSeries}"
            textViewRepeticoes.text = "Repetições: ${treino.numeroRepeticoes}"
            Glide.with(itemView.context).load(treino.imagemUri).into(imageViewExercicio)
        }
    }
}
