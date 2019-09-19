package com.mvvm.demo.service.room.model

import android.arch.persistence.room.*

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User) : Long

    @Query("select * from user where email =:email and password = :password")
    fun getUser(email : String, password : String) : User

    @Query("select * from user")
    fun getAllUser() : List<User>

    @Delete
    fun deleteUser(user: User)

    @Query("delete from user")
    fun deleteAllUser()

    @Update
    fun updateUser(user: User)

}