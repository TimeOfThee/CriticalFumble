package cards;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Roll {

	private int size,result=0,timer,delay,skew=0,stack=0,reset,multiplier=1;
	
	public Roll(int size,int delay) {
		this.size=size;
		this.timer=30;
		this.delay=delay;
		this.stack=0;
		this.reset=delay;
	}
	public Roll(int size,int delay,int mult) {
		this.size=size;
		this.timer=30;
		this.delay=delay;
		this.stack=0;
		this.reset=delay;
		this.multiplier=mult;
	}
	public void update() {
		if(delay>0) {
			delay--;
		}else if(timer>0) {
			result=(int)(Math.random()*size)+1;
			timer--;
			
			int max=30;
			float perc=timer*100/max;
			skew=(int)( -(perc*perc/122)+(perc/1.22) );
		}else {
			skew=0;
		}
	}
	public void render(Graphics g,int x,int y,Color color) {
		g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),100));
		g.setFont(Entry.fontText);
		Graphics2D g2=(Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		int strWid=g2.getFontMetrics().stringWidth(Integer.toString(result*multiplier));
		g.fillOval(x, y-skew, 30, 30);
		g.setColor(color);
		g.drawOval(x, y-skew, 30, 30);
		g.drawString(Integer.toString(result*multiplier), x+30/2-strWid/2, y+21-skew);
		//g.drawString(Integer.toString(size), x+30/2-strWid/2, y+21+30-skew);
	}
	public void renderBack(Graphics g,int x,int y,Color color) {
		g.setColor(color);
		g.fillOval(x-8, y-8, 46, 46);
	}
	public int getStack() {return this.stack;}
	public void setStack(int to) {this.stack=to;}
	
	public void reset(boolean manual) {
		if(!manual)this.delay=reset;
		if(timer==0)this.timer=30;
	}
}
