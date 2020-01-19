package ru.suleymanovtat.androidclient.adapter

import android.graphics.Color
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_group, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.mView) {
            tvName.text = item.name
            enable.text = if (item.isSelect) "Вкл" else "Выкл"
            enable.setTextColor(if (item.isSelect) Color.GREEN else Color.BLUE)
            Glide.with(imAvatar.context!!).load(item.photo200).into(imAvatar);
            setOnClickListener {
                items.get(position).isSelect = !items.get(position).isSelect
                notifyDataSetChanged()
                listener?.onGroupClick(item)
            }
        }
    }

    fun getIds(): List<Int> {
        val ids = arrayListOf<Int>()
        for (item in items) {
            if (item.isSelect)
                ids.add(item.id)
        }
        return ids
    }


    interface OnClickGroupListener {
        fun onGroupClick(group: VKGroup)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)
}
