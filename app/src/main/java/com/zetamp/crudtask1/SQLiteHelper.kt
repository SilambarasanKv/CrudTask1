package com.zetamp.crudtask1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    companion object {

        private const val DATABASE_NAME = "USERDB"
        private const val DATABASE_VERSION = 1
        const val USERS = "users"
        private const val ID = "id"
        private const val EMAIL = "email"
        private const val NAME = "name"
        private const val PASSWORD = "password"
        private const val DATE = "date"
        private const val TIME = "time"

    }


    override fun onCreate(db: SQLiteDatabase?) {

        val createTblUser = ("CREATE TABLE " + USERS + "("
                + ID + " INTEGER PRIMARY KEY," + EMAIL + " TEXT,"
                + NAME + " TEXT," + PASSWORD + " TEXT," + DATE + " TEXT," + TIME + " TEXT" + ")")
        db?.execSQL(createTblUser)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS $USERS")
        onCreate(db)

    }


    fun insertUser(user: UserModel): Long {

        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(ID, user.id)
        cv.put(EMAIL, user.email)
        cv.put(NAME, user.name)
        cv.put(PASSWORD, user.password)
        cv.put(DATE, user.date)
        cv.put(TIME, user.time)

        val success = db.insert(USERS, null, cv)
        db.close()
        return success

    }
    fun getAllUsers(orderby: String): ArrayList<UserModel> {
        var selectQuery = ""
        val userList: ArrayList<UserModel> = ArrayList()

        if (orderby == "DESC") {
            selectQuery = "SELECT * FROM $USERS ORDER BY DATE DESC, TIME DESC"
        }
        else if (orderby == "ASC"){
            selectQuery = "SELECT * FROM $USERS ORDER BY DATE ASC, TIME ASC"
        }

        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var email: String
        var name: String
        var date: String
        var time: String

        if(cursor.moveToFirst()) {

            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                date = cursor.getString(cursor.getColumnIndex("date"))
                time = cursor.getString(cursor.getColumnIndex("time"))

                val user = UserModel(id = id, email = email, name = name, date = date, time = time)
                userList.add(user)
            } while (cursor.moveToNext())

        }

        return userList

    }

    fun updateUser(user: UserModel): Int {

        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(ID, user.id)
        cv.put(EMAIL, user.email)
        cv.put(NAME, user.name)
        cv.put(PASSWORD, user.password)
        cv.put(DATE, user.date)
        cv.put(TIME, user.time)

        val success = db.update(USERS, cv, "id=" + user.id, null)
        db.close()
        return success

    }

    fun deleteUserbyID(id: Int): Int {

        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(ID, id)

        val success = db.delete(USERS, "id=" + id, null)
        db.close()
        return success

    }

}