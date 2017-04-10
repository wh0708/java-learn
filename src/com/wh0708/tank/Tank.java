package com.wh0708.tank;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	public static final int HEIGHT = 30;
	public static final int WIDTH = 30;
	
	TankClient tc;
	
	private int life = 100;
	
	public void setLife(int life) {
		this.life = life;
	}

	public int getLife() {
		return life;
	}

	private boolean good;
	
	public boolean isGood() {
		return good;
	}

	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	private BloodBar bb = new BloodBar();
	private int x,y;
	private int oldX,oldY;
	private static Random r = new Random();
	
	private boolean bL = false, bU= false, bR = false, bD = false;
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};
	
	private Direction dir = Direction.STOP;
	private Direction fireDir = Direction.D;
	
	private int step = r.nextInt(12)+3;
	
	public Tank(int x, int y, boolean good) {
		
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x,y,good);
		this.dir=dir;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		if(!live) {
			if(!good) { 
				tc.tanks.remove(this);
			}
				return;
		}
		
		Color c = g.getColor();
		if(good) g.setColor(Color.RED); 
		else g.setColor(Color.BLUE);
		
		g.fillOval(x, y, 30, 30);
		g.setColor(c);
		
		if(good ) {
		bb.draw(g);
		}
		
		switch(fireDir) {
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);	
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x+ Tank.WIDTH/2, y);	
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);	
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);	
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);	
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);	
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);	
			break;
		
		}
		move();
	}
	
	void move() { 
		
		this.oldX = x; //tank上一步的位置
		this.oldY = y;
		
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
		case STOP:	
			break;
		}
		
		if(this.dir != Direction.STOP) {
			this.fireDir = this.dir;
		}
		
		if(x < 0) x = 0; //边界
		if(y < 30) y = 30;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HIGHT) y = TankClient.GAME_HIGHT - Tank.HEIGHT;
		
		if(!good) {
			Direction[] dirs = Direction.values();
			if(step == 0) {
				step = r.nextInt(12)+3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			if(r.nextInt(40)>38) {
				this.fire();
			}
			
			
		}
	}
	
	private void stay() {
		x = oldX;
		y = oldY;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F2 :
			if(!this.live) {
				this.live = true;
				this.life = 100;
			}
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
//			if(key == KeyEvent.VK_RIGHT) {
//			x += 5;
		
		}
		locateDirection();
	}
	
	void locateDirection() {
		if(bL && !bU && !bD && !bR) 
			dir = Direction.L;
		else if(bL && bU && !bD && !bR) 
			dir = Direction.LU;
		else if(!bL && bU && !bD && !bR) 
			dir = Direction.U;
		else if(!bL && bU && !bD && bR) 
			dir = Direction.RU;
		else if(!bL && !bU && !bD && bR) 
			dir = Direction.R;
		else if(!bL && !bU && bD && bR) 
			dir = Direction.RD;
		else if(!bL && !bU && bD && !bR) 
			dir = Direction.D;
		else if(bL && !bU && bD && !bR) 
			dir = Direction.LD;
		else if(!bL && !bU && !bD && !bR) 
			dir = Direction.STOP;
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_CONTROL:
			fire();
			break;		
		case KeyEvent.VK_RIGHT :
			bR = false;
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		case KeyEvent.VK_A :
			this.superFire();
			break;
		}
		locateDirection();
	}
	
	public Missile fire() {
		if(!live) {
			return null;
		}
		Missile m = new Missile(x,y,good,fireDir,this.tc);
		tc.missiles.add(m);
		
 		return m;
	}
	
	public Missile fire(Direction dir) {
		if(!live) {
			return null;
		}
		Missile m = new Missile(x,y,good,dir,this.tc);
		tc.missiles.add(m);
		
 		return m;
	}
	
	private void superFire() {
		Direction[] dirs = Direction.values();
		for(int i=0; i<8; i++) {
			fire(dirs[i]);
		}
	}
	/**
	 * 撞墙
	 * @param w 被撞的墙
	 * @return 是否撞上 
	 */
	public boolean collidesOnWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.stay(); //返回上一步的位置
			return true;
		}
		return false;
	}
	
	public boolean collidesOnTanks(java.util.List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t) {
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.stay(); 
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public class BloodBar {
		public void draw(Graphics g) {
			g.drawRect(x, y-10, WIDTH, 5);
			Color c = g.getColor();
			g.setColor(Color.RED);
			int w =WIDTH * life/100;
			g.fillRect(x, y-10, w, 5);
			g.setColor(c);
		}
	}
	
	public boolean eat(Medicine med) {
		if(this.live && med.isLive()&& this.getRect().intersects(med.getRect())) {
			this.life = 100;
			med.setLive(false);
			return true;
		}
		return false;
	
	}
}
