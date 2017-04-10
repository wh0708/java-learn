package com.wh0708.tank;
//增加坦克的复活

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类的作用是主窗口
 * @author wanghao1996
 *
 */
public class TankClient extends Frame {
	/**
	 * 游戏窗口的宽度
	 */
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HIGHT = 600;
	
	Tank myTank = new Tank(500,400,true,Tank.Direction.STOP,this);
//	Tank enemyTank = new Tank(100,100,false,this);
	Wall w1 = new Wall(300,300,20,80,this);
	Wall w2 = new Wall(300,300,80,20,this);
	Wall w3 = new Wall(380,300,20,80,this);
	Wall w4 = new Wall(300,380,100,20,this);
//	Explode e = new Explode(170,170,this);
	
	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Tank> tanks = new ArrayList<Tank>();
	Image offScreenImage = null;
	
	Medicine med = new Medicine();
	public void paint(Graphics g) {
		/*
		 *指明子弹-爆炸-坦克的数量
		 *以及坦克的生命值
		 */
		g.drawString("missiles count: "+missiles.size(), 5, 50);
		g.drawString("exploeds count: "+explodes.size(), 5, 65);
		g.drawString("tanks count: "+tanks.size(), 5, 80);
		g.drawString("Health point: "+myTank.getLife(), 5, 95);
		for(int i=0;i<missiles.size();i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.hitWall(w3);
			m.hitWall(w4);
			m.draw(g);
			med.draw(g);
//			if(!m.isLive()) {
//				missiles.remove(m);
//			} else {
//				m.draw(g);
//			}
		}
		
		if(tanks.size() <= 0) {
			for(int i=0; i<5; i++) {
				tanks.add(new Tank(50+40*(i+1), 100, false, Tank.Direction.D, this));
			}
		}
		
		for(int i=0; i<explodes.size();i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.collidesOnWall(w1);
			t.collidesOnWall(w2);
			t.collidesOnWall(w3);
			t.collidesOnWall(w4);
			t.collidesOnTanks(tanks);
			t.draw(g);
		}
		myTank.draw(g);
		myTank.eat(med);
		w1.draw(g);
		w2.draw(g);
		w3.draw(g);
		w4.draw(g);
//		enemyTank.draw(g);
		
	}
	
	public void update(Graphics g) {  //双缓冲。。使画面不闪烁
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	} 

	public void lauchFrame() {
		
		for(int i=0; i<10; i++) {
			tanks.add(new Tank(50+40*(i+1), 100, false, Tank.Direction.D, this));
		}
		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);
		
		new Thread(new PaintThread()).start();
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
	
		tc.lauchFrame();
	}
	
	private class PaintThread implements Runnable {
		
		public void run() {
			while(true) {
				repaint(); //外部包装类的 父类的方法
				try {
					Thread.sleep(78);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);

		}
		
	}
	

}
