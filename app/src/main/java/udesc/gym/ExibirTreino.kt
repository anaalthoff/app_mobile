package udesc.gym

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityExibirTreinoBinding

class ExibirTreino : AppCompatActivity() {

    private lateinit var binding: ActivityExibirTreinoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExibirTreinoBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Recebe os dados do treino
        val dadosTreino = intent.getStringArrayListExtra("dadosTreino") ?: arrayListOf()
        val nomeTreino = dadosTreino.firstOrNull() ?: return // Nome do treino é o primeiro item

        // Configura o ListView para exibir o treino selecionado
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dadosTreino)
        binding.listViewExercicios.adapter = adapter

        // Configura o botão cancelar para voltar
        binding.cancelButton.setOnClickListener { finish() }

        // Configura o botão excluir
        binding.buttonExcluir.setOnClickListener {
            excluirTreino(nomeTreino)
        }
    }

    private fun excluirTreino(nomeTreino: String) {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val treinosSalvos = sharedPreferences.getString("lista_treinos", "").orEmpty()

        // Remove o treino selecionado
        val novosTreinos = treinosSalvos.split("\n")
            .filter { it.isNotEmpty() && !it.startsWith(nomeTreino) } // Exclui o treino pelo nome
            .joinToString("\n")

        editor.putString("lista_treinos", novosTreinos) // Salva a nova lista
        editor.apply()

        Toast.makeText(this, "Treino excluído com sucesso!", Toast.LENGTH_SHORT).show()

        // Finaliza a Activity e volta para "Meus Treinos"
        finish()
    }
}