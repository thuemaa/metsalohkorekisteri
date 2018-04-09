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
 * Metsälohkorekisterin luokka. Välittää mm. metodikutsuja muille luokille,
 * huolehtii päätallenustiedoston tallentamisesta ja tiedoston avauksesta.
 * Tarkistaa pvm kenttien oikeellisuuskirjoituksen.
 */
public class Metsalohkorekisteri {
	private Metsalohkot lohkot = new Metsalohkot();
	private Lohkotoimenpiteet lohkotoimenpiteet = new Lohkotoimenpiteet();
	private Hakkuut hakkuut = new Hakkuut();
	private boolean muutoksia = false;
	Lohko lohko;
	
	/**
	 * Lisää rekisteriin uuden lohkon
	 * @param lisattavaLohko lisättävä lohko
	 * @throws SailoException ei käytössä
	 */
	public void lisaa(Lohko lisattavaLohko) throws SailoException {
		lohkot.lisaa(lisattavaLohko);
		muutoksia = true;
	}
	
	/**
	 * Lisää toimenpidelistaan uuden hoitoimenpiteen
	 * @param toimenpide Lisättävä hoitotoimenpide
	 */
	public void lisaa(Ltoimenpide toimenpide){
		lohkotoimenpiteet.lisaa(toimenpide);
		muutoksia = true;
	}
	
	/**
	 * Lisää hakkuulistaan uuden hakkuun
	 * @param hakkuu Lisättävä hakkuu
	 */
	public void lisaa(Hakkuu hakkuu) {
		hakkuut.lisaa(hakkuu);
		muutoksia = true;
	}
	
	/**
	 * Haetaan kaikki lohkon toimenpiteet.
	 * @param valittuLohko Lohko jonka toimenpiteet haetaan
	 * @return Lista lohkon toimenpiteistä
	 */
	public List<Ltoimenpide> annaHoitotoimenpiteet(Lohko valittuLohko) {
		return lohkotoimenpiteet.annaToimenpiteet(valittuLohko.getLohkoId());
	}
	
	/**
	 * Haetaan kaikki lohkon hakkuut
	 * @param valittuLohko Lohko jonka hakkuut haetaan
	 * @return Lista lohkon hakkuista
	 */
	public List<Hakkuu> annaHakkuut(Lohko valittuLohko) {
		return hakkuut.annaHakkuut(valittuLohko.getLohkoId());
	}
	
	/**
	 * Palauttaa lohkojen lukumäärän.
	 * @return lohkojen lukumäärä.
	 */
	public int getLohkot() {
		return lohkot.getLkm();
	}
	
	/**
	 * Antaa rekisterissä olevan lohkon
	 * @param i haettavan lohkon indeksi
	 * @return taulukon paikassa i oleva lohko.
	 */
	public Lohko annaLohko(int i ) {
		return lohkot.anna(i);
	}	
	
	/**
	 * erottelee päätallennustiedoston polusta muiden tallennustiedostojen polut
	 * ja kutsuu luokkien omia avausmetodeita annetulla polulla.
	 * @param nimi avattavan tiedoston nimi
	 * @throws SailoException ei käytössä
	 */
	public void avaaTiedosto(String nimi) throws SailoException {
		String[] tiedostot = new String[3];
		int i = 0;
		try (Scanner fi = new Scanner(new FileInputStream(new File(nimi).getAbsolutePath()));) {
			while (fi.hasNextLine()) {
				String rivi = fi.nextLine()+".dat";
				tiedostot[i] = rivi.trim(); 
				i++;
			}
			lohkot = new Metsalohkot();
			lohkotoimenpiteet = new Lohkotoimenpiteet();
			hakkuut = new Hakkuut();
			
			lohkot.lueTiedostosta(tiedostot[0]);
			lohkotoimenpiteet.lueTiedostosta(tiedostot[1]);
			hakkuut.lueTiedostosta(tiedostot[2]);
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedostoa ei löytynyt"); 
			//e.printStackTrace();
		}
		muutoksia = false;
	}
	
