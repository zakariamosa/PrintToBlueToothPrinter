package com.example.printtobluetouthprinter.ui.home

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.printtobluetouthprinter.databinding.FragmentHomeBinding
import java.io.IOException
import java.util.UUID

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val discoveredDevices = mutableListOf<BluetoothDevice>()
    private lateinit var deviceListAdapter: DeviceListAdapter
    private var bluetoothSocket: BluetoothSocket? = null
    private val REQUEST_ENABLE_BT = 1
    private val PERMISSION_REQUEST_CODE = 100

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, proceed with Bluetooth operations
            startBluetoothDiscovery()
        } else {
            // Permission denied, show a message to the user
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize BluetoothAdapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(requireContext(), "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            return root
        }

        // Check and enable Bluetooth
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        // Initialize RecyclerView for device list
        deviceListAdapter = DeviceListAdapter(discoveredDevices) { device ->
            connectToPrinter(device)
        }
        binding.deviceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.deviceRecyclerView.adapter = deviceListAdapter

        // Check and request permissions
        checkAndRequestPermissions()

        // Set up print button click listener
        binding.printButton.setOnClickListener {
            val textToPrint = binding.textInput.text.toString()
            if (textToPrint.isNotEmpty()) {
                printText(textToPrint)
            } else {
                Toast.makeText(requireContext(), "Please enter text to print", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun checkAndRequestPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted, proceed with Bluetooth operations
                startBluetoothDiscovery()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // Explain why the permission is needed
                Toast.makeText(requireContext(), "Location permission is required for Bluetooth scanning", Toast.LENGTH_LONG).show()
                // Request the permission
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                // Request the permission
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun startBluetoothDiscovery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Start Bluetooth discovery
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            requireActivity().registerReceiver(bluetoothReceiver, filter)
            bluetoothAdapter.startDiscovery()
        } else {
            // Permission not granted, request it
            checkAndRequestPermissions()
        }
    }

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        if (!discoveredDevices.contains(it)) {
                            discoveredDevices.add(it)
                            deviceListAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private fun connectToPrinter(device: BluetoothDevice) {
        // Check for BLUETOOTH_CONNECT permission (required for Android 12+)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission if not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                PERMISSION_REQUEST_CODE
            )
            return
        }

        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Standard SerialPortService ID
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            Toast.makeText(requireContext(), "Connected to ${device.name}", Toast.LENGTH_SHORT).show()
            binding.connectionStatus.text = "Connected to ${device.name}"
            binding.connectionStatus.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Failed to connect: ${e.message}", Toast.LENGTH_SHORT).show()
            binding.connectionStatus.text = "Connection Failed"
            binding.connectionStatus.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
        }
    }

    private fun printText(text: String) {
        if (bluetoothSocket == null) {
            Toast.makeText(requireContext(), "Not connected to a printer", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val outputStream = bluetoothSocket?.outputStream
            outputStream?.write(text.toByteArray())
            outputStream?.flush()
            Toast.makeText(requireContext(), "Printing successful", Toast.LENGTH_SHORT).show()
            binding.printStatus.text = "Printing Successful"
            binding.printStatus.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Print failed: ${e.message}", Toast.LENGTH_SHORT).show()
            binding.printStatus.text = "Printing Failed"
            binding.printStatus.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(bluetoothReceiver)
        bluetoothSocket?.close()
        _binding = null
    }
}