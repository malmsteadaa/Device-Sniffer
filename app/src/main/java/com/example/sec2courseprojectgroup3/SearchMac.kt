package com.example.sec2courseprojectgroup3
import java.net.URL
import java.sql.DriverManager.println

fun lookupVendor(mac: String) = URL("http://api.macvendors.com/" + mac)


class SearchMac {
    fun SearchMac(args: Array<String>){
        val macs = args
        for (mac in macs) println(lookupVendor(mac).toString())
    }

}