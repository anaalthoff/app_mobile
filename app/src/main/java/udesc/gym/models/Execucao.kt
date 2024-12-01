package udesc.gym.models;

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Modelo de dados - Execucao
 *
 *      id: ID PK autoincrement
 *      id_treino: ID FK
 *      criadoEm: Text
 *      iniciadoEm: Text
 *      finalizadoEm: Text
 *      arquivadoEm: Text
 *      deletadoEm: Text
 */
data class Execucao(val id: Int,
                    val id_treino: Int,
                    val criadoEm: String = getDateTime(),
                    val iniciadoEm: String,
                    val finalizadoEm: String,
                    val arquivadoEm: String,
                    val deletadoEm: String) {
    companion object {
        private fun getDateTime(): String {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val date = Date()
            return dateFormat.format(date)
        }
    }
}
