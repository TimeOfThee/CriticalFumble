package cards;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import main.Action;
import main.KeyManager;
import main.Plan;
import ui.Button;
import ui.ButtonText;
import ui.Theme;

public class Table {

	private KeyManager kM;
	
	private CardList list;
	private ArrayList<Integer> deck,discard;
	private int[] draw,disc,deckLoc=new int[] {1190,170,200,300},discLoc=new int[] {1220,deckLoc[1]+deckLoc[3]+10,140,200},lookup=new int[] {1300,30,400,100,-1,-83};
	private int perc=100,lookStart=lookup[0],lookEnd=1000,cx=150,cy=50;
	private ArrayList<Button> btns;
	
	boolean fromDeck=true;
	int btnDIsh,btnDIdr,btnDEdr,btnDEdi,btnDEre,btnLO,btnSE;
	int waiting=0;
	
	public Table(Plan plan,CardList list) {
		this.kM=plan.getKM();
		this.list=list;
		init();
		this.btns=new ArrayList<Button>();
		btns.add(new ButtonText(plan,"Shuffle",discLoc[0],discLoc[1]+discLoc[3]-30,discLoc[2],30,Plan.getColor(Theme.purple).darker(),new Action() {
			@Override
			public void action() {
				int size=discard.size();
				if(perc<100 || size==0)return;
				for(int a=size-1;a>=0;a--) {
					deck.add(discard.get(a));
					discard.remove(a);
				}
			}}));
		btnDIsh=0;
		btns.add(new ButtonText(plan,"Redraw",discLoc[0],discLoc[1]+30,discLoc[2],discLoc[3]-30-30,Plan.getColor(Theme.purple).darker(),new Action() {
				@Override
				public void action() {
					if(discard.size()>0)
						draw(discard.get((int)(Math.random()*discard.size())));
				}}));
		btnDIdr=1;
		btns.add(new ButtonText(plan,"Draw",deckLoc[0],deckLoc[1]+30,deckLoc[2],deckLoc[3]-30-30,Plan.getColor(Theme.blue).darker(),new Action() {
			@Override
			public void action() {
				if(deck.size()>0)draw();
			}}));
		btnDEdr=2;
		btns.add(new ButtonText(plan,"Discard",deckLoc[0],deckLoc[1]+deckLoc[3]-30,deckLoc[2],30,Plan.getColor(Theme.blue).darker(),new Action() {
			@Override
			public void action() {
				disc();
			}}));
		btnDEdi=3;
		btns.add(new ButtonText(plan,"Reroll",deckLoc[0]-115,deckLoc[1]+deckLoc[3]/2-50,100,100,Plan.getColor(Theme.blue).darker(),new Action() {
			@Override
			public void action() {
				if(draw[1]!=-1) {
					list.getCard(draw[1]).reset(true);
				}
			}}));
		btnDEre=4;
		btns.get(btnDEre).setCircle();
		btns.get(btnDEre).setVisible(false);
		btns.add(new ButtonText(plan,"Lookup",lookup[0],lookup[1],100,100,Plan.getColor(Theme.orange).darker(),new Action() {
			@Override
			public void action() {
				if(lookup[4]==-1) {
					lookup[4]=0;
					waiting=0;
				}
				else lookup[4]=-1;
			}}));
		btnLO=5;
		btns.add(new ButtonText(plan,"Search",1400-80,lookup[1],80,100,Plan.getColor(Theme.orange).darker(),new Action() {
			@Override
			public void action() {
				if(lookup[4]!=-1){
					searchInput(true);
				}
			}}));
		btnSE=6;
		btns.get(btnSE).setVisible(false);
	}
	public void init() {
		this.deck=new ArrayList<Integer>();
		this.discard=new ArrayList<Integer>();
		for(int a=0;a<list.getListSize();a++) {
			this.deck.add(a);
		}
		this.draw=new int[] {50,-1,0};
		this.disc=new int[] {50,-1};
		this.perc=100;
	}
	public void update() {
		Color set=Plan.getColor(Theme.purple).darker();
		btns.get(btnDIsh).setColor(set);
		btns.get(btnDIdr).setColor(set);
		set=Plan.getColor(Theme.blue).darker();
		btns.get(btnDEdr).setColor(set);
		btns.get(btnDEdi).setColor(set);
		btns.get(btnDEre).setColor(set);
		set=Plan.getColor(Theme.orange).darker();
		btns.get(btnLO).setColor(set);
		btns.get(btnSE).setColor(set);
		if(discard.size()==0) {
			btns.get(btnDIsh).setVisible(false);
			btns.get(btnDIdr).setVisible(false);
		}else {
			btns.get(btnDIsh).setVisible(true);
			btns.get(btnDIdr).setVisible(true);
		}
		if(deck.size()==0) {
			btns.get(btnDEdr).setVisible(false);
		}else {
			btns.get(btnDEdr).setVisible(true);
		}
		if(draw[1]!=-1) {
			btns.get(btnDEdi).setVisible(true);
		}else{
			btns.get(btnDEdi).setVisible(false);
		}
		for(Button b:btns) {
			b.update();
		}
		
		if(kM.kSP)draw();
		
		if(lookup[4]!=-1) {
			if(lookup[0]>lookEnd) {
				lookup[0]-=10;
				btns.get(btnLO).setLoc(lookup[0], lookup[1]);
				if(lookup[0]<=lookEnd) {
					lookup[0]=lookEnd;
					btns.get(btnSE).setVisible(true);
				}
			}
			searchInput(false);
		}else {
			if(kM.kSH) {
				if(lookup[5]!=-2) {
					lookup[4]=0;
					waiting=0;
					lookup[5]=-2;
				}
			}else {
				lookup[5]=-83;
			}
			btns.get(btnSE).setVisible(false);
			if(lookup[0]<lookStart) {
				lookup[0]+=10;
				btns.get(btnLO).setLoc(lookup[0], lookup[1]);
				if(lookup[0]>lookStart)lookup[0]=lookStart;
			}
		}
		
		if(perc<100) {
			perc++;
			//move
			int target=1000;
			float pow=7f;
			int skew= (int)((Math.pow(perc, pow) / (( Math.pow(perc, pow) )+( Math.pow( (100-perc) , pow)) ))*target);
			
			disc[0]=cy+skew;
			target=6000;
			skew= (int)((Math.pow(perc, pow) / (( Math.pow(perc, pow) )+( Math.pow( (100-perc) , pow)) ))*target);
			draw[0]=-target+skew+cx;
			
			if(draw[2]==1 && draw[1]!=-1)list.getCard(draw[1]).update();
			if(disc[1]!=-1)list.getCard(disc[1]).update();
		}else {
			if(draw[1]!=-1) {
				if(draw[2]==0) {
					draw[2]=1;
					list.getCard(draw[1]).reset(false);
				}
				list.getCard(draw[1]).update();
				
				if(list.getCard(draw[1]).isRoll()) {
					btns.get(btnDEre).setVisible(true);
				}else{
					btns.get(btnDEre).setVisible(false);
				}
			}
			if(disc[1]!=-1 && disc[1]!=draw[1])list.getCard(disc[1]).update();
		}
	}
	public void render(Graphics g) {
		g.setColor(Plan.getColor(Theme.draw));
		g.fillRect(deckLoc[0]-20, deckLoc[1]-20, deckLoc[2]+40, deckLoc[3]+40);
		g.setColor(Plan.getColor(Theme.discard));
		g.fillRect(discLoc[0]-20, discLoc[1]-20, discLoc[2]+40, discLoc[3]+40);
		g.setColor(Plan.getColor(Theme.lookup));
		g.fillRect(lookup[0]-20, lookup[1]-20, lookup[2]+40, lookup[3]+40);
		g.setColor(Plan.getColor(Theme.lookup).darker());
		g.fillRect(lookup[0]+110, lookup[1]+30,200,50);
		
		if(perc<100) {
			int target=1600;
			float pow=7f;
			int x= (int)((Math.pow(perc, pow) / (( Math.pow(perc, pow) )+( Math.pow( (100-perc) , pow)) ))*target) ;
			
			if(draw[1]!=-1) {
				g.setColor(Plan.getColor(Theme.card).darker().darker());
				if(fromDeck) {
					g.fillRect(deckLoc[0]-x,deckLoc[1],deckLoc[2],deckLoc[3]);
				}else {
					g.fillRect(discLoc[0]-x,discLoc[1],discLoc[2],discLoc[3]);
				}
			}
			if(disc[1]!=-1) {
				g.setColor(Plan.getColor(Theme.card).darker().darker());
				g.fillRect(discLoc[0],discLoc[1]-x+target-1,discLoc[2],discLoc[3]);
			}
		}
		
		if(deck.size()>0) {
			g.setColor(Plan.getColor(Theme.draw));
			g.fillRect(deckLoc[0],deckLoc[1],deckLoc[2],deckLoc[3]);
			Color model=Plan.getColor(Theme.blue).brighter();
			g.setColor(new Color(model.getRed(),model.getGreen(),model.getBlue(),100));
			g.fillRect(deckLoc[0],deckLoc[1],deckLoc[2],deckLoc[3]);
		}
		g.setFont(ButtonText.font);
		g.setColor(Plan.getColor(Theme.blue).darker());
		Graphics2D g2=(Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		int strWid=g2.getFontMetrics().stringWidth(Integer.toString(deck.size()));
		g.drawRect(deckLoc[0],deckLoc[1],deckLoc[2],deckLoc[3]);
		g.drawString(Integer.toString(deck.size()), deckLoc[0]+deckLoc[2]/2-strWid/2,deckLoc[1]+23);
		
		if(discard.size()>0) {
			g.setColor(Plan.getColor(Theme.discard));
			Color model=Plan.getColor(Theme.purple).brighter();
			g.fillRect(discLoc[0],discLoc[1],discLoc[2],discLoc[3]);
			g.setColor(new Color(model.getRed(),model.getGreen(),model.getBlue(),100));
			g.fillRect(discLoc[0],discLoc[1],discLoc[2],discLoc[3]);
		}
		g.setColor(Plan.getColor(Theme.purple).darker());
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		strWid=g2.getFontMetrics().stringWidth(Integer.toString(discard.size()));
		g.drawRect(discLoc[0],discLoc[1],discLoc[2],discLoc[3]);
		g.drawString(Integer.toString(discard.size()), discLoc[0]+discLoc[2]/2-strWid/2,discLoc[1]+23);

		g.setColor(Plan.getColor(Theme.orange).darker());
		g.drawRect(lookup[0], lookup[1], lookup[2], lookup[3]);
		if(lookup[4]!=-1) {
			g.drawString(Integer.toString(lookup[4])+"|", lookup[0]+110, lookup[1]+50+12);
		}
		
		if(disc[1]!=-1) {
			list.getCard(disc[1]).render(g,cx,disc[0]);
		}
		if(draw[1]!=-1) {
			list.getCard(draw[1]).render(g, draw[0], cy);
		}
		for(Button b:btns) {
			b.render(g);
		}
	}
	public void draw() {
		fromDeck=true;
		if(deck.size()<=0 || perc<100)return;
		int point=(int)(Math.random()*deck.size()),id=deck.get(point);
		disc[1]=draw[1];
		disc[0]=cy;
		perc=0;
			
		draw[1]=id;
		draw[2]=0;
		
		if(disc[1]!=-1){
			boolean cancel=false;
			for(int a=0;a<discard.size();a++) {
				if(disc[1]==discard.get(a)) {
					cancel=true;
					break;
				}
			}
			if(!cancel)discard.add(disc[1]);
		}
		deck.remove(point);
		
		perc=0;
		if(!list.getCard(draw[1]).isRoll()) {
			btns.get(btnDEre).setVisible(false);
		}
	}
	public void draw(int id) {
		if(perc<100 || id==draw[1])return;
		if(id>=list.getListSize())id=list.getListSize()-1;
		else if(id<0)id=0;
		disc[1]=draw[1];
		disc[0]=cy;
		perc=0;
			
		draw[1]=id;
		draw[2]=0;
		
		if(disc[1]!=-1){
			boolean cancel=false;
			for(int a=0;a<discard.size();a++) {
				if(disc[1]==discard.get(a)) {
					cancel=true;
					break;
				}
			}
			if(!cancel)discard.add(disc[1]);
		}
		for(int a=0;a<deck.size();a++) {
			if(deck.get(a)==id) {
				deck.remove(a);
				fromDeck=true;
			}
		}for(int a=0;a<discard.size();a++) {
			if(discard.get(a)==id) {
				discard.remove(a);
				fromDeck=false;
			}
		}
		
		perc=0;
		if(!list.getCard(draw[1]).isRoll()) {
			btns.get(btnDEre).setVisible(false);
		}
	}
	public void disc() {
		btns.get(btnDEre).setVisible(false);
		if(draw[1]==-1 || perc<100)return;
		disc[1]=draw[1];
		if(disc[1]!=-1) {
			boolean cancel=false;
			for(int a=0;a<discard.size();a++) {
				if(disc[1]==discard.get(a)) {
					cancel=true;
					break;
				}
			}
			if(!cancel)discard.add(disc[1]);
		}
		draw[1]=-1;
		disc[0]=cy;
		perc=0;
	}
	private void searchInput(boolean finish) {
		int append=-83,ret=lookup[4];
		if(kM.kSH){
			if(lookup[5]!=-2) {
				lookup[4]=-1;
				lookup[5]=-2;
				return;
			}
			append=-2;
		}else if(kM.kBA) {
			append=-2;
		}else if(kM.k9) {
			append=9;
		}else if(kM.k8) {
			append=8;
		}else if(kM.k7) {
			append=7;
		}else if(kM.k6) {
			append=6;
		}else if(kM.k5) {
			append=5;
		}else if(kM.k4) {
			append=4;
		}else if(kM.k3) {
			append=3;
		}else if(kM.k2) {
			append=2;
		}else if(kM.k1) {
			append=1;
		}else if(kM.k0) {
			append=0;
		}
		if(append!=-83) {
			waiting=0;
			if(lookup[5]!=append) {
				if(append==-2) {
					ret=Math.floorDiv(ret, 10);
					lookup[5]=-2;
				}else {
					ret=ret*10+append;
					if(ret-1>list.getListSize())ret=list.getListSize();
					lookup[5]=append;
				}
			}
		}else {
			lookup[5]=-83;
			waiting++;
			if(waiting>240) {
				ret=0;
				waiting=0;
			}
		}
		if(ret<0)ret=0;
		
		lookup[4]=ret;
		if(finish || kM.kEN) {
			draw(ret-1);
		}
	}
}
