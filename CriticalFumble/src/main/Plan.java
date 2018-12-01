package main;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import cards.CardList;
import cards.Entry;
import cards.Table;
import ui.Button;
import ui.ButtonText;
import ui.ButtonTheme;
import ui.Theme;

public class Plan {
	
	private KeyManager kM;
	private MouseManager mM;
	
	Table table;
	CardList list;
	
	static ArrayList<Theme> themes;
	public static int theme=0;
	
	public static final String space="//s+",line="\n";
	
	private ArrayList<Button> btns;
	private Button themeSelect,left,right;
	private boolean open=false;
	private int raise=800,start=800,end=700;
	private boolean tog=true;
	
	public Plan(KeyManager km,MouseManager mm) {
		this.kM=km;
		this.mM=mm;
		themeInit();
		this.list=new CardList();
		this.table=new Table(this,this.list);
		this.btns=new ArrayList<Button>();
		
		themeSelect=new ButtonText(this,"Theme", 0, 400, 1400, 30, getColor(Theme.yellow),new Action() {
			@Override
			public void action() {
				if(tog) {
					open=!open;
					tog=false;
				}
			}
		});
		left=new ButtonText(this,"<|", 0, end, 30, start-end, getColor(Theme.yellow),new Action() {
			@Override
			public void action() {
				theme--;
				if(theme<0)theme=themes.size()-1;
			}
		});
		right=new ButtonText(this,"|>", 1400-30, end, 30, start-end, getColor(Theme.yellow),new Action() {
			@Override
			public void action() {
				theme++;
				if(theme>=themes.size())theme=0;
			}
		});
		left.setVisible(false);
		right.setVisible(true);
		for(int a=0;a<themes.size();a++) {
			int set=a;
			btns.add(new ButtonTheme(this, 0, 0, 80, 80, themes.get(set),new Action() {
				@Override
				public void action() {
					if(tog) {
						theme=set;
						tog=false;
					}
				}
			}));
		}
	}
	
	public void update() {
		kM.update();
		
		if(open) {
			left.setVisible(true);
			right.setVisible(true);
			if(raise>end) {
				raise-=10;
				if(raise<=end)raise=end;
			}
		}else {
			left.setVisible(false);
			right.setVisible(false);
			if(raise<start) {
				raise+=10;
				if(raise>=start)raise=start;
			}
		}
		themeSelect.setLoc(0, raise-30);
		
		int avg=(int)( getColor(Theme.table).getRed()+getColor(Theme.table).getGreen()+getColor(Theme.table).getBlue() )/3;
		avg-=128;
		if(avg<0)avg+=255;
		Color model=new Color(avg,avg,avg);
		themeSelect.setColor(model);
		left.setColor(model);
		right.setColor(model);
		
		themeSelect.update();
		left.update();
		right.update();
		
		int shift=10+80;
		for(int a=0;a<btns.size();a++) {
			int dis=a-theme;
			dis=700+(dis*shift);
			btns.get(a).setLoc(dis-40, raise+10);
			if(dis<0+30 || dis+30>1400-30) {
				btns.get(a).setVisible(false);
			}else{
				btns.get(a).setVisible(true);
			}
			
			btns.get(a).update();
		}
		
		if(!mM.isM1()) {
			tog=true;
		}
		
		table.update();
	}
	public void render(Graphics g) {
		g.setColor(themes.get(theme).getColor(Theme.table));
		g.fillRect(0, 0, 1400, 800);
		g.setColor(new Color(255,255,255,100));
		g.fillRect(0, raise, 1400, 800-raise);
		table.render(g);
		
		for(Button b:btns) {
			b.render(g);
		}
		themeSelect.render(g);
		left.render(g);
		right.render(g);
	}
	
	private void themeInit() {
		this.themes=new ArrayList<Theme>();
		read("res/Themes.txt");
	}
	private void read(String path) {
		String file=Utils.loadFileAsString(path);
		String[] tokens=file.split(" ");
		ArrayList<String[]>sub=new ArrayList<String[]>();
		ArrayList<String>part=new ArrayList<String>();
		for(String s:tokens) {
			sub.add(s.split(line));
		}		
		for(String[] s:sub) {
			for(String p:s) {
				part.add(p);
			}
		}
		tokens=new String[part.size()];
		for(int a=0;a<tokens.length;a++) {
			tokens[a]=part.get(a);
		}
		
		int b=0,shift=0;
		int[] add=new int[30];
		while(b<tokens.length) {
			if(strToInt(tokens[b])!=-83) {
				add[shift]=strToInt(tokens[b]);
				shift++;
				if(shift==30) {
					themes.add(new Theme(add));
					shift=0;
				}
			}
			b++;
		}
	}
	
	public KeyManager getKM(){return this.kM;}
	public MouseManager getMM(){return this.mM;}
	
	public static Color getColor(int point) {
		return themes.get(theme).getColor(point);
	}
	public static void setTheme(int to) {theme=to;}
	public static int strToInt(String check) {
		int ret=0;
		if(check.length()==0)return -83;
		for(int a=0;a<check.length();a++) {
			int add=Entry.charToInt(check.substring(a, a+1));
			if(add!=-83) {
				ret=(ret*10)+add;
			}else {
				return -83;
			}
		}
		return ret;
	}
	
}
