package pt.ipg.livros

import android.content.ContentProvider
import android.content.ContentValues
import android.content.IntentFilter
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class ContentProviderLivros : ContentProvider() {
    private var bdLivrosOpenHelper : BdLivrosOpenHelper? = null

    override fun onCreate(): Boolean {
         bdLivrosOpenHelper = BdLivrosOpenHelper(context)

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        val bd = bdLivrosOpenHelper!!.readableDatabase

        return when (getUriMatcher().match(uri)){

            URI_LIVROS -> TabelaLivros(bd).query(
                            projection as Array<String>,
                            selection,
                            selectionArgs as Array<String>?,
                            null,
                            null,
                            sortOrder
                            )
            URI_LIVRO_ESPECIFICO -> TabelaLivros(bd).query(
                            projection as Array<String>,
                            "${BaseColumns._ID}=?",
                            arrayOf(uri.lastPathSegment!!),
                            null,
                            null,
                            null
                            )
            URI_CATEGORIAS -> TabelaCategorias(bd).query(
                                projection as Array<String>,
                                selection,
                                selectionArgs as Array<String>?,
                                null,
                                null,
                                sortOrder
                            )
            URI_CATEGORIA_ESPECIFICA -> TabelaCategorias(bd).query(
                                projection as Array<String>,
                                "${BaseColumns._ID}=?",
                                arrayOf(uri.lastPathSegment!!),
                                null,
                                null,
                                null
                            )
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return when (getUriMatcher().match(uri)){
                        URI_LIVROS -> "$MULTIPLOS_ITEMS/$LIVROS"
                        URI_LIVRO_ESPECIFICO -> "$UNICO_ITEM/$LIVROS"
                        URI_CATEGORIAS -> "$MULTIPLOS_ITEMS/$CATEGORIAS"
                        URI_CATEGORIA_ESPECIFICA -> "$UNICO_ITEM/$CATEGORIAS"
            else -> null
                    }
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val bd = bdLivrosOpenHelper!!.writableDatabase

        val id =  when (getUriMatcher().match(uri)){

            URI_LIVROS -> TabelaLivros(bd).insert(values!!)

            URI_CATEGORIAS -> TabelaCategorias(bd).insert(values!!)

            else -> -1L
        }

        if(id == -1L) return null

        return Uri.withAppendedPath(uri, id.toString())
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val bd = bdLivrosOpenHelper!!.writableDatabase

        return when (getUriMatcher().match(uri)){

            URI_LIVRO_ESPECIFICO -> TabelaLivros(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_CATEGORIA_ESPECIFICA -> TabelaCategorias(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )
            else -> 0
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val bd = bdLivrosOpenHelper!!.writableDatabase

        return when (getUriMatcher().match(uri)){

            URI_LIVRO_ESPECIFICO -> TabelaLivros(bd).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
                )

            URI_CATEGORIA_ESPECIFICA -> TabelaCategorias(bd).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )
            else -> 0
        }
    }
    companion object{

        const val AUTHORITY = "pt.ipg.livros"
        const val LIVROS = "livros"
        const val CATEGORIAS = "categorias"

        private const val URI_LIVROS = 100
        private const val URI_LIVRO_ESPECIFICO = 101
        private const val URI_CATEGORIAS = 200
        private const val URI_CATEGORIA_ESPECIFICA = 201

        private const val MULTIPLOS_ITEMS = "vnd.adroid.cursor.dir"
        private const val UNICO_ITEM = "vnd.android.cursor.item"

        private val ENDERECO_BASE = Uri.parse("content://$AUTHORITY")
        public val ENDERECO_LIVROS = Uri.withAppendedPath(ENDERECO_BASE, LIVROS)
        public val ENDERECO_CATEGORIAS = Uri.withAppendedPath(ENDERECO_BASE, CATEGORIAS)

        private fun getUriMatcher() : UriMatcher{
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            // content://pt.ipg.livros/livros
            // content://pt.ipg.livros/categorias
            // content://pt.ipg.livros/livros/5

            uriMatcher.addURI(AUTHORITY, LIVROS, URI_LIVROS)
            uriMatcher.addURI(AUTHORITY,"$LIVROS/#", URI_LIVRO_ESPECIFICO)
            uriMatcher.addURI(AUTHORITY, CATEGORIAS, URI_CATEGORIAS)
            uriMatcher.addURI(AUTHORITY, "$CATEGORIAS/#", URI_CATEGORIA_ESPECIFICA)

            return uriMatcher
        }
    }
}