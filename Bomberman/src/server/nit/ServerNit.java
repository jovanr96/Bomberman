package server.nit;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import server.Server;

public class ServerNit extends Thread {
	private Socket soket;

	public Socket getSoket() {
		return soket;
	}

	public void setSoket(Socket soket) {
		this.soket = soket;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getBrIgraca() {
		return brIgraca;
	}

	public void setBrIgraca(int brIgraca) {
		this.brIgraca = brIgraca;
	}

	private String username = "";
	private int brIgraca;

	public ServerNit(Socket soket, int brIgraca) {
		this.soket = soket;
		this.brIgraca = brIgraca;
	}

	@Override
	public void run() {

		System.out.println("Zapocinjem slanje!");
		try {
			PrintWriter izlaz = new PrintWriter(soket.getOutputStream(), true);
			izlaz.println("Dobrodosli!," + brIgraca);
			BufferedReader ulaz = new BufferedReader(new InputStreamReader(soket.getInputStream()));
			boolean p = true;
			// Provera da li je username zauzet!
			while (p) {
				username = ulaz.readLine().trim();
				if(username==null)
					p = false;
				else if(username.startsWith(" "))
					p = false;
				if (!Server.klijenti.isEmpty())
					for (ServerNit k : Server.klijenti) {
						if (k.username.equals(username) && k != this) {
							p = false;
						}
					}
				if (!p) {
					p = true;
					System.out.println("Saljem ponovo");
					izlaz.println("Ponovo");
					System.out.println("Poslato ponovo");
				} else {
					System.out.println("Saljem moze!");
					izlaz.println("Moze");
					System.out.println("Poslato moze!");
					break;
				}
			}
			Server.textArea.append("Novi klijent: " + username + "\n");
			System.out.println(brIgraca + "");
			if (brIgraca == 1) {
				Server.posaljiPoruku("[NOVI IGRAC] : Username = " + username, this);
			}
			while (true) {
				String prijem = ulaz.readLine();
				if (prijem.startsWith("[PREKID]")) {
					Server.textArea.append("Igrac " + username + " je napustio igru...\n");
					Server.posaljiPoruku(prijem, this);
					ulaz.close();
					izlaz.close();
					Server.izbaciKlijenta(this);
					break;
				}
				Server.posaljiPoruku(prijem, this);
			}
		}catch(SocketException ex) {
			try {
				Server.izbaciKlijenta(this);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
