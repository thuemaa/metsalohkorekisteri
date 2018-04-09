package metsalohkorekisteri;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import metsaluokat.Lohko;
import metsaluokat.Metsalohkorekisteri;

/**
 * @author Tuomas
 * @version 14.2.2017
 * Metsälohkorekisterin hakuikkunan tapahtumien hoitaminen.
 */
public class MetsaHakuController {
	@FXML private javafx.scene.control.Button peruuta;
	@FXML private StringGrid<Lohko> tableHakutulokset;
	@FXML private TextField lohkoT, paMin, ikaMin, tilavuusMin, kuusiMin, mantyMin, koivuMin, muuMin, paMax, ikaMax,
							tilavuusMax, kuusiMax, mantyMax, koivuMax, muuMax;
    @FXML private ComboBoxChooser<?> tilaChooser;
    @FXML private ComboBoxChooser<?> tyyppiChooser;
    @FXML private ComboBoxChooser<?> luokkaChooser;


    /**
     * handleri lohkon avaukselle. Hakee listasta valitun lohkon aktiiviseksi pääikkunaan,
     * sitten sulkee hakuikkunan.
     */
	@FXML
	void handleAvaaLohko() {
		if (tableHakutulokset.getObject() == null) return;
		ctrl.hae(tableHakutulokset.getObject().getLohkoId());
		Stage hakuikkuna = (Stage) peruuta.getScene().getWindow();
	    hakuikkuna.close();
	 }
	
	/**
	 * Handleri haku painikkeelle
	 */
	 @FXML
	 void handleHae() {
	    haku();
	 }
	 
	 /**
	  * Handleri hakuikkunan sulkemiselle
	  */
	 @FXML
	 void handlePeruuta() {
		Stage hakuikkuna = (Stage) peruuta.getScene().getWindow();
	    hakuikkuna.close();
	 }
	 
	 
	 //-------------------------------------------------------------------------------------
	 //Ei varsinaista käyttöliittymäkoodia
	 private Metsalohkorekisteri metsalohkorekisteri;
	 private MetsaController ctrl;
	 
	 /**
	  * Alustustoimenpiteet. Asettaa StringGridin otsiko, asetukset ja placeholder tekstin.
	  * hakee ComboBoxChoosereihin oikeat tekstit.
	  */
	 public void alusta() {
		 String[] lohkoOtsikot = { "Lohkon numero", "Lohkon nimi", "Tilan nimi", "Pinta-ala", "Metsätyyppi", "Kehitysluokka",
				 				"Ikä", "Kuusi", "Mänty", "Koivu", "Muut puulajit" };
		 
		 tableHakutulokset.initTable(lohkoOtsikot);
		 //tableHakutulokset.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		 tableHakutulokset.setEditable(false);
		 tableHakutulokset.setPlaceholder(new Label("Ei hakutuloksia"));
		 
		 
		 String tilat[] = metsalohkorekisteri.annaLohkojenAtt(2);
		 tilaChooser.setRivit(tilat);
		 
		 String tyypit[] = metsalohkorekisteri.annaLohkojenAtt(4);
		 tyyppiChooser.setRivit(tyypit);
		 
		 String kehitysluokat[] = metsalohkorekisteri.annaLohkojenAtt(5);
		 luokkaChooser.setRivit(kehitysluokat);
	 }
	 
