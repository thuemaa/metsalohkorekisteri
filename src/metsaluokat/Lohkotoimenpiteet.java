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
 * Rekisterin hoitotoimenpiteiden luokka. 
 * Osaa mm. lisätä ja poistaa hoitotoimenpiteen ja antaa kaikki lohkon hoitotoimenpiteet.
 */
public class Lohkotoimenpiteet implements Iterable<Ltoimenpide>{
	private final Collection<Ltoimenpide> alkiot = new ArrayList<Ltoimenpide>();
	
	/**
	 * täytetään myöhemmin
	 */
	public Lohkotoimenpiteet() {
		
	}
	
	/**
	 * Lisää hoitotoimenpiteen hoitoimenpidelistaan
	 * @param tpide lisättävä hoitotoimenpide
	 */
	public void lisaa(Ltoimenpide tpide) {
		alkiot.add(tpide);
	}
	
	/**
	 * Poistaa toimenpiteen toimenpidelistasta
	 * @param toimenpide poistettava toimenpide
	 */
	public void poista(Ltoimenpide toimenpide) {
		alkiot.remove(toimenpide);
	}	
	
	/**
	 * Antaa tietyn lohkon kaikki hoitotoimenpiteet
	 * @param tunnusnro haettavan lohkon lohkonron.
	 * @return lohkon toimenpiteet
	 */
	public List<Ltoimenpide> annaToimenpiteet(int tunnusnro) {
		List<Ltoimenpide> lohkonTpiteet = new ArrayList<Ltoimenpide>();
		for (Ltoimenpide tpide : alkiot)
			if(tpide.getToimenpiteenId() == tunnusnro) lohkonTpiteet.add(tpide);
		return lohkonTpiteet;
	}
	
	/**
	 * lukee metsälohkojen tiedot tiedostosta.
	 * @param polku luettavan tiedoston polku
	 * @throws SailoException  ei käytössä
	 */
	public void lueTiedostosta(String polku) throws SailoException {
		String tiednimi = polku;
		
		try (Scanner fi = new Scanner(new FileInputStream(new File(tiednimi)));) {
			while (fi.hasNextLine()) {
				String rivi = fi.nextLine();
				Ltoimenpide toimenpide = new Ltoimenpide();
				toimenpide.parse(rivi);
				lisaa(toimenpide);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * tallentaa hoitotoimenpiteet tiedostoon.
	 * @param nimi tallennettavan tiedoston nimi
	 */
	public void tallennaTiedosto(String nimi) {
		String tiednimi = nimi+"toimenpiteet.dat";
		
		File fileTiedosto = new File(tiednimi);
		
		try (PrintWriter fo = new PrintWriter(new FileWriter(fileTiedosto.getAbsolutePath())) ) {
			for (Ltoimenpide tpide : this) {
				fo.println(tpide.toString());
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Testiohjelma lohktoimenpiteille.
	 * @param args ei käyttösä
	 * @example
	 * <pre name="test">
	 * Lohkotoimenpiteet esimpiteet = new Lohkotoimenpiteet();
	 * Ltoimenpide esim1 = new Ltoimenpide();
	 * Ltoimenpide esim2 = new Ltoimenpide();
	 * Ltoimenpide esim3 = new Ltoimenpide();
	 * esim1.taytaLaikutus(2); esim2.taytaLaikutus(2);
	 * esim3.parse("3|Raivaus|15.5.2005|1500");
	 * esim1.getToimenpiteenId() === esim2.getToimenpiteenId();
	 * esim1.getToimenpiteenId() == esim3.getToimenpiteenId() === false;
	 * esimpiteet.lisaa(esim1); esimpiteet.lisaa(esim2); esimpiteet.lisaa(esim3);
	 * Iterator iter = esimpiteet.iterator();
	 * iter.next() === esim1;
	 * iter.next() === esim2;
	 * iter.next() === esim3;
	 * </pre>
	 */
	public static void main(String[] args) {
		Lohkotoimenpiteet lohkotoimenpiteet = new Lohkotoimenpiteet();
		Ltoimenpide esim1 = new Ltoimenpide();
		esim1.taytaLaikutus(1);
		Ltoimenpide esim2 = new Ltoimenpide();
		esim2.taytaLaikutus(1);
		Ltoimenpide esim3 = new Ltoimenpide();
		esim3.taytaLaikutus(2);
		
		lohkotoimenpiteet.lisaa(esim1);
		lohkotoimenpiteet.lisaa(esim2);
		lohkotoimenpiteet.lisaa(esim3);
		
		System.out.println("Testataan lohkotoimenpiteitä");
		List<Ltoimenpide> toimenpidelista = lohkotoimenpiteet.annaToimenpiteet(1);
		for (Ltoimenpide tpid : toimenpidelista) {
			System.out.print("Lohkonumero: " + tpid.getToimenpiteenId()+ " ");
			tpid.tulosta(System.out);
		}
	}
	
	/**
	 * iteraattori lohkotoimenpiteille
	 */
	@Override
	public Iterator<Ltoimenpide> iterator() {
		return alkiot.iterator();
	}

	/**
	 * Tarkistaa onko tekstikentän syöte oikea
	 * @param s tarkistettava syöte
	 * @return 1 jos väärä syöte, 0 jos oikea.
	 */
	public int tarkistaTpideSyote(String s) {
		if (!s.matches("[0-9]*.?[0-9]*.?[0-9]*")) return 1;
		return 0;
	}	
}
