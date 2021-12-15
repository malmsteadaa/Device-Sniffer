package com.example.sec2courseprojectgroup3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView


class DeviceInfoAdapter (private val layoutInflater: LayoutInflater) :
    RecyclerView.Adapter<DeviceInfoAdapter.DeviceInfoViewHolder>() {

    private val deviceInfoList = mutableListOf<DeviceInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceInfoViewHolder =
        DeviceInfoViewHolder(layoutInflater.inflate(R.layout.view_device_info, parent, false))

    override fun getItemCount() = deviceInfoList.size

    override fun onBindViewHolder(holder: DeviceInfoViewHolder, position: Int) {
        holder.bind(deviceInfoList[position])
    }

    fun updateDeviceInfoList(deviceInfo: List<DeviceInfo>) {
        this.deviceInfoList.clear()
        this.deviceInfoList.addAll(deviceInfo)
        this.notifyDataSetChanged()
    }

    inner class DeviceInfoViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        private val ipAddressTexView: TextView =
            containerView.findViewById<TextView>(R.id.view_deviceInfo_row_ipaddress)
        private val macAddressTextView: TextView =
            containerView.findViewById<TextView>(R.id.view_deviceInfo_row_macAddress)
        private val deviceVendorTextView: TextView =
            containerView.findViewById<TextView>(R.id.view_deviceInfo_row_vendorInfo)

        fun bind(deviceInfo: DeviceInfo) {
            ipAddressTexView.text = "IP Address: " + deviceInfo.ipAddress
            macAddressTextView.text = "MacAddress: " + deviceInfo.macAddress
            deviceVendorTextView.text = "Vendor: " + deviceInfo.deviceVendor
        }
    }
}