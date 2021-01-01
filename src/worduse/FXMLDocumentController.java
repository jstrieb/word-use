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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

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
    @FXML
    private ListView table;
    
    private File documentFile;
    
    @FXML
    public void handleBrowse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Word Document");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Word Documents", "*.docx"));
        documentFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        
        if (documentFile == null) return;
        
        ((HBox) wordField.getParent()).setDisable(false);
        
        documentPathField.setText(documentFile.getAbsolutePath());
    }
    
    @FXML
    public void handleGraph(ActionEvent event) {
        // Load document
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
        
        // Create table
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String documentText = extractor.getText();
        String[] wordArray = documentText.split("[^A-Za-z0-9]");
        
        int occurrences = 0;
        for (String w : wordArray) {
            if (w.toLowerCase().equals(word)) {
                occurrences++;
            }
        }
        
        table.getItems().removeAll(table.getItems());
        table.getItems().add("Number of occurrences: " + Integer.toString(occurrences));
        
        // Create chart
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        
        XYChart.Series data = new XYChart.Series();
        chart.getData().removeAll(chart.getData());
        chart.getData().add(data);
        
        int maxCount = 0;
        for (int i=0; i < paragraphs.size(); i++) {
            String[] words = paragraphs.get(i).getText().split("[^A-Za-z0-9]");
            int count = 0;
            for (String w : words) {
                if (word.equals(w.toLowerCase())) {
                    count++;
                }
            }
            
            if (count > 0) {
                data.getData().add(new XYChart.Data(i, count));
            }
            
            if (count > maxCount) {
                maxCount = count;
            }
        }
        
        data.getData().add(new XYChart.Data(paragraphs.size() - 1, 0));
        
        table.getItems().add("Most occurrences in a paragraph: " + Integer.toString(maxCount));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Do nothing...
    }    
    
}
