package metsalohkorekisteri;


import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import metsaluokat.Hakkuu;
import metsaluokat.Metsalohkorekisteri;

/**
 * @author Tuomas
 * @version 14.2.2017
 * Metsälohkorekisterin hakkuuikkunan tapahtumien hoitaminen.
 */
public class MetsaHakkuuController {
	 @FXML private Button tallenna;
	 @FXML private Button peruuta;
	 @FXML private GridPane gridHakkuu;
	 
	 /**
	  * Kontrollerin avauksessa tapahtuvat toiminnot
	  */
	 public void initialize() {
		 taytaTekstikentat(gridHakkuu);
	 }
	 
	 
	/**
	 * Sulkee hakkuuikkunan
	 * @param event
	 */
    @FXML
    void handlePeruuta() {
    	Stage hakkuuikkuna = (Stage) peruuta.getScene().getWindow();
	    hakkuuikkuna.close();
    }
    
    /**
     * Tallenna napin handler
     * @param event
     */
    @FXML
    void handleTallenna() {
    	tallennaHakkuu();
    	ctrl.hae(lohkoId);
    	
    }
 
    //---------------ei varsinaista käyttöliittymäkoodia-----------------
    private int lohkoId;
    private Metsalohkorekisteri metsalohkorekisteri;
    private MetsaController ctrl;
    private Hakkuu hakkuu = new Hakkuu();
    private TextField[] hintaKentat;
    private TextField[] maaraKentat;
    private boolean lisataanko;
    
    /**
     * Alustaa gridPaneen Labelit ja tekstikentät. Jos hakkua muokataan, muokattavan hakkuun tiedot kenttiin.
     * Jos kyseessä uusi hakkuu, täyttää kentät oletusarvoilla (0.0).
     * @param gridHakkuu tekstikenttien grid
     */
    protected void taytaTekstikentat(@SuppressWarnings("hiding") GridPane gridHakkuu) {
    	gridHakkuu.getChildren().clear();
    	gridHakkuu.setPadding(new Insets(0,0,0,0));
		gridHakkuu.setMinHeight(300); gridHakkuu.setPrefWidth(205);
		@SuppressWarnings("hiding")
        TextField[] maaraKentat = new TextField[hakkuu.getKentat()];
		@SuppressWarnings("hiding")
        TextField[] hintaKentat = new TextField[hakkuu.getKentat()];
		
		Label maara = new Label("m/m3");
		Label hinta = new Label("€/m3");
		gridHakkuu.add(maara, 1, 0);
		gridHakkuu.add(hinta, 2, 0);
		
		for (int i = 1; i < 9; i++) {
			Label label = new Label(hakkuu.getKenttaLabel(i));
			label.setFont(new Font(12));
			gridHakkuu.add(label, 0, i);
			
			TextField text = new TextField();
			maaraKentat[i] = text;
			text.setId("e1"+i);
			text.setText(hakkuu.annaMaara(i));
			gridHakkuu.add(text, 1, i);
				
			TextField text2 = new TextField();
			hintaKentat[i] = text2;
			text2.setId("e2"+i);
			text2.setText(hakkuu.annaHinta(i));
			gridHakkuu.add(text2, 2, i);
			
		}
		this.hintaKentat = hintaKentat;
		this.maaraKentat = maaraKentat;
    }
    
    /**
     * Tekee StringBuilder olion johon hakee oikeassa järjestyksessä gridpanen tekstikentistä
     * tiedot, tarkastaa syötteen oikeinkirjoituksen jonka jälkeen kutsuu hakkuun parse metodia, eli tallentaa hakkuun annetuista tiedoista
     */
    private void tallennaHakkuu() {
    	if (!lisataanko) metsalohkorekisteri.poista(hakkuu);
    	int virheet = 0;
    	
    	Hakkuu uusihakkuu = new Hakkuu();
    	
    	StringBuilder talTeksti = new StringBuilder();
    	talTeksti.append(lohkoId+"|"); //asettaa lohkoID:n ensimmäiseksi kohdaksi
    	talTeksti.append(maaraKentat[8].getText()+"|"); //hakee pvm kohdan tekstin
    	for (int i = 1; i < hakkuu.getKentat(); i++) {	
    		if (i != 8) {
    			virheet += metsalohkorekisteri.tarkistaHakkuuSyote(maaraKentat[i].getText());
        		virheet += metsalohkorekisteri.tarkistaHakkuuSyote(hintaKentat[i].getText());
        		talTeksti.append(maaraKentat[i].getText()+"|");
    		}
    		talTeksti.append(hintaKentat[i].getText()+"|");
    	}
    	
    	virheet += metsalohkorekisteri.tarkistaPvmSyote(maaraKentat[8].getText());
    	if (virheet != 0) {
    		Dialogs.showMessageDialog("Anna numeerinen arvo kuluille sekä määrä- ja hintakenttiin.\nSyötä päivämäärä muodossa vvvv.kk.pp");
        	return;
    	}
    	
    	talTeksti.setLength(talTeksti.length() - 1); //poistaa viimeisen | merkin stringbuilderista
    	uusihakkuu.parse(talTeksti.toString());
    	metsalohkorekisteri.lisaa(uusihakkuu);
    	
    	Stage hakkuuikkuna = (Stage) peruuta.getScene().getWindow();
	    hakkuuikkuna.close();
	    
    }
    
    
    /**
     * asettaa sen lohkon ID:n jolle hakku lisätään
     * @param lohkoId id joka asetetaan
     */
	public void setId(int lohkoId) {
		this.lohkoId = lohkoId;
	}
	
	/**
	 * asettaa muokattavan metsälohkorekisterin
	 * @param metsalohkorekisteri rekisteri jota asetetaan
	 */
	public void setMetsa(Metsalohkorekisteri metsalohkorekisteri) {
		this.metsalohkorekisteri = metsalohkorekisteri;		
	}
	
	/**
	 * asettaa boolean arvon joka kertoo muokataanko vai lisätäänkö uusi hakkuu
	 * true jos lisätään, false jos muokataan
	 * @param bool arvo joka kertoo onko lisäys vai muokkaus
	 */
	public void setMode(boolean bool) {
		this.lisataanko = bool;
	}
	
	/**
	 * asettaa muokattavan hakkuun.
	 * @param hakkuu hakkuu jota muokataan.
	 */
	public void setHakkuu(Hakkuu hakkuu) {
		this.hakkuu = hakkuu;
	}

	/**
	 * asettaa MetsaController luokan controllerin, jotta tästä controllerista voidaan
	 * kutsua sen metodeja
	 * @param ctrl MetsaControllerin controller
	 */
	public void setController(MetsaController ctrl) {
		this.ctrl = ctrl;
	}
}
