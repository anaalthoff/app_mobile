package udesc.gym.models

/**
 *
 * Modelo de dados - Treino
 *      id: ID PK autoincrement
 *      nome: Text
 *      validoAte: Text
 *      criadoEm: Text
 *      ultimaAtualizacaoEm: Text
 *      arquivadoEm: Text
 *      deletadoEm: Text
 *
 */
data class Treino(val id: Int,
                  val nome: String,
                  val validoAte: String,
                  val criadoEm: String,
                  val ultimaAtualizacaoEm: String,
                  val arquivadoEm: String,
                  val deletadoEm: String,
                  val listaExercicios: ArrayList<Exercicio> = ArrayList<Exercicio>()) {
    override fun toString(): String {
        return nome
    }
}
