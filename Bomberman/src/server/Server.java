package server;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;

import server.nit.ServerNit;

public class Server {
	public static ArrayList<ServerNit> klijenti = new ArrayList<>();
	public static JTextArea textArea;
	public static void main(String[] args) {
		int brIgraca = 0;
		
		try {
			
			JFrame frame = new JFrame("Bomberman - server");
			frame.setSize(400, 400);
			frame.setResizable(false);
			frame.setFocusable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			JScrollPane scrollPane = new JScrollPane();
			frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
			textArea = new JTextArea();
			scrollPane.setViewportView(textArea);
			scrollPane.setVisible(true);
			frame.setVisible(true);
			ServerSocket server = new ServerSocket(2401);
			textArea.append("Server startovan\n" + InetAddress.getLocalHost().getHostAddress() + "\n");
			
			while(true) {
				Socket soket = server.accept();
				if(brIgraca < 2) {
					prihvati(soket);
					brIgraca++;
				}else {
					odbi(soket);
					textArea.append("Klijent odbijen!\n");
				}
			}
			
			
		}catch (IOException e) {
			textArea.append("Nije moguce startovati server!\n");
		}
		
		
	}

	private static void odbi(Socket soket) {
		try {
			DataOutputStream izlaz = new DataOutputStream(soket.getOutputStream());
			izlaz.writeBytes("Prebukirano!");
			izlaz.close();
			soket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void prihvati(Socket soket) {
		ServerNit serverNit = new ServerNit(soket, klijenti.size());
		klijenti.add(serverNit);
		Thread sn = new Thread(serverNit);
		sn.start();
	}

	public static void posaljiPoruku(String string, ServerNit serverNit) throws IOException {
		for (ServerNit sn : klijenti) {
			if(sn.getUsername() != serverNit.getUsername()) {
				textArea.append("Pocinjem slanje poruke ka "+sn.getUsername() + "\n");
				PrintWriter pw = new PrintWriter(sn.getSoket().getOutputStream(), true);
				pw.println(string);
				textArea.append("Poruka poslata" + "\n");
			}
		}
	}
	
	
}
