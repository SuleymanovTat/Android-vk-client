package ru.suleymanovtat.androidclient.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.tbruyelle.rxpermissions2.RxPermissions
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import ru.suleymanovtat.androidclient.R
import ru.suleymanovtat.androidclient.model.VKUser
import ru.suleymanovtat.androidclient.requests.VKUsersCommand
import ru.suleymanovtat.androidclient.requests.VKWallPostCommand
import ru.suleymanovtat.androidclient.utils.PathUtils

class HomeFragment : Fragment() {

    val disposables = CompositeDisposable()
    var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.tvPostText.setOnClickListener { sharePost() }
        view.ivPhoto.setOnClickListener { requestPhoto() }
        view.tvPostTextImage.setOnClickListener { sharePost(uri) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestUsers()
    }

    private fun requestUsers() {
        VK.execute(VKUsersCommand(), object : VKApiCallback<List<VKUser>> {
            override fun success(result: List<VKUser>) {
                if (!result.isEmpty()) {
                    val user = result[0]
                    tvUserInfo.text = "${user.firstName} ${user.lastName}"
                    Glide.with(activity!!).load(user.photo).into(ivAvatar);
                }
            }

            override fun fail(error: VKApiExecutionException) {
                Log.e(TAG, error.toString())
            }
        })
    }

    private fun requestPhoto() {
        disposables.add(
            RxPermissions(activity!!).request(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).subscribe({
                if (it) { // Always true pre-M
                    // I can control the camera now
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, IMAGE_REQ_CODE)
                } else {
                    // Oups permission denied
                    Toast.makeText(activity, "Oups permission denied", Toast.LENGTH_LONG).show()
                }
            }, {
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
            })
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQ_CODE) {
            if (resultCode == RESULT_OK && data != null && data.data != null) {
                uri = Uri.parse(PathUtils.getPath(activity!!, data.data!!))
                Glide.with(activity!!).load(data.data).into(ivPhoto);
            } else {
                sharePost()
            }
        }
    }

    private fun sharePost(uri: Uri? = null) {
        val photos = ArrayList<Uri>()
        uri?.let {
            photos.add(it)
        }
        VK.execute(
            VKWallPostCommand(editMessageField.text.toString(), photos),
            object : VKApiCallback<Int> {
                override fun success(result: Int) {
                    Toast.makeText(activity, R.string.wall_ok, Toast.LENGTH_SHORT).show()
                }

                override fun fail(error: VKApiExecutionException) {
                    Log.e(TAG, "VKApiExecutionException " + error.toString())
                }
            })
    }

    override fun onPause() {
        disposables.clear()
        super.onPause()
    }

    companion object {
        private const val IMAGE_REQ_CODE = 101
        private const val TAG = "tag"
    }
}
