package ui;

import java.awt.Color;

public class Theme {
	
	private Color[] colors=new Color[] {
			new Color(220,50,40),
			new Color(160,150,30),
			new Color(190,120,30),
			new Color(140,30,160),
			new Color(50,80,190),
			new Color(150,150,140),
			new Color(200,200,180),
			new Color(200,200,180),
			new Color(200,200,180),
			new Color(200,200,180),
	};
	public static int red=0
	,yellow=1
	,orange=2
	,purple=3
	,blue=4
	,table=5
	,card=6
	,draw=7
	,discard=8
	,lookup=9;
	
	public Theme(int[] list) {
		for(int a=0;a<10;a++) {
			for(int b=0;b<3;b++) {
				if(list[(a*3)+b]>255)list[(a*3)+b]=255;
				else if(list[(a*3)+b]<0)list[(a*3)+b]=0;
			}
			colors[a]=new Color(list[(a*3)],list[(a*3)+1],list[(a*3)+2]);
		}
	}
	public Color getColor(int point) {
		return colors[point];
	}
}
