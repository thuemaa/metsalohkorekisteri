package metsaluokat;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Tuomas
 * @version 20 Apr 2017
 * Rekisterin hakkuu. Tietää mm. tarvittavat tiedot ja osaa tehdä merkkijonosta olion
 * ja oliosta merkkijonon.
 */
public class Hakkuu {
	@SuppressWarnings("javadoc")
    protected int hakkuuId;
	private String pvm;
	private double kuusitukkiM;
	private double kuusitukkiH;
	private double kuusikuituM;
	private double kuusikuituH;
	private double mantytukkiM;
	private double mantytukkiH;
	private double mantykuituM;
	private double mantykuituH;
	private double koivutukkiM;
	private double koivutukkiH;
	private double koivukuituM;
	private double koivukuituH;
	private double energiapuuM;
	private double energiapuuH;
	private double kulut;
	
	private static int seuraavaNro = 1;
	
	/**
	 * Antaa hakkuun idksi aina 1 suuremman kuin edellinen hakkuu
	 * @return hakkuu id;
	 */
	public int asetaHakkuuId() {
		hakkuuId = seuraavaNro;
		seuraavaNro++;
		return hakkuuId;
	}
	
	/**
	 * konstruktori uuden tyhjän hakkuun luomiselle
	 */
	public Hakkuu() {		
	}
	
	/**
	 * Alustetaan tietylle lohkolle hakkuu.
	 * @param lnumero Lohkon numero.
	 */
	public Hakkuu(int lnumero) {
		this.hakkuuId = lnumero;
	}
	
	/**
	 * Antaa hakkuun id numeron.
	 * @return hakkuun ID
	 */
	public int getHakkuuId() {
		return hakkuuId;
	}
	
	/**
	 * Täyttää esimerkkitiedot hakkuusta.
	 * @param lnumero Lohkon numero jolle hakkuu annetaan.
	 */
	public void taytaHakkuu(int lnumero) {
		double random = Math.random();
		
		hakkuuId = lnumero;
		pvm = "20.12.2012";
		kuusitukkiM = random * 100 + 1;
		kuusitukkiH = random * 40 + 1;
		kuusikuituM = random * 100 + 1;
		kuusikuituH = random * 20 + 1;
		mantytukkiM = random * 100 + 1;
		mantytukkiH = random * 45 + 1;
		mantykuituM = random * 100 + 1;
		mantykuituH = random * 20 + 1;
		koivutukkiM = random * 100 + 1;
		koivutukkiH = random * 35 + 1;
		koivukuituM = random * 100 + 1;
		koivukuituH = random * 15 + 1;
		energiapuuM = random  * 100 + 1;
		energiapuuH = random * 15 + 1;
		kulut = random * 10000 + 1;
	}
	
	/**
	 * Tulostaa hakkuun tiedot konsoliin.
	 * @param out tietovirta johon tulostetaan.
	 */
	public void tulosta(PrintStream out) {
		//out.println("Lohkonumero: " + lohkonumero);
		out.println(pvm);
		out.println("Kuusitukki määrä: " + String.format("%.2f", kuusitukkiM));
		out.println("Kuusitukki hinta: " + String.format("%.2f", kuusitukkiH));
		out.println("Kuusikuitu määrä: " + String.format("%.2f", kuusikuituM));
		out.println("Kuusikuitu hinta: " + String.format("%.2f", kuusikuituH));
		out.println("Mäntytukki määrä: " + String.format("%.2f", mantytukkiM));
		out.println("Mäntytukki hinta: " + String.format("%.2f", mantytukkiH));
		out.println("Mäntykuitu määrä: " + String.format("%.2f", mantykuituM));
		out.println("Mäntykuitu hinta: " + String.format("%.2f", mantykuituM));
		out.println("Koivutukki määrä: " + String.format("%.2f", koivutukkiM));
		out.println("Koivutukki hinta: " + String.format("%.2f", koivutukkiH));
		out.println("Koivukuitu määrä: " + String.format("%.2f", koivukuituM));
		out.println("Koivukuitu hinta: " + String.format("%.2f", koivukuituH));
		out.println("Energiapuu määrä: " + String.format("%.2f", energiapuuM));
		out.println("Energiapuu hinta: " + String.format("%.2f", energiapuuM));
		out.println("Kulut: " + String.format("%.2f", kulut));	
		out.println("---------------------------------------------------");
	}
	
