/*
 * Created by Jacob Strieb
 * January 2021
 */
package worduse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 *
 * @author jacobstrieb
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private AnchorPane root;
    @FXML
    private TextField documentPathField;
    @FXML
    private TextField wordField;
    @FXML
    private ScatterChart chart;
    
    private File documentFile;
    
    @FXML
    public void handleBrowse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Word Document");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Word Documents", "*.doc", "*.docx"));
        documentFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        
        if (documentFile == null) return;
        
        ((HBox) wordField.getParent()).setDisable(false);
        
        documentPathField.setText(documentFile.getAbsolutePath());
    }
    
    @FXML
    public void handleGraph(ActionEvent event) {
        String word = wordField.getText().toLowerCase();
        
        XWPFDocument document;
        try {
            FileInputStream f = new FileInputStream(documentFile);
            document = new XWPFDocument(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String documentText = extractor.getText();
        
        String[] words = documentText.split("[^A-Za-z0-9]");
        
        XYChart.Series data = new XYChart.Series();
        chart.getData().removeAll(chart.getData());
        chart.getData().add(data);
        
        for (int i=0; i < words.length; i++) {
            String w = words[i].toLowerCase();
            if (word.equals(w)) {
                data.getData().add(new XYChart.Data(i, 10));
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Do nothing...
    }    
    
}
