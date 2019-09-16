package com.cell_tower.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.View
import android.widget.Toast
import com.cell_tower.R
import com.cell_tower.databinding.ActivityProfileBinding
import com.cell_tower.utils.AppPreferences
import com.cell_tower.viewmodel.UpdateProfileViewModel
import com.cell_tower.webservices.requestpojos.ChangePasswordRequest
import com.cell_tower.webservices.responsepojos.ChangePwdResponse
import com.cell_tower.webservices.responsepojos.LoginResponse
import com.cell_tower.webservices.responsepojos.UpdateProfileResponse
import java.util.*


class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    var isEditProfile  = false
    var isChangePwd  = false
    var isNotification  = false
    var isStatistic  = false
    var isTouchId  = false

    var profileViewModel : UpdateProfileViewModel ? = null

    var profileBinding : ActivityProfileBinding?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val metrics = resources.displayMetrics
        val screenWidth = (metrics.widthPixels * 0.80).toInt()
        val screenHeight = (metrics.heightPixels * 0.80).toInt()

        profileBinding = DataBindingUtil.setContentView(this, com.cell_tower.R.layout.activity_profile)

        profileViewModel = ViewModelProviders.of(this).get(UpdateProfileViewModel::class.java)

        window.setLayout(screenWidth, screenHeight)

        profileBinding!!.clickListener = this
        profileBinding!!.isEditProfile = isEditProfile
        profileBinding!!.isChangePwd = isChangePwd

        profileViewModel!!.profileResponse.observe(this@ProfileActivity,ProfileUpdateObserver())
        profileViewModel!!.changePwdResponse.observe(this@ProfileActivity,ChangePwdObserver())
        profileViewModel!!.passwordNotMatchResponse.observe(this@ProfileActivity,PasswordMatchObserver())

        profileBinding!!.profileViewModel = profileViewModel

        profileBinding!!.userInfo = AppPreferences.getUserInfo(this)

        profileBinding!!.strAuthToken = AppPreferences.getString(AppPreferences.AUTH_TOKEN,"",this@ProfileActivity)
        profileBinding!!.change = ChangePasswordRequest()

    }

    override fun onClick(v: View?) {

        when (v!!.id)
        {
            R.id.tvEditProfile -> {
                profileBinding!!.isEditProfile = !profileBinding!!.isEditProfile!! // If true than set it to false else true
                        profileBinding!!.isChangePwd = false
            }
            R.id.tvChangePWD -> {
                profileBinding!!.isChangePwd = !profileBinding!!.isChangePwd!! // If true than set it to false else true
                profileBinding!!.isEditProfile = false

            }
            R.id.tvNotification -> profileBinding!!.isNotification = !profileBinding!!.isNotification!!

            R.id.tvStatistic -> profileBinding!!.isStatistic = !profileBinding!!.isStatistic!!

            R.id.ivClose -> this.finish()

            R.id.tvTouchId ->  profileBinding!!.isTouchId = !profileBinding!!.isTouchId!!
//            R.id.btnSavePassword -> profileViewModel!!.callChangePwd()
        }
    }

    inner class ProfileUpdateObserver : Observer<UpdateProfileResponse>
    {
    override fun onChanged(response: UpdateProfileResponse?) {

        Toast.makeText(this@ProfileActivity,response!!.data!!.message,Toast.LENGTH_SHORT).show()
        if(response!!.status == 200)
        {
            var userInfo = AppPreferences.getUserInfo(this@ProfileActivity) as LoginResponse.UserInfo
            profileBinding!!.isEditProfile = false

            response.data!!.userInfo!!.id = userInfo.id
            AppPreferences.saveUserInfo(response!!.data!!.userInfo,this@ProfileActivity)
            profileBinding!!.userInfo = response!!.data!!.userInfo
            this@ProfileActivity.finish()
        }
    }
}

    inner class PasswordMatchObserver : Observer<String>
    {
        override fun onChanged(response: String?) {

            Toast.makeText(this@ProfileActivity,response,Toast.LENGTH_SHORT).show()

        }
    }

    inner class ChangePwdObserver : Observer<ChangePwdResponse>
    {
        override fun onChanged(response: ChangePwdResponse?) {

            Toast.makeText(this@ProfileActivity,response!!.data!!.message,Toast.LENGTH_SHORT).show()
            if(response!!.status == 200)
            {
//                var userInfo = AppPreferences.saveUserInfo(response.data!!.userInfo,this@ProfileActivity) as LoginResponse.UserInfo
                profileBinding!!.isEditProfile = false
                profileBinding!!.isChangePwd = false

//                response.data!!.userInfo!!.id = userInfo.id
                AppPreferences.saveUserInfo(response!!.data!!.userInfo,this@ProfileActivity)
                this@ProfileActivity.finish()
//                ActivityUtils.startNewActivityWithFinish(this@ProfileActivity, null, LoginActivity::class.java)
            }
        }
    }

}