	/**
	 * Erottelee luetusta rivistä tiedot ja asettaa ne hakkuulle
	 * @param rivi luettava rivi
	 */
	public void parse(String rivi) {
		StringBuilder sb = new StringBuilder(rivi);
		hakkuuId = Mjonot.erota(sb, '|', hakkuuId);
		pvm = Mjonot.erota(sb, '|', pvm);
		kuusitukkiM = Mjonot.erota(sb, '|', kuusitukkiM);
		kuusitukkiH = Mjonot.erota(sb, '|', kuusitukkiH);
		kuusikuituM = Mjonot.erota(sb, '|', kuusikuituM);
		kuusikuituH = Mjonot.erota(sb, '|', kuusikuituH);
		mantytukkiM = Mjonot.erota(sb, '|', mantytukkiM);
		mantytukkiH = Mjonot.erota(sb, '|', mantytukkiH);
		mantykuituM = Mjonot.erota(sb, '|', mantykuituM);
		mantykuituH = Mjonot.erota(sb, '|', mantykuituH);
		koivutukkiM = Mjonot.erota(sb, '|', koivutukkiM);
		koivutukkiH = Mjonot.erota(sb, '|', koivutukkiH);
		koivukuituM = Mjonot.erota(sb, '|', koivukuituM);
		koivukuituH = Mjonot.erota(sb, '|', koivukuituH);
		energiapuuM = Mjonot.erota(sb, '|', energiapuuM);
		energiapuuH = Mjonot.erota(sb, '|', energiapuuH);
		kulut = Mjonot.erota(sb, '|', kulut);
	}
	
	@Override
	public String toString() {
		return 
				hakkuuId + "|" +
				pvm + "|" +
				kuusitukkiM + "|" +
				kuusitukkiH + "|" +
				kuusikuituM + "|" +
				kuusikuituH + "|" +
				mantytukkiM + "|" +
				mantytukkiH + "|" +
				mantykuituM + "|" +
				mantykuituH + "|" +
				koivutukkiM + "|" +
				koivutukkiH + "|" +
				koivukuituM + "|" +
				koivukuituH + "|" +
				energiapuuM + "|" +
				energiapuuH + "|" +
				kulut;
	}
	
	/**
	 * palauttaa hakkuun päivämäärän.
	 * @return hakkuun pvm.
	 */
	public String getPvm() {
		return pvm;
	}
	
	/**
	 * Muuttaa hakkuun kulut Stringiksi ja palauttaa ne
	 * @return hakkuun kulut stringinä
	 */
	public String getKulut() {
		return Double.toString(kulut);
	}
	
	/**
	 * Laskee hakkuun puiden kokonaiskertymän, laskee sen ja palauttaa sen Stringinä
	 * @return Puiden kokonaiskertymä stringinä.
	 */
	public String laskeKertyma() {
		Double kertyma = 0.0;
		kertyma += kuusitukkiM; 
		kertyma += kuusikuituM;
		kertyma += koivutukkiM;
		kertyma += koivukuituM;
		kertyma += mantykuituM;
		kertyma += mantytukkiM;
		kertyma += energiapuuM;
		return String.format("%.0f", kertyma);
	}
	
