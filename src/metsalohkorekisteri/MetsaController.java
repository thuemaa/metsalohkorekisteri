package metsalohkorekisteri;
 
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import metsaluokat.*;

/**
 * @author Tuomas
 * @version 14.2.2017
 * Metsälohkorekisterin pääikkunan tapahtumien hoitaminen.
 */
public class MetsaController implements Initializable{
	@FXML private StringGrid<Ltoimenpide> tableToimenpiteet;
	@FXML private StringGrid<Hakkuu> tableHakkuut;
	@FXML private ScrollPane panelLohkot;
	@FXML private ListChooser<Lohko> chooserLohkot;
	@FXML private GridPane gridLohko;
	@FXML private Button muutaLohko;
	@FXML private Button tallennaLohko;
	@FXML private TextField kulutText, myyntiText, tulotText;
	
	
	
	/**
	 * Alustaa ohjelman
	 */
	@Override
    public void initialize(URL url, ResourceBundle bundle) {
			alusta();
	}
	
	/**
	 * Avaa ohjelman tietoikkunan.
	 */
	@FXML
	void handleAbout() {
		try {
			FXMLLoader aboutLoader = new FXMLLoader(getClass().getResource("MetsaAboutView.fxml"));
			Parent root = (Parent) aboutLoader.load();
			Stage aboutIkkuna = new Stage();
			aboutIkkuna.setScene(new Scene(root));
			aboutIkkuna.setResizable(false);
			aboutIkkuna.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	  }
	
	/**
	 * Tallentaa lohkon tiedot
	 */
	@FXML
    void handleTallennaTiedosto() {
		FileChooser tallennus = new FileChooser();
		tallennus.setTitle("Tallenna tiedosto");
		//File oletuskansio = new File("C:\\Users\\Tuomas\\workspace\\Metsälohkorekisteri\\tallennus");
		tallennus.setInitialDirectory(new File(System.getProperty("user.home")));
		File tallennustiedosto = tallennus.showSaveDialog(null);
		
		if (tallennustiedosto != null) {
			tallennaTiedosto(tallennustiedosto.getAbsolutePath());
		}	
		
		else Dialogs.showMessageDialog("Tiedosto ei kelpaa");
    }
	
	

	/**
	 * Käynnistää FileChooserin jolla avataan haluttu tallenustiedosto
	 */
	@FXML
    void handleAvaaTiedosto() throws SailoException {
		FileChooser avaus = new FileChooser();
		avaus.setTitle("Avaa tiedosto");
		//File oletuskansio = new File("C:\\Users\\Tuomas\\workspace\\Metsälohkorekisteri\\tallennus");
		avaus.setInitialDirectory(new File(System.getProperty("user.home")));
		File valittuTiedosto = avaus.showOpenDialog(null);
		
		if (valittuTiedosto != null) {
			lueTiedosto(valittuTiedosto.getAbsolutePath());
		}
		else Dialogs.showMessageDialog("Tiedosto ei kelpaa");
    }
	
	/**
	 * Handleri uuden lohkon lisäykseksi
	 */
	@FXML void handleUusiLohko() {
		uusiLohko();
	}

	/**
	 * Avaa ohjelman hakuikkunan
	 */
	@FXML void handleAvaaHaku() {
		try {
			FXMLLoader hakuL = new FXMLLoader(getClass().getResource("MetsaHakuView.fxml"));
			Parent root = (Parent) hakuL.load();		
			MetsaHakuController kontrolleri = hakuL.getController();
			kontrolleri.setMetsa(metsalohkorekisteri);
			kontrolleri.setController(ctrl);
			kontrolleri.alusta();
			Scene hakuS = new Scene(root);
			Stage hakuIkkuna = new Stage();
			hakuIkkuna.setScene(hakuS);
			hakuIkkuna.setResizable(false);
			hakuIkkuna.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * handleri lohkon tietojen muuttamiseksi
	 */
	
	@FXML void handleMuutaLohko() {
		muutaLohko();
	}

    /**
     * Handleri hakkuun tietojen muuttamiseksi
     */
	@FXML void handleMuutaHakkuu() {
		if (tableHakkuut.getObject() == null) return;
		muutaTaiLisaaHakkuu(false, tableHakkuut.getObject());
    }
	
	/**
	 * handleri tiedostojen tallenuksen muuttamiseksi.
	 */
	@FXML
    void handleTallennaMuutokset() {
		tallennaMuutokset();
    }

	/**
	 * avaa hoitotoimenpiteiden lisäysikkunan. Toistaiseksi lisää vain esimerkkihoitotoimenpiteen.
	 */
	@FXML void handleLisaaHoito() {
		muutaTaiLisaaHoito(true, null);
	
	}
	
    @FXML void handleMuutaToimenpide() {
    	if (tableToimenpiteet.getObject() == null) return;
    	muutaTaiLisaaHoito(false, tableToimenpiteet.getObject());
    }
	
	
	/**
	 * avaa hakkuiden lisäysikkunan. toistaiseksi lisää vain esimerkkihakkuun.
	 */
	@FXML void handleLisaaHakkuu() {
		Hakkuu uusi = new Hakkuu();
		muutaTaiLisaaHakkuu(true, uusi);
	}
	
	/**
	 * Avaa ikkunan jossa säädetään hakkuun puiden myyntihinnat. (EI KÄYTÖSSÄ)
	 */
	@FXML void handleLisaaMyynti() {
		try {
			FXMLLoader myyntiLoader = new FXMLLoader(getClass().getResource("MetsaMyyntiView.fxml"));
			Parent root = (Parent) myyntiLoader.load();
			Stage myyntiIkkuna = new Stage();
			myyntiIkkuna.setScene(new Scene(root));
			myyntiIkkuna.setResizable(false);
			myyntiIkkuna.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Uuden lohkon lisäyshandleri. Toistaiseksi lisää vain uusia lohkoja testien vuoksi. POISTUU LOPULLISESSA VERSIOSSA
	 */
	@FXML void handleUusiToimenpide() {
		uusiLohkoTesti();
	}
	
	/**
	 * Handlerit hakkuiden lohkojen ja toimenpiteiden poistolle.
	 */
	@FXML void handlePoistaToimenpide() {
	poistaToimenpide();
    }

    @FXML void handlePoistaLohko() {
    poistaLohko();
    }

    @FXML void handlePoistaHakkuu() {
    poistaHakkuu();
    }	
    
    @FXML void handleUusiTiedosto() {
        alusta();
    }
    
	//=======================================================================
	//ei varsinaista käyttöliittymäkoodia
	private Metsalohkorekisteri metsalohkorekisteri;
	private TextArea areaLohkot = new TextArea();
	private Lohko lohkoKohdalla;
	private TextField[] tekstikentat;
	private static Lohko apulohko = new Lohko(); //tyhjä lohko jota käytetään osassa aliohjelmia avuksi
	private int muutaLohkoTila = 0;
	//private String tallennusTiedosto = "tallennus"; //ei käytössä, oli aikasemmin testausta varten
	private MetsaController ctrl; //Tämän luokan controller
	
	/**
	 * asettaa ctrl attribuutin arvoksi pääikkunan Controllerin, jotta sen voi lähettää
	 * eteenpäin muille controllereille. Näin muista controllereista voi kutsua
	 * MetsaControllerin metodeja. (toimenpideiten yms päivitys, haussa valitun lohkon avaus jne)
	 * @param ctrl Asetettava kontrolleri
	 */
	public void setController(MetsaController ctrl) {
		this.ctrl = ctrl;
	}
	
	/**
	 * Tiedoston avausohjelma
	 * @param nimi Päätallennustiedoston polku
	 * @return ei mitään
	 * @throws SailoException ei käytössä
	 */
	protected String lueTiedosto(String nimi) throws SailoException {
		metsalohkorekisteri.avaaTiedosto(nimi);
		hae(0);
		return null;
	}
	
	/**
	 * Tiedoston tallennus
	 * @param nimi tiedosto johon tallennetaan
	 */
	private void tallennaTiedosto(String nimi) {
		//tallennusTiedosto = nimi;
		metsalohkorekisteri.tallennaTiedosto(nimi);
	}
	
	/**
	 * ohjelman alustus. Laittaa tyhjän lohkopaneelin.
	 */
	protected void alusta() {
		this.metsalohkorekisteri = new Metsalohkorekisteri();
		
		/* lohkojen ja toimenpiteiden textArean alustaminen testausta varten
		panelLohkot.setContent(areaLohkot);
		areaLohkot.setFont(new Font("Arial", 12));
		panelLohkot.setFitToHeight(true);
		*/
		tekstikentat = taytaTekstikentat(gridLohko);
		
		for (TextField text : tekstikentat)
			if (text != null) {
				text.setEditable(false);
			}
		
		String[] tpideOtsikot = {"Toimenpide", "Pvm.", "Kulut"};
		
		tableToimenpiteet.initTable(tpideOtsikot);
		tableToimenpiteet.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableToimenpiteet.setEditable(false);
		tableToimenpiteet.setPlaceholder(new Label("Ei lisättyjä hoitotoimenpiteitä"));
		
		String[] hakkuuOtsikot = {"kertymä", "Pvm.", "Kulut"};
		tableHakkuut.initTable(hakkuuOtsikot);
		tableHakkuut.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableHakkuut.setEditable(false);
		tableHakkuut.setPlaceholder(new Label("Ei lisättyjä hakkuita"));
		
		
		chooserLohkot.clear();
		chooserLohkot.addSelectionListener(e -> naytaLohko(chooserLohkot.getSelectedObject()));
		tableHakkuut.setOnMouseClicked( e ->  hakkuuLaskut(tableHakkuut.getObject()));
			
		
		}
	
	/**
	 * Laskee valitun hakkuun tietoja hakkuiden StringGridin alapuolelle
	 * @param hakkuu Hakkuu jonka tiedot haetaan.
	 */
	public void hakkuuLaskut(Hakkuu hakkuu) {
		myyntiText.setText(String.format("%.2f", hakkuu.laskeKokonaismyynti()));
		kulutText.setText(String.format("%.2f", hakkuu.annaKulut()));
		tulotText.setText(String.format("%.2f",(Double.parseDouble(myyntiText.getText()) -  Double.parseDouble(kulutText.getText()))));
	}
	
	/**
	 * Täytetään gridin tekstikenttiin lohkon tiedot
	 * @param gridLohko grid mihin tiedot tuodaan.
	 * @return luodut tekstikentät
	 */
	protected static TextField[] taytaTekstikentat(GridPane gridLohko) {
		gridLohko.getChildren().clear();
		gridLohko.setPadding(new Insets(35,0,0,10));
		gridLohko.setMinHeight(415); gridLohko.setPrefWidth(300);
		TextField[] kentat = new TextField[apulohko.getKentat()];
	
		for (int i = 0; i < apulohko.getKentat(); i++ ) {
			Label label = new Label(apulohko.getKenttaLabel(i));
			label.setFont(new Font(15));
			gridLohko.add(label, 0, i);
			if (i != 7) {
				TextField text = new TextField();
				kentat[i] = text;
				text.setId("e"+i);
				gridLohko.add(text, 1, i);
			}
		}
		return kentat;
	}
	
	/**
	 * Tämä tulee lisäämään uuden tyhjän lohko-olion.
	 */
	private void uusiLohko() {
		Lohko lohko = new Lohko();
		lohko.setNimi("-UUSI LOHKO-");
		lohko.asetaLohkoId();
		try {
			metsalohkorekisteri.lisaa(lohko);
		} catch (SailoException e) {
			Dialogs.showMessageDialog("Ongelmia uuden lohkon luomisessa:\n " + e.getMessage());
		}
		hae(lohko.getLohkoId());
	}
	
	/**
	 * Poistaa valitun lohkon
	 */
	private void poistaLohko() {
		if (lohkoKohdalla == null) return;
		metsalohkorekisteri.poista(lohkoKohdalla);
		hae(0);
	}
	
	/**
	 * poistaa stringridistä valitun toimenpiteen
	 */
	private void poistaToimenpide() {
		Ltoimenpide tpide = tableToimenpiteet.getObject();
		if(tpide == null) return;
		metsalohkorekisteri.poista(tpide);
		hae(lohkoKohdalla.getLohkoId());
	}
	
	/**
	 * poistaa stringridistä valitun hakkuun.
	 */
	private void poistaHakkuu() {
	Hakkuu hakkuu = tableHakkuut.getObject();
	if(hakkuu == null) return;
	metsalohkorekisteri.poista(hakkuu);
	hae(lohkoKohdalla.getLohkoId());
		
	}
	/**
	 * EI KÄYTÖSSÄ!
	 * Testiohjelma uuden lohkon tekemiseksi.
	 * Täyttää uuden lohkon satunnaisilla tiedoilla.
	 */
	protected void uusiLohkoTesti() {
		Lohko lohko = new Lohko();
		lohko.taytaEsim();
		lohko.asetaLohkoId();
		try {
			metsalohkorekisteri.lisaa(lohko);
		} catch (SailoException e) {
			Dialogs.showMessageDialog("Ongelmia uuden lohkon luomisessa:\n " + e.getMessage());
			return;
		}
		hae(lohko.getLohkoId());
	}
	
	/**
	 * EI KÄYTÖSSÄ!
	 * Testiohjelma uuden toimenpiteen luomiseksi
	 */
	protected void uusiToimenpideTesti() {
		if (lohkoKohdalla == null) return;
		Ltoimenpide toimenpide = new Ltoimenpide();
		toimenpide.taytaLaikutus(lohkoKohdalla.getLohkoId());
		metsalohkorekisteri.lisaa(toimenpide);
		hae(lohkoKohdalla.getLohkoId());
	}
	
	/**
	 * EI KÄYTÖSSÄ!
	 * testiohjelma uuden hakkuun luomiseksi.
	 */
	protected void uusiHakkuuTesti() {
		if (lohkoKohdalla == null) return;
		Hakkuu hakkuu = new Hakkuu();
		hakkuu.taytaHakkuu(lohkoKohdalla.getLohkoId());
		metsalohkorekisteri.lisaa(hakkuu);
		hae(lohkoKohdalla.getLohkoId());
	}
	
	/**
	 * Hakee lohkon tiedot listaan
	 * @param lnumero lohkon ID numero joka haetaan
	 */
	protected void hae(int lnumero) {
		chooserLohkot.clear();
		
		int index = 0;
		for (int i = 0 ; i < metsalohkorekisteri.getLohkot(); i++) {
			Lohko lohko = metsalohkorekisteri.annaLohko(i);
			if (lohko.getLohkoId() == lnumero) index = i;
			chooserLohkot.add(lohko.getNimijaTila(), lohko);
		}
		chooserLohkot.setSelectedIndex(index);
	}
	
	/**
	 * näyttää listasta valitun lohkon tiedot TextFieldissä.
	 * @param lohko Valittu lohko
	 */
	protected void naytaLohko(Lohko lohko) {
		lohkoKohdalla = lohko;
		if (lohkoKohdalla == null) return;
		areaLohkot.setText("");
		try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaLohkot)) {
			tulosta(os, lohkoKohdalla);
		}
		
		if (lohkoKohdalla == null) return;
		for (int i = lohko.getEkaKentta(); i < lohko.getKentat(); i++) {
			if (i != 7) tekstikentat[i].setText(lohko.anna(i));
		}
		
		naytaToimenpiteet(lohkoKohdalla);
		naytaHakkuut(lohkoKohdalla);
	}
	
	/**
	 * Näyttää lohkon hoitotoimenpiteet StringGridissä. 
	 * @param lohko Lohko jonka tpiteet näytetään
	 */
	private void naytaToimenpiteet(Lohko lohko) {
		tableToimenpiteet.clear();
		
		List<Ltoimenpide> toimenpiteet = metsalohkorekisteri.annaHoitotoimenpiteet(lohko);
		if (toimenpiteet.size() == 0) return;
		for (Ltoimenpide tpid : toimenpiteet) {
			naytaToimenpide(tpid);
		}
		//hae(lohko.getLohkoId());
	}
	
	/**
	 * Täyttää yhden StringGridin rivin toimenpiteellä.
	 * @param toimenpide Toimenpide jonka tiedot lisätään riville
	 */
	private void naytaToimenpide(Ltoimenpide toimenpide) {
		int kenttaMaara = toimenpide.getKentat();
		String rivi[] = new String[kenttaMaara];
		for (int i = 0; i < kenttaMaara; i++) {
			rivi[i] = toimenpide.anna(i);
		}
		tableToimenpiteet.add(toimenpide, rivi);
	}
	
	private void naytaHakkuut(Lohko lohko) {
		tableHakkuut.clear();
		
		List<Hakkuu> hakkuut = metsalohkorekisteri.annaHakkuut(lohko);
		if(hakkuut.size() == 0) return;
		for (Hakkuu hakkuu : hakkuut) {
			naytaHakkuu(hakkuu);
		}
		//hae(lohko.getLohkoId());
	}
	
	private void naytaHakkuu(Hakkuu hakkuu) {
		String rivi[] = new String[3];
		rivi[0] = hakkuu.laskeKertyma(); //Laskee ja palauttaa hakkuun kokonaiskertymän
		rivi[1] = hakkuu.getPvm(); //palauttaa hakkuun päivämäärän
		rivi[2] = hakkuu.getKulut(); //palauttaa hakkuun kulut
		tableHakkuut.add(hakkuu, rivi);
	}
	
	
	/**
	 * EI KÄYTÖSSÄ! Toimi testiohjelmana aikaisemmin!
	 * tulostaa tekstilaatikkoon lohkon  toimenpiteet ja hakkuut.
	 * @param os tietovirta johon tulostetaan
	 * @param lohko lohko joka tulostetaan
	 */
	public void tulosta(PrintStream os, final Lohko lohko) {
		List<Hakkuu> hakkuut = metsalohkorekisteri.annaHakkuut(lohko);
		for (Hakkuu hakkuu : hakkuut)
			hakkuu.tulosta(os);
		List<Ltoimenpide> toimenpiteet = metsalohkorekisteri.annaHoitotoimenpiteet(lohko);
		for (Ltoimenpide toimenpide : toimenpiteet)
			toimenpide.tulosta(os);
	}
	
	/**
	 * Muuttaa lohkokenttien tekstilaatikot muutettaviksi.
	 */
	private void muutaLohko() {	
		if (muutaLohkoTila == 0){
			for (int i = 0 ; i < tekstikentat.length; i++) {
				if (i != 7) tekstikentat[i].setEditable(true);
			}
			muutaLohkoTila = 1;
		}
		else {
			for (int i = 0 ; i < tekstikentat.length; i++) {
				if (i != 7) tekstikentat[i].setEditable(false);
			}
			muutaLohkoTila = 0;
		}
	}
	
	/**
	 * Avaa uuden ikkunan hoitotoimenpiteen lisäykseksi tai muuttamiseksi. Jos boolean on true, lisätään, jos false niin muutetaan
	 * @param lisataanko arvo joka määrää tehdäänkö uusi toimenpide vai muutetaanko vanhaa.
	 * @param toimenpide muutettava toimenpide
	 */
	private void muutaTaiLisaaHoito(boolean lisataanko, Ltoimenpide toimenpide) {
		if (lohkoKohdalla == null) return;
		
		try {
			FXMLLoader tpideL = new FXMLLoader(getClass().getResource("MetsaToimenpideView.fxml"));
			Parent root = (Parent) tpideL.load();		
			MetsaToimenpideController kontrolleri = tpideL.getController();
			kontrolleri.setId(lohkoKohdalla.getLohkoId());
			kontrolleri.setMetsa(metsalohkorekisteri);
			kontrolleri.setMode(lisataanko);
			kontrolleri.setToimenpide(toimenpide);
			kontrolleri.setController(ctrl);
			kontrolleri.alusta();
			Scene tpideS = new Scene(root);
			Stage hoitoIkkuna = new Stage();
			hoitoIkkuna.setScene(tpideS);
			hoitoIkkuna.setResizable(false);
			hoitoIkkuna.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Avaa uuden ikkunan hakkuun lisäämikseksi tai poistamiseksi.
	 * @param lisataanko Jos true, lisätään uusi hakkuu poistamisen sijasta
	 * @param hakkuu muutettava hakkuu
	 */
	private void muutaTaiLisaaHakkuu(boolean lisataanko, Hakkuu hakkuu) {
		if (lohkoKohdalla == null) return;
		
		try {
			FXMLLoader hakkuuL = new FXMLLoader(getClass().getResource("MetsaHakkuuView.fxml"));
			Parent root = (Parent) hakkuuL.load();		
			MetsaHakkuuController kontrolleri = hakkuuL.getController();
			kontrolleri.setId(lohkoKohdalla.getLohkoId());
			kontrolleri.setMetsa(metsalohkorekisteri);
			kontrolleri.setMode(lisataanko);
			kontrolleri.setHakkuu(hakkuu);
			kontrolleri.setController(ctrl);
			kontrolleri.initialize();
			Scene hakkuuS = new Scene(root);
			Stage hakkuuIkkuna = new Stage();
			hakkuuIkkuna.setScene(hakkuuS);
			hakkuuIkkuna.setResizable(false);
			hakkuuIkkuna.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * tallentaa muutokset lohkon tietoihin tekstikentistä.
	 */
	private void tallennaMuutokset() {
		if (lohkoKohdalla == null) return;	
		
		for (int i = 0; i < 12; i++) {
			if ( i != 7) {
				String s = tekstikentat[i].getText();
				kasitteleMuutosLohkoon(i, s);
			}
		}
		hae(lohkoKohdalla.getLohkoId()); //päivittää ListChooserin kaikki lohkot, jotta oikea lohkon nimi näkyy listChooserissa.
		metsalohkorekisteri.setMuutoksia();
	}
	
	/**
	 * Kutsuu lohkon metodeita muuttamaan attribuuttinsa teksikentäsyötteenä annettuun arvoon
	 * Jos tekstikentän syöte on väärää syntaksia, avaa dialog ikkunan joka kertoo virheen.
	 * @param k case:n muuttuja, eli mitä arvoa muutetaan
	 * @param s Tekstikentästä haettu tekstin muutos.
	 */
	private void kasitteleMuutosLohkoon(int k, String s) {
		if (lohkoKohdalla == null) return;
		String virhe = null;
		switch (k) {
		case 0: virhe = lohkoKohdalla.setNumero(s); break;
		case 1: virhe = lohkoKohdalla.setNimi(s); break;
		case 2: virhe = lohkoKohdalla.setTila(s); break;
		case 3: virhe = lohkoKohdalla.setPintaAla(s); break;
		case 4: virhe = lohkoKohdalla.setTyyppi(s); break;
		case 5: virhe = lohkoKohdalla.setKehitys(s); break;
		case 6: virhe = lohkoKohdalla.setIka(s); break;
		case 7: break; //teksikentässä numero 7 ei ole tekstiä.
		case 8: virhe = lohkoKohdalla.setKuusi(s); break;
		case 9: virhe = lohkoKohdalla.setManty(s); break;
		case 10: virhe = lohkoKohdalla.setKoivu(s); break;
		case 11: virhe = lohkoKohdalla.setMuupuu(s); break;
		default: break;	
		}
		if (virhe != null) Dialogs.showMessageDialog(virhe);
	}	
	
	/**
	 * Asettaa käsiteltävän metsälohkorekisterin
	 * @param metsalohkorekisteri rekisteri joka halutaan asettaa
	 */
	public void setMetsa(Metsalohkorekisteri metsalohkorekisteri) {
		this.metsalohkorekisteri = metsalohkorekisteri;		
	}
	
	/**
	 * Tarkistaa onko tehty muutoksia, jos on avaa AlertWindowin ja kysyy haluatko varmasti sulkea.
	 * @return true jos muutoksia ei tehty, tällöin ikkuna ei aukea. Muuten false
	 */
	public boolean tallennetaanko() {
		if (metsalohkorekisteri.getMuutoksia() == true) {
			Alert kysyTallennus = new Alert(AlertType.CONFIRMATION);
			kysyTallennus.setTitle("Suljetaanko?");
			kysyTallennus.setContentText("Et ole tallentanut muutoksia. Haluatko varmasti sulkea? Muutokset menetetään jos et tallenna!");
			Optional<ButtonType> result = kysyTallennus.showAndWait();
		
			if ((result.isPresent() && (result.get() == ButtonType.OK))) {
				return true;		
			}
            return false;
		}
		return true;
	}
	
}