package udesc.gym;

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Essa clase permite criar e acessar o Banco de Dados (BD)
// Essa classe extende, herda, outra classe( : SQLiteOpenHelper). Assim pode implementar os métodos dessa classe
// Dois métodos precisam ser implementados ao extender a classe: onCreate e onUpgrade
// Context é o parâmetro
class DBHelper(context: Context) : SQLiteOpenHelper(context, "exercicios.db", null, 1) {

    companion object {
        const val TABLE_EXERCICIOS = "exercicios"
        const val COLUMN_ID = "id"
        const val COLUMN_NOME = "nome"
        const val COLUMN_LINK = "linkImagem"
    }
    // Na criação do BD, constrói-se a tabela (exercicios)
    // Isso é feito no método onCreate
    // Feito apenas uma vez
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_EXERCICIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_NOME TEXT, 
                $COLUMN_LINK TEXT
            )
        """
        db.execSQL(createTable)
    }

    // onUpgrade é quando lança uma nova versão do BD
    // Apaga a tabela veículo e chama novamente o onCreate
    // É obrigatório definir o onUpgrade (pode colocar outro comando dentro)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCICIOS")
        onCreate(db)
    }

    // Recebe o id como parâmetro
    // O retornoserá um objeto da classe Exercicio
    fun exerciciosSelectById(id: Int): Exercicio? {
        // Para acessar a basse de dados é necessário criar uma variável de acesso (db)
        // Acesso de leitura, consulta. This é a referência para o DBHelper
        val db = this.readableDatabase
        // O ? vai ser definido pelo id, tem que mudar para string, pois ele é int
        // Retorna um objeto do tipo cursor que foi delcarado na variável 'c'
        val c = db.rawQuery("SELECT * FROM $TABLE_EXERCICIOS WHERE $COLUMN_ID = ?", arrayOf((id.toString())))
        // Cria o objeto da classe Exercicio
        var exercicio: Exercicio? = null
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
            exercicio = Exercicio(id, nome, linkImagem)
        }
        c.close()
        return exercicio
    }

    // Para popular a lista completa
    fun exerciciosSelectAll(): ArrayList<Exercicio> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM $TABLE_EXERCICIOS", null)
        val listaExercicios: ArrayList<Exercicio> = ArrayList()
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
                listaExercicios.add(Exercicio(id, nome, linkImagem))
            } while(c.moveToNext()) // moveToNext retorna verdadeiro, caso exista um next
        }
        c.close()
        return listaExercicios
    }

    // Funções de Escrita

    // O tipo de retorno é Long: retorna o id atribuído a esse registro
    fun exercicioInsert(nome: String, linkImagem: String) : Long {
        // writableDatabase dá permissão para escrita
        val db = this.writableDatabase
        // Cria-se um objeto da classe ContentValues, pois ela encapsula os dados a serem inseridos
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("linkImagem", linkImagem)
        val resultado = db.insert(TABLE_EXERCICIOS, null, contentValues)
        db.close()
        return resultado
    }

    fun exercicioUpdate(id: Int, nome: String, linkImagem: String) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("linkImagem", linkImagem)
        // O arrayOf passa a lista de parâmetros
        val resultado = db.update(TABLE_EXERCICIOS, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    fun exercicioDelete(id: Int) : Int {
        val db = this.writableDatabase
        val resultado = db.delete(TABLE_EXERCICIOS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }
}