	/**
	 * Laskee hakkuun kokonaismyyntihinnan.
	 * @return myynnistä saadut tulot
	 */
	public double laskeKokonaismyynti() {
		Double myynti = 0.0;
		myynti += kuusitukkiH * kuusitukkiM; 
		myynti += kuusikuituH * kuusikuituM;
		myynti += koivutukkiH * koivutukkiM;
		myynti += koivukuituH * koivukuituM;
		myynti += mantykuituH * mantykuituM;
		myynti += mantytukkiH * mantytukkiM;
		myynti += energiapuuH * energiapuuM;
		return myynti;
	}
	
	/**
	 * Testiohjelma hakkuiden tulostukselle
	 * @param args ei käytössä
	 * @example
	 * <pre name="test">
	 * Hakkuu hakkuu1 = new Hakkuu(); hakkuu1.asetaHakkuuId();
	 * Hakkuu hakkuu2 = new Hakkuu(); hakkuu2.asetaHakkuuId();
	 * Hakkuu hakkuu3 = new Hakkuu();
	 * hakkuu3.parse("3|12.12.2012|10|40|15|15|15|35|20|14|30|30|20|10|40|7|4000");
	 * hakkuu1.getHakkuuId() === 1;
	 * hakkuu3.getHakkuuId() === 3;
	 * hakkuu2.getHakkuuId() === hakkuu1.getHakkuuId() + 1;
	 * hakkuu3.getPvm() === "12.12.2012";
	 */
	public static void main(String args[]) {
		Hakkuu hakkuu1 = new Hakkuu();
		hakkuu1.taytaHakkuu(1);
		Hakkuu hakkuu2 = new Hakkuu();
		hakkuu2.taytaHakkuu(1);
		
		hakkuu1.tulosta(System.out);
		hakkuu2.tulosta(System.out);
		
	}
	
	/**
	 * palauttaa Hakkuiden GridPane kenttien määrän
	 * @return kenttien määrä
	 */
	public int getKentat() {
		return 9;
	}
	
	/**
	 * antaa gridiin laitettavan kentän label tiedon
	 * @param k teksti joka haetaan
	 * @return labelin teksti
	 */
	public String getKenttaLabel(int k) {
		switch (k) {
		case 0: return "";
		case 1: return "Kuusitukki";
		case 2: return "Kuusikuitu";
		case 3: return "Mäntytukki";
		case 4: return "Mäntykuitu";
		case 5: return "Koivutukki";
		case 6: return "Koivukuitu";
		case 7: return "Energiapuu";
		case 8: return "Pvm ja kulut";
		default: return "väärä";
		}
	}
	
	/**
	 * Antaa String muodossa olion puiden määrä attribuutit
	 * @param i attribuutti joka haetaan
	 * @return halutun attribuutti
	 */
	public String annaMaara(int i) {
		switch (i) {
		case 0: return null;
		case 1: return Double.toString(kuusitukkiM);
		case 2: return Double.toString(kuusikuituM);
		case 3: return Double.toString(mantytukkiM);
		case 4: return Double.toString(mantykuituM);
		case 5: return Double.toString(koivutukkiM);
		case 6: return Double.toString(koivukuituM);
		case 7: return Double.toString(energiapuuM);
		case 8: return pvm;
		default: return "väärä";
		}
	}
	
	/**
     * Antaa String muodossa olion puiden hinta attribuutit
     * @param i attribuutti joka haetaan
     * @return halutun attribuutti
     */
	public String annaHinta(int i) {
		switch (i) {
		case 0: return null;
		case 1: return Double.toString(kuusitukkiH);
		case 2: return Double.toString(kuusikuituH);
		case 3: return Double.toString(mantytukkiH);
		case 4: return Double.toString(mantykuituH);
		case 5: return Double.toString(koivutukkiH);
		case 6: return Double.toString(koivukuituH);
		case 7: return Double.toString(energiapuuH);
		case 8: return Double.toString(kulut);
		default: return "väärä";
		}
	}
	
	/**
	 * palauttaa hakkuun kulut
	 * @return kulut
	 */
	public double annaKulut() {
		return kulut;
	}
}
