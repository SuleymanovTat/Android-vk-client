package ru.suleymanovtat.androidclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_group.view.*
import ru.suleymanovtat.androidclient.R
import ru.suleymanovtat.androidclient.model.VKGroup

class GroupsAdapter(
    val items: List<VKGroup>,
    private val listener: OnClickGroupListener?
) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder.mView) {
            tvName.text = item.name
            Glide.with(imAvatar.context!!).load(item.photo200).into(imAvatar);
            setOnClickListener {
                listener?.onGroupClick(item)
            }
        }
    }


    interface OnClickGroupListener {
        fun onGroupClick(group: VKGroup)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)
}