	 /**
	  * Tyhjentää StringGridin tulokset, sen jälkeen käy silmukassa läpi kaikki rekisterissä olevat lohkot.
	  * Asettaa hakuehtomuuttujien oletusarvoiksi 0 ja 9999999, jotta jos yhden hakukriteerin kenttä on tyhjänä, on tarkoittaa se
	  * samaa kuin sitä ei olisi. Jos tekstikentässä on tekstiä, otetaan se hakuehtomuuttujien arvoksi.
	  * Tämän jälkeen käydään ehtolauseilla läpi lohkon attribuutit, ja jos se osuu minimi- ja maksimiehtojen väliin
	  * asetetaan boolean taulukon yhden alkion arvoksi true. Lopuksi katsotaan onko kaikki bools taulukon muuttujat true;
	  * jos on, kutsutaan naytaLohko aliohjelmaa näyttämään StringGridissä sen lohkon.
	  */
	 private void haku() { 
		 tableHakutulokset.clear(); 
		 
		 for (int i = 0; i < metsalohkorekisteri.getLohkot(); i++) {
			 Lohko lohko = metsalohkorekisteri.annaLohko(i);
			 boolean[] bools = new boolean[10];
			 
			 //Hakee muuttujiin lohkon attribuuttien arvot
			 double pa = Double.parseDouble(lohko.anna(3));
			 double ika = Double.parseDouble(lohko.anna(6));
			 double kuusi = Double.parseDouble(lohko.anna(8));
			 double manty = Double.parseDouble(lohko.anna(9));
			 double koivu = Double.parseDouble(lohko.anna(10));
			 double muuPuu = Double.parseDouble(lohko.anna(11));
			 
			 //Asettaa mini- ja maksimihakuehtojen muuttujien arvot pieniksi ja suuriksi. Jos hakuehtojen textFieldissä on
			 //jotain, asettaa hakuehtomuuttujien arvot tekstikenttäsyötteenä annetuksi luvuksi.
			 double paMinimi = 0;
			 if (!paMin.getText().isEmpty()) paMinimi = Double.parseDouble(paMin.getText());		 
			 double paMaksimi = 9999999;
			 if (!paMax.getText().isEmpty()) paMaksimi = Double.parseDouble(paMax.getText());
			 
			 double ikaMinimi = 0;
			 if (!ikaMin.getText().isEmpty()) ikaMinimi = Double.parseDouble(ikaMin.getText());		 
			 double ikaMaksimi = 9999999;
			 if (!ikaMax.getText().isEmpty()) ikaMaksimi = Double.parseDouble(ikaMax.getText());
			 
			 double kuusiMinimi = 0;
			 if (!kuusiMin.getText().isEmpty()) kuusiMinimi = Double.parseDouble(kuusiMin.getText());	 
			 double kuusiMaksimi = 9999999;
			 if (!kuusiMax.getText().isEmpty()) kuusiMaksimi = Double.parseDouble(kuusiMax.getText());
			 
			 double mantyMinimi = 0;
			 if (!mantyMin.getText().isEmpty()) mantyMinimi = Double.parseDouble(mantyMin.getText()); 
			 double mantyMaksimi = 9999999;
			 if (!mantyMax.getText().isEmpty()) mantyMaksimi = Double.parseDouble(mantyMax.getText());
			 
			 double koivuMinimi = 0;
			 if (!koivuMin.getText().isEmpty()) koivuMinimi = Double.parseDouble(koivuMin.getText());		 
			 double koivuMaksimi = 9999999;
			 if (!koivuMax.getText().isEmpty()) koivuMaksimi = Double.parseDouble(koivuMax.getText());
			 
			 double muuMinimi = 0;
			 if (!muuMin.getText().isEmpty()) muuMinimi = Double.parseDouble(muuMin.getText()); 
			 double muuMaksimi = 9999999;
			 if (!muuMax.getText().isEmpty()) muuMaksimi = Double.parseDouble(muuMax.getText());
			 
			 //Tarkistaa jokaisen lohkon attribuutit, ja jos attribuutti osuu hakuvälille, antaa boolean arvoksi true.
			 if (lohkoT.getText().equals(lohko.anna(0)) || lohkoT.getText().trim().isEmpty()) bools[0] = true;
			 if (paMinimi <= pa && paMaksimi >= pa) bools[1] = true;
			 if (ikaMinimi <= ika && ikaMaksimi >= ika) bools[2] = true;
			 if (kuusiMinimi <= kuusi && kuusiMaksimi >= kuusi) bools[3] = true;
			 if (mantyMinimi <= manty && mantyMaksimi >= manty) bools[4] = true;
			 if (koivuMinimi <= koivu && koivuMaksimi >= koivu) bools[5] = true;
			 if (muuMinimi <= muuPuu && muuMaksimi >= muuPuu) bools[6] = true;
			 if (tilaChooser.getSelectedText().equals("-KAIKKI-") || tilaChooser.getSelectedText().equals(lohko.anna(2))) bools[7] = true;
			 if (tyyppiChooser.getSelectedText().equals("-KAIKKI-") || tyyppiChooser.getSelectedText().equals(lohko.anna(4))) bools[8] = true;
			 if (luokkaChooser.getSelectedText().equals("-KAIKKI-") || luokkaChooser.getSelectedText().equals(lohko.anna(5))) bools[9] = true;
			 
			 //Boolean kaikkiTruen arvo muutetaan falseksi, jos joku bools taulukon boolean ei ole true.
			 //Tämän jälkeen tarkistetaan onko kaikkiTrue edelleen true, jos on, kutsutaan aliohjelmaa näyttämään
			 //lohkon
			 boolean kaikkiTrue = true;
			 for (int j = 0; j < bools.length; j++) {
				 if (!bools[j]) kaikkiTrue = false;
			 }
			 
			 if (kaikkiTrue) naytaLohko(lohko);
			 
		 }

	 }
	 
	 /**
	  * Lisää StringGridiin yhden lohkon tiedot
	  * @param lohko lohko joka lisätään.
	  */
	 private void naytaLohko(Lohko lohko) {
		 int kenttaMaara = lohko.getKentat();
		 String rivi[] = new String[11];
		 int j = 0;
		 for (int i = lohko.getEkaKentta(); i < kenttaMaara- 1; i++) {
			 if (i == 7) j++;
			 rivi[i] = lohko.anna(j);
			 j++;
		 }
		 tableHakutulokset.add(lohko, rivi);
	 }
	 
	 /**
	  * asettaa MetsaController luokan controllerin, jotta tästä controllerista voidaan
	  * kutsua sen metodeja
	  * @param ctrl MetsaControllerin controller
	  */
		public void setController(MetsaController ctrl) {
			this.ctrl = ctrl;
		} 
	 
	 /**
	  * Asetttaa käsiteltävän metsälohkorekisterin.
	  * @param metsalohkorekisteri asetettava rekisteri
	  */
	 public void setMetsa(Metsalohkorekisteri metsalohkorekisteri) {
		this.metsalohkorekisteri = metsalohkorekisteri;		
	}
}
