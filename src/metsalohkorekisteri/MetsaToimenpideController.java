package metsalohkorekisteri;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import metsaluokat.Ltoimenpide;
import metsaluokat.Metsalohkorekisteri;

/**
 * @author Tuomas
 * @version 14.2.2017
 * Metsälohkorekisterin toimenpideikkunan tapahtumien hoitaminen.
 */
public class MetsaToimenpideController {
    @FXML private Button tallenna;
    @FXML private Button peruuta;
    @FXML private TextField textPvm;
    @FXML private TextField textKulut;
    @FXML private TextField textTpide;


	/**
     * Sulkee toimenpiteiden lisäysikkunan.
     */
    @FXML
    void handlePeruuta() {
    	Stage toimenpideikkuna = (Stage) peruuta.getScene().getWindow();
	    toimenpideikkuna.close();
    }
    
    /**
     * handleri tallenna napille
     */
    @FXML
    void handleTallenna() {
    	tallennaToimenpide();
    	ctrl.hae(lohkoKohdallaId);
    }
  ///----------------------ei varsinaista käyttöliittymäkoodia
    private Metsalohkorekisteri metsalohkorekisteri;
    private int lohkoKohdallaId;
    private boolean uusiVaiMuutos;
    private Ltoimenpide muutettava;
    private MetsaController ctrl;
    
    
    /**
	 * asettaa MetsaController luokan controllerin, jotta tästä controllerista voidaan
	 * kutsua sitä
	 * @param ctrl MetsaControllerin controller
	 */
    public void setController(MetsaController ctrl) {
    	this.ctrl = ctrl;
    }
    
    /**
     * Tarkistaa onko teksikenttiin syötetty tieto oikeassa muodossa; jos ei antaa varoituksen
     * Muussa tapauksessa lisää/muuttaa toimenpiteen pääikkunassa aktiiviselle lohkolle.
     */
    private void tallennaToimenpide() {
    if (!uusiVaiMuutos) metsalohkorekisteri.poista(muutettava);
    
    int virheet = 0;
    virheet+=(metsalohkorekisteri.tarkistaPvmSyote(textPvm.getText()));
    virheet+=(metsalohkorekisteri.tarkistaTpideSyote(textKulut.getText())); 
    if (virheet != 0) {
    	Dialogs.showMessageDialog("Anna numeerinen arvo kuluille, ja syötä päivämäärä numeromuodossa vvvv.kk.pp");
    	return;
    }
    
    Ltoimenpide tpide = new Ltoimenpide();
    tpide.parse(lohkoKohdallaId + "|" + textTpide.getText()+"|"+textPvm.getText()+"|"+textKulut.getText());
    metsalohkorekisteri.lisaa(tpide);
    
    Stage toimenpideikkuna = (Stage) peruuta.getScene().getWindow();
    toimenpideikkuna.close();
    } 
    
    /**
     * asettaa textfieldeihin valitun toimenpiteen tiedot.
     */
    public void alusta() {
		if (muutettava != null) {
			textTpide.setText(muutettava.anna(0));
			textPvm.setText(muutettava.anna(1));
			textKulut.setText(muutettava.anna(2));
		}	
	}
    
    /**
     * käsiteltävä lohko johon toimenpide tallennetaan.
     * @param lohkoId lohkon ID
     */
	public void setId(int lohkoId) {
		this.lohkoKohdallaId = lohkoId;	
	}
	
	/**
	 * asettaa käsiteltävän metsälohkorekisterin.
	 * @param metsalohkorekisteri asetettava rekisteri
	 */
	public void setMetsa(Metsalohkorekisteri metsalohkorekisteri) {
		this.metsalohkorekisteri = metsalohkorekisteri;		
	}

	/**
	 * Asettaa muuttujan arvon, joka kertoo muutetaanko vai lisätäänkö toimenpide. Jos true, lisätään.
	 * @param bool muuttujan arvo
	 */
	public void setMode(boolean bool) {
		this.uusiVaiMuutos = bool;
	}
    
	/**
	 * asettaa muutettavan toimenpiteen
	 * @param tpide tpide jota muutetetaan
	 */
	public void setToimenpide(Ltoimenpide tpide) {
		this.muutettava = tpide;
	}
}
