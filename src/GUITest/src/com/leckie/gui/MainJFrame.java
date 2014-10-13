package com.leckie.gui;

import java.awt.AWTException;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

/**
 * 测试GUI应用打包问题<br>
 * 
 * @author Leckie
 * @date 2014-10-9
 */
public class MainJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public Robot robot = null;

	public MainJFrame() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage() {
		robot.keyPress(KeyEvent.VK_RIGHT);
	}
	
	public static void main(String[] args) throws InterruptedException {
		final MainJFrame mjf = new MainJFrame();
		mjf.setSize(600,400);
		mjf.setLayout(new FlowLayout());
		mjf.setLocationRelativeTo(null);
		mjf.setVisible(true);
		
		for(int i = 0;i<100;i++) {
			mjf.sendMessage();
			Thread.sleep(1000);
		}
		
	}

}
