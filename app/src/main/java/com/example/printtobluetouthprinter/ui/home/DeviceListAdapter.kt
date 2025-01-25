package com.example.printtobluetouthprinter.ui.home

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class DeviceListAdapter(
    private val devices: List<BluetoothDevice>,
    private val onDeviceSelected: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        // Check for BLUETOOTH_CONNECT permission before accessing the device name
        if (ContextCompat.checkSelfPermission(
                holder.itemView.context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            holder.textView.text = device.name ?: "Unknown Device"
        } else {
            holder.textView.text = "Unknown Device (Permission Required)"
        }
        holder.itemView.setOnClickListener { onDeviceSelected(device) }
    }

    override fun getItemCount(): Int = devices.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }
}