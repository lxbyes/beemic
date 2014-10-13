package com.beemic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 处理ip地址，获取各种IP
 * 
 * @author Kevin
 * @version 2014-04-03
 */
public class IpResolverUtil {

	/*
	 * private static String[] getAllLocalHostIP() { List<String> res = new
	 * ArrayList<String>(); Enumeration netInterfaces; try { netInterfaces =
	 * NetworkInterface.getNetworkInterfaces(); InetAddress ip = null; while
	 * (netInterfaces.hasMoreElements()) { NetworkInterface ni =
	 * (NetworkInterface) netInterfaces .nextElement();
	 * System.out.println("---Name---:" + ni.getName()); Enumeration nii =
	 * ni.getInetAddresses(); while (nii.hasMoreElements()) { ip = (InetAddress)
	 * nii.nextElement(); if (ip.getHostAddress().indexOf(":") == -1) {
	 * res.add(ip.getHostAddress()); System.out.println("本机的ip=" +
	 * ip.getHostAddress()); } } } } catch (SocketException e) {
	 * e.printStackTrace(); } return (String[]) res.toArray(new String[0]); }
	 */

	public static String getLocalIP() {
		String ip = "";
		try {
			Enumeration<?> e1 = (Enumeration<?>) NetworkInterface
					.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e1.nextElement();
				System.out.println("getLocalIP--nic.getDisplayName ():"
						+ ni.getDisplayName());
				System.out
						.println("getLocalIP--nic.getName ():" + ni.getName());
				if (!ni.getName().equals("eth0")) {
					continue;
				} else {
					Enumeration<?> e2 = ni.getInetAddresses();
					while (e2.hasMoreElements()) {
						InetAddress ia = (InetAddress) e2.nextElement();
						if (ia instanceof Inet6Address)
							continue;
						ip = ia.getHostAddress();
					}
					break;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return ip;
	}

	public static String getWinLocalIP() {
		String ip = "";
		try {
			Enumeration<?> e1 = (Enumeration<?>) NetworkInterface
					.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e1.nextElement();
				System.out.println("getWinLocalIP--nic.getDisplayName ():"
						+ ni.getDisplayName());
				System.out.println("getWinLocalIP--nic.getName ():"
						+ ni.getName());
				Enumeration<?> e2 = ni.getInetAddresses();
				while (e2.hasMoreElements()) {
					InetAddress ia = (InetAddress) e2.nextElement();
					ip = ia.getHostAddress();
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return ip;
	}

	/**
	 * 获取本机所有物理地址
	 * 
	 * @return
	 */
	public static String getMacAddress() {
		String mac = "";
		String line = "";

		String os = System.getProperty("os.name");

		if (os != null && os.startsWith("Windows")) {
			try {
				String command = "cmd.exe /c ipconfig /all";
				Process p = Runtime.getRuntime().exec(command);

				BufferedReader br = new BufferedReader(new InputStreamReader(
						p.getInputStream()));

				while ((line = br.readLine()) != null) {
					if (line.indexOf("Physical Address") > 0) {
						int index = line.indexOf(":") + 2;

						mac = line.substring(index);

						break;
					}
				}

				br.close();

			} catch (IOException e) {
			}
		}

		return mac;
	}

	public static String getLocalHostName() {
		String hostName;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception ex) {
			hostName = "";
		}
		return hostName;
	}

	public static String[] getAllLocalHostIP() {
		String[] ret = null;
		try {
			String hostName = getLocalHostName();
			if (hostName.length() > 0) {
				InetAddress[] addrs = InetAddress.getAllByName(hostName);
				if (addrs.length > 0) {
					ret = new String[addrs.length];
					for (int i = 0; i < addrs.length; i++) {
						ret[i] = addrs[i].getHostAddress();
						System.out.println(addrs[i].getHostAddress());
						System.out.println(addrs[i].getCanonicalHostName());
					}
				}
			}

		} catch (Exception ex) {
			ret = null;
		}
		return ret;
	}

	/**
	 * 获取本地首选IP
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static InetAddress getPreferedLocalHost() {
		String hostName = getLocalHostName();
		InetAddress ret = null;
		if(hostName != null && hostName.length() > 0) {
			try {
				InetAddress[] addrs = InetAddress.getAllByName(hostName);
				if(addrs.length > 0) {
					ret = addrs[0];
					for (int i = 0; i < addrs.length; i++) {
						if(addrs[i].getHostAddress().startsWith("192.168")) {
							ret = addrs[i];
							break;
						}
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
		}
		return ret;
	}
	
	/**
	 * 获取本地无线网卡IP
	 * 
	 * @return WLAN IP
	 */
	public static InetAddress getLocalWlanIp() {
		return null;
	}

	/**
	 * @param args
	 *//*
	public static void main(String[] args) {
		getPreferedLocalHost();
	}*/

}