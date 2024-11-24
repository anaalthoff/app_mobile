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
    private lateinit var dbHelper: DBHelper
    private var exercicioId: Int = 0 // ID do exercício para operações no banco

    // Registrar o launcher para abrir a câmera
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val photo = result.data?.extras?.get("data") as Bitmap
                val savedImageUri = saveImageToStorage(photo)

                if (savedImageUri != null) {
                    // Atualiza o URI da imagem
                    // uriImagemOriginal = savedImageUri
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

        dbHelper = DBHelper(this)

        binding.cancelButton.setOnClickListener { finish() }

        // Recupera os dados enviados pela Intent
        exercicioId = intent.getIntExtra("ID_EXERCICIO", -1)
        val nomeExercicio = intent.getStringExtra("NOME_EXERCICIO") ?: ""
        val linkImagem = intent.getStringExtra("LINK_IMG_EXERCICIO") ?: ""

        // Preenche os campos com os dados do exercício selecionado
        binding.textNomeExercicio.setText(nomeExercicio)
        binding.textImagemExercicio.setText(linkImagem)

        // Se tiver o URI da imagem salvo, exibe a imagem
        if (linkImagem.isNotEmpty()) {
            binding.imageView.setImageURI(Uri.parse(linkImagem))
        }

        // Botão Salvar
        binding.buttonSalvar.setOnClickListener {
            val novoNome = binding.textNomeExercicio.text.toString().trim()
            val novoLink = binding.textImagemExercicio.text.toString().trim()

            if (novoNome.isNotEmpty()) {
                atualizarExercicio(exercicioId, novoNome, novoLink)
                Toast.makeText(this, "Exercício atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "O nome do exercício não pode ficar vazio.", Toast.LENGTH_SHORT).show()
            }
        }

        // Botão Excluir
        binding.buttonExcluir.setOnClickListener {
            excluirExercicio(exercicioId)
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

    // Atualiza o exercício no banco de dados
    private fun atualizarExercicio(id: Int, novoNome: String, novoLink: String) {
        val resultado = dbHelper.exercicioUpdate(id, novoNome, novoLink)
        if (resultado > 0) {
            Toast.makeText(this, "Exercício atualizado com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Erro ao atualizar o exercício.", Toast.LENGTH_SHORT).show()
        }
    }

    // Exclui o exercício no banco de dados
    private fun excluirExercicio(id: Int) {
        val resultado = dbHelper.exercicioDelete(id)
        if (resultado > 0) {
            Toast.makeText(this, "Exercício excluído com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Erro ao excluir o exercício.", Toast.LENGTH_SHORT).show()
        }
    }
}