	/**
	 * tallentaa päätallennustiedoston, johon tulee varsinaisten datatiedostojen polut
	 * eri riveille. Tämän jälkeen kutsuu niistä vastaavia luokkia tallentamaan tiedostoon
	 * lohkot, hoitotoimenpiteet ja hakkuut.
	 * @param nimi tiedoston nimi
	 */
	public void tallennaTiedosto(String nimi) {	

		try ( PrintWriter fo = new PrintWriter(new FileWriter(new File(nimi+".dat").getAbsolutePath())) ) {
			fo.println(nimi+"lohkot");
			fo.println(nimi+"toimenpiteet");
			fo.println(nimi+"hakkuut");
			
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
		}
		
		lohkot.tallennaTiedosto(nimi);
		lohkotoimenpiteet.tallennaTiedosto(nimi);
		hakkuut.tallennaTiedosto(nimi);
		muutoksia = false;
	}
	
	/**
	 * Testiohjelma metsälohkorekisterille
	 * @param args ei käytössä
	 * @throws SailoException ei käytössä
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * #import java.util.List;
	 * 
	 * Metsalohkorekisteri rekisteri = new Metsalohkorekisteri();
	 * Lohko esim1 = new Lohko(); esim1.taytaEsim(); esim1.asetaLohkoId();
	 * Lohko esim2 = new Lohko(); esim2.taytaEsim(); esim2.asetaLohkoId();
	 * 
	 * Ltoimenpide lpide1 = new Ltoimenpide(); lpide1.taytaLaikutus(esim1.getLohkoId());
	 * Ltoimenpide lpide2 = new Ltoimenpide(); lpide2.taytaLaikutus(esim2.getLohkoId());
	 * Ltoimenpide lpide3 = new Ltoimenpide(); lpide3.taytaLaikutus(esim2.getLohkoId());
	 * 
	 * Hakkuu hakkuu1 = new Hakkuu(); hakkuu1.taytaHakkuu(esim1.getLohkoId());
	 * Hakkuu hakkuu2 = new Hakkuu(); hakkuu2.taytaHakkuu(esim1.getLohkoId());
	 * Hakkuu hakkuu3 = new Hakkuu(); hakkuu3.taytaHakkuu(esim1.getLohkoId());
	 * 
	 * rekisteri.lisaa(esim1); rekisteri.lisaa(esim2);
	 * rekisteri.lisaa(lpide1); rekisteri.lisaa(lpide2); rekisteri.lisaa(lpide3);
	 * rekisteri.lisaa(hakkuu1); rekisteri.lisaa(hakkuu2); rekisteri.lisaa(hakkuu3);
	 * 
	 * List<Ltoimenpide> tpidelista;
	 * tpidelista = rekisteri.annaHoitotoimenpiteet(esim1);
	 * tpidelista.size() === 1;
	 * tpidelista = rekisteri.annaHoitotoimenpiteet(esim2);
	 * tpidelista.size() === 2;
	 * 
	 * List<Hakkuu> hakkuulista;
	 * hakkuulista = rekisteri.annaHakkuut(esim1);
	 * hakkuulista.size() === 3;
	 * hakkuulista = rekisteri.annaHakkuut(esim2);
	 * hakkuulista.size() === 0;
	 * </pre>
	 */
	public static void main(String[] args) throws SailoException {
		Metsalohkorekisteri metsalohkorekisteri = new Metsalohkorekisteri();
		
		/* Testaus tiedoston avaukseen.
		String esim = "C:\\Users\\Tuomas\\workspace\\Metsälohkorekisteri\\tallennus\\save.dat";
		System.out.println(esim);
		metsalohkorekisteri.avaaTiedosto(esim);
		*/
		
		Lohko esim1 = new Lohko(), esim2 = new Lohko();
		esim1.taytaEsim();
		esim1.asetaLohkoId();
		esim2.taytaEsim();
		esim2.asetaLohkoId();
		
		metsalohkorekisteri.lisaa(esim1);
		metsalohkorekisteri.lisaa(esim2);
		int id1 = esim1.getLohkoId();
		int id2 = esim2.getLohkoId();
		
		Ltoimenpide laikutus1 = new Ltoimenpide(id1); laikutus1.taytaLaikutus(id1); metsalohkorekisteri.lisaa(laikutus1);
		Ltoimenpide laikutus2 = new Ltoimenpide(id1); laikutus2.taytaLaikutus(id1); metsalohkorekisteri.lisaa(laikutus2);
		Ltoimenpide laikutus3 = new Ltoimenpide(id2); laikutus3.taytaLaikutus(id2); metsalohkorekisteri.lisaa(laikutus3);
		
		Hakkuu hakkuu1 = new Hakkuu(id1); hakkuu1.taytaHakkuu(id1); metsalohkorekisteri.lisaa(hakkuu1);
		Hakkuu hakkuu2 = new Hakkuu(id2); hakkuu2.taytaHakkuu(id2); metsalohkorekisteri.lisaa(hakkuu2);
		Hakkuu hakkuu3 = new Hakkuu(id2); hakkuu3.taytaHakkuu(id2); metsalohkorekisteri.lisaa(hakkuu3);
		
		System.out.println("----------Metsälohkorekisterin testaus---------");
		
		for (int i = 0 ; i < metsalohkorekisteri.getLohkot(); i++) {
			Lohko lohko = metsalohkorekisteri.annaLohko(i);
			System.out.println("Lohko paikassa: " + i);
			lohko.tulosta(System.out);
			
			List<Ltoimenpide> toimenpiteet = metsalohkorekisteri.annaHoitotoimenpiteet(lohko);
			for (Ltoimenpide toimenpide : toimenpiteet)
				toimenpide.tulosta(System.out);
			
			List<Hakkuu> hakkuut = metsalohkorekisteri.annaHakkuut(lohko);
			for (Hakkuu hakkuu : hakkuut)
				hakkuu.tulosta(System.out);
		}
	}
	
