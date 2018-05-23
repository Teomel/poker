
public class Punteggio {
	
	static public int[] cercaPunti(Carta[] mano)
	{
		int[] mano_faccia = new int[5];
		for (int i=0; i<5; i++)
			mano_faccia[i] = mano[i].getFaccia();
		int[] mano_seme = new int[5];
		for (int i=0; i<5; i++)
			mano_seme[i] = mano[i].getSeme();
		int[] mano_faccia2 = new int[5];
		for (int i=0; i<5; i++)
		{
			if (mano_faccia[i] == 0)
				mano_faccia2[i] = 6;
			else
				mano_faccia2[i] = mano_faccia[i];
		}
		boolean colore = false, scala = false;
		int[] comb = new int[5];
		comb = cercaUguali(mano_faccia2);
		
		/*
		 * Scala reale: 19.2
		 * Scala colore: 19 (19.1)
		 * Poker: 18
		 * Full: 12
		 * Colore: 11
		 * Scala: 10 (10.1,10.2)
		 * Tris: 9
		 * Doppiacoppia: 6
		 * Coppia: 3
		 * Sequenza: 0
		 */
		
		/*
		 * 0 1 2 3 4 minima 
		 * 1 2 3 4 5 media   
		 * 0 2 3 4 5 massima 
		 */
		
		if (comb[0] == 0)
		{
			comb = cercaUguali(mano_faccia);
			
			int[] comb_seme = cercaUguali(mano_seme);
			if (comb_seme[0] == 30)
				colore = true;
			
			scala = true;
			int maggiore = comb[1];
			for (int i=maggiore-1; i>=maggiore-4; i--)
			{
				if (!esiste(mano_faccia,i) && i>=maggiore-3)
					scala = false;
				if (!(esiste(mano_faccia,0) || esiste(mano_faccia,1)) && i == maggiore-4)
					scala = false;
			}
			
			if (scala == false && colore == true)
			{
				comb[0] = 11;
				comb[1] = comb_seme[1];
			}
			
			if (scala == true)
			{
				int minore = cercaMinore(mano_faccia);
				if (maggiore == 4)
					comb[1] = 0;
				if (maggiore == 5)
				{
					if (minore == 1)
						comb[1] = 1;
					if (minore == 0)
						comb[1] = 2;
				}
				
				if (colore == true)
				    comb[0] = 19;
				else
				    comb[0] = 10;
				comb[2] = comb_seme[1];
			}
		}
		
		return comb;
	}
	
	public static int[] cercaUguali(int[] mano)
	{
		int uguali = 0, maggiore, k = 0;
		int[] ncarta = new int[mano.length-1], mano_min;
		int[] comb = new int[3];
		
		for (int i=0; i<mano.length-1; i++)
		{
			int[] ugualiDa = ugualiDa(i,mano);
			uguali += ugualiDa[0];
			ncarta[i] = ugualiDa[1];
		}
		
		if (uguali == 0)
			ncarta = mano;
		
		if (uguali == 12)
		{
			int tmp = cercaUguali(ncarta)[1];
			for (int i=0; i<mano.length-1; i++)
				ncarta[i] = tmp;
		}
		
		maggiore = cercaMaggiore(ncarta);
		
		for (int i=0; i<mano.length; i++)
		{
			if (maggiore != mano[i])
				k++;
		}
		
		if (k > 0)
		{
			mano_min = new int[k];
			k = 0;
			
			for (int i=0; i<mano.length; i++)
			{
				if (maggiore != mano[i])
				{
					mano_min[k] = mano[i];
					k++;
				}
			}
			
			comb[0] = cercaUguali(mano_min)[1];
			comb[1] = cercaUguali(mano_min)[2];
			comb[2] = cercaUguali(mano_min)[3];
		}
		
		return new int[] {uguali,maggiore,comb[0],comb[1],comb[2]};
	}
	
	private static int[] ugualiDa(int partiDa, int[] mano)
	{
		int posPrimo = partiDa,
			posCorrente = posPrimo + 1,
			uguali = 0, 
			ncarta = 0;
		
		while (posCorrente < mano.length)
		{
			if (mano[posPrimo] == mano[posCorrente])
			{
				uguali += 3;
				ncarta = mano[posPrimo];
			}
			posCorrente++;
		}
		
		return new int[]{uguali,ncarta};
	}
	
	private static int cercaMaggiore(int[] vet)
	{
		int posMaggiore = 0,
			posCorrente = 1;
		
		while (posCorrente < vet.length)
		{
			if (vet[posMaggiore] < vet[posCorrente])
				posMaggiore = posCorrente;
			posCorrente++;
		}
		
		return vet[posMaggiore];
	}
	
	private static int cercaMinore(int[] vet)
	{
		int posMinore = 0,
			posCorrente = 1;
		
		while (posCorrente < vet.length)
		{
			if (vet[posMinore] > vet[posCorrente])
				posMinore = posCorrente;
			posCorrente++;
		}
		
		return vet[posMinore];
	}
	
	public static boolean esiste(int[] vet, int num)
	{
		int i = 0;
		boolean esiste = false;
		
		while (esiste == false && i<vet.length)
		{
			if (vet[i] == num)
			  esiste = true;
			i++;
		}
			
		return esiste;
	}

}
