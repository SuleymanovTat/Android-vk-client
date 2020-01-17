package ru.suleymanovtat.androidclient

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.VK
import ru.suleymanovtat.androidclient.fragment.HomeFragment
import ru.suleymanovtat.androidclient.fragment.LoginFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            if (VK.isLoggedIn()) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, HomeFragment(), "home")
                    .commit()
            } else
                supportFragmentManager.beginTransaction().add(
                    R.id.container,
                    LoginFragment(),
                    "login"
                )
                    .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentById(R.id.container)
            ?.onActivityResult(requestCode, resultCode, data)
    }
}