	/**
	 * palauttaa muutoksia muttujan boolean arvon
	 * @return muutoksia boolean
	 */
	public boolean getMuutoksia() {
		return muutoksia;
	}
	
	/**
	 * Vaihtaa muutoksia muuttujan arvoksi true.
	 * Kutsutaan metodia kun lohkon tietoja muutetaan.
	 */
	public void setMuutoksia() {
		muutoksia = true;
		
	}
	
	/**
	 * Poistaa valitun lohkon, hakee listoihin sen toimenpiteet ja hakkuut ja
	 * poistaa silmukassa kaikki kyseisen poiston hakkuut ja toimenpiteet.
	 * @param lohko poistettava lohko.
	 */
	public void poista(@SuppressWarnings("hiding") Lohko lohko) {
		lohkot.poista(lohko.getLohkoId());
		
		List<Hakkuu> poistettavatHakkuut = hakkuut.annaHakkuut(lohko.getLohkoId());
		for (Hakkuu hakkuu : poistettavatHakkuut) {
			hakkuut.poista(hakkuu);
		}
		
		List<Ltoimenpide> poistettavatToimenpiteet = lohkotoimenpiteet.annaToimenpiteet(lohko.getLohkoId());
		for (Ltoimenpide tpide : poistettavatToimenpiteet) {
			lohkotoimenpiteet.poista(tpide);
		}
		
		muutoksia = true;
	}
	
	/**
	 * Poistaa valitun toimenpiteen
	 * @param toimenpide toimenpide joka poistetaan
	 */
	public void poista(Ltoimenpide toimenpide) {
		lohkotoimenpiteet.poista(toimenpide);
		muutoksia = true;
	}
	
	/**
	 * poistaa valitun hakkuun
	 * @param hakkuu hakkuu joka poistetaan.
	 */
	public void poista(Hakkuu hakkuu) {
		hakkuut.poista(hakkuu);
		muutoksia = true;
	}
	
	/**
	 * kutsuu metsälohkot luokan annaAttribuutit metodia
	 * @param i argumentti annaAttribuutit metodille
	 * @return taulukko kaikkien lohkojen tietystä attribuutista
	 */
	public String[] annaLohkojenAtt(int i) {
		return lohkot.annaAttribuutit(i);
	}
	
	/**
	 * Kutsuu lohkotoimenpiteiden syötteentarkistusmetodia.
	 * @param text syöte joka tarkistetaan
	 * @return 0 jos ei virhettä, 1 jos virhe
	 */
	public int tarkistaTpideSyote(String text) {
		return lohkotoimenpiteet.tarkistaTpideSyote(text);
		
	}
	
	/**
	 * Kutsuu hakkuiden syötteentarkistusmetodia.
	 * @param text syöte joka tarkistetaan
	 * @return 0 jos ei virhettä, 1 jos virhe.
	 */
	public int tarkistaHakkuuSyote(String text) {
		return hakkuut.tarkistaHakkuusyote(text);
	}
	
	/**
	 * tarkistaa onko syöte päivämääräksi kelpaava, eli pp.kk.vv
	 * @param s syöte joka tarkistetaan
	 * @return 0 jos ei virhettä, 1 jos virhe.
	 */
	public int tarkistaPvmSyote(String s){
		if (s == null) return 1;
		if (!s.matches("[0-9]{4}$*+.[0-9]{2}$*+.[0-9]{2}$*")) return 1;
		return 0;
	}
}
