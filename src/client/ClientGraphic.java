package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.Client;
import enums.*;
import game.Player;

@SuppressWarnings("serial")
public class ClientGraphic extends JFrame implements WindowListener {
	private Mode mode;
	private Dimension dimension;
	private String error;
	private String serverAdress;
	private String serverPort;
	private String username;
	private String password;
	private boolean check;

	private Client client;
	private Player[] players;

	public ClientGraphic() {
		super("Eat Me If You Can !");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setBackground(Color.lightGray);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/icone.png"));
		setSize(1000, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

		init();
	}

	private void init() {
		dimension = new Dimension(600, 600);
		error = "";
		serverAdress = "";
		serverPort = "";
		username = "";
		password = "";
		check = true;
		client = new Client();
		players = null;

		switchMode(Mode.DEFAULT);
	}

	private void ok() {
		error = "";
		check = true;
	}

	private JLabel getError() {
		JLabel jlError = new JLabel(error);
		jlError.setForeground(new Color(205, 92, 92));
		return jlError;
	}

	private void switchMode(Mode mode) {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());

		this.mode = mode;

		switch (mode) {
		case DEFAULT:
			pane.add(getLogoPanel(), BorderLayout.WEST);
			pane.add(getDefaultPanel(), BorderLayout.CENTER);
			break;

		case SUBSCRIBE:
			pane.add(getLogoPanel(), BorderLayout.WEST);
			pane.add(getSubscribePanel(), BorderLayout.CENTER);
			break;

		case CONNECT:
			pane.add(getLogoPanel(), BorderLayout.WEST);
			pane.add(getConnectPanel(), BorderLayout.CENTER);
			break;

		case MENU:
			pane.add(getLogoPanel(), BorderLayout.WEST);
			pane.add(getMenuPanel(), BorderLayout.CENTER);
			break;

		case WAITING:
			pane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));	
			break;

