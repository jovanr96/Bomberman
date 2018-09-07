package server.nit;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

	private String username;
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
			username = ulaz.readLine();
			Server.textArea.append("Novi klijent: " + username + "\n");
			System.out.println(brIgraca + "");
			if (brIgraca == 1) {
				Server.posaljiPoruku("[NOVI IGRAC] : Username = " + username, this);
			}
			while (true) {
				String prijem = ulaz.readLine();

				Server.posaljiPoruku(prijem, this);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
