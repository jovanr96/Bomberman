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
import javax.swing.Timer;

import bomba.Bomba;

import client.MainClient;

public class ClientIgra extends JPanel implements KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;
	public int brIgraca;
	public int brPartija;
	private int[][] tabla = new int[14][18];
	private ImageIcon prvaSlika = new ImageIcon("resursi/prvaSlika.png");
	private ImageIcon zid9 = new ImageIcon("resursi/zid9.png");
	private ImageIcon zid1 = new ImageIcon("resursi/zid1.png");
	private ImageIcon slikaIgraca1 = new ImageIcon("resursi/player1/6.png");
	private ImageIcon slikaIgraca2 = new ImageIcon("resursi/player2/6.png");
	private ImageIcon bomba = new ImageIcon("resursi/bomba/bomb4.png");
	private ImageIcon tataSpalioDjenku = new ImageIcon("resursi/player1/Eksplozija1.png");
	private ImageIcon plamenPoPoljima = new ImageIcon("resursi/eksplozija/expl1.png");
	private ArrayList<Igrac> igraci = new ArrayList<>();
	private MainClient mc;
	private boolean novaPartija = true;
	private boolean prvaSlikaProvera = true;
	public int is = 4;
	public boolean pocelo = true;
	public int p = 2;
	public short boom = 1;
	public short boomPoPoljima = 1;
	public short mrtav;
	public boolean proveraZaEksploziju = true;

	public ClientIgra(int brIgraca, MainClient mc, int i) {
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
		brPartija = i;
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

	Timer t = new Timer(1000, new ActionListener() { // Timer za ispis odbrojavanja

		@Override
		public void actionPerformed(ActionEvent e) {
			pocelo = false;
			repaint();
			is = is - 1;
			if (is == 0) {
				novaPartija = false;
				repaint();
			}
		}
	});

	Timer cekajSek = new Timer(1000, new ActionListener() { // Timer koji omogucava ispis pobednika nakon partije

		@Override
		public void actionPerformed(ActionEvent e) {
			p = p - 1;
			System.out.println("Smanjeno za jedan!");
			if (p == 0) {
				cekajSek.stop();
				repaint();
			}
		}
	});

	Timer eksplozijaIgraca = new Timer(250, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			boom++;
			tataSpalioDjenku = new ImageIcon("resursi/player" + mrtav + 1 + "/Eksplozija" + boom + ".png");
			repaint();
			if (boom == 6) {
				boom = 1;
				eksplozijaIgraca.stop();
			}
		}
	});

	Timer eksplozijaPoPoljima = new Timer(50, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			boomPoPoljima++;
			plamenPoPoljima = new ImageIcon("resursi/eksplozija/expl" + boomPoPoljima + ".png");
			repaint();
			if (boomPoPoljima == 6) {
				vracanjeNaNulu();
				eksplozijaPoPoljima.stop();
				repaint();
			}
		}

		private void vracanjeNaNulu() {
			for (int i = 0; i < 14; i++) {
				for (int j = 0; j < 18; j++) {
					if (tabla[i][j] == 7)
						tabla[i][j] = 0;
				}
			}
			boomPoPoljima = 1;
		}
	});

	@Override
	public void paint(Graphics g) {

		if (!cekajSek.isRunning()) { // Ova provera obezbedjuje da se ispise pobednik
			Font f = new Font("Arial", Font.BOLD, 20);
			g.setFont(f);
			g.setColor(Color.DARK_GRAY);
			g.fillRect(1, 1, 724, 799);
			if (prvaSlikaProvera) {
				prvaSlika.paintIcon(this, g, 1, 1);
				prvaSlikaProvera = false;
			}
			if (novaPartija && pocelo) { // Ova provera obezbedjuje pocetak rada tajmera za odbrojavanje do pocetka
											// partije
				t.start();
			} else if (novaPartija && !pocelo) { // Ispis odbrojavanja
				g.setFont(new Font("Arial", Font.BOLD, 64));
				g.setColor(Color.yellow);
				g.drawString(is + "", 350, 400);
			} else if (!novaPartija) {
				t.stop();
				g.setColor(Color.DARK_GRAY);
				g.fillRect(1, 1, 724, 799);
				g.setFont(f);
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
						case 7: {
							plamenPoPoljima.paintIcon(this, g, j * 40, i * 40 + 50);
							break;
						}
						}
					}
				}
				if (igraci.get(0).isZiv())
					slikaIgraca1.paintIcon(this, g, igraci.get(0).getPixY(), igraci.get(0).getPixX());
				if (igraci.get(1).isZiv())
					slikaIgraca2.paintIcon(this, g, igraci.get(1).getPixY(), igraci.get(1).getPixX());

				// Ispis pobednika i animacija eksplozije

				if (!igraci.get(0).isZiv()) {
					mrtav = 0;
					if (proveraZaEksploziju) {
						tataSpalioDjenku = new ImageIcon("resursi/player1/Eksplozija" + boom + ".png");
						eksplozijaIgraca.start();
						proveraZaEksploziju = false;
					}

					tataSpalioDjenku.paintIcon(this, g, igraci.get(0).getPixY(), igraci.get(0).getPixX());
					if (!eksplozijaIgraca.isRunning()) {
						g.setColor(Color.RED);
						g.drawString("Pobednik  je " + igraci.get(1).getUsername(), 200, 20);
						igraci.get(1).setBrPobeda(igraci.get(1).getBrPobeda() + 1);
						cekajSek.start();
						ponovo();
					}
				} else if (!igraci.get(1).isZiv()) {
					mrtav = 1;
					if (proveraZaEksploziju) {
						tataSpalioDjenku = new ImageIcon("resursi/player2/Eksplozija" + boom + ".png");
						eksplozijaIgraca.start();
						proveraZaEksploziju = false;
					}

					tataSpalioDjenku.paintIcon(this, g, igraci.get(1).getPixY(), igraci.get(1).getPixX());
					if (!eksplozijaIgraca.isRunning()) {
						g.setColor(Color.RED);
						g.drawString("Pobednik  je " + igraci.get(0).getUsername(), 200, 20);
						igraci.get(0).setBrPobeda(igraci.get(0).getBrPobeda() + 1);
						cekajSek.start();
						ponovo();
					}
				}
			}
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
		eksplozijaPoPoljima.start();
		System.out.println("Pozicije pred pucanje:");
		for (Igrac igrac : igraci) {
			System.out.println(igrac.getPozX() + ", " + igrac.getPozY());
		}
		tabla[x][y] = 7;
		for (Igrac igrac : igraci) {
			if (igrac.getPozX() == x && igrac.getPozY() == y) {
				igrac.setZiv(false);
			}
		}
		for (int i = 1; i < 4; i++) {
			if (x - i < 0)
				break;
			for (Igrac igrac : igraci) {
				if (igrac.getPozX() == x - i && igrac.getPozY() == y) {
					igrac.setZiv(false);
				}
			}
			if (tabla[x - i][y] != 9 && tabla[x - i][y] > 0) {
				tabla[x - i][y] = 0;
				break;
			} else if (tabla[x - i][y] == 0)
				tabla[x - i][y] = 7;
		}
		for (int i = 1; i < 4; i++) {

			if (x + i > 13)
				break;
			for (Igrac igrac : igraci) {
				if (igrac.getPozX() == x + i && igrac.getPozY() == y) {
					igrac.setZiv(false);
				}
			}
			if (tabla[x + i][y] != 9 && tabla[x + i][y] > 0) {
				tabla[x + i][y] = 0;
				break;
			} else if (tabla[x + i][y] == 0)
				tabla[x + i][y] = 7;
		}
		for (int i = 1; i < 4; i++) {
			if (y - i < 0)
				break;
			for (Igrac igrac : igraci) {
				if (igrac.getPozX() == x && igrac.getPozY() == y - i) {
					igrac.setZiv(false);
				}
			}
			if (tabla[x][y - i] != 9 && tabla[x][y - i] > 0) {
				tabla[x][y - i] = 0;
				break;
			} else if (tabla[x][y - i] == 0)
				tabla[x][y - i] = 7;
		}
		for (int i = 1; i < 4; i++) {
			if (y + i > 17)
				break;
			for (Igrac igrac : igraci) {
				if (igrac.getPozX() == x && igrac.getPozY() == y + i) {
					igrac.setZiv(false);
				}
			}
			if (tabla[x][y + i] != 9 && tabla[x][y + i] > 0) {
				tabla[x][y + i] = 0;
				break;
			} else if (tabla[x][y + i] == 0)
				tabla[x][y + i] = 7;
		}
		repaint();

	}

	public void ponovo() {
		if (igraci.get(0).getBrPobeda() == brPartija) {
			mc.ugasiIgruIPrikaziRezultat(2);
			return;
		} else if (igraci.get(1).getBrPobeda() == brPartija) {
			mc.ugasiIgruIPrikaziRezultat(3);
			return;
		}
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
		pocelo = true;
		proveraZaEksploziju = true;
		boom = 1;
		is = 4;
		p = 2;
		ucitajTabelu();
		repaint();
	}

	public String vratiPobede() {
		return "Pobede igraca " + igraci.get(0).getUsername() + ": " + igraci.get(0).getBrPobeda() + "\n Pobede igraca "
				+ igraci.get(1).getUsername() + ": " + igraci.get(1).getBrPobeda() + "\n";
	}
}