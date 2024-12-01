package udesc.gym.repository;

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import udesc.gym.models.Exercicio
import udesc.gym.models.Treino

// Essa clase permite criar e acessar o Banco de Dados (BD)
// Essa classe extende, herda, outra classe( : SQLiteOpenHelper). Assim pode implementar os métodos dessa classe
// Dois métodos precisam ser implementados ao extender a classe: onCreate e onUpgrade
// Context é o parâmetro
open class DBHelper(private var context: Context) : SQLiteOpenHelper(context, "dbhelper", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val exercicio = ExercicioRepository(this.context)
        val treino = TreinoRepository(this.context)
        val execucao = ExecucaoRepository(this.context)
    }

    // onUpgrade é quando lança uma nova versão do BD
    // Apaga a tabela veículo e chama novamente o onCreate
    // É obrigatório definir o onUpgrade (pode colocar outro comando dentro)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

}
