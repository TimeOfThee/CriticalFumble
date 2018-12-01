package main;
public class Launcher {
	public static final int screenSX=1400,screenSY=800;
	public static void main(String[] srgs) {
		Screen game=new Screen("Title",screenSX,screenSY);
		game.start();
	}
}
