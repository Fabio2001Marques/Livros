package pt.ipg.livros

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class Livro (var id: Long = -1, var titulo: String, var autor: String, var idCategoria: Long) {

    fun toContentValues() : ContentValues {
        val valores = ContentValues().apply {
            put(TabelaLivros.CAMPO_TITULO,titulo)
            put(TabelaLivros.CAMPO_AUTOR,autor)
            put(TabelaLivros.CAMPO_ID_CATEGORIA, idCategoria)
        }

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor) : Livro{

            val posCampoId = cursor.getColumnIndex(BaseColumns._ID)
            val posCampoTitulo = cursor.getColumnIndex(TabelaLivros.CAMPO_TITULO)
            val posCampoAutor = cursor.getColumnIndex(TabelaLivros.CAMPO_AUTOR)
            val posCampoIdCategoria = cursor.getColumnIndex(TabelaLivros.CAMPO_ID_CATEGORIA)

            val id =cursor.getLong(posCampoId)
            val titulo = cursor.getString(posCampoTitulo)
            val autor = cursor.getString(posCampoAutor)
            val idCategoria = cursor.getLong(posCampoIdCategoria)

            return Livro(id,titulo,autor,idCategoria)
        }
    }

}