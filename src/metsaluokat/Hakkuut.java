package metsaluokat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Tuomas
 * @version 20 Apr 2017
 * Rekisterin hakkuut. Osaa mm. lisätä ja poistaa hakkuita
 */
public class Hakkuut implements Iterable<Hakkuu> {
	private final Collection<Hakkuu> alkiot = new ArrayList<Hakkuu>();
	
	/**
	 * Täytetään myöhemmin
	 */
	public Hakkuut() {
		
	}
	
	/**
	 * Lisää hakkuun hakkuulistaan
	 * @param hakkuu lisättävä hakkuu
	 */
	public void lisaa(Hakkuu hakkuu){
		alkiot.add(hakkuu);
	}
	
	/**
	 * Poistaa hakkuun hakkuulisasta 
	 * @param hakkuu poistettava hakkuu
	 */
	public void poista(Hakkuu hakkuu) {
		alkiot.remove(hakkuu);	
	}
	
	/**
	 * Antaa kaikki tietyllä lohkolle tehdyt hakkuut
	 * @param tunnusnro haettavan lohkon numero
	 * @return lohkon hakkuut
	 */
	public List<Hakkuu> annaHakkuut(int tunnusnro) {
		List<Hakkuu> hakkuut = new ArrayList<Hakkuu>();
		for (Hakkuu hakkuu : alkiot)
			if(hakkuu.getHakkuuId() == tunnusnro) hakkuut.add(hakkuu);
		return hakkuut;
	}
	
	/**
	 * lukee metsälohkojen tiedot tiedostosta.
	 * @param polku tiedoston polku, josta luetaan
	 * @throws SailoException  ei käytössä
	 */
	public void lueTiedostosta(String polku) throws SailoException {
		String tiednimi = polku;
		
		try (Scanner fi = new Scanner(new FileInputStream(new File(tiednimi)));) {
			while (fi.hasNextLine()) {
				String rivi = fi.nextLine();
				Hakkuu hakkuu = new Hakkuu();
				hakkuu.parse(rivi);
				lisaa(hakkuu);
			}
		} catch (FileNotFoundException e) {
			throw new SailoException("tiedostoa ei löytynyt");
			//e.printStackTrace();
		}
		
	}	
	
	/**
	 * tallentaa hoitotoimenpiteet tiedostoon.
	 * @param nimi tallennustiedoston nimi
	 */
	public void tallennaTiedosto(String nimi) {
		String tiednimi = nimi+"hakkuut.dat";
		
		File fileTiedosto = new File(tiednimi);
		
		try (PrintWriter fo = new PrintWriter(new FileWriter(fileTiedosto.getCanonicalPath())) ) {
			for (Hakkuu hakkuu : this) {
				fo.println(hakkuu.toString());
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Tarkistaa onko annettu numeerinen syöte
	 * @param s syöte joka tarkistetaan
	 * @return 0 jos ei virhettä, 1 jos virhe.
	 */
	public int tarkistaHakkuusyote(String s) {
		if (!s.matches("[0-9]*.?[0-9]*.?[0-9]*")) return 1;
        return 0;
	}
	
	/**
	 * Testiohjelma hakkuille.
	 * @param args ei käytössä
	 * @example
	 * <pre name="test">
	 * Hakkuut hakkuut = new Hakkuut();
	 * Hakkuu esim1 = new Hakkuu();
	 * Hakkuu esim2 = new Hakkuu();
	 * Hakkuu esim3 = new Hakkuu();
	 * esim1.taytaHakkuu(1); esim2.taytaHakkuu(2); esim3.taytaHakkuu(1);
	 * esim1.getHakkuuId() == esim2.getHakkuuId() === false;
	 * esim1.getHakkuuId() === esim3.getHakkuuId();
	 * hakkuut.lisaa(esim3); hakkuut.lisaa(esim1); hakkuut.lisaa(esim2);
	 * Iterator iter = hakkuut.iterator();
	 * iter.next() === esim3;
	 * iter.next() === esim1;
	 * iter.next() === esim2;
	 */
	public static void main(String[] args) {
		Hakkuut hakkuut = new Hakkuut();
		Hakkuu hakkuu1 = new Hakkuu();
		hakkuu1.taytaHakkuu(1);
		Hakkuu hakkuu2 = new Hakkuu();
		hakkuu2.taytaHakkuu(2);
		Hakkuu hakkuu3 = new Hakkuu();
		hakkuu3.taytaHakkuu(2);
		
		hakkuut.lisaa(hakkuu1);
		hakkuut.lisaa(hakkuu2);
		hakkuut.lisaa(hakkuu3);
		
		System.out.println("Testataan hakkuita");
		List<Hakkuu> hakkuulista = hakkuut.annaHakkuut(2);
		for (Hakkuu hakkuu : hakkuulista) {
			System.out.print("Lohkonumero: " + hakkuu.getHakkuuId()+ " ");
			hakkuu.tulosta(System.out);
		}
	}

	@Override
	public Iterator<Hakkuu> iterator() {
		return alkiot.iterator();
	}
}
