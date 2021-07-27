package com.zetamp.crudtask1

import java.util.*


data class UserModel(

    var id: Int = getAutoId(),
    var email: String = "",
    var name: String = "",
    var password: String = "",
    val date: String = "",
    val time: String = ""

) {

    companion object {

        fun getAutoId(): Int {
            val random = Random()
            return random.nextInt(100)


        }

    }

}