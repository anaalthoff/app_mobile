package udesc.gym.repository;

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import udesc.gym.models.Exercicio

// Essa clase permite criar e acessar o Banco de Dados (BD)
// Essa classe extende, herda, outra classe( : SQLiteOpenHelper). Assim pode implementar os métodos dessa classe
// Dois métodos precisam ser implementados ao extender a classe: onCreate e onUpgrade
// Context é o parâmetro
class ExercicioRepository(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, 1) {

    companion object {
        const val TABLE_NAME = "exercicios"
        const val COLUMN_ID = "id"
    }

    private fun createTable(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                "nome" TEXT, 
                "urlImagem" TEXT,
                "carga" TEXT,
                "criadoEm" TEXT,
                "ultimaAtualizacaoEm" TEXT,
                "arquivadoEm" TEXT,
                "deletadoEm" TEXT
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
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Recebe o id como parâmetro
    // O retornoserá um objeto da classe Exercicio
    fun selectById(id: Int): Exercicio? {
        // Para acessar a basse de dados é necessário criar uma variável de acesso (db)
        // Acesso de leitura, consulta. This é a referência para o DBHelper
        val db = this.readableDatabase
        // O ? vai ser definido pelo id, tem que mudar para string, pois ele é int
        // Retorna um objeto do tipo cursor que foi delcarado na variável 'c'
        val c = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf((id.toString())))
        // Cria o objeto da classe Exercicio
        var exercicio: Exercicio? = null
        // Se o registro retornar um valor, faz a consulta
        if(c.count == 1){
            // O cursos passa por todos os registros e quando é retornado e armazenado, ele aponta para o final da lista
            c.moveToFirst()
            // Agora pode acessar os campos dp registro
            val idIndex = c.getColumnIndex("id")
            val idNome = c.getColumnIndex("nome")
            val idUrlImagem = c.getColumnIndex("urlImagem")
            val idCarga = c.getColumnIndex("carga")
            val idCriadoEm = c.getColumnIndex("criadoEm")
            val idUltimaAtualizacaoEm = c.getColumnIndex("ultimaAtualizacaoEm")
            val idArquivadoEm = c.getColumnIndex("arquivadoEm")
            val idDeletadoEm = c.getColumnIndex("deletadoEm")
            val id = c.getInt(idIndex)
            val nome = c.getString(idNome)
            val urlImagem = c.getString(idUrlImagem)
            val carga = c.getString(idCarga)
            val criadoEm = c.getString(idCriadoEm)
            val ultimaAtualizacaoEm = c.getString(idUltimaAtualizacaoEm)
            val arquivadoEm = c.getString(idArquivadoEm)
            val deletadoEm = c.getString(idDeletadoEm)

            exercicio = Exercicio(id, nome, urlImagem, carga, criadoEm, ultimaAtualizacaoEm, arquivadoEm, deletadoEm)
        }
        c.close()
        return exercicio
    }

    // Para popular a lista completa
    fun selectAll(): ArrayList<Exercicio> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
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
                val idUrlImagem = c.getColumnIndex("urlImagem")
                val idCarga = c.getColumnIndex("carga")
                val idCriadoEm = c.getColumnIndex("criadoEm")
                val idUltimaAtualizacaoEm = c.getColumnIndex("ultimaAtualizacaoEm")
                val idArquivadoEm = c.getColumnIndex("arquivadoEm")
                val idDeletadoEm = c.getColumnIndex("deletadoEm")
                val id = c.getInt(idIndex)
                val nome = c.getString(idNome)
                val urlImagem = c.getString(idUrlImagem)
                val carga = c.getString(idCarga)
                val criadoEm = c.getString(idCriadoEm)
                val ultimaAtualizacaoEm = c.getString(idUltimaAtualizacaoEm)
                val arquivadoEm = c.getString(idArquivadoEm)
                val deletadoEm = c.getString(idDeletadoEm)

                val exercicio = Exercicio(id, nome, urlImagem, carga, criadoEm, ultimaAtualizacaoEm, arquivadoEm, deletadoEm)
                listaExercicios.add(exercicio)
            } while(c.moveToNext()) // moveToNext retorna verdadeiro, caso exista um next
        }
        c.close()
        return listaExercicios
    }

    // Funções de Escrita

    // O tipo de retorno é Long: retorna o id atribuído a esse registro
    fun insert(nome: String, linkImagem: String, carga: String, criadoEm: String, ultimaAtualizacaoEm: String, arquivadoEm: String, deletadoEm: String) : Long {
        // writableDatabase dá permissão para escrita
        val db = this.writableDatabase
        // Cria-se um objeto da classe ContentValues, pois ela encapsula os dados a serem inseridos
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("urlImagem", linkImagem)
        contentValues.put("carga", carga)
        contentValues.put("criadoEm", criadoEm)
        contentValues.put("ultimaAtualizacaoEm", ultimaAtualizacaoEm)
        contentValues.put("arquivadoEm", arquivadoEm)
        contentValues.put("deletadoEm", deletadoEm)

        val resultado = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return resultado
    }

    fun update(id: Int, nome: String, linkImagem: String, carga: String, criadoEm: String, ultimaAtualizacaoEm: String, arquivadoEm: String, deletadoEm: String) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("urlImagem", linkImagem)
        contentValues.put("carga", carga)
        contentValues.put("criadoEm", criadoEm)
        contentValues.put("ultimaAtualizacaoEm", ultimaAtualizacaoEm)
        contentValues.put("arquivadoEm", arquivadoEm)
        contentValues.put("deletadoEm", deletadoEm)
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


