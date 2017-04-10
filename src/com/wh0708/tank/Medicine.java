package com.wh0708.tank;
import java.awt.*;

public class Medicine {
	int x,y,w=10,h=10;
	TankClient tc;
	
	int step = 0;
	private boolean live = true;
	
	public void setLive(boolean live) {
		this.live = live;
	}
	public boolean isLive() {
		return live;
	}

	private int[] [] pos ={
			{500, 500},{510,500},{520,500},{520,510},{520,520},{510,520},{500,520},{500,510}
	};
	
	public Medicine() {
		x = pos[0][0];
		y = pos[0][1];
	}
	public void draw(Graphics g) {
		if(!live) return;
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}
	
	private void move() {
		step++;
		if(step == pos.length) {
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}
}
