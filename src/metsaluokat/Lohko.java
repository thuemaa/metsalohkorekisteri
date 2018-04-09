package metsaluokat;

import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;


/**
 * @author Tuomas
 * @version 20 Apr 2017
 * Rekisterin lohko. Tietää lohkon tiedot ja osaa tehdä lohko-olion merkkijonosta, ja toisinpäin.
 */
public class Lohko {
	@SuppressWarnings("javadoc")
    protected int lohkonumero;
	@SuppressWarnings("javadoc")
    protected int lohkoId;
	private String lohkonimi = "";
	private String tilanimi = "";
	private double pintaAla = 0;
	private String metsatyyppi = "";
	private String kehitysluokka ="";
	private int ika = 0;
	private double tilavuus = 0;
	private double kuusi = 0;
	private double manty = 0;
	private double koivu = 0;
	private double muuPuu = 0;
	
	private static int seuraavaNro = 1;
	
	/**
	 * Antaa lohkon numeroksi aina 1 suurempi kuin edellinen
	 * @return lohko numero
	 */
	public int asetaLohkoId() {
		lohkoId = seuraavaNro;
		seuraavaNro++;
		return lohkoId;
	}
	
	/**
	 * Asettaa seuraavan vapaan lohkon Id:n oikeaksi tiedoston avauksen jälkeen.  
	 * @param i seuraava vapaa ID.
	 */
	public static void setLohkoId(int i) {
		seuraavaNro = i;
	}
	
	/**
	 * TESTIOHJELMA! EI KÄYTÖSSÄ!
	 * Tulostaa esimerkkilohkon tiedot.
	 * @param out virta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println("----------LOHKO-----------");
		out.println("Lohkonumero: " + lohkonumero);
		out.println(lohkonimi);
		out.println(tilanimi);
		out.println(String.format("%.2f",pintaAla));
		out.println(metsatyyppi);
		out.println(kehitysluokka);
		out.println(ika);
		out.println(String.format("%.2f",tilavuus));
		out.println(String.format("%.2f",kuusi));
		out.println(String.format("%.2f",manty));
		out.println(String.format("%.2f",koivu));
		out.println(String.format("%.2f",muuPuu));
		
	}
	
	
	/**
	 * Apumetodi joka antaa uudelle lohkolle kenttiin satunnaiset arvot.
	 */
	public void taytaEsim() {
		double random = Math.random();// * 100 + 1;
		lohkonumero = (int)(random * 15 + 1);
		lohkonimi = "EsimLohko " + String.format("%.0f",random * 20 + 1);
		tilanimi = "Esimerkkitila";
		pintaAla = random * 50 + 1;
		metsatyyppi = "Lehto";
		kehitysluokka = "Y2";
		ika = 15;
		tilavuus = random * 100 + 1;
		kuusi = random * 50 + 1;
		manty = random * 20 + 1;
		koivu = random * 10 + 1;
		muuPuu = random * 20 + 1;
	}
	
	/**
	 * Palauttaa lohkon ID:n
	 * @return lohkon id
	 */
	public int getLohkoId() {
		return lohkoId;
	}

	/**
	 * palauttaa lohkon nimen
	 * @return lohkon nimi
	 */
	public String getNimijaTila() {
		return lohkonimi + " (" + tilanimi + ")";
	}
	
	/**
	 * metodi lohkon tietokenttien määrän palautukseksi
	 * @return tietokenttien määrä
	 */
	public int getKentat() {
		return 12;
	}
	
	/**
	 * metodi palauttaa ensimmäisen tekstikentän jota muutetaan
	 * @return ensimmäisen tekstikentän indeksi
	 */
	public int getEkaKentta() {
		return 0;
	}
	
	/**
	 * antaa gridiin laitettavan kentän label tiedon
	 * @param k label joka halutaan
	 * @return labelin sisältö Stringinä
	 */
	public String getKenttaLabel(int k) {
		switch (k) {
		case 0: return "Lohkon numero";
		case 1: return "Lohkon nimi";
		case 2: return "Tilan nimi";
		case 3: return "Pinta-ala";
		case 4: return "Metsätyyppi";
		case 5: return "Kehitysluokka";
		case 6: return "Ikä";
		case 7: return "puulajiosuudet";
		case 8: return "kuusi";
		case 9: return "Mänty";
		case 10: return "Koivu";
		case 11: return "Muut puulajit";
		default: return "väärä";
		}
		
	}
	
	/**
	 * Antaa tekstikenttään lohkon sen kohdan tiedon
	 * @param i attribuutti joka halutaan
	 * @return Attribuutin arvo stringinä
	 */
	public String anna(int i) {
		switch (i) {
		case 0: return ""+lohkonumero;
		case 1: return lohkonimi;
		case 2: return tilanimi;
		case 3: return ""+pintaAla;
		case 4: return metsatyyppi;
		case 5: return kehitysluokka;
		case 6: return ""+ika;
		case 7: return null;
		case 8: return ""+kuusi;
		case 9: return ""+manty;
		case 10: return ""+koivu;
		case 11: return ""+muuPuu;
		default: return "väärä";
		}
	}
	
