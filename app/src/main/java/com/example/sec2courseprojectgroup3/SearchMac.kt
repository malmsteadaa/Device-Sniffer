package com.example.sec2courseprojectgroup3
import java.net.URL
import java.sql.DriverManager.println

fun lookupVendor(mac: String) = URL("http://api.macvendors.com/$mac")


class SearchMac {
    fun SearchMac(args: String):String{
        val macs = args
        return lookupVendor(macs).toString()
    }

}