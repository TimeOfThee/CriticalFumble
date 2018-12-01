package cards;

import java.awt.Color;
import java.awt.Graphics;

import main.Plan;
import ui.Theme;

public class Card {
	private Entry[] entries;
	private int id;
	private boolean roll=false;
	
	public Card(Entry[] entries,int id) {
		this.roll=false;
		this.entries=entries;
		for(Entry e:entries) {
			if(e.isRoll())this.roll=true;
		}
		this.id=id;
	}
	
	public void update() {
		for(Entry e:entries) {
			e.update();
		}
	}
	public void render(Graphics g, int x,int y) {
		g.setColor(Plan.getColor(Theme.card));
		g.fillRect(x-30, y-30, 760, 60+(140*entries.length));
		g.setColor(Plan.getColor(Theme.card).darker());
		g.setFont(Entry.fontText);
		g.drawString(Integer.toString(id+1), x-29, y-15);
		
		if(entries.length<4) {
			for(int a=0;a<entries.length;a++) {
				g.setColor(Plan.getColor(Theme.blue));
				g.drawString("event",x+30, y+(a*140)-2);
				entries[a].render(g, x, y+(a*140), g.getColor());
			}
		}else {
			for(int a=0;a<entries.length;a++) {
				switch(a) {
				case 0:
					g.setColor(Plan.getColor(Theme.red));
					g.drawString("melee",x+30, y+(a*140)-2);
					break;
				case 1:
					g.setColor(Plan.getColor(Theme.yellow));
					g.drawString("ranged",x+30, y+(a*140)-2);
					break;
				case 2:
					g.setColor(Plan.getColor(Theme.orange));
					g.drawString("natural",x+30, y+(a*140)-2);
					break;
				case 3:
					g.setColor(Plan.getColor(Theme.purple));
					g.drawString("magic",x+30, y+(a*140)-2);
					break;
				default:
					g.setColor(Plan.getColor(Theme.blue));
					g.drawString("event",x+30, y+(a*140)-2);
				}
				entries[a].render(g, x, y+(a*140), g.getColor());
			}
		}
	}
	public int getID() {return this.id;}
	public void reset(boolean manual) {
		for(Entry e:entries)e.reset(manual);
	}
	public boolean isRoll() {return roll;}
}
