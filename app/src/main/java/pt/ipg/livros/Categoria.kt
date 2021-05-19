package pt.ipg.livros

import android.content.ContentValues

data class Categoria (val id: Long = -1, val nome: String) {

    fun toContentValues() : ContentValues{
        val valores = ContentValues()

        valores.put(TabelaCategorias.CAMPO_NOME, nome)
        return valores
    }
}