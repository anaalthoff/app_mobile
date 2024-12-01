package udesc.gym.repository;

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import udesc.gym.models.Exercicio
import udesc.gym.models.Treino

/**
 *
 * Modelo de dados - Treino
 *      id: ID PK autoincrement
 *      nome: Text
 *      criadoEm: Text
 *      dataHoraCriacao: Text
 *      ultimaAtualizacaoEm: Text
 *      arquivadoEm: Text
 *      deletadoEm: Text
 *
 */
class TreinoRepository(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, 1) {

    companion object {
        const val TABLE_NAME = "treinos"
        const val COLUMN_ID = "id"
    }

    private fun createTable(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                "nome" TEXT,
                "validoAte" TEXT,
                "criadoEm" TEXT,
                "ultimaAtualizacaoEm" TEXT,
                "arquivadoEm" TEXT,
                "deletadoEm" TEXT
            )
        """
        db.execSQL(createTable)
    }

    override fun onCreate(db: SQLiteDatabase) {
        createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Recebe o id como parâmetro
    // O retornoserá um objeto da classe Exercicio
    fun selectById(id: Int): Treino? {
        // Para acessar a basse de dados é necessário criar uma variável de acesso (db)
        // Acesso de leitura, consulta. This é a referência para o DBHelper
        val db = this.readableDatabase
        // O ? vai ser definido pelo id, tem que mudar para string, pois ele é int
        // Retorna um objeto do tipo cursor que foi delcarado na variável 'c'
        val c = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf((id.toString())))
        // Cria o objeto da classe Exercicio
        var objeto: Treino? = null
        // Se o registro retornar um valor, faz a consulta
        if(c.count == 1){
            // O cursos passa por todos os registros e quando é retornado e armazenado, ele aponta para o final da lista
            c.moveToFirst()
            // Agora pode acessar os campos dp registro
            val idIndex = c.getColumnIndex("id")
            val idNome = c.getColumnIndex("nome")
            val idValidoAte = c.getColumnIndex("validoAte")
            val idCriadoEm = c.getColumnIndex("criadoEm")
            val idUltimaAtualizacaoEm = c.getColumnIndex("ultimaAtualizacaoEm")
            val idArquivadoEm = c.getColumnIndex("arquivadoEm")
            val idDeletadoEm = c.getColumnIndex("deletadoEm")
            val id = c.getInt(idIndex)
            val nome = c.getString(idNome)
            val validoAte = c.getString(idValidoAte)
            val criadoEm = c.getString(idCriadoEm)
            val ultimaAtualizacaoEm = c.getString(idUltimaAtualizacaoEm)
            val arquivadoEm = c.getString(idArquivadoEm)
            val deletadoEm = c.getString(idDeletadoEm)

            objeto = Treino(id, nome, validoAte, criadoEm, ultimaAtualizacaoEm, arquivadoEm, deletadoEm)
        }
        c.close()
        return objeto
    }

    // Para popular a lista completa
    fun selectAll(): ArrayList<Treino> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val listaTreinos: ArrayList<Treino> = ArrayList()
        // Se o registro retornar um valor, faz a consulta
        if(c.count > 0){
            // O cursos passa por todos os registros e quando é retornado e armazenado, ele aponta para o final da lista
            c.moveToFirst()
            // Usa do while, pois percorre todos os itens encontrados
            do {
                // Agora pode acessar os campos dp registro
                val idIndex = c.getColumnIndex("id")
                val idNome = c.getColumnIndex("nome")
                val idValidoAte = c.getColumnIndex("validoAte")
                val idCriadoEm = c.getColumnIndex("criadoEm")
                val idUltimaAtualizacaoEm = c.getColumnIndex("ultimaAtualizacaoEm")
                val idArquivadoEm = c.getColumnIndex("arquivadoEm")
                val idDeletadoEm = c.getColumnIndex("deletadoEm")
                val id = c.getInt(idIndex)
                val nome = c.getString(idNome)
                val validoAte = c.getString(idValidoAte)
                val criadoEm = c.getString(idCriadoEm)
                val ultimaAtualizacaoEm = c.getString(idUltimaAtualizacaoEm)
                val arquivadoEm = c.getString(idArquivadoEm)
                val deletadoEm = c.getString(idDeletadoEm)

                val treino = Treino(id, nome, validoAte, criadoEm, ultimaAtualizacaoEm, arquivadoEm, deletadoEm)
                listaTreinos.add(treino)
            } while(c.moveToNext()) // moveToNext retorna verdadeiro, caso exista um next
        }
        c.close()
        return listaTreinos
    }

    // Funções de Escrita

    // O tipo de retorno é Long: retorna o id atribuído a esse registro
    fun insert(nome: String, validoAte: String, criadoEm: String, ultimaAtualizacaoEm: String, arquivadoEm: String, deletadoEm: String) : Long {
        // writableDatabase dá permissão para escrita
        val db = this.writableDatabase
        // Cria-se um objeto da classe ContentValues, pois ela encapsula os dados a serem inseridos
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("validoAte", validoAte)
        contentValues.put("criadoEm", criadoEm)
        contentValues.put("ultimaAtualizacaoEm", ultimaAtualizacaoEm)
        contentValues.put("arquivadoEm", arquivadoEm)
        contentValues.put("deletadoEm", deletadoEm)
        val resultado = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return resultado
    }

    fun update(id: Int, nome: String, validoAte: String, criadoEm: String, ultimaAtualizacaoEm: String, arquivadoEm: String, deletadoEm: String) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("validoAte", validoAte)
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


