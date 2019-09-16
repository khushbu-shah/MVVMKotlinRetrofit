package com.cell_tower.adapter

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cell_tower.R
import com.cell_tower.activity.TowerDetailActivity
import com.cell_tower.databinding.RowAssetsListBinding
import com.cell_tower.generated.callback.OnClickListener
import com.cell_tower.interfaces.OnItemClick
import com.cell_tower.webservices.responsepojos.AssetsResponse
import kotlin.collections.ArrayList

class LocationDevicesAdapter(items: ArrayList<AssetsResponse.Data.Location.LocationDevice>, context: Context, itemClickListener : OnItemClick) : RecyclerView.Adapter<LocationDevicesAdapter.MyHolder>() {

    var alDevice = ArrayList<AssetsResponse.Data.Location.LocationDevice>()
    var mContext : Context
    var onIeamClickListener : OnItemClick

    init {
        this.alDevice = items
        this.mContext = context
        this.onIeamClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding: RowAssetsListBinding = DataBindingUtil.inflate(inflater, R.layout.row_assets_list, parent, false)

        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        var binding: RowAssetsListBinding = holder.bind(alDevice[position])
    }

    override fun getItemCount(): Int {
        return alDevice.size
    }

    inner class MyHolder(private val binding: RowAssetsListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AssetsResponse.Data.Location.LocationDevice) : RowAssetsListBinding {

            binding.deviceBean = item
            binding.isFromUI = false

            binding.tvAssetName.setOnClickListener{
                if(binding.tvAssetName.text.toString()!="N/A"){
                    onIeamClickListener.onImageClick(item)
                }
            }
            return binding
        }
    }
}