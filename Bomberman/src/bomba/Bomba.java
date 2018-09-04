package bomba;

import client.MainClient;

public class Bomba extends Thread {
	
	private int pozX, pozY;
	private MainClient mc;
	
	public Bomba(int pozX, int pozY, MainClient mc) {
		this.pozX = pozX;
		this.pozY = pozY;
		this.mc = mc;
	}
	
	@Override
	public void run() {
		
		try {
			sleep(3000);
			mc.posaljiBombu(pozX, pozY, "[PUCANJE]");
			mc.ci.pucanje(pozX,  pozY);
			
			mc.ci.repaint();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
