package cards;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import main.Plan;
import ui.Theme;

public class Entry {

	public static final Font fontTitle = new Font("Chiller",Font.BOLD,26);
	public static final Font fontText = new Font("Viner Hand ITC",Font.BOLD,18);
	
	private String title;
	private ArrayList<String> text;
	private ArrayList<Roll> rolls;
	private boolean roll=false;
	
	int wid=700,hei=35,hei2=30;
	
	public Entry(String title,String text) {
		this.roll=false;
		this.title=title;
		this.text=new ArrayList<String>();
		this.rolls=new ArrayList<Roll>();
		String[] tokens=text.split(" ");
		int words=11,shift=0;
		String line="";
		for(int a=0;a<tokens.length;a++) {
			int foundD=-83,foundX=-83;
			for(int d=0;d<tokens[a].length()-1;d++) {
				if(tokens[a].substring(d,d+1).equals("D") && strToInt(tokens[a].substring(d+1,tokens[a].length()))!=0) {//found d
					foundD=d;
				}else if(foundD!=-83 && tokens[a].substring(d,d+1).equals("x") && strToInt(tokens[a].substring(d+1,tokens[a].length()))!=0){
					foundX=d;
				}
			}
				if(foundD!=-83){
					
					if(foundX!=-83) {
						int amount=strToInt(tokens[a].substring(0, foundD));
						if(amount==0)amount=1;
						int mult=strToInt(tokens[a].substring(foundX, tokens[a].length()));
						if(amount>=0) {
							for(int b=0;b<amount;b++) {
								rolls.add(new Roll(strToInt(tokens[a].substring(foundD+1,foundX)),(10*shift)+(3*b),mult));
								rolls.get(rolls.size()-1).setStack(b);
								this.roll=true;
							}
							shift++;
						}
					}else {
						int amount=strToInt(tokens[a].substring(0, foundD));
						if(amount==0)amount=1;
						if(amount>=0) {
							for(int b=0;b<amount;b++) {
								rolls.add(new Roll(strToInt(tokens[a].substring(foundD+1,tokens[a].length())),(10*shift)+(3*b)));
								rolls.get(rolls.size()-1).setStack(b);
								this.roll=true;
							}
							shift++;
						}
						System.out.println(amount);
						System.out.println(foundD+" "+foundX);
					}
				}
				
			line=line+tokens[a]+" ";
			if((a>0 && a%(words-1)==0)||a==tokens.length-1) {
				this.text.add(line);
				line="";
			} 
		}
	}
	
	public void update() {
		for(Roll r:rolls) {
			r.update();
		}
	}
	
	public void render(Graphics g,int x,int y,Color color) {
		int skew=0;
		for(int a=0;a<rolls.size();a++) {
			if(a>0 && rolls.get(a).getStack()<=rolls.get(a-1).getStack())skew++;
			rolls.get(a).renderBack(g, x+wid+5+(rolls.get(a).getStack()*30), y+2+(skew*35), Plan.getColor(Theme.card));
		}
		
		g.setColor(color);
		g.drawRect(x, y, wid, hei);
		g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),60));
		g.fillRect(x, y, wid, hei+8+(text.size()*hei2));
		
		g.setColor(color);
		
		int shX=0,shY=0;
		if((int)(Math.random()*30)==0) {
			shX=(int)Math.pow(-1, (int)(Math.random()*2))*4;
		}if((int)(Math.random()*20)==0) {
			shY=(int)Math.pow(-1, (int)(Math.random()*2))*2;
		}
		
		g.setFont(fontTitle);
		Graphics2D g2=(Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		int strWid=g2.getFontMetrics().stringWidth(title);
		g.drawString(title, x+shX+wid/2-strWid/2, y+shY+28);
		
		g.setFont(fontText);
		skew=0;
		for(String str:text) {
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			strWid=g2.getFontMetrics().stringWidth(str);
			g.drawString(str, x+wid/2-strWid/2, y+60+skew);
			skew+=30;
		}
		skew=0;
		for(int a=0;a<rolls.size();a++) {
			if(a>0 && rolls.get(a).getStack()<=rolls.get(a-1).getStack())skew++;
			rolls.get(a).render(g, x+wid+5+(rolls.get(a).getStack()*30), y+2+(skew*35), color);
		}
	}
	public static int strToInt(String check) {
		int ret=0;
		if(check.length()==0)return -83;
		for(int a=0;a<check.length();a++) {
			int add=charToInt(check.substring(a, a+1));
			if(add!=-83) {
				ret=(ret*10)+add;
			}
		}
		return ret;
	}
	public static int charToInt(String check) {
			switch(check) {
			case "0":
				return 0;
			case "1":
				return 1;
			case "2":
				return 2;
			case "3":
				return 3;
			case "4":
				return 4;
			case "5":
				return 5;
			case "6":
				return 6;
			case "7":
				return 7;
			case "8":
				return 8;
			case "9":
				return 9;
			default:
				return -83;
			}
	}
	
	public String getTitle() {return this.title;}
	public ArrayList<String> getText() {return this.text;}
	
	public void reset(boolean manual) {
		for(Roll r:rolls)r.reset(manual);
	}
	public boolean isRoll() {return roll;}
}
