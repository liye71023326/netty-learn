package com.demon.netty.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class OSUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(OSUtil.class);
	private static String localIp;
	private static String localName;

	static {
		localIp = getLinuxLocalIp();
		localName=getLinuxName();

	}
	public static String linuxLocalIp(){
		if(localIp!=null && localIp.length()>0){
			return localIp;
		}else{
			localIp = getLinuxLocalIp();
			return localIp;
		}
	}
	public static String linuxLocalName(){
		if(localName!=null && localName.length()>0){
			return localName;
		}else{
			localName = getLinuxName();
			return localName;
		}
	}

	/**
	 * 获取linux服务器ip地址
	 * @return
	 */
	private static String getLinuxLocalIp() {
//		Process p=null;
//		String line =null;
//		try {
//			p = Runtime
//					.getRuntime()
//					.exec(new String[] {
//							"/bin/sh",
//							"-c",
//							"/sbin/ifconfig | grep 'inet addr:'| grep -v '127.0.0.1' | cut -d: -f2 | awk '{ print $1}'" });
//			InputStream fis = p.getInputStream();
//			LineNumberReader input = new LineNumberReader(
//					new InputStreamReader(fis));
//			line = input.readLine();
//
//			p.waitFor();
//
//		} catch (IOException e) {
//
//			LOGGER.error("get local ip error",e);
//		} catch (InterruptedException e) {
//			LOGGER.error("get local ip error",e);
//		}
//
//		return line;
		
		
		String sIP = "";
		InetAddress ip = null;
		try {
			boolean bFindIP = false;
			Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				if (bFindIP) {
					break;
				}
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				// ----------特定情况，可以考虑用ni.getName判断
				// 遍历所有ip
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = (InetAddress) ips.nextElement();
					if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
							&& ip.getHostAddress().indexOf(":") == -1) {
						bFindIP = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("OSUtil|getLinuxLocalIp-->error",e);
		}
		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		return sIP;
	}
	/**
	 * 获取linux机器名名称
	 * @return
	 */
	private static String getLinuxName(){
		String name = "";
//		String line = null;
//		try {
//			Process p = Runtime.getRuntime().exec(
//					new String[] { "/bin/sh", "-c", "uname -n" });
//
//			InputStream fis = p.getInputStream();
//			LineNumberReader input = new LineNumberReader(
//					new InputStreamReader(fis));
//			line = input.readLine();
//
//			p.waitFor();
//		} catch (IOException e) {
//			LOGGER.error("get linux name error",e);
//		} catch (InterruptedException e) {
//			LOGGER.error("get linux name error",e);
//		}
//		return line;
		try {
			name = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			LOGGER.error("OSUtil|getLinuxName-->error",e);
		}
		return name;
	}

}
