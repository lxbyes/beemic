package com.beemic.common;

import java.util.Date;

/**
 * 用以维护一次会话信息
 * <p>用单例模式保证了一个程序只能有一个连接
 * @author Kevin
 * @version 2014-04-03
 */
public class Session {
	
	//连接的ip
	private String ip;
	
	//TCP通信端口
	private int tcpPort;
	
	//UDP通信端口
	private int udpPort;
	
	//最后一次消息
	private String lastMessage;
	
	//是否连接
	private boolean isConnected = false;
	
	//会话开始时间
	private Date startTime;
	
	//会话是否结束
	private boolean isAlive;
	
	//会话唯一实例
	private static Session session = null;
	
	private Session() {
		setLastMessage("");
		setStartTime(new Date());
		setConnected(false);
	}
	
	public static Session getSession() {
		if(session == null) {
			session = new Session();
		}
		return session;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int i) {
		this.tcpPort = i;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
}
