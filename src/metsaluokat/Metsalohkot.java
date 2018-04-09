package metsaluokat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * @author Tuomas
 * @version 20 Apr 2017
 * Metsälohkorekisterin metsälohkojen luokka joka osaa lisätä ja poistaa uuden lohkon.
 * Huolehtii myös lohkojen tallennustiedoston tallennuksesta.
 */
public class Metsalohkot implements Iterable<Lohko> {
    
	private static final int MAX_LOHKOT = 10;
	private Lohko[] alkiot = new Lohko[MAX_LOHKOT];
    private int lkm = 0;
	
    /**
     * Lisää uuden lohkon lohkojen listaan, lisättävä vielä alkioiden lisäys jos taulukko täynnä.
     * @param lohko lisättävä lohko
     */
	public void lisaa(Lohko lohko)  {
		if(lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, alkiot.length + 5);
		alkiot[lkm] = lohko;
		lkm++;
	}
	
	/**
	 * Vähentää lohkotaulukon alkioiden määrää yhdellä, jonka jälkeen sijoittaa poistettavan
	 * alkion paikalle lohkotaulukon seuraavan indeksin silmukassa. Silmukka siirtää myös kaikkia
	 * taulukon muita loppupään lohkoja yhden taakseppäin.
	 * @param lohkoId poistettavan lohkon id
	 */
	public void poista(int lohkoId) {
	    int poistettavanIndeksi = -1;
	    
	    for (int i = 0; i < lkm; i++) {
	        if (alkiot[i].getLohkoId() == lohkoId) poistettavanIndeksi = i;
	    }
	    
	    if (poistettavanIndeksi == -1) return;
	    
		lkm--;
		for (int i = poistettavanIndeksi; i < lkm; i++) {
			alkiot[i] = alkiot[i + 1];
		}
		
	}
	
	/**
	 * palauttaa määrän montako lohkoa on taulukossa.
	 * @return lohkojen määrä
	 */
	public int getLkm() {
		return lkm;
	}
	
	/**
	 * antaa tietyssä indeksissä olevan lohkon
	 * @param i palautettavan lohkon indeksi
	 * @return palautettava lohko
	 * @throws IndexOutOfBoundsException ei käytössä
	 */
	public Lohko anna(int i) throws IndexOutOfBoundsException {
		return alkiot[i];
	}
	
	/**
	 * lukee metsälohkojen tiedot tiedostosta.
	 * @param polku tiedoston polku josta luetaan tiedot
	 */
	public void lueTiedostosta(String polku) {
		String tiednimi = polku;
		
		try (Scanner fi = new Scanner(new FileInputStream(new File(tiednimi)));) {
			while (fi.hasNextLine()) {
				String rivi = fi.nextLine();
				Lohko lohko = new Lohko();
				lohko.parse(rivi);
				lisaa(lohko);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Lohko.setLohkoId(lkm+1);
	}		
	
	/**
	 * Tallentaa metsälohkot tiedostoon.
	 * @param nimi tiedoston nimi
	 */
	public void tallennaTiedosto(String nimi) {
		String tiednimi = nimi+"lohkot.dat";
	
		File fileTiedosto = new File(tiednimi);
			
		try (PrintWriter fo = new PrintWriter(new FileWriter(fileTiedosto.getAbsolutePath())) ) {
			for (Lohko lohko : this) {
				fo.println(lohko.toString());
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * hakee kaikkien lohkojen tietty attribuutti, tekee niistä SB olion, joka erottelee ne | merkillä,
	 * poistaa samat attribuutit ja palauttaa niistä tehdyn listan, josta on poistettu saman nimisten attribuuttien
	 * useampi esiintymä.
	 * @param i haettava attribuutti anna metodin avulla
	 * @return lista kaikkien lohkojen tietystä attribuutista, joista toistot poistettu.
	 */
	public String[] annaAttribuutit(int i) {
		StringBuilder atts = new StringBuilder();
		//atts.append("-KAIKKI-|");
		for (Lohko lohko : this) {
			atts.append(lohko.anna(i)+"|");
		}
		atts.setLength(atts.length()-1);
		atts.insert(0, "-KAIKKI-|");
		String s = atts.toString();
		String[] attribuutit = s.split("\\|");
		attribuutit = new HashSet<String>(Arrays.asList(attribuutit)).toArray(new String[0]);
		return attribuutit;
	}
	
	/**
	 * Testiohjelma mestälohkoille
	 * @param args ei käytös
	 * @throws SailoException ei käytös
	 * @example
	 * <pre name="test">
	 * Metsalohkot esimlohkot = new Metsalohkot();
	 * Lohko esim1 = new Lohko();
	 * Lohko esim2 = new Lohko();
	 * Lohko esim3 = new Lohko();
	 * esim1.taytaEsim(); esim1.asetaLohkoId();
	 * esim2.parse("2|2|Jokulohko|Esimerkkitila|6|Lehto|Y2|15|7|36|4|6");
	 * esim3.parse("3|2|Jokulohko|Esimerkkitila|6|Lehto|Y2|15|7|36|4|6");
	 * esimlohkot.lisaa(esim1); esimlohkot.lisaa(esim2); esimlohkot.lisaa(esim3);
	 * esimlohkot.getLkm() === 3;
	 * esim2.anna(0) === esim3.anna(0);
	 * esim2.anna(3) == esim1.anna(3) === false;
 	 * </pre>
	 */
	public static void main(String[] args) throws SailoException {
		Metsalohkot lohkot = new Metsalohkot();
		
		Lohko esim1 = new Lohko();
		Lohko esim2 = new Lohko();
		esim1.taytaEsim();
		esim1.asetaLohkoId();
		esim2.taytaEsim();
		esim2.asetaLohkoId();
		Lohko esim3 = new Lohko();
		esim3.taytaEsim();
		esim3.asetaLohkoId();
	
		lohkot.lisaa(esim1);
		lohkot.lisaa(esim2);
		lohkot.lisaa(esim3);
		
		for (int i = 0; i < lohkot.getLkm(); i++) {
			Lohko lohko = lohkot.anna(i);
			lohko.tulosta(System.out);
		}		
	}
	
	/**
	 * Iteraattori lohkoille
	 */
	
	@Override
	public Iterator<Lohko> iterator() {
		return new LohkoIterator();
	}
	
	/**
	 * @author Tuomas
	 * @version 20 Apr 2017
	 * Lohkon iteraattorin luokka. Sisältää tarvittavia metodeja
	 */
	public class LohkoIterator implements Iterator<Lohko> {
		private int kohdalla = 0;
		
		@Override
		public boolean hasNext() {
			return kohdalla < getLkm();
		}
		
		@Override
		public Lohko next() throws NoSuchElementException {
			if (!hasNext()) throw new NoSuchElementException("Ei enempää lohkoja");
			return anna(kohdalla++);
		}
		
		@Override
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Lohkoja ei poisteta");
		}
		
	}
	
	/* EI KÄYTÖSSÄ!
	public void muutaLohko(Lohko muutettu) {
		int id = muutettu.getLohkoId();
		for (int i = 0; i < lkm; i++) {
			if (alkiot[i].getLohkoId() == id) {
				alkiot[i] = muutettu;
				return;
			}
		}
	}	
	*/
}
