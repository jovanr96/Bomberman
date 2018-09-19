package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.nit.ClientIgra;
import client.nit.ClientNit;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainClient {

	private JFrame frame;
	public JFrame frame2;
	private JTextField textField;
	private JTextArea textArea;
	private Socket soket;
	private int b;
	public ClientIgra ci;
	private ClientNit cn;
	public static String drugiKlijent;
	public String[] options = { "Da", "Ne" };

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	private static final int port = 2401;
	public String user;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainClient window = new MainClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame("Bomberman client");
		frame.setBounds(100, 100, 886, 800);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();

		panel.setPreferredSize(new Dimension(200, 10));
		scrollPane.setRowHeaderView(panel);
		panel.setLayout(null);

		JLabel lblAdresaServera = new JLabel("Adresa servera");
		lblAdresaServera.setBounds(12, 13, 176, 16);
		panel.add(lblAdresaServera);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		textField = new JTextField();
		textField.setBounds(12, 55, 116, 22);
		panel.add(textField);
		textField.setColumns(10);
		textField.setText("localhost");

		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(10, 40));
		scrollPane.setColumnHeaderView(panel_1);
		panel_1.setLayout(null);

		JTextField textField_1 = new JTextField();
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				// Chat
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER && soket != null) {
					textArea.append("[" + user + "] : " + textField_1.getText() + "\n");
					posaljiPoruku(textField_1.getText());
					textField_1.setText("");
				}
			}

		});
		textField_1.setBounds(0, 0, 666, 27);
		panel_1.add(textField_1);
		textField_1.setColumns(10);

		JButton btnPoveziSe = new JButton("Povezi se");
		btnPoveziSe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String adresa = textField.getText();
				if (soket == null)
					try {
						soket = new Socket(adresa, 2401);

						BufferedReader ulaz = new BufferedReader(new InputStreamReader(soket.getInputStream()));
						PrintWriter izlaz = new PrintWriter(soket.getOutputStream(), true);
						System.out.println("Zapocinjem prijem poruke...");
						String prijem = ulaz.readLine();

						System.out.println(prijem);
						if (prijem.contains("Prebukirano")) {
							JOptionPane.showMessageDialog(frame, "Server je prebukiran", "Greska",
									JOptionPane.ERROR_MESSAGE);
						} else if (prijem.contains("Dobrodosli")) {
							b = Integer.parseInt(prijem.split(",")[1]);
							textArea.append("Br igraca je : " + b + "\n");
							boolean provera = true;
							String text = "Unesite korisnicko ime";
							while (provera) {
								user = JOptionPane.showInputDialog(frame, text);
								if (!user.isEmpty()) {
									izlaz.println(user);
									String poruka = ulaz.readLine();
									if (poruka.contains("Moze")) {
										provera = false;
										napraviNit(b);
									} else if (poruka.contains("Ponovo")) {
										text = "Unesite novo korisnicko ime, prethodno je zauzeto!";
									}

								}
							}
						}
						textArea.append("Dobrodosli " + user + "\n");
					} catch (IOException ex) {
						System.out.println("Kontrolisana greska! ");
						System.out.println("Nije moguce povezati se na server. Proverite da li je server pokrenut!");
					}
				else
					textArea.append("Vec ste povezani sa serverom!\n");
			}
		});
		btnPoveziSe.setBounds(12, 116, 176, 33);
		panel.add(btnPoveziSe);

		JButton btnNewButton = new JButton("Prekini\t");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int PromptResult = JOptionPane.showOptionDialog(frame,
						"Da li ste sigurni da zelite da izadjete iz igre?", "Izlazak", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, null);
				if (PromptResult == 0)
					try {
						if (soket != null) {
							posaljiPorukuZaIzlazak();
							if (cn != null) {
								if (cn.getSoket() != null)
									cn.getSoket().close();
								if (cn.getUlaz() != null)
									cn.getUlaz().close();
							}
							soket.close();
						}
					} catch (IOException e) {
						textArea.append("Greska pri obustavljanju konekcije!\n");
					}
				frame.dispose();
			}
		});
		btnNewButton.setBounds(12, 371, 176, 46);
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Startuj igru");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PrintWriter pw;
				int brPartija;
				String text = "Koliko partija zelite da igrate?";

				try {
					pw = new PrintWriter(soket.getOutputStream(), true);
					while (true) {
						try {
							brPartija = Integer.parseInt(JOptionPane.showInputDialog(frame, text, "Start igre",
									JOptionPane.QUESTION_MESSAGE));
							if (brPartija < 1)
								throw new NumberFormatException();
							break;
						} catch (NumberFormatException ex) {
							text = "Lose uneta vrednost! Unesite ponovo.";
						}
					}
					pw.println("[START] : " + brPartija);

				} catch (IOException e) {
					System.out.println("Kontrolisana greska! Problem pri startovanju igre!");
				}

			}
		});
		btnNewButton_1.setBounds(12, 247, 176, 46);
		panel.add(btnNewButton_1);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent we) {

				int PromptResult = JOptionPane.showOptionDialog(frame,
						"Da li ste sigurni da zelite da izadjete iz igre?", "Izlazak", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, null);
				if (PromptResult == 0) {
					try {
						if (soket != null) {
							posaljiPorukuZaIzlazak();
							if (cn != null) {
								if (cn.getSoket() != null && cn.getUlaz() != null)
									cn.getSoket().close();
								cn.getUlaz().close();
							}
							soket.close();
						}
					} catch (IOException e) {
						textArea.append("Greska pri obustavljanju konekcije!\n");
					}
					frame.dispose();
				}
			}
		});
	}

	private void posaljiPoruku(String text) {
		try {
			PrintWriter izlaz = new PrintWriter(soket.getOutputStream(), true);
			System.out.println("Pocinjem slanje poruke od " + user);
			izlaz.println("CHAT;[" + user + "] : " + text);
			System.out.println("Poruka poslata serveru");

		} catch (IOException ex) {
			textArea.append("Kontrolisana greska! Problem pri slanju poruke\n");
		}
	}

	private void posaljiPorukuZaIzlazak() {
		try {
			PrintWriter izlaz = new PrintWriter(soket.getOutputStream(), true);
			izlaz.println("[PREKID]: " + user);

		} catch (IOException ex) {
			textArea.append("Kontrolisana greska! Problem pri slanju poruke\n");
		}
	}

	public void posaljiPorukuZaStart(int i) {
		try {
			PrintWriter izlaz = new PrintWriter(soket.getOutputStream(), true);
			int b = JOptionPane.showOptionDialog(frame, "Da li zelite da zapocnete igru u " + i + " parija?", "Game",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
			if (b == JOptionPane.YES_OPTION) {
				izlaz.println("START : " + i);
				pokreniIgru(i);
			} else {
				izlaz.println("CHAT;[" + user + "]: Ne zelim da zapocnem igru sada");
			}

		} catch (IOException ex) {
			textArea.append("Greska u posaljiPorukuZaStart, MainClient line 287! Problem pri slanju poruke\n");
		}
	}

	private void napraviNit(int b) {
		cn = new ClientNit(soket, this, b);
		Thread t = new Thread(cn);
		t.start();
	}

	public void pokreniIgru(int i) {
		ci = new ClientIgra(b, this, i);
		frame2 = new JFrame("Game");
		frame2.setLocationRelativeTo(frame);
		frame2.setVisible(true);
		frame2.setResizable(false);
		frame2.setBounds(100, 100, 825, 900);

		frame2.add(ci);
		frame2.pack();
		frame2.setSize(725, 800);
		frame.setVisible(false);
		frame2.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				textArea.append(ci.vratiPobede());
				frame.setVisible(true);
				frame2.dispose();
				// Obavestiti drugog igraca da je igra napustena...
				posaljiPorukuOPrekiduIgre();
			}
		});

	}

	private void posaljiPorukuOPrekiduIgre() {
		try {
			PrintWriter izlaz = new PrintWriter(soket.getOutputStream(), true);
			izlaz.println("[PREKID IGRE]: " + user);

		} catch (IOException ex) {
			textArea.append("Kontrolisana greska! Problem pri slanju poruke\n");
		}
	}

	public void dopisiNovogIgraca(String prijem) {
		textArea.append(prijem + "\n");
		try {
			PrintWriter izlaz = new PrintWriter(soket.getOutputStream(), true);
			izlaz.println("[POSTOJECI KORISNIK] Username = " + user);
		} catch (IOException ex) {
			System.out.println("Nije moguce uspostaviti konekciju sa serverom!");
		}
	}

	public void posaljiPomeraj(int b) {
		PrintWriter izlaz;
		try {
			izlaz = new PrintWriter(soket.getOutputStream(), true);
			switch (b) {
			case 8:
				izlaz.println("[POMERANJE]: Gore");
				break;
			case 2:
				izlaz.println("[POMERANJE]: Dole");
				break;
			case 4:
				izlaz.println("[POMERANJE]: Levo");
				break;
			case 6:
				izlaz.println("[POMERANJE]: Desno");
				break;
			default:
				break;
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public void pomeranjeIgraca(String poruka) {
		textArea.append("Pomeranje!\n");
		if (poruka.contains("Gore"))
			ci.pomeranje(8);
		else if (poruka.contains("Dole"))
			ci.pomeranje(2);
		else if (poruka.contains("Levo"))
			ci.pomeranje(4);
		else if (poruka.contains("Desno"))
			ci.pomeranje(6);

	}

	public void posaljiBombu(int pozX, int pozY, String prefiks) {
		try {
			PrintWriter izlaz = new PrintWriter(soket.getOutputStream(), true);
			izlaz.println(prefiks + ": " + pozX + ": " + pozY);
		} catch (IOException ex) {
			System.out.println("Greska pri povezivanju PrintWritera!");
		}
	}

	public void postaviBombu(int b2, int c) {
		ci.postaviBombu(b2, c);

	}

	public void izbaciIgraca() {
		textArea.append("Igrac " + drugiKlijent + " je napustio igru\n");
		if (b == 1) {
			b = 0;
			textArea.append("Novi broj igraca : 0\n");
		}
		ugasiIgruIPrikaziRezultat(0);

	}

	public void ugasiIgruIPrikaziRezultat(int i) {
		if (i == 1)
			textArea.append("Igrac " + drugiKlijent + " je napustio partiju\n");
		// Za i = 2 ispisuje da je pobednik prvi igrac, dok za i = 3 ispisuje da je
		// pobednik drugi igrac

		else if (i == 2) {
			if (b == 0) {
				textArea.append("Pobednik partije je " + user + "\n");
			} else {
				textArea.append("Pobednik partije je " + drugiKlijent + "\n");
			}
		} else if (i == 3) {
			if (b == 0) {
				textArea.append("Pobednik partije je " + drugiKlijent + "\n");
			} else {
				textArea.append("Pobednik partije je " + user + "\n");
			}
		}
		if (frame2 != null) {
			frame2.setVisible(false);
			frame2.dispose();
			frame.setVisible(true);
			textArea.append(ci.vratiPobede());
			ci = null;
		}

	}

	public void prekinutaKonekcija() {
		try {
			cn.join();
			ci = null;
			soket.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ex) {
			textArea.append("Greska prilikom zatvaranja soketa! prekinutaKonekcija()\n");
		}

	}
}
