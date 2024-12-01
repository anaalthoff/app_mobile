package udesc.gym.repository;

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import udesc.gym.models.Execucao
import udesc.gym.models.Exercicio
import udesc.gym.models.Treino

// Essa clase permite criar e acessar o Banco de Dados (BD)
// Essa classe extende, herda, outra classe( : SQLiteOpenHelper). Assim pode implementar os métodos dessa classe
// Dois métodos precisam ser implementados ao extender a classe: onCreate e onUpgrade
// Context é o parâmetro
class ExecucaoRepository(context: Context) : SQLiteOpenHelper(context, "execucoes", null, 1) {

    companion object {
        const val TABLE_NAME = "execucoes"
        const val COLUMN_ID = "id"
    }

    private fun createTable(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT
            )
        """
        db.execSQL(createTable)
    }

    // Na criação do BD, constrói-se a tabela (exercicios)
    // Isso é feito no método onCreate
    // Feito apenas uma vez
    override fun onCreate(db: SQLiteDatabase) {
        createTable(db)
    }

    // onUpgrade é quando lança uma nova versão do BD
    // Apaga a tabela veículo e chama novamente o onCreate
    // É obrigatório definir o onUpgrade (pode colocar outro comando dentro)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Recebe o id como parâmetro
    // O retornoserá um objeto da classe Exercicio
    fun selectById(id: Int): Execucao? {
        // Para acessar a basse de dados é necessário criar uma variável de acesso (db)
        // Acesso de leitura, consulta. This é a referência para o DBHelper
        val db = this.readableDatabase
        // O ? vai ser definido pelo id, tem que mudar para string, pois ele é int
        // Retorna um objeto do tipo cursor que foi delcarado na variável 'c'
        val c = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf((id.toString())))
        // Cria o objeto da classe Exercicio
        var execucao: Execucao? = null
        // Se o registro retornar um valor, faz a consulta
        if(c.count == 1){
            // O cursos passa por todos os registros e quando é retornado e armazenado, ele aponta para o final da lista
            c.moveToFirst()
            // Agora pode acessar os campos dp registro
            val idIndex = c.getColumnIndex("id")
            val idNome = c.getColumnIndex("nome")
            val idLink = c.getColumnIndex("linkImagem")
            val id = c.getInt(idIndex)
            val nome = c.getString(idNome)
            val linkImagem = c.getString(idLink)
            execucao = Execucao(id, Treino(id, nome), linkImagem)
        }
        c.close()
        return execucao
    }

    // Para popular a lista completa
    fun selectAll(): ArrayList<Execucao> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val listaExecucoes: ArrayList<Execucao> = ArrayList()
        // Se o registro retornar um valor, faz a consulta
        if(c.count > 0){
            // O cursos passa por todos os registros e quando é retornado e armazenado, ele aponta para o final da lista
            c.moveToFirst()
            // Usa do while, pois percorre todos os itens encontrados
            do {
                // Agora pode acessar os campos dp registro
                val idIndex = c.getColumnIndex("id")
                val idNome = c.getColumnIndex("nome")
                val idLink = c.getColumnIndex("linkImagem")
                val id = c.getInt(idIndex)
                val nome = c.getString(idNome)
                val linkImagem = c.getString(idLink)
                listaExecucoes.add(Execucao(id, Treino(id, nome), linkImagem))
            } while(c.moveToNext()) // moveToNext retorna verdadeiro, caso exista um next
        }
        c.close()
        return listaExecucoes
    }

    // Funções de Escrita

    // O tipo de retorno é Long: retorna o id atribuído a esse registro
    fun insert(nome: String, linkImagem: String) : Long {
        // writableDatabase dá permissão para escrita
        val db = this.writableDatabase
        // Cria-se um objeto da classe ContentValues, pois ela encapsula os dados a serem inseridos
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("linkImagem", linkImagem)
        val resultado = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return resultado
    }

    fun update(id: Int, nome: String, linkImagem: String) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("linkImagem", linkImagem)
        // O arrayOf passa a lista de parâmetros
        val resultado = db.update(TABLE_NAME, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    fun delete(id: Int) : Int {
        val db = this.writableDatabase
        val resultado = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }
}


