package udesc.gym

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityEditarExercicioBinding
import android.net.Uri
import androidx.activity.enableEdgeToEdge
import android.graphics.Bitmap
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditarExercicio : AppCompatActivity() {

    private lateinit var binding: ActivityEditarExercicioBinding
    private var nomeOriginal: String? = null
    private var uriImagemOriginal: Uri? = null // Guardar o URI original da imagem

    // Registrar o launcher para abrir a câmera
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val photo = result.data?.extras?.get("data") as Bitmap
                val savedImageUri = saveImageToStorage(photo)

                if (savedImageUri != null) {
                    uriImagemOriginal = savedImageUri // Atualiza o URI da imagem
                    binding.imageView.setImageBitmap(photo) // Exibe a nova foto
                    binding.textImagemExercicio.setText(savedImageUri.toString()) // Atualiza o campo de texto com o URI
                } else {
                    Toast.makeText(this, "Erro ao salvar a imagem", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditarExercicioBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Cria um OnClickListener que chama finish()
        val finishListener = View.OnClickListener {
            finish()
        }

        // Atribui o mesmo listener a todos os botões
        binding.cancelButton.setOnClickListener(finishListener)
        binding.buttonSalvar.setOnClickListener(finishListener)
        binding.buttonExcluir.setOnClickListener(finishListener)

        // Recupera os dados enviados pela Intent
        nomeOriginal = intent.getStringExtra("NOME_EXERCICIO")
        val linkImagemOriginal = intent.getStringExtra("LINK_IMG_EXERCICIO")

        // Preenche os campos com os dados do exercício selecionado
        binding.textNomeExercicio.setText(nomeOriginal)
        binding.textImagemExercicio.setText(linkImagemOriginal)

        // Se tiver o URI da imagem salvo, exibe a imagem
        if (linkImagemOriginal != null) {
            uriImagemOriginal = Uri.parse(linkImagemOriginal)
            binding.imageView.setImageURI(uriImagemOriginal) // Exibe a imagem
        }

        // Botão Salvar
        binding.buttonSalvar.setOnClickListener {
            val novoNome = binding.textNomeExercicio.text.toString().trim()
            val novoLink = binding.textImagemExercicio.text.toString().trim()

            if (novoNome.isNotEmpty()) {
                atualizarExercicio(nomeOriginal.orEmpty(), novoNome, novoLink)
                Toast.makeText(this, "Exercício atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "O nome do exercício não pode ficar vazio.", Toast.LENGTH_SHORT).show()
            }
        }

        // Botão Excluir
        binding.buttonExcluir.setOnClickListener {
            excluirExercicio(nomeOriginal.orEmpty())
            Toast.makeText(this, "Exercício excluído com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Botão para tirar uma nova foto
        binding.buttonCamera.setOnClickListener {
            openCamera()
        }

    }

    // Abrir a câmera
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent) // Usar o launcher registrado
    }

    // Salvar a imagem no armazenamento
    private fun saveImageToStorage(bitmap: Bitmap): Uri? {
        return try {
            // Escolhe o local de armazenamento
            val storageDir = filesDir // Armazenamento interno
            val imageFile = File(storageDir, "${System.currentTimeMillis()}.jpg")

            // Salva a imagem no arquivo
            FileOutputStream(imageFile).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }

            // Retorna o URI do arquivo salvo
            Uri.fromFile(imageFile)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Atualiza o exercício no SharedPreferences
    private fun atualizarExercicio(nomeAntigo: String, novoNome: String, novoLink: String) {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val exerciciosSalvos = sharedPreferences.getString("lista_exercicios", "").orEmpty()
        val exerciciosAtualizados = exerciciosSalvos.split("\n").joinToString("\n") { linha ->
            val partes = linha.split(" - ")
            val nome = partes.getOrNull(0) ?: ""
            val link = partes.getOrNull(1) ?: ""
            if (nome == nomeAntigo) "$novoNome - $novoLink" else "$nome - $link"
        }

        val editor = sharedPreferences.edit()
        editor.putString("lista_exercicios", exerciciosAtualizados)
        editor.apply()
    }

    // Exclui o exercício no SharedPreferences
    private fun excluirExercicio(nome: String) {
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)
        val exerciciosSalvos = sharedPreferences.getString("lista_exercicios", "").orEmpty()
        val exerciciosAtualizados = exerciciosSalvos.split("\n")
            .filter { linha ->
                val partes = linha.split(" - ")
                val nomeAtual = partes.getOrNull(0) ?: ""
                nomeAtual != nome
            }
            .joinToString("\n")

        val editor = sharedPreferences.edit()
        editor.putString("lista_exercicios", exerciciosAtualizados)
        editor.apply()
    }
}