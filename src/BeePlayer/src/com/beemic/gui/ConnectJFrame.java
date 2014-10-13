package com.beemic.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;

/**
 * 生成二维码的页面
 * 
 * @author Kevin
 * @version 2014-04-02
 */
public class ConnectJFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_WIDTH = 400;

	public static final int DEFAULT_HEIGHT = 400;
	
	public static final int TITLE_PANEL_HEIGHT = 100;
	
	public static final int IMAGE_PANEL_HEIGHT = 400;

	public ConnectJFrame(String str, File file) {
		//UIManager.setLookAndFeel(newLookAndFeel)
		//setUndecorated(true); // 去掉窗口的装饰 
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);//采用指定的窗口装饰风格 
		setLayout(new BorderLayout());
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		//add(new TitlePanel(), BorderLayout.NORTH);
		add(new ImagePanel(str, file), BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		Image image = null;
		try {
			image = ImageIO.read(new File("ic_launcher.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(null != image)
			setIconImage(image);
	}

	/**
	 * 内部类<br>
	 * 重写画二维码的JPanel
	 */
	private class ImagePanel extends JPanel {

		private static final long serialVersionUID = 1L;

		private Image image;

		private String str;

		public ImagePanel(String str, File file) {
			//setBackground(new Color(255, 255, 255));
			setSize(DEFAULT_WIDTH, IMAGE_PANEL_HEIGHT);
			this.str = str;
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (null == image)
				return;
			int x = (getWidth() - image.getWidth(this)) / 2;
			int y = (getHeight() - image.getHeight(this)) / 2;
			// g.setFont(new Font("Arial",Font.BOLD,12));//设置字体
			g.drawString(str, x, y - 10);
			g.drawImage(image, x, y, null);// 居于正中
		}
	}
	
	/**
	 * 标题的画板
	 */
	public class TitlePanel extends JPanel {
		
		private static final long serialVersionUID = 2L;

		public TitlePanel() {
			setBackground(new Color(189, 255, 127));
			setPreferredSize(new Dimension(DEFAULT_WIDTH, TITLE_PANEL_HEIGHT));
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Image image = null;
			try {
				image = ImageIO.read(new File("ic_launcher.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(null != image)
				g.drawImage(image, 16, 16, this);
			//g.drawString("随麦", 80, 50);
		}
	}

}
