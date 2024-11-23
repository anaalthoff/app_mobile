package udesc.gym;

data class Exercicio(val nome: String,val linkImagem: String) {
    // Sobrescreve o método toString para exibir informações no ListView
    override fun toString(): String {
        return nome
    }
}
