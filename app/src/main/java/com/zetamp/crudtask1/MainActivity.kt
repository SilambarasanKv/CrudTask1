package com.zetamp.crudtask1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.database.Cursor
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etName: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button

    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var btnDate: Button
    private lateinit var btnTime: Button
    private lateinit var btnSort: Button

    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: UserAdapter? = null

    private var user: UserModel? = null

    private var flag = "DESC"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        sqliteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener { addUser() }
        btnView.setOnClickListener { getUsers(flag) }
        btnUpdate.setOnClickListener { updateUsers() }

        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()

            //To update the data

            etEmail.setText(it.email)
            etName.setText(it.name)
            tvDate.setText(it.date)
            tvTime.setText(it.time)
            user = it
        }

        adapter?.setOnClickDeleteItem {
            deleteUser(it.id)
        }

        btnDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                handleDateButton()
            }
        })
        btnTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                handleTimeButton()
            }
        })

        btnSort.setOnClickListener {

            if (flag == "DESC") {
                flag = "ASC"
            } else {
                flag = "DESC"
            }

            getUsers(flag)


        }



    }



    private fun handleTimeButton() {
        val calendar = Calendar.getInstance()
        val HOUR = calendar[Calendar.HOUR_OF_DAY]
        val MINUTE = calendar[Calendar.MINUTE]
        var am_pm = ""

        val timePickerDialog = TimePickerDialog(this,
            { timePicker, hour, minute ->
                Log.i(TAG, "onTimeSet: $hour$minute")
                val calendar1 = Calendar.getInstance()
                calendar1[Calendar.HOUR_OF_DAY] = hour
                calendar1[Calendar.MINUTE] = minute
                val dateText = DateFormat.format("h:mm a", calendar1).toString()
                tvTime.setText(dateText)
            }, HOUR, MINUTE, false
        )

        timePickerDialog.show()
        tvTime.visibility = View.VISIBLE
    }

    private fun handleDateButton() {
        val calendar: Calendar = Calendar.getInstance()
        val YEAR: Int = calendar.get(Calendar.YEAR)
        val MONTH: Int = calendar.get(Calendar.MONTH)
        val DATE: Int = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(this,
            { datePicker, year, month, date ->
                val calendar1: Calendar = Calendar.getInstance()
                calendar1.set(Calendar.YEAR, year)
                calendar1.set(Calendar.MONTH, month)
                calendar1.set(Calendar.DATE, date)
                val dateText: String = DateFormat.format("dd/MM/yyyy", calendar1).toString()
                tvDate.setText(dateText)
            }, YEAR, MONTH, DATE
        )

        datePickerDialog.show()
        tvDate.visibility = View.VISIBLE
    }



    private fun getUsers(orderby: String) {
        val userList = sqliteHelper.getAllUsers(orderby)
        Log.e("pppp", "${userList.size}")

        //To display data in Recycler View

        adapter?.addItems(userList)
    }

    private fun addUser() {

        val email = etEmail.text.toString()
        val name = etName.text.toString()
        val password = etPassword.text.toString()
        val date = tvDate.text.toString()
        val time = tvTime.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please enter the required field", Toast.LENGTH_SHORT).show()
        } else {
            val user = UserModel(email = email, name = name, password = password, date = date, time = time)
            val status = sqliteHelper.insertUser(user)

            if (status > -1) {
                Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
                clearEditText()
                getUsers(flag)
            } else {
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun updateUsers() {

        val email = etEmail.text.toString()
        val name = etName.text.toString()
        val password = etPassword.text.toString()
        val date = tvDate.text.toString()
        val time = tvTime.text.toString()

        //To check whether the record not changed

        if (email == user?.email && name == user?.name && password == user?.password && date == user?.date && time == user?.time) {
            Toast.makeText(this, "Record not changed", Toast.LENGTH_SHORT).show()
            return
        }

        if (user == null) return

        val user = UserModel(id = user!!.id, email = email, name = name, password = password, date = date, time = time)
        val status = sqliteHelper.updateUser(user)

        if (status > -1) {
            clearEditText()
            getUsers(flag)
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun deleteUser(id: Int) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete the item?")
        builder.setCancelable(true)

        builder.setPositiveButton("Yes") {dialog,_ ->
            sqliteHelper.deleteUserbyID(id)
            getUsers(flag)
            dialog.dismiss()
        }

        builder.setNegativeButton("No") {dialog,_ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()

    }

    private fun clearEditText() {
        etEmail.setText("")
        etName.setText("")
        etPassword.setText("")
        tvTime.setText("")
        tvDate.setText("")
        etEmail.requestFocus()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        etEmail = findViewById(R.id.etEmail)
        etName = findViewById(R.id.etName)
        etPassword = findViewById(R.id.etPassword)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
        tvDate = findViewById(R.id.tvDate)
        tvTime = findViewById(R.id.tvTime)
        btnDate = findViewById(R.id.btnDate)
        btnTime = findViewById(R.id.btnTime)
        btnSort = findViewById(R.id.btnSort)


    }
}