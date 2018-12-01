package cards;

import java.util.ArrayList;
import cards.Card;
import main.Utils;

public class CardList {

	public static final String line="\n",space="//s+",enterS=(line+">"+line),enter=(";"+"\n?");

	private ArrayList<Card> cards;
	
	public CardList() {
		this.cards=new ArrayList<Card>();
		read("res/Critical Fumble.txt");
	}
	public Card getCard(int id) {return cards.get(id);}
	
	/*private void read(String path) {
		int id=0;
		String file=Utils.loadFileAsString(path);
		String[] cardsS=file.split(enterS);
		for(String card: cardsS) {

			System.out.println("----------------------------------------");
			System.out.println(card);
			String[] entries=file.split(enter);
			for(String entry: entries) {
			}
			
		}
		
	}*/
	
	private void read(String path) {
		int id=0;
		String file=Utils.loadFileAsString(path);
		String[] cardsS=file.split(enterS);
		for(String card: cardsS) {
			ArrayList<Entry> add2=new ArrayList<Entry>();
			ArrayList<String> add=new ArrayList<String>();
			
			String[] entries=card.split(enter);
			for(String entry: entries) {
				String[] tokens=entry.split(",",2);
				add.add(tokens[0]);
				add.add(tokens[1]);
			}
			for(int a=0;a<add.size()/2;a++) {
				add2.add(new Entry(add.get(a*2),add.get((a*2)+1)));
			}
			Entry[] addTo=new Entry[add2.size()];
			for(int a=0;a<addTo.length;a++) {
				addTo[a]=add2.get(a);
			}
			this.cards.add(new Card(addTo,id));
			id++;
		}
		
	}
	public int getListSize() {return cards.size();}
}
