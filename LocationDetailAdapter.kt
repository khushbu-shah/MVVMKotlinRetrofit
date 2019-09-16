package com.cell_tower.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cell_tower.R
import com.cell_tower.databinding.RowAlertsBinding
import com.cell_tower.webservices.responsepojos.AlertListResponse
import com.cell_tower.utils.CommonUtils
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class LocationDetailAdapter(dayMap : HashMap<Int,ArrayList<AlertListResponse.Data.LocationTower>>, context: Context) : RecyclerView.Adapter<LocationDetailAdapter.MyHolder>() {

    var alDeviceInfo = ArrayList<AlertListResponse.Data.LocationTower>()
    var alKey = ArrayList<Int>()
    var hashMap = HashMap<Int,ArrayList<AlertListResponse.Data.LocationTower>>()
    var mContext : Context = context
    var mKeys: Array<String>? = null

    init {
        this.hashMap = dayMap

        val beans = dayMap.values

        /*convert HashMap data value to ArrayList of Custom Bean*/
        for(bean in dayMap.values)
        {
            for(data in bean)
            alDeviceInfo.add(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding: RowAlertsBinding = DataBindingUtil.inflate(inflater, com.cell_tower.R.layout.row_alerts, parent, false)

        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var binding: RowAlertsBinding = holder.bind(alDeviceInfo[position])
    }

    override fun getItemCount(): Int {
        return alDeviceInfo.size
    }

    inner class MyHolder(private val binding: RowAlertsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlertListResponse.Data.LocationTower): RowAlertsBinding {

            binding.alertBean = item

            val str = item.createdAt.split("T")

            val strLast = str[1]
            val tmp = strLast.split(".")
            tmp[0]

            binding.str = tmp[0]

            val diffDays: Int = CommonUtils.getDayDifference(item)

            return binding
        }
    }
}