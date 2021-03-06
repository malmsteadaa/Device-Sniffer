package com.example.sec2courseprojectgroup3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WifiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WifiFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var spinner: ProgressBar
    private lateinit var deviceInfoAdapter: DeviceInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_wifi, container, false)
        // Inflate the layout for this fragment

        spinner = view.findViewById<ProgressBar>(R.id.progressBar1)
        val macScanner = MacAddressScan()
        val context = this.requireContext()
        var deviceList = listOf<DeviceInfo>()
        val recyclerView = view.findViewById<RecyclerView>(R.id.lvDisplay)

        // spinner disabled
        spinner.visibility = View.GONE
        // adapter
        deviceInfoAdapter = DeviceInfoAdapter(LayoutInflater.from(this.context))
        recyclerView.adapter = deviceInfoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        view.findViewById<Button>(R.id.bScan).setOnClickListener {
            // coroutine to get the list of devices on network
            CoroutineScope(Dispatchers.IO).launch {
                requireActivity().runOnUiThread(Runnable {
                    spinner.visibility = View.VISIBLE // displaying visibility
                })
                // start ping
                deviceList = macScanner.startPing(context)
                // disabling spinner once process is done
                requireActivity().runOnUiThread(Runnable {
                    deviceInfoAdapter.updateDeviceInfoList(deviceList)
                  spinner.visibility = View.GONE
                })
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
         * @param param2 Parameter 2.
         * @return A new instance of fragment WifiFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WifiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}