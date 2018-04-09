package metsalohkorekisteri;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Tuomas
 * @version 20 Apr 2017
 * Controlleri uusien toimenpiteiden lisäämiseksi.
 * EI KÄYTÖSSÄ!
 */
public class MetsaUusiToimenpideController {
	@FXML private Button tallenna;
	@FXML private Button peruuta;
	
    @FXML
    private TextField toimenpide;

    @FXML
    void handlePeruuta() {
    	Stage myyntiIkkuna = (Stage) peruuta.getScene().getWindow();
	    myyntiIkkuna.close();
    }
    
    //Handleri tallenna napille.
    @FXML
    void handleTallenna() {
    	tallennaToimenpide();
    }
    
    //Tallentaa uuden hoitotoimenpiteen.
    private void tallennaToimenpide() {
    	Dialogs.showMessageDialog("Hoitotoimenpiteiden lisäys ei toimi vielä!");
    }

}


