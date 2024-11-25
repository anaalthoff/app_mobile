package udesc.gym

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)

        binding.buttonConfiguracoes.setOnClickListener {
            // intent é o recurso de mudar de tela
            // cria um objeto da classe Intent, diz a tela inicial e a final, informando a classe que implementa essa activity
            val intent = Intent(this, Painel::class.java)
            // chama o método abaixo e passa a intent
            startActivity(intent)
        }

        if (carregarQtdExercicios() == 0) {
            binding.buttonExecutarTreino.isEnabled = false
            binding.buttonExecutarTreino.setBackgroundColor(Color.parseColor("#7184c7"))
        } else {
            binding.buttonExecutarTreino.isEnabled = true
            binding.buttonExecutarTreino.setBackgroundColor(Color.parseColor("#2445bd"))
        }

        binding.buttonExecutarTreino.setOnClickListener {
            val intent = Intent(this, CadastrarExecucao::class.java)
            startActivity(intent)
        }

    }

    private fun carregarQtdExercicios(): Int {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val treinosSalvos = sharedPreferences.getString("lista_treinos", "").orEmpty()
        return treinosSalvos.split("\n").filter { it.isNotEmpty() }.size
    }

}