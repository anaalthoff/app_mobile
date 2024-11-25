package udesc.gym;

import java.text.SimpleDateFormat
import java.util.Date

data class Execucao(val treino: Treino, val dataHoraExecucao: String = getDateTime()) {
    companion object {
        private fun getDateTime(): String {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val date = Date()
            return dateFormat.format(date)
        }
    }
}
