package ui;

import java.awt.Color;
import java.awt.Graphics;

import main.Action;
import main.Plan;

public class ButtonTheme extends Button{

	Theme theme;
	
	public ButtonTheme(Plan plan, int x, int y, int sx, int sy,Theme theme, Action act) {
		super(plan, x, y, sx, sy, act);
		this.theme=theme;
	}

	@Override
	public void render(Graphics g) {
		if(!visible)return;
		
		g.setColor(theme.getColor(Theme.table));
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		
		float board=bounds.width/8;
		
		for(int a=0;a<3;a++) {
			for(int b=0;b<3;b++) {
				int view=(a*3)+b;
				if(view>=Theme.table) {
					view++;
				}
				g.setColor(theme.getColor(view));
			
				g.fillOval((int)(bounds.x+board+(board*2*b)), (int)(bounds.y+(board*2*a)+board), (int)(board*2),(int)(board*2));
			}
		}
		if(hover) {
			g.setColor(Color.white);
			g.drawRect(bounds.x-1, bounds.y-1, bounds.width+1, bounds.height+1);
			g.setColor(Color.black);
			g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}

}
