import java.util.Random;


public class Giocatore {

	private Random random = new Random();
	private Carta[] mano = new Carta[5];
	private int[] comb;
	private int puntata;
	private boolean bluff = false;
	
	public int[] cambio()
	{
		int[] pos_cambio = new int[5];
		for (int i=0; i<5; i++)
			pos_cambio[i] = -1;
		int[] mano_faccia = new int[5];
		for (int i=0; i<5; i++)
			mano_faccia[i] = mano[i].getFaccia();
		int[] mano_seme = new int[5];
		for (int i=0; i<5; i++)
			mano_seme[i] = mano[i].getSeme();
		int[] comb_new = comb.clone();
		for (int i=0; i<5; i++)
		{
			if (comb_new[i] == 6)
				comb_new[i] = 0;
		}
		
		switch (comb[0])
		{
			case 0:
				pos_cambio[0] = 1;
				int[] comb_seme = Punteggio.cercaUguali(mano_seme);
				if (comb_seme[0] == 18)
				{
					for (int i=0; i<5; i++)
					{
						if (mano_seme[i] == comb_seme[2])
							if (mano_faccia[i] == 0 || mano_faccia[i] == 5)
								pos_cambio[0] = mano_faccia[i];
					}
				}
				break;
			case 3:
				pos_cambio[0] = comb_new[2];
				pos_cambio[1] = comb_new[3];
				pos_cambio[2] = comb_new[4];
			    break;
			case 6:
				pos_cambio[0] = comb_new[3];
				break;
			case 9:
				pos_cambio[0] = comb_new[2];
				pos_cambio[1] = comb_new[3];
		}
		
		int k = 0; boolean trovato = false;
		for (int i=0; i<5; i++)
		{
			k = 0; trovato = false;
			do 
			{
				if (pos_cambio[i] == mano_faccia[k])
				{
					pos_cambio[i] = k;
					trovato = true;
				}
				k++;
			} while (k < 5 && !trovato);
		}
		
		for (int i=0; i<5; i++)
			System.out.println("Pos_cambio: "+pos_cambio[i]);
		
		return pos_cambio;
	}
	
	public int getPuntata1()
	{
		int limite = limite();
		
		switch (comb[0])
		{
			case 0: puntata = cercaPuntata(1,2,75,1); break; 
			case 3: puntata = cercaPuntata(1,2,65,1); break;
			case 6: puntata = cercaPuntata(1,2,55,1); break;
			case 9: puntata = cercaPuntata(2,3,45,1); break;
			case 10: puntata = cercaPuntata(2,3,35,1); break;
			case 11: puntata = cercaPuntata(2,3,25,1); break;
			case 12: puntata = cercaPuntata(3,4,15,1); break;
			case 18: puntata = cercaPuntata(3,4,5,1); break;
			case 19: puntata = cercaPuntata(3,4,1,1); break;
		}
		
		if (puntata > limite)
			puntata = limite;
		
		System.out.println("Limite: "+limite);
		System.out.println("Puntata1: "+puntata);
		return puntata;
	}
	public int getPuntata2(boolean bluff)
	{
		this.bluff = bluff;
		int limite = limite();
		
		switch (comb[0])
		{
			case 0: puntata = cercaPuntata(1,2,75,2); break; 
			case 3: puntata = cercaPuntata(1,3,65,2); break;
			case 6: puntata = cercaPuntata(2,3,55,2); break;
			case 9: puntata = cercaPuntata(2,4,45,2); break;
			case 10: puntata = cercaPuntata(3,4,35,2); break;
			case 11: puntata = cercaPuntata(3,5,25,2); break;
			case 12: puntata = cercaPuntata(4,5,15,2); break;
			case 18: puntata = cercaPuntata(4,6,5,2); break;
			case 19: puntata = cercaPuntata(5,6,1,2); break;
		}
		
		if (puntata > limite)
			puntata = limite;
		
		System.out.println("Puntata2: "+puntata);
		return puntata;
	}
	
	private int limite()
	{
		int fiches_g1 = FinestraPoker.fiches_g1,
			fiches_g2 = FinestraPoker.fiches_g2;
		boolean cambio_effetuato = FinestraPoker.cambio_effetuato;
		
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
		
		return limite;
	}
	
	public void setMano(Carta[] mano) 
	{ 
		this.mano = mano;
		comb = Punteggio.cercaPunti(mano);
	}

	private int cercaPuntata(int min, int max, int per, int fase)
	{
		int rand_per = random.nextInt(100),
			puntata = 0;
		
		if (rand_per < per || bluff) //Bluff
		{
			if (fase == 1)
				puntata = random.nextInt(2)+2;
			else
				puntata = random.nextInt(4)+3;
			
			bluff = false;
			System.out.println("Bluff rand: "+puntata+"  per_rand: "+rand_per);
		}
		else
			puntata = random.nextInt(max-min+1)+min;
		
		return puntata;
	}
	
	public boolean lascia(int puntata_g)
	{
		boolean lascia = false;
		
		if (comb[0] <= 9)
		{
			switch (puntata_g - puntata)
			{
				case 1:
					lascia = per_lascia(new int[]{30,20,15,12});
					break;
				case 2:
					lascia = per_lascia(new int[]{40,25,18,13});
					break;
				case 3:
					lascia = per_lascia(new int[]{50,30,21,14});
					break;
				case 4:
					lascia = per_lascia(new int[]{60,35,23,16});
					break;
				case 5:
					lascia = per_lascia(new int[]{70,40,25,18});	
			}
		}
		
		return lascia;
	}
	
	private boolean per_lascia(int[] per_comb)
	{
		int rand_per = random.nextInt(100);
		System.out.println("Cambio: " + (FinestraPoker.cambio ? "si" : "no"));
		
		if (rand_per < per_comb[comb[0]/3] + (FinestraPoker.cambio ? 0 : 10))
			return true;
		else
			return false;
	}
	
}
