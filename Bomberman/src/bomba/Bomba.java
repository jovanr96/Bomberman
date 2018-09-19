package bomba;

import client.MainClient;
import client.nit.ClientIgra;

public class Bomba extends Thread {

	public int pozX, pozY;
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
			if (ClientIgra.tabla[pozX][pozY] == 4) {
				mc.posaljiBombu(pozX, pozY, "[PUCANJE]");
				mc.ci.pucanje(pozX, pozY);
				mc.ci.repaint();
				
			}
			mc.ci.mozeBombaPonov();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
