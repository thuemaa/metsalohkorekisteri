package metsaluokat;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Tuomas
 * @version 20 Apr 2017
 * Luokka hoitotoimenpiteelle. Tietää  houitotoimenpiteen tarvitsevat tiedot 
 * ja osaa mm. tehdä hoitotoimenpiteestä merkkijonon, ja merkkijonosta Ltoimenpide olion.
 */
public class Ltoimenpide {
	//private int lohkonumero;
	private int toimenpiteenId;
	private String toimenpide;
	private String pvm;
	private double kulut;
	
	private static int seuraavaNro = 1;
	
	/**
	 * Konstruktori uudelle tyhjälle lohktoimenpiteelle.
	 */
	public Ltoimenpide() {
		
	}
	
	/**
	 * Alustetaan tietyn lohkon toimenpide.
	 * @param lnumero Lohkon lohkonumero.
	 */
	public Ltoimenpide(int lnumero) {
		this.toimenpiteenId = lnumero;
	}
	
	/**
	 * Täyttää esimerkkitiedot toimenpiteestä.
	 * @param lnumero Lohkonumero, jolle toimenpide laitetaan.
	 */
	public void taytaLaikutus(int lnumero) {	
		double random = Math.random();// * 100 + 1;
		
		toimenpiteenId = lnumero;
		toimenpide = "Laikutus";
		pvm = "25.5.2005";
		kulut = random * 2500 + 1;
	}
	
	/**
	 * Annetaan harrastukselle idnumero ja kasvatetaan sen jälkeen seuraavaNro muuttujaa yhdellä
	 * jotta seuraavan lisättävän harrastuksen id on 1 suurempi.
	 * @return toimenpiteelle tuleva id
	 */
	public int asetaToimenpideId() {
		toimenpiteenId = seuraavaNro;
		seuraavaNro++;
		return toimenpiteenId;
	}
	
	/**
	 * Lohkotoimenpiteen tietojen tulostus.
	 * @param out Tietovirta johon tulostetaan.
	 */
	public void tulosta(PrintStream out) {
		out.println("Toimenpide: " + toimenpide + ". Pvm: " + pvm + ". Kulut: " + (String.format("%.2f",kulut)));
	}
	
	/**
	 * Tulostetaan lohkon tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}
	
	/**
	 * Erottelee tiedoston rivistä toimenpiteen tiedot ja asettaa ne toimenpiteelle
	 * @param rivi luettava rivi
	 */
	public void parse(String rivi) {
		StringBuilder sb = new StringBuilder(rivi);
		toimenpiteenId = Mjonot.erota(sb, '|', toimenpiteenId);
		//lohkonumero = Mjonot.erota(sb, '|', lohkonumero);
		toimenpide = Mjonot.erota(sb, '|', toimenpide);
		pvm = Mjonot.erota(sb, '|', pvm);
		kulut = Mjonot.erota(sb, '|', kulut);
	}
	
	@Override
	public String toString() {
		return  "" +
				toimenpiteenId + "|" +
				toimenpide + "|" +
				pvm + "|" +
				kulut;
	}
	
	/**
	 * antaa toimenpiteen Id:n
	 * @return toimenpiteen id
	 */
	public int getToimenpiteenId() {
		return toimenpiteenId;
	}	
	
	/**
	 * palauttaa toimenpiteen kulut. Käytössä vain testien takia
	 * @return toimenpiteen kulut
	 */
	public Double getToimenpiteenKulut() {
		return kulut;
	}
	
	/**
	 * palauttaa toimenpiteen attribuuttien määrän -1 (ID numero)
	 * @return attribuuttien määrä
	 */
	public int getKentat() {
		return 3;
	}
	
	/**
	 * palauttaa tietyn toimenpiteen ominaisuuden
	 * @param i ominaisuuden indeksi
	 * @return ominaisuus joka palautetaan
	 */
	public String anna(int i) {
		switch (i) {
		case 0: return toimenpide;
		case 1: return pvm;
		case 2: return Double.toString(kulut);
		default: return null;
		}
	}
	
	/**
	 * Testiohjelma lohkotoimenpiteille.
	 * @param args ei käytössä
	 * @example
	 * <pre name="test";
	 * Ltoimenpide tpide1 = new Ltoimenpide();
	 * Ltoimenpide tpide2 = new Ltoimenpide();
	 * Ltoimenpide tpide3 = new Ltoimenpide();
	 * tpide1.asetaToimenpideId();
	 * tpide2.asetaToimenpideId();
	 * tpide3.parse("3|Raivaus|15.5.2005|1500");
	 * tpide1.getToimenpiteenId() === 1;
	 * tpide2.getToimenpiteenId() === 2;
	 * tpide3.getToimenpiteenId() === 3;
	 * tpide3.getToimenpiteenKulut() ~~~ 1500; 
	 */
	public static void main(String args[]) {
		Ltoimenpide tpide = new Ltoimenpide();
		tpide.taytaLaikutus(1);
		tpide.tulosta(System.out);
	}
}
