package udesc.gym

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.gym.databinding.ActivityCadastrarExercicioBinding
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CadastrarExercicio : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarExercicioBinding
    private var selectedImageUri: Uri? = null

    // Registrando contratos de atividade
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photo = result.data?.extras?.get("data") as Bitmap

                // Salva a imagem no armazenamento
                val savedImageUri = saveImageToStorage(photo)
                if (savedImageUri != null) {
                    selectedImageUri = savedImageUri // Armazena o URI para uso futuro
                    binding.imageView.setImageBitmap(photo) // Mostra a foto na tela
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastrarExercicioBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Configurações para botões da câmera e galeria
        binding.buttonCamera.setOnClickListener { openCamera() }

        // se colocar uma nova intent, vai ficar criando intents, telas. Então o finish fecha essa Activity e volta pro ponto anterior
        binding.cancelButton.setOnClickListener { finish() }

        // Inicializa o SharedPreferences
        val sharedPreferences = getSharedPreferences("dados", MODE_PRIVATE)

        // Botão para salvar os dados
        binding.buttonSalvar.setOnClickListener {
            val nomeExercicio = binding.textNomeExercicio.text.toString().trim()
            val linkImgExercicio = binding.textImagemExercicio.text.toString().trim()

            // Validação simples: o nome do exercício é obrigatório
            if (nomeExercicio.isNotEmpty()) {
                // Recupera os exercícios já salvos
                var exerciciosSalvos = sharedPreferences.getString("lista_exercicios", "").orEmpty()

                // Adiciona o novo exercício no formato: "Nome - LinkImagem"
                if (exerciciosSalvos.isNotEmpty()) {
                    exerciciosSalvos += "\n" // Adiciona uma quebra de linha entre os exercícios
                }

                // Adiciona apenas o nome do exercício se o link estiver vazio
                exerciciosSalvos += if (linkImgExercicio.isNotEmpty()) {
                    "$nomeExercicio - $linkImgExercicio"
                } else {
                    nomeExercicio
                }

                // Salva os exercícios atualizados no SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("lista_exercicios", exerciciosSalvos)
                editor.apply()

                // Exibe mensagem de sucesso e limpa os campos
                Toast.makeText(this, "Exercício salvo com sucesso!", Toast.LENGTH_SHORT).show()
                binding.textNomeExercicio.setText("")
                binding.textImagemExercicio.setText("")
                finish() // Finaliza a Activity e volta para a lista
            } else {
                // Exibe uma mensagem de erro se o campo nome estiver vazio
                Toast.makeText(
                    this,
                    "Por favor, preencha o nome do exercício antes de salvar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Função para abrir a câmera
    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent) // Usa o launcher em vez de startActivityForResult
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), PERMISSION_CAMERA
            )
        }
    }

    companion object {
        private const val PERMISSION_CAMERA = 100
    }

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
}