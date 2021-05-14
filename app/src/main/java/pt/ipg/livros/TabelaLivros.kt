package pt.ipg.livros

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaLivros(db: SQLiteDatabase) {
    private val db: SQLiteDatabase = db

    fun cria() {
        db.execSQL("CREATE TABLE " + NOME_Tabela+
                "(" + BaseColumns._ID+
                " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CAMPO_TITULO+ "TEXT NOT NULL," +
                CAMPO_AUTOR+ "TEXT NOT NULL," +
                CAMPO_ID_CATEGORIA+ " INTEGER NOT NULL,"+
                "FOREIGN KEY ("+ CAMPO_ID_CATEGORIA+")"+
                    "REFERENCES " + TabelaCategorias.NOME_Tabela+
                ")"
        )
    }

    // CREATE TABLE livros(
    //       _id INTEGER PRIMARY KEY AUTOINCREMENT,
    //       titulo TEXT NOT NULL,
    //       autor TEXT NOT NULL,
    //       id_categoria INTEGER NOT NULL, FOREIGN KEY (id_categoria) REFERENCES categorias

    // CRUD

    fun insert(values: ContentValues): Long {
        return db.insert(NOME_Tabela, null, values)
    }

    fun update(values: ContentValues, whereClause: String, whereArgs: Array<String>): Int {
        return db.update(NOME_Tabela, values, whereClause, whereArgs)
    }

    fun delete(whereClause: String, whereArgs: Array<String>): Int {
        return db.delete(NOME_Tabela, whereClause, whereArgs)
    }

    fun query(
        columns: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        groupBy: String,
        having: String,
        orderBy: String
    ): Cursor? {
        return db.query(NOME_Tabela, columns, selection, selectionArgs, groupBy, having, orderBy)
    }

    companion object{
        const val NOME_Tabela = "livros"
        const val CAMPO_TITULO = "titulo"
        const val CAMPO_AUTOR = "autor"
        const val CAMPO_ID_CATEGORIA = "id_categoria"
    }
}