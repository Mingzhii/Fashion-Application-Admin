package my.com.fashionappstaff.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Reward


class RewardAdapter (val fn: (ViewHolder, Reward) -> Unit = { _, _ -> } )
    :  ListAdapter<Reward, RewardAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Reward>() {
        override fun areItemsTheSame(a: Reward, b: Reward)    = a.rewardID == b.rewardID
        override fun areContentsTheSame(a: Reward, b: Reward) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgReward     : ImageView = view.findViewById(R.id.imgReward)
        val txtRewardName : TextView = view.findViewById(R.id.txtRewardName)
        val txtRewardQuantity : TextView = view.findViewById(R.id.txtRewardQuantity)
        val txtPoint     : TextView = view.findViewById(R.id.txtRewardPoint)
        val btnDelete: Button = view.findViewById(R.id.btnListRewardDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_reward, parent, false)
        return RewardAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardAdapter.ViewHolder, position: Int) {
        val reward = getItem(position)

        holder.imgReward.setImageBitmap(reward.rewardPhoto.toBitmap())
        holder.txtRewardName.text = reward.rewardName
        holder.txtRewardQuantity.text = reward.rewardQuan.toString()
        holder.txtPoint.text = reward.rewardPoint.toString()

        fn(holder, reward)
    }


}