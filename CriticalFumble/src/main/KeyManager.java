package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener{

	private boolean[] keys,pressed,nopress;
	public boolean kW,kA,kS,kD,kUP,kDO,kLE,kRI,kSP,kEN,kBA,kSH;
	public boolean k0,k1,k2,k3,k4,k5,k6,k7,k8,k9;
	
	public KeyManager(){
		keys=new boolean[256];
		pressed=new boolean[keys.length];
		nopress=new boolean[keys.length];
	}
	public void update() {
		for(int i=0;i<keys.length;i++) {
			if(nopress[i] && !keys[i])
				nopress[i]=false;
			else if(pressed[i]) {
				nopress[i]=true;
				pressed[i]=false;
			}
			if(!nopress[i] && keys[i]) {
				pressed[i]=true;
			}
		}
		
		if(justPressed(KeyEvent.VK_E)) {
			System.out.println("e");
		}
		
		kW=keys[KeyEvent.VK_W];
		kS=keys[KeyEvent.VK_S];
		kA=keys[KeyEvent.VK_A];
		kD=keys[KeyEvent.VK_D];
		kUP=keys[KeyEvent.VK_UP];
		kDO=keys[KeyEvent.VK_DOWN];
		kLE=keys[KeyEvent.VK_LEFT];
		kRI=keys[KeyEvent.VK_RIGHT];
		kSP=keys[KeyEvent.VK_SPACE];
		kEN=keys[KeyEvent.VK_ENTER];
		kBA=keys[KeyEvent.VK_BACK_SPACE];
		kSH=keys[KeyEvent.VK_SHIFT];
		k0=keys[KeyEvent.VK_0];
		k1=keys[KeyEvent.VK_1];
		k2=keys[KeyEvent.VK_2];
		k3=keys[KeyEvent.VK_3];
		k4=keys[KeyEvent.VK_4];
		k5=keys[KeyEvent.VK_5];
		k6=keys[KeyEvent.VK_6];
		k7=keys[KeyEvent.VK_7];
		k8=keys[KeyEvent.VK_8];
		k9=keys[KeyEvent.VK_9];
	}
	public boolean justPressed(int keyCode){
		if(keyCode < 0 || keyCode >= keys.length)
			return false;
		return pressed[keyCode];
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode()<0||arg0.getKeyCode()>=keys.length)
			return;
		keys[arg0.getKeyCode()]=true;
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode()<0||arg0.getKeyCode()>=keys.length)
			return;
		keys[arg0.getKeyCode()]=false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

}
