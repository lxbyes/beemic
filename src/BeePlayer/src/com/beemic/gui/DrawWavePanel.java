package com.beemic.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DrawWavePanel extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel = null;

	private static int h = 0;

	private static int m = 0;

	private static int s = 0;

	public DrawWavePanel() {
		
		panel = new MyPanel();
		
		add(panel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(400, 100);

		setLocationRelativeTo(null);

		setResizable(false);

		Image image = null;
		try {
			image = ImageIO.read(new File("ic_launcher.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null != image)
			setIconImage(image);
		
	}


	public static String getTimeString() {
		return "" + (h < 10 ? "0" : "") + h + ":" + (m < 10 ? "0" : "") + m
				+ ":" + (s < 10 ? "0" : "") + s;
	}
	
	class MyPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		public MyPanel() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						repaint();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						s++;
						if (s < 60) {
							continue;
						}
						s = 0;
						m++;
						if (m < 60) {
							continue;
						}
						m = 0;
						h++;
					}
				}
			}).start();
		}

		@Override
		public void paintComponent(Graphics g) {

			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g.drawString("正在播放：" + getTimeString(), 10, 20);

		}
	}

}