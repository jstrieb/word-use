/*
 * Created by Jacob Strieb
 * January 2021
 */
package worduse;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
        
        documentPathField.setText(documentFile.getAbsolutePath());
    }
    
    @FXML
    public void handleGraph(ActionEvent event) {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
