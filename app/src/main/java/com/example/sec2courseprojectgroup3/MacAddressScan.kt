package com.example.sec2courseprojectgroup3

import android.content.Context
import android.net.wifi.WifiInfo

import android.net.wifi.WifiManager
import java.net.InetAddress;
import android.util.Log
import androidx.annotation.NonNull
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.FileReader
import kotlin.Exception
import kotlin.concurrent.thread
import kotlin.math.pow


class MacAddressScan {
    suspend fun startPingService(context:Context) : List<DeviceInfo>{

        // the list that will contain all the deviceInfo objects
        val deviceInfoList = mutableListOf<DeviceInfo>()
        val timeout = 10
        try {
            // wifi manager
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

            val subnet = getSubnetAddress(wifiManager.dhcpInfo.gateway, wifiManager.dhcpInfo.netmask)


            // pinging every ip address in the network and checking if they reply timeout is set to 10 milliseconds
            for (i in 1..254) {
                val host = "$subnet.$i"
                if (InetAddress.getByName(host).isReachable(timeout)) {
                    val strMacAddress: String = getMacAddressFromIP(host)
                    Log.w(
                        "Scan",
                        "Host: $host and Mac : $strMacAddress replied to ping"
                    )
                    //TODO: use API with the given mac address and add it to currentDeviceInfo
                    var deviceVendor : String
                    try {
                        delay(1100)
                        deviceVendor = SearchMac(strMacAddress)

                    } catch (e: Throwable) {
                        deviceVendor = "NONE"
                    }
                    val currentDeviceInfo = DeviceInfo(host, strMacAddress, deviceVendor)
                    deviceInfoList.add(currentDeviceInfo)
                } else {
                    Log.e("Scan", "No Reply from: $host")
                }
            }
        } catch (e: Throwable) {
            Log.d("Scan", e.toString())
        }


        return deviceInfoList
    }

    // getting the subnet address given an ip address
    private fun getSubnetAddress(address: Int, mask: Int): String {
        // NOTE: due to bugs in andoird, getting the netmask is not possible
        // so i found a nother method to get the subnetaddress
        //val temp = address and mask


        return String.format(
            "%d.%d.%d",
            address and 0xff,
            address shr 8 and 0xff,
            address shr 16 and 0xff
        )
    }

    private fun getSubNetMask(prefixLenght: Int) : Long {
        val temp : Long = 4294967295
        var prefix = 2
        val mask =  prefix.toDouble().pow(prefixLenght) - 1

        return (temp and  mask.toLong())

    }
    private fun getMacAddressFromIP(ipAddress: String): String {
        // buffered reader to open the file at /proc/net/arp
        var bufferedReader: BufferedReader? = null

        try {

            // opening file
            bufferedReader = BufferedReader(FileReader("/proc/net/arp"))

            var line: String // current line

            // reading a line on the file and comparing the ip address to the given ip address
            while (bufferedReader.readLine().also { line = it } != null) {

                // splitting the line into different sections, the string is split by white spaces (1..*)
                val splitted = line.split(" +".toRegex()).toTypedArray() // and converting to a typed array

                // if the given array is not empty and contains >= 4 items then the split was sucessful
                if (splitted != null && splitted.size >= 4) {

                    // the ip address should be in the first position
                    val ip = splitted[0]

                    // the mac address on the arp table is located in the 4rt (index = 3) position
                    val mac = splitted[3]

                    // lastly if the given ip equals the found ip address on the arp table then we return the mac address
                    if (ip.equals(ipAddress, ignoreCase = true)) {
                        // closing buffered reader before exiting the function
                        if (bufferedReader != null) {
                            bufferedReader.close()
                        }
                        return mac
                    }


                }
            }
        } catch (e : Throwable) {
            Log.d("Scan", e.toString())
        }
        // if the address could not be found the an empty mac address is returned
        return "00:00:00:00"
    }



}

