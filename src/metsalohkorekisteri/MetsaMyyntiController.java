package metsalohkorekisteri;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Tuomas
 * @version 14.2.2017
 * Metsälohkorekisterin myynti-ikkunan tapahtumien hoitaminen.
 * EI KÄYTÖSSÄ
 */
public class MetsaMyyntiController {

    @FXML private Button tallenna;
    @FXML private Button peruuta;
    
    //Sulkee myynti-ikkunan.
    @FXML
    void handlePeruuta() {
    	Stage myyntiIkkuna = (Stage) peruuta.getScene().getWindow();
	    myyntiIkkuna.close();
    }
    
    //Handleri tallenna napille.
    @FXML
    void handleTallenna() {
    	tallennaMyynti();
    }
    
    //Tallentaa annetut myyntihinnat StringGridissä valitulle hakkuullee.
    private void tallennaMyynti() {
    	Dialogs.showMessageDialog("Myyntihintojen tallennus ei toimi vielä!");
    }

}
