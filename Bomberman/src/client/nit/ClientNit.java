package client.nit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import client.MainClient;

public class ClientNit extends Thread {
	
	private Socket soket;
	private BufferedReader ulaz;
	private int brIgraca;
	private int pozX, pozY;
	private int pixX, pixY;
	private MainClient mc;
	
	public Socket getSoket() {
		return soket;
	}

	public void setSoket(Socket soket) {
		this.soket = soket;
	}

	public BufferedReader getUlaz() {
		return ulaz;
	}

	public void setUlaz(BufferedReader ulaz) {
		this.ulaz = ulaz;
	}

	public ClientNit(Socket soket, MainClient mc, int brIgraca) {
		this.soket = soket;
		this.mc = mc;
		this.brIgraca = brIgraca;
	}

	@Override
	public void run() {

		try {
			ulaz = new BufferedReader(new InputStreamReader(soket.getInputStream()));
			while (true) {
				System.out.println("Cekam poruku!");
				String prijem = ulaz.readLine();
				System.out.println("Primljena  poruka" + prijem);
				if (prijem.startsWith("CHAT"))
					mc.getTextArea().append(prijem.split(";")[1] + "\n");
				else if(prijem.startsWith("[START]")) {
					mc.posaljiPorukuZaStart(Integer.parseInt(prijem.split(":")[1].trim()));
				}else if(prijem.startsWith("START")) {
					mc.pokreniIgru(Integer.parseInt(prijem.split(":")[1].trim()));
				}
				else if(prijem.startsWith("[NOVI IGRAC]")) {
					System.out.println("Primljen novi igrac!");
					MainClient.drugiKlijent = prijem.split("=")[1].trim();
					mc.dopisiNovogIgraca(prijem);
				}else if(prijem.startsWith("[POSTOJECI KORISNIK]")) {
					MainClient.drugiKlijent = prijem.split("=")[1].trim();
					mc.getTextArea().append(prijem + "\n");
				}else if(prijem.startsWith("[POMERANJE]")) {
					mc.pomeranjeIgraca(prijem);
				}else if(prijem.startsWith("[BOMBA]")) {
					int b = Integer.parseInt(prijem.split(":")[1].trim());
					int c = Integer.parseInt(prijem.split(":")[2].trim());
					mc.postaviBombu(b,c);
				}else if(prijem.startsWith("[PUCANJE]")) {
					int b = Integer.parseInt(prijem.split(":")[1].trim());
					int c = Integer.parseInt(prijem.split(":")[2].trim());
					mc.ci.pucanje(b, c);
				}
				else if(prijem.startsWith("[PREKID]")) {
					System.out.println("Primljena poruka za prekid!");
					mc.izbaciIgraca();
				}
				else if(prijem.startsWith("[PREKID IGRE]")) {
					mc.ugasiIgruIPrikaziRezultat(1);
					
				}
					
			}

		} catch (IOException ex) {
			mc.getTextArea().append("Prekinuta konekcija\n");
		}

	}
}
