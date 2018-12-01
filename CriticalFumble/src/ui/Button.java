package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import main.Action;
import main.Plan;

public abstract class Button {

	protected Plan plan;
	
	protected Rectangle bounds;
	protected Action act;
	protected boolean visible,hover,toggle;
	protected int alph;
	
	int spd=8;
	
	public Button(Plan plan,int x,int y,int sx,int sy,Action act) {
		this.plan=plan;
		this.bounds=new Rectangle(x,y,sx,sy);
		this.act=act;
		this.visible=true;
		this.hover=false;
		this.toggle=true;
		this.alph=0;
	}
	
	public void update() {
		if(!visible) {
			if(alph>0) {
				alph-=spd;
				if(alph<0)alph=0;
			}
		}else {
			if(alph<255) {
				alph+=spd;
				if(alph>255)alph=255;
			}
			if(bounds.contains(new Point(plan.getMM().getMX(),plan.getMM().getMY()))) {
				hover=true;
			}else {
				hover=false;
			}
			if(plan.getMM().isM1()) {
				if(hover && toggle) {
					act.action();
					toggle=false;
				}
			}else {
				toggle=true;
			}
		}
	}
	public abstract void render(Graphics g);
	
	public void setLoc(int x,int y) {bounds=new Rectangle(x,y,bounds.width,bounds.height);}
	public Rectangle getBounds() {return this.bounds;}
	public boolean isVisible() {return this.visible;}
	public void setVisible(boolean to) {this.visible=to;}
	
	public void setCircle() {}
	public void setColor(Color to) {}
}
