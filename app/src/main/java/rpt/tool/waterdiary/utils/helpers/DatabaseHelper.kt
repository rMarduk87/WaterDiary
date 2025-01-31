package rpt.tool.waterdiary.utils.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.utils.UtilityFunction
import rpt.tool.waterdiary.utils.log.e
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class DatabaseHelper
@SuppressLint("WrongConstant") constructor(var mContext: Context, var act: Activity) {
    var uf: UtilityFunction = UtilityFunction(mContext, act)
    var ah: AlertHelper
    var sh: StringHelper

    init {
        uf.permission_StrictMode()

        ah = AlertHelper(mContext)
        sh = StringHelper(mContext, act)

        AppUtils.SDB = mContext.openOrCreateDatabase(
            AppUtils.DATABASE_NAME,
            SQLiteDatabase.CREATE_IF_NECESSARY,
            null
        )
    }

    fun createTable(table_name: String, fields: HashMap<String, String>) {
        var query = "CREATE TABLE IF NOT EXISTS $table_name("

        val myVeryOwnIterator: Iterator<*> = fields.keys.iterator()
        while (myVeryOwnIterator.hasNext()) {
            val key = myVeryOwnIterator.next() as String
            val value = fields[key]

            query += "$key $value,"
        }

        query = query.substring(0, query.length - 1)

        query = "$query);"

        println("CREAT QUERY : $query")

        fire(query)
    }

    fun insert(table_name: String?, fields: HashMap<String?, String?>) {

        val initialValues = ContentValues()

        val myVeryOwnIterator: Iterator<*> = fields.keys.iterator()
        while (myVeryOwnIterator.hasNext()) {
            val key = myVeryOwnIterator.next() as String
            val value = fields[key]
            initialValues.put(key, value)
        }

        AppUtils.SDB!!.insert(table_name, null, initialValues)
    }

    fun insert(table_name: String?, fields: ContentValues?) {
        AppUtils.SDB!!.insert(table_name, null, fields)
    }

    fun update(table_name: String?, fields: HashMap<String?, String?>, where_con: String?) {

        val initialValues = ContentValues()

        val myVeryOwnIterator: Iterator<*> = fields.keys.iterator()
        while (myVeryOwnIterator.hasNext()) {
            val key = myVeryOwnIterator.next() as String
            val value = fields[key]
            initialValues.put(key, value)
        }

        AppUtils.SDB!!.update(table_name, initialValues, where_con, null)
    }

    fun update(table_name: String?, fields: ContentValues?, where_con: String?) {
        AppUtils.SDB!!.update(table_name, fields, where_con, null)
    }

    @SuppressLint("Recycle")
    fun getLoginUserDetails() {
        try {

            val cur: Cursor = AppUtils.SDB!!.rawQuery("select * from tbl_user_login", null)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e), it) }
        }
    }

    @SuppressLint("Recycle")
    fun getdataquery(query: String): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        println("SELECT QUERY : $query")

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i) ?:""
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    @SuppressLint("Recycle")
    fun getdata(table_name: String): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        val query = "SELECT * FROM $table_name"

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i) ?:""
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    @SuppressLint("Recycle")
    fun getdata(table_name: String, where_con: String): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT * FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " where $where_con"

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        println("SELECT QUERY : $query")

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i) ?:""
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    @SuppressLint("Recycle")
    fun getdata(
        table_name: String,
        order_field: String,
        order_by: Int
    ): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT * FROM $table_name"

        if (!sh.check_blank_data(order_field)) {
            query += if (order_by == 0) " ORDER BY $order_field ASC"
            else " ORDER BY $order_field DESC"
        }

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        println("DESC QUERY:$query")

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i) ?:""
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    @SuppressLint("Recycle")
    fun getdata(
        table_name: String,
        where_con: String,
        order_field: String,
        order_by: Int
    ): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT * FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " WHERE $where_con"

        if (!sh.check_blank_data(order_field)) {
            query += if (order_by == 0) " ORDER BY $order_field ASC"
            else " ORDER BY $order_field DESC"
        }

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i) ?:""
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    @SuppressLint("Recycle")
    fun getdata(
        field_name: String,
        table_name: String,
        where_con: String
    ): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT $field_name FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " where $where_con"

        print("JOIN QUERY:$query")
        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)


        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i) ?:""
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    @SuppressLint("Recycle")
    fun getdata(
        field_name: String,
        table_name: String,
        where_con: String,
        order_field: String,
        order_by: Int
    ): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT $field_name FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " where $where_con"

        if (!sh.check_blank_data(order_field)) {
            query += if (order_by == 0) " ORDER BY $order_field ASC"
            else " ORDER BY $order_field DESC"
        }

        println("HISTORY JOIN QUERY:$query")

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i) ?:""
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    fun remove(table_name: String) {
        val query = "DELETE FROM $table_name"
        fire(query)
    }

    fun remove(table_name: String, where_con: String) {
        var query = "DELETE FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " WHERE $where_con"

        fire(query)
    }

    @SuppressLint("Recycle")
    fun totalRow(table_name: String): Int {
        val query = "SELECT * FROM $table_name"
        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        return c.count
    }

    fun totalRow(table_name: String, where_con: String): Int {
        var query = "SELECT * FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " WHERE $where_con"

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        val count = c.count

        c.close()

        return count
    }

    @SuppressLint("Recycle")
    fun isExists(table_name: String, where_con: String): Boolean {
        var query = "SELECT * FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " WHERE $where_con"

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        return c.count > 0
    }

    @SuppressLint("Recycle")
    fun getLastId(table_name: String): String {
        val query = "SELECT id FROM $table_name"

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        if (c.moveToLast()) return "" + c.getString(0)

        return "0"
    }

    private fun fire(query: String?) {
        AppUtils.SDB!!.execSQL(query)
    }

    fun md5(md5: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            val array = md.digest(md5.toByteArray())
            val sb = StringBuffer()
            for (i in array.indices) {
                sb.append(Integer.toHexString((array[i].toInt() and 0xFF) or 0x100).substring(1, 3))
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    fun exportDB(): Boolean {
        var result = false
        val sd: File = File(Environment.getExternalStorageDirectory().absolutePath, "Databackup")

        if (!sd.exists()) {
            sd.mkdirs()
        }

        val data: File = Environment.getDataDirectory()
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val currentDBPath =
            "/data/" + "com.appname.appnamebasic" + "/databases/" + AppUtils.DATABASE_NAME
        val backupDBPath: String = AppUtils.DATABASE_NAME
        val currentDB: File = File(data, currentDBPath)
        val backupDB: File = File(sd, backupDBPath)
        try {
            source = FileInputStream(currentDB).channel
            destination = FileOutputStream(backupDB).channel
            destination.transferFrom(source, 0, source.size())
            source.close()
            destination.close()
            result = true
            //Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (e: Exception) {
            result = false
            e.printStackTrace()
        }
        return result
    }
}