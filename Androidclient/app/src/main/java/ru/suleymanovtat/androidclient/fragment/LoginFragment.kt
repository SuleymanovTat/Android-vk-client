package ru.suleymanovtat.androidclient.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.fragment_login.view.*
import ru.suleymanovtat.androidclient.R

class LoginFragment : Fragment() {

    companion object {
        private const val TAG = "tag"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        view.btnLogin.setOnClickListener {
            VK.login(
                activity!!,
                arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.PAGES, VKScope.GROUPS)
            )
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                // User passed authorization
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, HomeFragment(), "home")
                    ?.commit()
            }

            override fun onLoginFailed(errorCode: Int) {
                // User didn't pass authorization
                Log.e(TAG, "errorCode " + errorCode)
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
            Log.e(TAG, "super.onActivityResult")
        }
    }
}
