import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

//enum faccia{A,N,D,J,Q,K}; // ASSO,NOVE,DIECI,JACK,QUEEN,KING
//enum seme{P,F,Q,C}; // PICCHE,FIORI,QUADRI,CUORI

public class FinestraPoker {
	// Variabili GUI
	private JFrame frame;
	private JPanel contentPane;
	private JPanel panel_gioco, panel_info, panel_carte1, panel_carte2, panel_puntata, panel_centro, panel_tipomano1, panel_tipomano2;
	private GridBagConstraints[] lim = new GridBagConstraints[7];
	private GridBagLayout grid, grid_puntata;
	private Color tableColor = new Color(3, 140, 1);
	private JButton button_up, button_down, button_ok, button_continua, button_lascia, button_cambio, button_ok2;
	private Scritta scritta;
    // Variabili di gioco	
	private final int NUMCARTE = 24;
	private Carta[] mazzo = new Carta[NUMCARTE];
	private int cont = 0;
	private Carta[] mano_g1 = new Carta[5], mano_g2 = new Carta[5];
	private Giocatore g2;
	private int cartePescate = 0, turno = 0;
	static  int fiches_g1 = 30, fiches_g2 = 30;
	private int puntata_g1 = 0, puntata_g2 = 0, puntata_tot = 0, cambio_g1 = 0, cambio_g2 = 0;
	private int[] pos_mazzo = new int[24];
	private int cont_fiche = 0;
	private boolean fine_partita = false;
	static boolean cambio_effetuato = false, cambio = false;
	private int tipo_continua = 1;
	
	// Avvio dell'applicazione
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraPoker window = new FinestraPoker();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FinestraPoker() {
		//Creazione finestra (frame)
		frame = new JFrame();
		frame.setSize(560, 400);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Poker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = (JPanel) frame.getContentPane();
		
		// Creazione e settaggi pannelli
		generaLim();
		grid = new GridBagLayout();
		grid.columnWidths = new int[] {85,85,85,85,85};
		grid_puntata = new GridBagLayout();
		grid_puntata.columnWidths = new int[] {42,42,42,42,42,42,42};
		panel_gioco = new JPanel();
		panel_gioco.setLayout(new BorderLayout());
		panel_info = new JPanel();
		panel_info.setBorder(new MatteBorder(0, 2, 0, 0, (Color) new Color(0, 0, 0)));
		panel_info.setPreferredSize(new Dimension(120,0));
		panel_carte1 = new JPanel();
		panel_carte1.setLayout(grid);
		panel_carte1.setBackground(tableColor);
		panel_carte1.setPreferredSize(new Dimension(0,125));
		panel_carte2 = new JPanel();
		panel_carte2.setLayout(grid);
		panel_carte2.setBackground(tableColor);
		panel_carte2.setPreferredSize(new Dimension(0,125));
		panel_centro = new JPanel();
		panel_centro.setLayout(new BorderLayout());
		panel_puntata = new JPanel();
		panel_puntata.setLayout(grid_puntata);
		panel_puntata.setBackground(tableColor);
		panel_tipomano1 = new JPanel();
		panel_tipomano1.setBackground(tableColor);
		panel_tipomano2 = new JPanel();
		panel_tipomano2.setBackground(tableColor);
		button_up = new JButton("<html>&#9650;</html>");
		button_up.setFocusable(false);
		button_up.addActionListener(new ClicButtonUp());
		button_down = new JButton("<html>&#9660;</html>");
		button_down.setFocusable(false);
		button_down.addActionListener(new ClicButtonDown());
		button_ok = new JButton("OK");
		button_ok.setFocusable(false);
		button_ok.addActionListener(new ClicButtonOk());
		button_continua = new JButton("Continua");
		button_continua.setFocusable(false);
		button_continua.addActionListener(new ClicButtonContinua());
		button_lascia = new JButton("Lascia");
		button_lascia.setFocusable(false);
		button_lascia.addActionListener(new ClicButtonLascia());
		button_cambio = new JButton("Servito");
		button_cambio.setFocusable(false);
		button_cambio.addActionListener(new ClicButtonCambio());
		button_ok2 = new JButton("OK");
		button_ok2.setFocusable(false);
		button_ok2.addActionListener(new ClicButtonOk2());
		scritta = new Scritta();
		g2 = new Giocatore();
		
		// Aggiunta pannelli alla finestra (contentPane)
		panel_centro.add(panel_tipomano1, BorderLayout.NORTH);
		panel_centro.add(panel_puntata, BorderLayout.CENTER);
		panel_centro.add(panel_tipomano2, BorderLayout.SOUTH);
		panel_gioco.add(panel_carte1, BorderLayout.NORTH);
		panel_gioco.add(panel_carte2, BorderLayout.SOUTH);
		panel_gioco.add(panel_centro, BorderLayout.CENTER);
		contentPane.add(panel_gioco, BorderLayout.CENTER);
		contentPane.add(panel_info, BorderLayout.EAST);
		
		// --------Creazione degli elementi della partita--------
		stampaInfo(); // Stampa informazione partita
		creaMazzo();
	    nuovaMano();
		contentPane.updateUI();
	}
	
