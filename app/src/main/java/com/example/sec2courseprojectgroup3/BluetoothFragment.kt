package com.example.sec2courseprojectgroup3

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BluetoothFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BluetoothFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val deviceList = mutableListOf<DeviceInfo>()
    private lateinit var deviceInfoAdapter: BluetoothDeviceAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("BluetoothScan", "DEVICE FOUND")
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    CoroutineScope(IO).launch {
                        var vendorName : String
                        try {
                            vendorName = SearchMac(deviceHardwareAddress)
                        } catch (e: Exception) {
                            vendorName = "Unknown"
                            Log.e("BlueetoothScan", e.toString())
                        }

                        deviceList.add(DeviceInfo(deviceName, deviceHardwareAddress, vendorName))
                        requireActivity().runOnUiThread(Runnable {
                            deviceInfoAdapter.updateDeviceInfoList(deviceList)
                        })

                    }

                    Log.d("BluetoothScan", "NAME: " + deviceName + " MAC ADDRESS" + deviceHardwareAddress)

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_bluetooth, container, false)
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)

        val recyclerView = view.findViewById<RecyclerView>(R.id.lvDisplayBlue)
        deviceInfoAdapter = BluetoothDeviceAdapter(LayoutInflater.from(this.context))
        recyclerView.adapter = deviceInfoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        var bManager = this.requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bManager.adapter
        view.findViewById<Button>(R.id.bBluetooth).setOnClickListener {
            if (bluetoothAdapter == null) {
                Toast.makeText(this.context, "Bluetooth is not available in this device", Toast.LENGTH_LONG).show()
            }
            else {
                when (PermissionChecker.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    PackageManager.PERMISSION_GRANTED ->  {if ( !bluetoothAdapter.isEnabled) {
                        bluetoothAdapter.enable()
                    }
                        deviceList.clear()
                        requireActivity().registerReceiver(receiver, filter)

                        val status = bluetoothAdapter.startDiscovery()

                        Toast.makeText(this.context, "Discovery Started!", Toast.LENGTH_SHORT).show()
                        Log.d("BluetoothScan", "DISCOVERY HAS STARTED STATUS: " + status) }
                    else -> requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                }
            }

        }

        view.findViewById<Button>(R.id.bStopDiscovery).setOnClickListener {
            if (bluetoothAdapter == null) {
                Toast.makeText(this.context, "Bluetooth is not available in this device", Toast.LENGTH_LONG).show()
            }
            else {
                bluetoothAdapter.cancelDiscovery()
                Toast.makeText(this.context, "Discovery Stopped", Toast.LENGTH_SHORT).show()
                Log.d("BluetoothScan", "SIZE: " + deviceList.size)
                Log.d("BluetoothScan", "DISCOVERY HAS STOPPED")
            }

        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param par``am2 Parameter 2.
         * @return A new instance of fragment BluetoothFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BluetoothFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}