		case INGAME:
			pane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));	
			pane.add(getIngamePanel(), BorderLayout.CENTER);
			break;

		default:
			dispose();
			break;
		}

		setContentPane(pane);
		refresh();

		if (mode == Mode.WAITING) {
			players = (Player[]) client.recieve();
			switchMode(Mode.INGAME);
		}
	}

	private void refresh() {
		System.out.println("SA REFRESH LA");
		validate();
		repaint();
	}

	public JPanel getLogoPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(new Dimension(200, 300));

		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		JLabel jlDate = new JLabel(date.format(new Date()).toString());
		jlDate.setOpaque(true);
		pane.add(jlDate);

		ImageIcon image = new ImageIcon("img/logo.png");
		JLabel jlImage = new JLabel(image);
		jlImage.setPreferredSize(new Dimension(200, 200));
		pane.add(jlImage);

		JLabel jlWelcome = new JLabel("<html>Bienvenue sur <br> Eat Me If You Can !</html>");
		pane.add(jlWelcome);

		return pane;
	}

	private JPanel getDefaultPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		JLabel jlServer = new JLabel("Entrez l'adresse du serveur : ");
		Color colorServer = (serverAdress.isEmpty()) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlServer.setForeground(colorServer);
		pane.add(jlServer);

		JTextField jtfServer = new JTextField(serverAdress);
		jtfServer.setPreferredSize(new Dimension(550, 25));
		pane.add(jtfServer);

		JLabel jlPort = new JLabel("Entrez le port du serveur : ");
		Color colorPort = (serverPort.isEmpty() || !check) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlPort.setForeground(colorPort);
		pane.add(jlPort);

		JTextField jtfPort = new JTextField(serverPort);
		jtfPort.setPreferredSize(new Dimension(550, 25));
		pane.add(jtfPort);

		JButton jbSubscribe = new JButton("S'inscrire");
		jbSubscribe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbSubscribe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverAdress = jtfServer.getText();
				serverPort = jtfPort.getText();

				if (!serverAdress.isEmpty() && !serverPort.isEmpty() && check) {
					int port = -1;

					try {
						port = Integer.parseInt(serverPort);
					} catch (NumberFormatException nfe) {
						error = "Le port est non valide.";
						switchMode(mode);
					}

					if (port < 0 || port > 65536) {
						error = "Le port est non valide.";
						switchMode(mode);
					} else if (client.setConnection(serverAdress, port)) {
						ok();
						switchMode(Mode.SUBSCRIBE);
					} else {
						error = "Connexion impossible !";
						switchMode(mode);
					}
				} else {
					error = "Champs obligatoire !";
					switchMode(mode);
				}
			}
		});
		pane.add(jbSubscribe);

		JButton jbConnect = new JButton("Se connecter");
		jbConnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverAdress = jtfServer.getText();
				serverPort = jtfPort.getText();

				if (!serverAdress.isEmpty() && !serverPort.isEmpty()) {
					int port = -1;

					try {
						port = Integer.parseInt(serverPort);
					} catch (NumberFormatException nfe) {
						error = "Le port est non valide.";
						switchMode(mode);
					}

					if (port < 0 || port > 65536) {
						error = "Le port est non valide.";
						switchMode(mode);
					} else if (client.setConnection(serverAdress, port)) {
						ok();
						switchMode(Mode.CONNECT);
					} else {
						error = "Connexion impossible !";
						switchMode(mode);
					}
				} else {
					error = "Champs obligatoire !";
					switchMode(mode);
				}
			}
		});
		pane.add(jbConnect);

		pane.add(getError());

		return pane;
	}

	private JPanel getSubscribePanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		JLabel jlUsername = new JLabel("Saisir identifiant : ");
		Color colorUsername = (username.isEmpty()) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlUsername.setForeground(colorUsername);
		pane.add(jlUsername);

		JTextField jtfUsername = new JTextField(username);
		jtfUsername.setPreferredSize(new Dimension(550, 25));
		pane.add(jtfUsername);

		Color colorPasswords = (password.isEmpty() || !check) ? new Color(205, 92, 92) : new Color(0, 0, 0);

		JLabel jlPassword = new JLabel("Saisir mot de passe : ");
		jlPassword.setForeground(colorPasswords);
		pane.add(jlPassword);

		JPasswordField jpfPassword = new JPasswordField();
		jpfPassword.setPreferredSize(new Dimension(550, 25));
		pane.add(jpfPassword);

		JLabel jlNewPassword = new JLabel("Confirmer mot de passe : ");
		jlNewPassword.setForeground(colorPasswords);
		pane.add(jlNewPassword);

		JPasswordField jpfNewPassword = new JPasswordField();
		jpfNewPassword.setPreferredSize(new Dimension(550, 25));
		pane.add(jpfNewPassword);

		JButton jbSubscribe = new JButton("S'inscrire");
		jbSubscribe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbSubscribe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username = jtfUsername.getText();
				password = new String(jpfPassword.getPassword());
				String newPassword = new String(jpfNewPassword.getPassword());
				check = (password.compareTo(newPassword) == 0);

				if (!username.isEmpty() && !password.isEmpty() && check) {
					Message msg = client.connect(Message.CREATE_ACCOUNT, username, password);
					if (msg == Message.SUCCESS) {
						ok();
						switchMode(Mode.MENU);
					} else {
						error = "Cet identifiant est déjà utilisé.";
						check = false;
						switchMode(mode);
					}
					switchMode(Mode.MENU);
				} else if (!check) {
					password = "";
					error = "Les mots de passe ne correspondent pas !";
					switchMode(mode);
				} else {
					error = "Champs obligatoire !";
					switchMode(mode);
				}
			}
		});
		pane.add(jbSubscribe);

		JButton jbCancel = new JButton("Retour");
		jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchMode(Mode.DEFAULT);
			}
		});
		pane.add(jbCancel);

		pane.add(getError());

		return pane;
	}

	private JPanel getConnectPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		JLabel jlUsername = new JLabel("Saisir identifiant : ");
		Color colorUsername = (username.isEmpty()) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlUsername.setForeground(colorUsername);
		pane.add(jlUsername);
		JTextField jtfUsername = new JTextField(username);
		jtfUsername.setPreferredSize(new Dimension(550, 25));
		pane.add(jtfUsername);

		JLabel jlPassword = new JLabel("Saisir mot de passe : ");
		Color colorPasswords = (password.isEmpty() || !check) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlPassword.setForeground(colorPasswords);
		pane.add(jlPassword);

		JPasswordField jpfPassword = new JPasswordField();
		jpfPassword.setPreferredSize(new Dimension(550, 25));
		jlPassword.setForeground(colorPasswords);
		pane.add(jpfPassword);
		
		JButton jbConnect = new JButton("Se connecter");
		jbConnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username = jtfUsername.getText();
				password = new String(jpfPassword.getPassword());

				if (!username.isEmpty() && !password.isEmpty() && check) {
					Message msg = client.connect(Message.AUTHENTICATE, username, password);
					if (msg == Message.SUCCESS) {
						ok();
						switchMode(Mode.MENU);
					} else {
						error = "Identifiants incorrects.";
						check = false;
						switchMode(mode);
					}
				} else {
					error = "Champs obligatoire !";
					switchMode(mode);
				}
			}
		});
		pane.add(jbConnect);

		JButton jbCancel = new JButton("Retour");
		jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchMode(Mode.DEFAULT);
			}
		});
		pane.add(jbCancel);

		pane.add(getError());

		return pane;
	}

	private JPanel getMenuPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		if (check) {
			JButton jbStartGame = new JButton("Lancer une partie");
			jbStartGame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStartGame.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					check = false;
					switchMode(mode);
				}
			});
			pane.add(jbStartGame);

			JButton jbDeconnect = new JButton("Se déconnecter");
			jbDeconnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbDeconnect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.send(Message.DECONNECT);
					switchMode(Mode.DEFAULT);
				}
			});
			pane.add(jbDeconnect);
		} else {
			JButton jbStart3 = new JButton("3 joueurs");
			jbStart3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.send(Message.START_3P);
					switchMode(Mode.WAITING);
				}
			});
			pane.add(jbStart3);

			JButton jbStart4 = new JButton("4 joueurs");
			jbStart4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.send(Message.START_4P);
					switchMode(mode);
				}
			});
			pane.add(jbStart4);

			JButton jbStart5 = new JButton("5 joueurs");
			jbStart5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.send(Message.START_5P);
					switchMode(mode);
				}
			});
			pane.add(jbStart5);

			JButton jbStart6 = new JButton("6 joueurs");
			jbStart6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart6.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.send(Message.START_6P);
					switchMode(mode);
				}
			});
			pane.add(jbStart6);

			JButton jbCancel = new JButton("Retour");
			jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					check = true;
					switchMode(mode);
				}
			});
			pane.add(jbCancel);
		}

		return pane;
	}

	private JPanel getWaitingPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		ImageIcon image = new ImageIcon("img/loading.gif");
		JLabel jlImage = new JLabel(image);
		pane.add(jlImage);

		return pane;
	}

	private JPanel getIngamePanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		pane.add(new JLabel("In game"));
		return pane;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (client.isAuthenticated()) {
			client.send(Message.DECONNECT);
		}

		e.getWindow().setVisible(false);
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	public static void main(String[] args) throws IOException {
		new ClientGraphic();
	}
}