	/**
	 * erottelee lohkon tiedot luetusta rivistä ja asettaa ne loholle
	 * @param rivi luettava rivi
	 */
	public void parse(String rivi) {
		StringBuilder sb = new StringBuilder(rivi);
		lohkoId = Mjonot.erota(sb, '|', lohkoId);
		lohkonumero = Mjonot.erota(sb, '|', lohkoId);
		lohkonimi = Mjonot.erota(sb, '|', lohkonimi);
		tilanimi = Mjonot.erota(sb, '|', tilanimi);
		pintaAla = Mjonot.erota(sb, '|', pintaAla);
		metsatyyppi = Mjonot.erota(sb, '|', metsatyyppi);
		kehitysluokka = Mjonot.erota(sb, '|', kehitysluokka);
		ika = Mjonot.erota(sb, '|', ika);
		kuusi = Mjonot.erota(sb, '|', kuusi);
		manty = Mjonot.erota(sb, '|', manty);
		koivu = Mjonot.erota(sb, '|', koivu);
		muuPuu = Mjonot.erota(sb, '|', muuPuu);
	}
	
	@Override
	public String toString() {
		return "" +
				lohkoId + "|" +
				lohkonumero + "|" +
				lohkonimi + "|" +
				tilanimi + "|" +
				pintaAla + "|" +
				metsatyyppi + "|" +
				kehitysluokka + "|" +
				ika + "|" +
				kuusi + "|" +
				manty + "|" +
				koivu + "|" +
				muuPuu;
	}
	
	/**
	 * Testiohjelma Lohko-luokalle
	 * @param args ei käytössä
	 * @example
	 * <pre name="test">
	 * Lohko lohko1 = new Lohko();
	 * Lohko lohko2 = new Lohko();
	 * Lohko lohko3 = new Lohko();
	 * lohko1.taytaEsim(); lohko1.asetaLohkoId();
	 * lohko2.taytaEsim(); lohko2.asetaLohkoId();
	 * lohko3.parse("3|2|EsimLohko 3|Esimerkkitila|6|Lehto|Y2|15|7|36|4|6");
	 * lohko3.anna(2) === "Esimerkkitila";
	 * lohko1.getLohkoId() === 1;
	 * lohko2.getLohkoId() === 2;
	 * lohko3.getLohkoId() === 3;
	 * lohko1.anna(1).length() > 3 === true;
	 * Integer.parseInt(lohko3.anna(0)) > 0 === true;
	 * </pre>
	 */
	public static void main(String[] args) {
		Lohko testi = new Lohko();
		//testi.tulosta(System.out);
		testi.asetaLohkoId();
		testi.taytaEsim();
		
		testi.tulosta(System.out);
		
		Lohko testi2 = new Lohko();
		testi2.asetaLohkoId();
		testi2.taytaEsim();
		
		testi2.tulosta(System.out);
	}
	
	/**
	 * Allaolevat metodit muuttavat lohkon tietoja.
	 * Tarkistaa numerollisilta attribuuteilta oikean kirjoitusasun. Jos syöte
	 * on väärä, palauttaa virheilmoituksen.
	 * @param s Uusi asettettava arvo
	 * @return ei mitään/virheilmoitus.
	 */
	
	public String setNumero(String s) {
		if (!s.matches("[0-9]*")) return "Lohkonumeron on oltava numeerinen!";
		lohkonumero = Integer.parseInt(s);
		return null;
	}

	@SuppressWarnings("javadoc")
    public String setNimi(String s) {
		lohkonimi = s;
		return null;
	}

	@SuppressWarnings("javadoc")
    public String setTila(String s) {
		tilanimi = s;
		return null;
	}

	@SuppressWarnings("javadoc")
    public String setPintaAla(String s) {
		if (!s.matches("[0-9]*.?[0-9]*")) return "Pinta-alan on oltava numeerinen!";
		pintaAla = Double.parseDouble(s.replaceAll(",", "."));
		return null;
	}

	@SuppressWarnings("javadoc")
    public String setTyyppi(String s) {
		metsatyyppi = s;
		return null;
	}

	@SuppressWarnings("javadoc")
    public String setKehitys(String s) {
		kehitysluokka = s;
		return null;
	}

	@SuppressWarnings("javadoc")
    public String setIka(String s) {
		if (!s.matches("^[0-9]*")) return "Iän on oltava positiivinen kokonaisluku!";
		ika = Integer.parseInt(s);
		return null;
	}

	@SuppressWarnings("javadoc")
    public String setKuusi(String s) {
		if (!s.matches("[0-9]*.?[0-9]*")) return "Kuusien prosenttimäärän on oltava positiivinen luku!";
		String poistaMiinus = s.replaceAll("-", "");
		kuusi = Double.parseDouble(poistaMiinus.replaceAll(",", "."));
		return null;
	}

	@SuppressWarnings("javadoc")
    public String setManty(String s) {
		if (!s.matches("^[0-9]*.?[0-9]*")) return "Mäntyjen prosenttimäärän on oltava positiivinen luku!";
		String poistaMiinus = s.replaceAll("-", "");
		manty = Double.parseDouble(poistaMiinus.replaceAll(",", "."));
		return null;
	}

	@SuppressWarnings("javadoc")
    public String setKoivu(String s) {
		if (!s.matches("^[0-9]*.?[0-9]*")) return "Koivujen prosenttimäärän on oltava positiivinen luku!";
		String poistaMiinus = s.replaceAll("-", "");
		koivu = Double.parseDouble(poistaMiinus.replaceAll(",", "."));
		return null;
	}

	@SuppressWarnings("javadoc")
    public String setMuupuu(String s) {
		if (!s.matches("^[0-9]*.?[0-9]*")) return "Muiden puiden prosenttimäärän on oltava positiivinen luku!";
		String poistaMiinus = s.replaceAll("-", "");
		muuPuu = Double.parseDouble(poistaMiinus.replaceAll(",", "."));
		return null;
	}
}
