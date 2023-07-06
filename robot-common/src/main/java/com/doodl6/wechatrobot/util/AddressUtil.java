package com.doodl6.wechatrobot.util;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressUtil {

    public static boolean validateAddress(String address) {
        String[] parts = address.split(":");
        if (parts.length > 2) {
            return false;
        }

        String host = parts[0];
        String port = "";

        if (parts.length == 2) {
            port = parts[1];
        }

        // 校验Host部分
        if (!isIP(host) && !isDomainName(host)) {
            return false;
        }

        // 校验Port部分
        if (!port.isEmpty() && !isPort(port)) {
            return false;
        }

        return true;
    }

    public static boolean isPort(String port) {
        try {
            int portNum = Integer.parseInt(port);
            if (portNum < 0 || portNum > 65535) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static boolean isIP(String ip) {
        try {
            InetAddress.getByName(ip);
        } catch (java.net.UnknownHostException e) {
            return false;
        }
        return true;
    }

    public static boolean isDomainName(String domain) {
        String domainRegex = "^[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(domainRegex);
        Matcher matcher = pattern.matcher(domain);
        return matcher.matches();
    }

    public static void main(String[] args) {
        String address = "example.com:8080";
        boolean isValid = validateAddress(address);
        System.out.println("Is address valid? " + isValid);
    }
}
