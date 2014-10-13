package com.beemic.thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.beemic.common.Session;
import com.beemic.util.IpResolverUtil;

/**
 * 通过<code>DatagramSocket</code>接受数据的线程
 * 
 * @author Kevin
 * @version 2014-04-03
 */
public class AudioStreamReciver implements Runnable {

	public final static int BUFFER_SIZE = 1600;// 每次读取的缓存大小

	private Session session;
	
	private DatagramSocket ds;

	public AudioStreamReciver() {
		try {
			ds = new DatagramSocket(9999, IpResolverUtil.getPreferedLocalHost());
		} catch (Exception e) {
			e.printStackTrace();
		}
		session = Session.getSession();
		session.setUdpPort(ds.getPort());
	}

	@Override
	public void run() {
		
		AudioFormat af = new AudioFormat(
				11025, 16, 1, true, false);
		SourceDataLine.Info info = new DataLine.Info(
		        SourceDataLine.class, af, BUFFER_SIZE);
		SourceDataLine sdl = null;
		try {
			sdl = (SourceDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
		
		byte[] buffer = new byte[BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
		
		try {
			sdl.open(af);
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
		sdl.start();
		boolean isPlaying = true;
		File file = new File("voice.pcm");
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		while (isPlaying) {
			try {
				ds.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sdl.write(buffer, 0, BUFFER_SIZE);
			try {
				os.write(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		sdl.drain();
		sdl.close();
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取本地端口
	 * 
	 * @return
	 */
	public int getLocalPort() {
		return ds.getLocalPort();
	}

}