	/* ------------------- CLASSI PER FUNZIONAMENTO BOTTONI ------------------------ */
	
	private class ClicButtonOk2 implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			
			if (fine_partita)
			{
				fine_partita = false;
				nuovaPartita();
			}
			else
			{
				if (fiches_g1 >= 3 && fiches_g2 >= 3)
					nuovaMano();
				else
				{
					String vincitore = fiches_g2 < 3 ? "G1" : "G2";	
					panel_puntata.removeAll();
					panel_puntata.add(new Scritta("   La partita è stata vinta da " + vincitore + " "),lim[3]);
					panel_puntata.add(button_ok2,lim[2]);
					panel_puntata.updateUI();
					fine_partita = true;
				}
			}
		}
	}
	
	private class ClicButtonCambio implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			
			int[] pos_carte_cambio_g2 = g2.cambio();
			int ncarte_cambiate = 0;
			cambio_effetuato = true;
			
			if (cambio == true)
			{
			    for (int i=0; i<5; i++)
			    {
			    	if (mano_g1[i].getSelected())
			    	{
			    		//System.out.println("Pos: "+pos_mazzo[cartePescate]+"    Carte pescate: "+cartePescate);
			    		int pos = pos_mazzo[cartePescate];
						mano_g1[i] = new Carta(mazzo[pos].getFaccia(),mazzo[pos].getSeme());
						cartePescate++;
						ncarte_cambiate++;
			    	}
			    }
			    
			    panel_carte2.removeAll();
			    stampaMano(mano_g1,panel_carte2,true);
			    cambio_g1 = ncarte_cambiate; 
			}
			
			ncarte_cambiate = 0;
			
			for (int i=0; i<pos_carte_cambio_g2.length; i++)
			{
				if (pos_carte_cambio_g2[i] != -1)
				{
//					System.out.println("Pos: "+pos_mazzo[cartePescate]+"    Carte pescate: "+cartePescate);
		    		int pos = pos_mazzo[cartePescate];
					mano_g2[pos_carte_cambio_g2[i]] = new Carta(mazzo[pos].getFaccia(),mazzo[pos].getSeme());
					cartePescate++;
					ncarte_cambiate++;
				}
			}
			
			g2.setMano(mano_g2);
			System.out.println("G1: "+Punteggio.cercaPunti(mano_g1)[0]+"  "+Punteggio.cercaPunti(mano_g1)[1]+"  "+Punteggio.cercaPunti(mano_g1)[2]+"  "+Punteggio.cercaPunti(mano_g1)[3]+"  "+Punteggio.cercaPunti(mano_g1)[4]);
			System.out.println("G2: "+Punteggio.cercaPunti(mano_g2)[0]+"  "+Punteggio.cercaPunti(mano_g2)[1]+"  "+Punteggio.cercaPunti(mano_g2)[2]+"  "+Punteggio.cercaPunti(mano_g2)[3]+"  "+Punteggio.cercaPunti(mano_g2)[4]);
			panel_carte1.removeAll();
		    stampaMano(mano_g2,panel_carte1,false);
		    cambio_g2 = ncarte_cambiate; 
			stampaInfo();
			
			boolean bluff = false, lascia = false;
			if (Punteggio.cercaPunti(mano_g2)[0] == 0)
			{
				Random random = new Random();
				int rand_per = random.nextInt(100);
				
				if (rand_per > 75) //Bluff
				{
					lasciaG2();
					lascia = true;
				}
				else
					bluff = true;
			}
			
			if (!lascia)
			{
				if (turno%2 == 0)
				{
				    puntata_g2 = g2.getPuntata2(bluff);
				    
				    if (g2.lascia(puntata_g1))
				    	lasciaG2();
				    	
				    panel_tipomano1.add(new Scritta("Punto "+ puntata_g2));
				    stampaInfo();
				}
				domandaLascia();
				panel_puntata.updateUI();	
			}
		}
	}
	
	private class ClicCarta implements MouseListener
	{
		public void mousePressed(MouseEvent e) {
			
			cambio = false;
			
			for (int i=0; i<5; i++)
			{
				if (mano_g1[i].getSelected() == true)
					cambio = true;
			}
			
			if (cambio == false)
				button_cambio.setText("Servito");
			if (cambio == true)
				button_cambio.setText("Cambia");
			
			panel_puntata.updateUI();
		}	
		
		public void mouseClicked(MouseEvent arg0) { }
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	}
	
	
	
	private class ClicButtonContinua implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			    
			    panel_tipomano1.removeAll();
			
			    if (tipo_continua == 1)
				{
					panel_puntata.removeAll();
					stampaButtonPuntata();
					if (turno%2 == 0)
					{
						cont_fiche = puntata_g2;
						
						if (cont_fiche < 5 && cont_fiche >= 0)
						{
							for (int i=0; i<cont_fiche; i++)
								panel_puntata.add(new Fiche(), lim[i+3]);
						}
						
						if (cont_fiche >= 5)
						{
							scritta.setText(""+cont_fiche);
							panel_puntata.add(scritta, lim[3]);
							panel_puntata.add(new Fiche(), lim[4]);
						}
					}
					panel_puntata.updateUI();
				}
				else
				{
					fiches_g1 -= puntata_g2-puntata_g1;
					puntata_tot += puntata_g2-puntata_g1;
					puntata_g1 = puntata_g2;
					
					if (cambio_effetuato == false)
					{
					    stampaFichesPuntata(0);
					    cambio();
					}
					else
					{
					    stampaFichesPuntata(1);
					    verificaVincitore();
					}
					tipo_continua = 1;
					stampaInfo();
				}
		}
	}
	
	private class ClicButtonLascia implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			fiches_g2 += puntata_tot;
			puntata_tot = 0;
			nuovaMano();
		}
	}
	
	private class ClicButtonUp implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			
			int limite = 6;
			
			if (fiches_g1 <= 6 || fiches_g2 <= 6)
			{
				if (fiches_g1 > fiches_g2)
				{
					if (cambio_effetuato)
						limite = fiches_g2;
					else
						limite = fiches_g2 - 1;
				}
				else
				{
					if (cambio_effetuato)
						limite = fiches_g1;
					else
						limite = fiches_g1 - 1;
				}
			}
			
			if (cont_fiche < limite)
			{
				cont_fiche++;
				System.out.println("Cont_fiche: "+cont_fiche);
				
				if (cont_fiche < 5 && cont_fiche >= 0)
					panel_puntata.add(new Fiche(), lim[cont_fiche+2]);
				
				if (cont_fiche >= 5)
				{
					panel_puntata.removeAll();
					stampaButtonPuntata();
					scritta.setText(""+cont_fiche);
					panel_puntata.add(scritta,lim[3]);
					panel_puntata.add(new Fiche(), lim[4]);
				}
				
				panel_puntata.updateUI();
			}
		}
	}
	
	private class ClicButtonDown implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			
			if (cont_fiche > ((turno%2 == 0) ? puntata_g2 : 0))
			{
				cont_fiche--;
				
				if (cont_fiche < 5 && cont_fiche >= 0)
				{
					panel_puntata.removeAll();
					stampaButtonPuntata();
					for (int i=0; i<cont_fiche; i++)
						panel_puntata.add(new Fiche(), lim[i+3]);
				}
				
				if (cont_fiche >= 5)
				{
					panel_puntata.removeAll();
					stampaButtonPuntata();
					scritta.setText(""+cont_fiche);
					panel_puntata.add(scritta, lim[3]);
					panel_puntata.add(new Fiche(), lim[4]);
				}
				
				panel_puntata.updateUI();
			}
			
		}
		
	}
	
	private class ClicButtonOk implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			
			if (cont_fiche != 0)
			{
				fiches_g1 -= cont_fiche;
				puntata_tot += cont_fiche;
				puntata_g1 = cont_fiche;
				
				if (turno%2 != 0)
				{
					if (cambio_effetuato == false)
						puntata_g2 = g2.getPuntata1();
					else
						puntata_g2 = g2.getPuntata2(false);
				}
				
				if (g2.lascia(puntata_g1))
				{
					puntata_tot += puntata_g2;
					fiches_g2 -= puntata_g2;
					cont_fiche = 0;
					lasciaG2();
				}
				else
				{
					if (puntata_g1 - puntata_g2 > 0)
					   puntata_g2 = puntata_g1;
					
					puntata_tot += puntata_g2;
					fiches_g2 -= puntata_g2;
					cont_fiche = 0;
					
					if (puntata_g2 == puntata_g1)
					{
						if (cambio_effetuato == false)
						{
						    stampaFichesPuntata(0);
						    cambio();
						}
						else
						{
						    stampaFichesPuntata(1);
						    verificaVincitore();
						}
					}
					else
					{
						panel_tipomano1.add(new Scritta("Rialzo di "+(puntata_g2-puntata_g1)));
						tipo_continua = 2;
						domandaLascia();
					}
				}
				
				stampaInfo();	
			}
		}
	}
	
	/* --------------- METODI PRINCIPALI DI FUNZIONAMENTO DI GIOCO --------------------------- */
	
	private void nuovaMano()
	{
		turno++;
		raccogliemischia();
		daiMano();
		panel_carte1.removeAll();
		panel_carte2.removeAll();
		panel_tipomano1.removeAll();
		panel_tipomano2.removeAll();
		//mano_g1[0] = new Carta(0,0); mano_g1[1] = new Carta(0,1); mano_g1[2] = new Carta(3,0); mano_g1[3] = new Carta(3,2); mano_g1[4] = new Carta(3,2);
		//mano_g2[0] = new Carta(1,2); mano_g2[1] = new Carta(3,2); mano_g2[2] = new Carta(0,2); mano_g2[3] = new Carta(2,2); mano_g2[4] = new Carta(5,1);
		g2.setMano(mano_g2);
		stampaMano(mano_g2,panel_carte1,false);
		stampaMano(mano_g1,panel_carte2,true);
		System.out.println("G1: "+Punteggio.cercaPunti(mano_g1)[0]+"  "+Punteggio.cercaPunti(mano_g1)[1]+"  "+Punteggio.cercaPunti(mano_g1)[2]+"  "+Punteggio.cercaPunti(mano_g1)[3]+"  "+Punteggio.cercaPunti(mano_g1)[4]);
		System.out.println("G2: "+Punteggio.cercaPunti(mano_g2)[0]+"  "+Punteggio.cercaPunti(mano_g2)[1]+"  "+Punteggio.cercaPunti(mano_g2)[2]+"  "+Punteggio.cercaPunti(mano_g2)[3]+"  "+Punteggio.cercaPunti(mano_g2)[4]);
		puntataIniziale();
		cambio_effetuato = false;
		Carta.bloccoSelect = true;
		cambio_g1 = 0; cambio_g2 = 0;
		if (turno%2 == 0)
		{
		    puntata_g2 = g2.getPuntata1();
		    panel_tipomano1.add(new Scritta("Punto "+ puntata_g2));
		    stampaInfo();
		}
		domandaLascia();
		stampaInfo();
		panel_gioco.updateUI();
	}
	
	private void nuovaPartita()
	{
		turno = 0; fiches_g1 = 30; fiches_g2 = 30;
		nuovaMano();
	}
	
	private void verificaVincitore()
	{
		int[] comb_g1 = Punteggio.cercaPunti(mano_g1),
			  comb_g2 = Punteggio.cercaPunti(mano_g2);
		int vincitore = 1;
		boolean pareggio = false;
		
		System.out.println("Comb_g1: "+comb_g1[0]+"  "+comb_g1[1]+"  "+comb_g1[2]);
		System.out.println("Comb_g2: "+comb_g2[0]+"  "+comb_g2[1]+"  "+comb_g2[2]);
		
		if (comb_g1[0] > comb_g2[0])
			vincitore = 1;
		else if (comb_g1[0] == comb_g2[0])
		{
			if (comb_g1[1] > comb_g2[1])
				vincitore = 1;
			else if (comb_g1[1] == comb_g2[1])
			{
				if (comb_g1[2] > comb_g2[2])
					vincitore = 1;
				else if (comb_g1[2] == comb_g2[2])
				{
					if (comb_g1[3] > comb_g2[3])
						vincitore = 1;
					else if (comb_g1[3] == comb_g2[3])
					{
						if (comb_g1[4] > comb_g2[4])
							vincitore = 1;
						else if (comb_g1[4] == comb_g2[4])
							pareggio = true;
						else
							vincitore = 2;
					}
					else
						vincitore = 2;
				}
				else
					vincitore = 2;
			}
			else
				vincitore = 2;
		}
		else
			vincitore = 2;
		
		panel_tipomano1.add(new Scritta(tipoMano(comb_g2)));
		panel_tipomano2.add(new Scritta(tipoMano(comb_g1)));
		panel_puntata.add(button_ok2,lim[0]);
		
		if (pareggio == false)
		{
			panel_puntata.add(new Scritta("  Ha vinto G"+vincitore+" "),lim[1]);
			if (vincitore == 1)
				fiches_g1 += puntata_tot;
			else
				fiches_g2 += puntata_tot;
		}
		else
		{
			panel_puntata.add(new Scritta("  Pareggio "),lim[1]);
			fiches_g1 += puntata_tot/2;
			fiches_g2 += puntata_tot/2;
		}
		
		puntata_tot = 0;
		panel_carte1.removeAll();
		stampaMano(mano_g2,panel_carte1,true);
		
		panel_puntata.updateUI();
		panel_carte1.updateUI();
		
	}
	
	private String tipoMano(int[] comb)
	{
		String str = "";
		int tipo = 0;
		
		switch (comb[0])
		{
			case 0: str = "Sequenza"; tipo = 0; break;
			case 3: str = "Coppia di "; tipo = 1; break;
			case 6: str = "Doppiacoppia di "; tipo = 1;	break;
			case 9:	str = "Tris di "; tipo = 1;	break;
			case 10: str = "Scala "; tipo = 2; break;
			case 11: str = "Colore di "; tipo = 3; break;
			case 12: str = "Full di "; tipo = 1; break;
			case 18: str = "Poker di "; tipo = 1; break;
			case 19:
				if (comb[1] == 2)
					str = "Scala reale";
				else
				{
					str = "Scala di colore ";
					tipo = 2;
				}
		}
		
		if (tipo == 1)
		{
			switch (comb[1])
			{
				case 1: str += "nove"; break;
				case 2:	str += "dieci";	break;
				case 3:	str += "fanti";	break;
				case 4:	str += "donne"; break;
				case 5:	str += "re"; break;
				case 6:	str += "assi";	
			}
		}
		
		if (tipo == 2)
		{
			switch (comb[1])
			{
				case 0:	str += "minima"; break;
				case 1:	str += "media";	break;
				case 2:	str += "massima";
			}
		}
		
		if (tipo == 3)
		{
			switch (comb[1])
			{
				case 0:	str += "cuori";	break;
				case 1:	str += "quadri"; break;
				case 2:	str += "fiori";	break;
				case 3:	str += "picche";					
			}
		}
		
		return str;
	}
	
	private void cambio()
	{
		panel_puntata.add(button_cambio, lim[0]);
		button_cambio.setText("Servito");
		Carta.bloccoSelect = false;
		
		for (int i=0; i<5; i++)
			mano_g1[i].addMouseListener(new ClicCarta());
	}
	
	private void domandaLascia()
	{
		panel_puntata.removeAll();
		
		scritta.setText("Continui o lasci ? ");
		panel_puntata.add(scritta, lim[2]);
		panel_puntata.add(button_continua);
		panel_puntata.add(button_lascia);
		
		panel_puntata.updateUI();
	}
	
	private void lasciaG2()
	{
		fiches_g1 += puntata_tot;
		puntata_tot = 0;
		panel_puntata.removeAll();
		panel_puntata.add(new Scritta("G2 ha lasciato la mano"),lim[4]);
		panel_puntata.add(button_ok2,lim[2]);
		panel_puntata.updateUI();
	}
	
	private void puntataIniziale()
	{
		fiches_g1--;
		puntata_g1 = 1;
		fiches_g2--;
		puntata_g2 = 1;
		puntata_tot = 2;
		stampaInfo();
	}
	
	private void stampaButtonPuntata()
	{
		panel_puntata.add(button_ok, lim[0]);
		panel_puntata.add(button_down, lim[1]);
		panel_puntata.add(button_up, lim[2]);
		panel_puntata.updateUI();
	}
	
	private void stampaFichesPuntata(int fiche_meno)
	{
		panel_puntata.removeAll();;
		
		if (puntata_tot < 7-fiche_meno && puntata_tot >= 0)
		{
			for (int i=0; i<puntata_tot; i++)
				panel_puntata.add(new Fiche(), lim[i+6/puntata_tot+fiche_meno]);
		}
		
		if (puntata_tot >= 7-fiche_meno)
		{
			scritta.setText(""+puntata_tot);
			panel_puntata.add(scritta, lim[2]);
			panel_puntata.add(new Fiche(), lim[3]);
		}
		
		panel_puntata.updateUI();
	}
	
	private void stampaInfo()
	{
		panel_info.removeAll();
		String info = "<html>"
				 +"<br />INFO DI GIOCO<br /><br />"
	             +"Fiches G1: "+fiches_g1+"<br />"
	             +"Fiches G2: "+fiches_g2+"<br /><br />"
	             +"Puntata G1: "+puntata_g1+"<br />"
	             +"Puntata G2: "+puntata_g2+"<br /><br />"
	             +"Posta in gioco: "+puntata_tot+"<br /><br />"
	             +"Cambio G1: "+cambio_g1+"<br />"
	             +"Cambio G2: "+cambio_g2+"<br /><br />"
	             +"Turno "+turno+" apre "+ (turno%2 == 0 ? "G2" : "G1") +"</html>";
		panel_info.add(new JLabel(info));
		panel_info.updateUI();
	}
	
	private void raccogliemischia() {
		cartePescate = 0;
		pos_mazzo = generaRand(23);
	}
	
	private void generaLim()
	{
		for (int i=0; i<lim.length; i++)
		{
			lim[i] = new GridBagConstraints();
			lim[i].gridx = i;
			lim[i].gridy = 0;
		}
	}
	
	private void stampaMano(Carta[] mano, JPanel panel, boolean visible)
	{				
		for (int i=0; i<mano.length; i++)
		{
			if (visible == true)
				panel.add(mano[i].getLabel(),lim[i]);
			else
			{
				JLabel label_retro = new JLabel();
				ImageIcon icon = new ImageIcon(FinestraPoker.class.getResource("Images/retro.png"));
				icon = new ImageIcon(icon.getImage().getScaledInstance(82, 115, java.awt.Image.SCALE_SMOOTH));
				label_retro.setIcon(icon);
				label_retro.setBorder(new LineBorder(Color.BLACK,1,true));
				panel.add(label_retro,lim[i]);
			}
		}
		
		panel.updateUI();
	}
	
	private void daiMano()
	{
		int i=0;
		
		while (i<5 && cartePescate<NUMCARTE)
		{
			int pos = pos_mazzo[cartePescate];
			mano_g1[i] = new Carta(mazzo[pos].getFaccia(),mazzo[pos].getSeme());
			i++;
			cartePescate++;
		}
		i = 0;
		while (i<5 && cartePescate<NUMCARTE)
		{
			int pos = pos_mazzo[cartePescate];
			mano_g2[i] = new Carta(mazzo[pos].getFaccia(),mazzo[pos].getSeme());
			i++;
			cartePescate++;
		}
		
		contentPane.updateUI();
	}
	
	private void creaMazzo()
	{
		for (int i=0; i<NUMCARTE; i++)
			mazzo[i] = new Carta(i%6,i/6);
	}
	
	@SuppressWarnings("unused")
	private void sfogliaMazzo()
	{
		if (cont < NUMCARTE)
		{
			System.out.println(cont+" - "+mazzo[cont].getFaccia()+","+mazzo[cont].getSeme());
			if (cont>=1)
			  contentPane.remove(mazzo[cont-1].getLabel());
			contentPane.add(mazzo[cont].getLabel(), BorderLayout.CENTER);
			cont++;
			contentPane.updateUI();
		}
		else
		{
			contentPane.remove(mazzo[cont-1].getLabel());
			cont = 0;
		}
	}
	
	private int[] generaRand(int n)
	{
		Random random = new Random();
		int nrand = 0, i = 0;
		int[] vet = new int[n];
		
		for (i=0; i<n; i++)
		{
			do 
			  nrand = random.nextInt(n+1); 
			while (Punteggio.esiste(vet,nrand));
			  vet[i] = nrand;
		}
		
		for (i=0; i<n; i++)
		{
			if (vet[i] == 16)
			  vet[i] = 0;
		}
		
		return vet;
	}
	
}