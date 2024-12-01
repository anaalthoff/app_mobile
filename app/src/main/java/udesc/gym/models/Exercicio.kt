package udesc.gym.models;

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Modelo de dados - Exercício
 *
 *      id: ID PK autoincrement
 *      nome: Text
 *      urlImagem: Text
 *      carga: Text
 *      criadoEm: Text
 *      ultimaAtualizacaoEm: Text
 *      arquivadoEm: Text
 *      deletadoEm: Text
 *
 */
data class Exercicio(val id: Int,
                     val nome: String,
                     val urlImagem: String,
                     val carga: String,
                     val criadoEm: String = getDateTime(),
                     val ultimaAtualizacaoEm: String,
                     val arquivadoEm: String,
                     val deletadoEm: String,
    ) {

    companion object {
        private fun getDateTime(): String {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val date = Date()
            return dateFormat.format(date)
        }
    }

    override fun toString(): String {
        return nome
    }
}
