package application;

import data.Bacteria;
import data.BacteriasAggregator;
import data.DataBaseConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationManager extends Application {

    private DataBaseConnector database;
    private ApplicationController controller;


    private BacteriasAggregator bacterias;
    private BacteriasAggregator bacteriasToExamine;
    private Stage stage;


    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;
        FXMLLoader loader = new  FXMLLoader(getClass().getResource("/fxml/loginWindow.fxml"));

        Parent root = loader.load();

        controller = loader.getController();
        controller.setManager(this);

        primaryStage.setTitle(" Log to base ");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }

    private void loadMainWindow() throws SQLException, IOException {

        FXMLLoader loader = new  FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));

        Parent root = loader.load();

        controller = loader.getController();
        controller.setManager(this);

        stage.close();
        stage.setTitle(" Bacteria Base ");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();

        bacteriasToExamine = new BacteriasAggregator();
        bacterias = new BacteriasAggregator(database.getExamined());

        refreshTables();
    }

    @Override
    public void stop(){
        database.close();
    }

    public void clearToExamine(){

        bacteriasToExamine.clearBacterias();
        controller.refreshToExamine(bacteriasToExamine.getBacterias());
    }

    private void addRandomBacteria() throws SQLException {
        int tmpGen;
        tmpGen = (int)(Math.random() * 899999 ) + 100000 ;
        examineNewGenotype(new Bacteria(String.valueOf(tmpGen)));
    }

    public void connectToDB(String path, String login, String password) {

        database = new DataBaseConnector(path,login,password);

        //database.generateRandomBase(5);

        try {

            database.showAll();

            loadMainWindow();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveXML() throws JAXBException {
        String XMLname = "./XML/bacterias.xml";

        JAXBContext context = JAXBContext.newInstance(BacteriasAggregator.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        m.marshal(bacterias, new File(XMLname));

    }

    public void addNewBacteria(Bacteria bacteria){
        bacteriasToExamine.addBacteria(bacteria);

        controller.refreshToExamine(bacteriasToExamine.getBacterias());
    }

    public void examineAllBacterias() throws SQLException {

        database.startTransaction();
        try {

        for (Bacteria bacteria: bacteriasToExamine.getBacterias()
             ) {
                examineNewGenotype(bacteria);
        }

        } catch (SQLException e) {
            e.printStackTrace();
            database.rollbackTransaction();
        } finally {
            database.commitTransaction();
            bacterias.addBacterias(bacteriasToExamine.getBacterias());
            bacteriasToExamine.clearBacterias();
        }

        refreshTables();
    }

    private void refreshTables() throws SQLException {

        controller.refreshToExamine(bacteriasToExamine.getBacterias());

        controller.refreshExaminedTable(database.getExamined());
        controller.refreshFlagellaTable(database.getFlagella());
        controller.refreshToughnessTable(database.getToughness());
    }

    public void examineNewGenotype(Bacteria bacteria) throws SQLException {

        int number = examineFlagella(bacteria);
        char rank = examineToughness(bacteria);

        bacteria.setgClass( String.valueOf(number) + rank );


        database.addExaminedBacteria(bacteria);
    }

    private char examineToughness(Bacteria bacteria) throws SQLException {
        ResultSet flagellas = database.getSorterToughness();

        int Beta = bacteria.getBeta();
        int Gama = bacteria.getGamma();
        int closeMetric;
        char closeElement;

        flagellas.next();
        int tmpBeta = Integer.parseInt(flagellas.getString("BETA"));
        int tmpGama = Integer.parseInt(flagellas.getString("GAMMA"));
        closeMetric = Math.abs( (Gama+Beta)/2 - (tmpGama+tmpBeta)/2 );
        closeElement = flagellas.getString("RANK").charAt(0);

        do {

            tmpBeta = Integer.parseInt(flagellas.getString("BETA"));
            tmpGama = Integer.parseInt(flagellas.getString("GAMMA"));
            if( Math.abs( (Gama+Beta)/2 - (tmpGama+tmpBeta)/2 ) <= closeMetric){
                closeMetric = Math.abs( (Gama+Beta)/2 - (tmpGama+tmpBeta)/2 );
                closeElement =flagellas.getString("RANK").charAt(0);
            }
        }while (flagellas.next());

        return closeElement;
    }

    private int examineFlagella(Bacteria bacteria) throws SQLException {

        ResultSet flagellas = database.getSorterFlagella();


        int Alpha = bacteria.getAlpha();
        int Beta = bacteria.getBeta();
        int closeMetric, closeElement;

        flagellas.next();
        int tmpAlpha = Integer.parseInt(flagellas.getString("ALPHA"));
        int tmpBeta = Integer.parseInt(flagellas.getString("BETA"));
        closeMetric = Math.abs( (Alpha+Beta)/2 - (tmpAlpha+tmpBeta)/2 );
        closeElement = Integer.parseInt(flagellas.getString("NUMBER"));

        do {

            tmpAlpha = Integer.parseInt(flagellas.getString("ALPHA"));
            tmpBeta = Integer.parseInt(flagellas.getString("BETA"));
            if( Math.abs( (Alpha+Beta)/2 - (tmpAlpha+tmpBeta)/2 ) <= closeMetric ){
                closeMetric = Math.abs( (Alpha+Beta)/2 - (tmpAlpha+tmpBeta)/2 );
                closeElement = Integer.parseInt(flagellas.getString("NUMBER"));
            }
        }while (flagellas.next());

        return closeElement;
    }

    public static void main(String[] args){
        launch(args);
    }
}
