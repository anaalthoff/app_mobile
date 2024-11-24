package udesc.gym

data class Treino(val nome: String, val listaExercicios: ArrayList<Exercicio> = ArrayList<Exercicio>()) {
    override fun toString(): String {
        return nome
    }
}
