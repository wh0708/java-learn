package com.wh0708.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;



public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	int x,y;
	Tank.Direction dir;
	private boolean good;
	
	private boolean live = true;
	
	private TankClient tc;
	
	public boolean isLive() {
		return live;
	}

	public Missile(int x, int y, Tank.Direction dir) {
		super();
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int x, int y, boolean good, Tank.Direction dir, TankClient tc) {
		
		this(x, y, dir);
		this.good=good;
		this.tc=tc;
	}

	public void draw(Graphics g) {
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		Color c=g.getColor();
		if(!good) {
			g.setColor(Color.GRAY);
		}
		else {
		g.setColor(Color.BLACK);
		}
		g.fillOval(x+Tank.WIDTH/2-WIDTH/2, y+Tank.HEIGHT/2-HEIGHT/2, WIDTH, HEIGHT);
		g.setColor(c);
		
		Move();
	}

	private void Move() {
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;	
			break;
		case U:
			y -= YSPEED;	
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;	
			break;
		case R:
			x += XSPEED;	
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;	
			break;
		case D:
			y += YSPEED;	
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;	
			break;
		}
		
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HIGHT) {
			live = false;
			tc.missiles.remove(this);
		}
		
	}
	
	public boolean hitTank(Tank t) {
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good !=t.isGood()) {
			if(t.isGood()) {
				t.setLife(t.getLife()-20);
				if(t.getLife() <=0) {
					t.setLive(false);
				}
			} else {
				t.setLive(false);
			}
			
			this.live=false;
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size();i++) {
			if(hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
}
