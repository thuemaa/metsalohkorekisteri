package metsalohkorekisteri;

import javafx.application.Application;
import javafx.stage.Stage;
import metsaluokat.Metsalohkorekisteri;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**^
 * @author Tuomas
 * @version 20.4.2017
 * Pääohjelma metsälohkorekisterin käynnistämiseksi.
 * Asioita joita ohjelmaan piti tulla, mutta ne puuttuvat:
 * Lohkojen valintapalkin yläpuolella oleva comboBoxChooser, jonka tarkoituksena oli listata vain
 * ne lohkot listchooserin, joiden tila on valittu. En tehnyt, koska haku ajaa täysin saman asian.
 * 
 * Hoitotoimepiteen luokka. En tehnyt koska olisi lisännyt ns. "turhaa" koodia ja ohjelman käyttämisestä
 * olisi tullut hankalampaa. Lisäksi sain tehtyä sen tarjoaman varsinaisen hyödyn muuten.
 * 
 * Luo taulukko. Tarkoitus oli tehdä ominaisuus joka luo rekisterin tiedoista excel taulukon.
 * Jätin tekemättä, koska ei sisälly harjoitustyön vaatimuksiin ja olisi vaatinut useamman tunnin panostuksen.
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			final FXMLLoader ldr = new FXMLLoader(getClass().getResource("MetsaMainView.fxml"));
			final Pane root = (Pane) ldr.load();
			final MetsaController  metsaCtrl = (MetsaController) ldr.getController();
 			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest((event) -> {
				if ( !metsaCtrl.tallennetaanko() ) event.consume();
			});
			
			Metsalohkorekisteri metsalohkorekisteri = new Metsalohkorekisteri();
			metsaCtrl.setMetsa(metsalohkorekisteri);
			metsaCtrl.setController(metsaCtrl);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Käynnistetään käyttöliittyymä
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
