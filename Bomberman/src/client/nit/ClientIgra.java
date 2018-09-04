package client.nit;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bomba.Bomba;

import client.MainClient;

public class ClientIgra extends JPanel implements KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;
	public int brIgraca;
	private int[][] tabla = new int[14][18];
	private ImageIcon zid9 = new ImageIcon("resursi/zid9.png");
	private ImageIcon zid1 = new ImageIcon("resursi/zid1.png");
	private ImageIcon slikaIgraca1 = new ImageIcon("resursi/player1/6.png");
	private ImageIcon slikaIgraca2 = new ImageIcon("resursi/player2/6.png");
	private ImageIcon bomba = new ImageIcon("resursi/bomba/bomb4.png");
	private ArrayList<Igrac> igraci = new ArrayList<>();
	private MainClient mc;
	private boolean novaPartija = true;

	public ClientIgra(int brIgraca, MainClient mc) {
		this.brIgraca = brIgraca;
		if (brIgraca == 0) {
			igraci.add(new Igrac(mc.user, 2, 2, 2 * 40 + 50, 2 * 40 + 10, true, true, false));
			igraci.add(new Igrac(MainClient.drugiKlijent, 11, 15, 11 * 40 + 50, 15 * 40 + 10, true, true, false));
		} else if (brIgraca == 1) {
			igraci.add(new Igrac(MainClient.drugiKlijent, 2, 2, 2 * 40 + 50, 2 * 40 + 10, true, true, false));
			igraci.add(new Igrac(mc.user, 11, 15, 11 * 40 + 50, 15 * 40 + 10, true, true, false));
		}
		ucitajTabelu();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		this.mc = mc;
	}

	private void ucitajTabelu() {
		File f = new File("pocetnaTabla.txt");

		Scanner skener;
		try {
			skener = new Scanner(f);
			for (int i = 0; i < 14; i++) {
				for (int j = 0; j < 18; j++) {
					if (skener.hasNext()) {
						tabla[i][j] = skener.nextInt();
						System.out.print(tabla[i][j] + " ");
					}
				}
				System.out.println("");
			}
			skener.close();

		} catch (FileNotFoundException e) {
			System.out.println("Nije moguce otvoriti file!");
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println("Uhvacen event!");
		if (arg0.getKeyCode() == KeyEvent.VK_W && proveraPomeranja(8)) {
			igraci.get(brIgraca).setPixX(igraci.get(brIgraca).getPixX() - 10);
			mc.posaljiPomeraj(8);
		} else if (arg0.getKeyCode() == KeyEvent.VK_S && proveraPomeranja(2)) {
			igraci.get(brIgraca).setPixX(igraci.get(brIgraca).getPixX() + 10);
			mc.posaljiPomeraj(2);
		} else if (arg0.getKeyCode() == KeyEvent.VK_A && proveraPomeranja(4)) {
			igraci.get(brIgraca).setPixY(igraci.get(brIgraca).getPixY() - 10);
			mc.posaljiPomeraj(4);
		} else if (arg0.getKeyCode() == KeyEvent.VK_D && proveraPomeranja(6)) {
			igraci.get(brIgraca).setPixY(igraci.get(brIgraca).getPixY() + 10);
			mc.posaljiPomeraj(6);
		} else if (arg0.getKeyCode() == KeyEvent.VK_M) {
			igraci.get(brIgraca).setMozeBomba(false);
			postaviBombu(igraci.get(brIgraca).getPozX(), igraci.get(brIgraca).getPozY());
			Bomba b = new Bomba(igraci.get(brIgraca).getPozX(), igraci.get(brIgraca).getPozY(), mc);
			b.start();
			mc.posaljiBombu(igraci.get(brIgraca).getPozX(), igraci.get(brIgraca).getPozY(), "[BOMBA]");
		}
		repaint();
		promeniPozicije();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(1, 1, 724, 799);
		Font f = new Font("Arial", Font.BOLD, 20);
		g.setFont(f);
		/*
		 * if (novaPartija) { try { g.setFont(new Font("Arial", Font.BOLD, 84));
		 * g.setColor(Color.yellow); g.drawString("3", 300, 500); wait(1000);
		 * g.setColor(Color.DARK_GRAY); g.fillRect(1, 1, 724, 799);
		 * g.setColor(Color.yellow); g.drawString("2", 300, 500); wait(1000);
		 * g.setColor(Color.DARK_GRAY); g.fillRect(1, 1, 724, 799);
		 * g.setColor(Color.yellow); g.drawString("1", 300, 500); wait(1000);
		 * novaPartija =false; } catch (InterruptedException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } } g.setColor(Color.DARK_GRAY);
		 * g.fillRect(1, 1, 724, 799);
		 */
		g.setColor(Color.ORANGE);
		g.drawString(igraci.get(0).getUsername() + ": " + igraci.get(0).getBrPobeda(), 20, 20);
		g.setColor(Color.green);
		g.drawString(igraci.get(1).getUsername() + ": " + igraci.get(1).getBrPobeda(), 600, 20);

		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 18; j++) {
				switch (tabla[i][j]) {
				case 9: {
					zid9.paintIcon(this, g, j * 40, i * 40 + 50);
					break;
				}
				case 1: {
					zid1.paintIcon(this, g, j * 40, i * 40 + 50);
					break;
				}
				case 4: {

					bomba.paintIcon(this, g, j * 40, i * 40 + 50);
					break;
				}
				}
			}
		}
		if (igraci.get(0).isZiv())
			slikaIgraca1.paintIcon(this, g, igraci.get(0).getPixY(), igraci.get(0).getPixX());
		if (igraci.get(1).isZiv())
			slikaIgraca2.paintIcon(this, g, igraci.get(1).getPixY(), igraci.get(1).getPixX());

		if (!igraci.get(0).isZiv()) {
			g.drawString("Pobednik  je " + igraci.get(1).getUsername(), 200, 20);
			//JOptionPane.showMessageDialog(mc.frame2, "Pobednik  je " + igraci.get(1).getUsername(), "Rezultat", JOptionPane.INFORMATION_MESSAGE);
			
			igraci.get(1).setBrPobeda(igraci.get(1).getBrPobeda() + 1);
			ponovo();
		} else if (!igraci.get(1).isZiv()) {
			g.drawString("Pobednik je " + igraci.get(0).getUsername(), 200, 20);
			//JOptionPane.showMessageDialog(mc.frame2, "Pobednik  je " + igraci.get(0).getUsername(), "Rezultat", JOptionPane.INFORMATION_MESSAGE);
			
			igraci.get(0).setBrPobeda(igraci.get(0).getBrPobeda() + 1);
			ponovo();
		}

	}

	public boolean proveraPomeranja(int b) {
		switch (b) {
		case 8:
			System.out.println(igraci.get(brIgraca).getPozX() + ", " + (igraci.get(brIgraca).getPixY() - 50 - 10));
			System.out.println(igraci.get(brIgraca).getPozX() + ", " + igraci.get(brIgraca).getPozY());
			System.out.println(igraci.get(brIgraca).getPixX() + ", " + igraci.get(brIgraca).getPixY());

			return (tabla[(igraci.get(brIgraca).getPixX() - 50 - 10) / 40][igraci.get(brIgraca).getPozY()] != 1
					&& tabla[(igraci.get(brIgraca).getPixX() - 50 - 10) / 40][igraci.get(brIgraca).getPozY()] != 9);
		case 2:
			return (tabla[(igraci.get(brIgraca).getPixX() - 20 + 10) / 40][igraci.get(brIgraca).getPozY()] != 1
					&& tabla[(igraci.get(brIgraca).getPixX() - 20 + 10) / 40][igraci.get(brIgraca).getPozY()] != 9);
		case 6:
			return (tabla[igraci.get(brIgraca).getPozX()][(igraci.get(brIgraca).getPixY() + 20) / 40] != 1
					&& tabla[igraci.get(brIgraca).getPozX()][(igraci.get(brIgraca).getPixY() + 20) / 40] != 9);
		case 4:
			return (tabla[igraci.get(brIgraca).getPozX()][(igraci.get(brIgraca).getPixY() - 10) / 40] != 1
					&& tabla[igraci.get(brIgraca).getPozX()][(igraci.get(brIgraca).getPixY() - 10) / 40] != 9);
		default:
			return false;
		}

	}

	private void promeniPozicije() {
		int b = 0;
		for (Igrac igrac : igraci) {
			System.out.println("Pozicije igraca br:" + b);
			b++;
			System.out.println("Pozicije pre provere pozicija:");
			System.out.println(igrac.getPozX() + ", " + igrac.getPozY());

			igrac.setPozX((igrac.getPixX() - 50) / 40);
			igrac.setPozY((igrac.getPixY()) / 40);
			System.out.println(igrac.getPozX() + ", " + igrac.getPozY());
			System.out.println(igrac.getPixX() + ", " + igrac.getPixY());
			System.out.println("Pozicije posle provere pozicija:");
		}

	}

	public void pomeranje(int b) {
		int a = 0;
		if (brIgraca == 0)
			a = 1;
		System.out.println("Pomeranje igraca " + a);
		switch (b) {
		case 8:
			igraci.get(a).setPixX(igraci.get(a).getPixX() - 10);
			System.out.println("Pomeren na gore!");
			break;
		case 2:
			igraci.get(a).setPixX(igraci.get(a).getPixX() + 10);
			System.out.println("Pomeren na dole");
			break;
		case 4:
			igraci.get(a).setPixY(igraci.get(a).getPixY() - 10);
			System.out.println("Pomeren u levo");
			break;
		case 6:
			igraci.get(a).setPixY(igraci.get(a).getPixY() + 10);
			System.out.println("Pomeren u desno");
			break;
		}
		promeniPozicije();
		repaint();

	}

	public void postaviBombu(int pozX, int pozY) {
		tabla[pozX][pozY] = 4;
		repaint();
	}

	public void pucanje(int x, int y) {
		System.out.println("Pozicije pred pucanje:");
		for (Igrac igrac : igraci) {
			System.out.println(igrac.getPozX() + ", " + igrac.getPozY());
		}
		tabla[x][y] = 0;
		for (Igrac igrac : igraci) {
			if (igrac.getPozX() == x && igrac.getPozY() == y) {
				igrac.setZiv(false);
			}
		}
		for (int i = 1; i < 4; i++) {
			if (x - i < 0)
				break;
			for (Igrac igrac : igraci) {
				if (igrac.getPozX() - i == x && igrac.getPozY() == y) {
					igrac.setZiv(false);
				}
			}
			if (tabla[x - i][y] != 9 && tabla[x - i][y] > 0) {
				tabla[x - i][y] = 0;
				break;
			}
		}
		for (int i = 1; i < 4; i++) {

			if (x + i > 13)
				break;
			for (Igrac igrac : igraci) {
				if (igrac.getPozX() + i == x && igrac.getPozY() == y) {
					igrac.setZiv(false);
				}
			}
			if (tabla[x + i][y] != 9 && tabla[x + i][y] > 0) {
				tabla[x + i][y] = 0;
				break;
			}
		}
		for (int i = 1; i < 4; i++) {
			if (y - i < 0)
				break;
			for (Igrac igrac : igraci) {
				if (igrac.getPozX() == x && igrac.getPozY() - i == y) {
					igrac.setZiv(false);
				}
			}
			if (tabla[x][y - i] != 9 && tabla[x][y - i] > 0) {
				tabla[x][y - i] = 0;
				break;
			}
		}
		for (int i = 1; i < 4; i++) {
			if (y + i > 17)
				break;
			for (Igrac igrac : igraci) {
				if (igrac.getPozX() == x && igrac.getPozY() + i == y) {
					igrac.setZiv(false);
				}
			}
			if (tabla[x][y + i] != 9 && tabla[x][y + i] > 0) {
				tabla[x][y + i] = 0;
				break;
			}
		}
		repaint();
		// proveraRezultataPartije();
	}

	public void ponovo() {
		igraci.get(0).setPozX(2);
		igraci.get(0).setPozY(2);
		igraci.get(0).setPixX(2 * 40 + 50);
		igraci.get(0).setPixY(2 * 40 + 10);
		igraci.get(0).setZiv(true);
		igraci.get(0).setMozeBomba(true);

		igraci.get(1).setPozX(11);
		igraci.get(1).setPozY(15);
		igraci.get(1).setPixX(11 * 40 + 50);
		igraci.get(1).setPixY(15 * 40 + 10);
		igraci.get(1).setZiv(true);
		igraci.get(1).setMozeBomba(true);
		novaPartija = true;
		ucitajTabelu();
		repaint();
	}

}
