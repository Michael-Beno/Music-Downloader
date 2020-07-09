/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.cit.gui;

import ie.cit.filesystem.CopyTask;
import ie.cit.folders.LocalFolder;
import ie.cit.folders.RemoteFolder;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Mick
 */
public class FXMLDocumentController extends Controller implements Initializable {

    @FXML private AnchorPane anch;
    @FXML private VBox vBoxRemote;
    @FXML private VBox vBoxLocal;
    @FXML private Label lblRemotePath;
    @FXML private Label lblLocalPath;
    @FXML private ProgressBar progress;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        remoteFM = RemoteFolder.getInstance(remotePath);
        localFM = LocalFolder.getInstance(localPath);

        lblRemotePath.setText(remotePath);
        lblLocalPath.setText(localPath);
    }

    @Override
    public void invalidate() {
        super.invalidate(); // controller invalidate
        System.out.println("FXML");
        showRemote(remoteFM.getNames());
        if(!localPath.equals(""))
            showLocal(localFM.getNames());
    }

    @FXML
    private void onBtnRemoteFolder(ActionEvent event) {
        remotePath = getPath();
        remoteFM.setPath(remotePath);
        lblRemotePath.setText("Path: " + remotePath);
        arrRemote = remoteFM.getNames();
        showRemote(arrRemote);
    }
    
    @FXML
    private void onBtnLocalFolder(ActionEvent event) {
        localPath = getPath();
        localFM.setPath(localPath);
        lblLocalPath.setText("Path: " + localPath);
        arrLocal = localFM.getNames();
        showLocal(arrLocal);
    }

    private void showLocal(String[] arrLocal) {
        vBoxLocal.getChildren().clear();
        if (remotePath.equals("") && !localPath.equals("")) {
            for (String arrLocal1 : arrLocal) {
                HBox hb = new HBox();
                Button btnPlay = new Button("play");
                hb.getChildren().add(btnPlay);
                Label l = new Label(arrLocal1);
                l.setFont(Font.font("Verdana", 20));
                Button btnUpload = new Button("upload");
                btnUpload.setVisible(false);
                hb.getChildren().add(btnUpload);
                hb.getChildren().add(l);
                vBoxLocal.getChildren().add(hb);
            }
        } else {
            for (int i = 0; i < aList.size(); i++) {
                HBox hb = new HBox();
                Button btnPlay = new Button("play");
                Label l = new Label(aList.get(i).getName());
                l.setFont(Font.font("Verdana", 20));
                Button btnUpload = new Button("upload");
                btnUpload.setOnAction((ActionEvent event) -> {
                    upload(l.getText());
                });
                Button btnDown = new Button("    download   ");
                btnDown.setOnAction((ActionEvent event) -> {
                    download(l.getText());
                });
                btnPlay.setStyle("-fx-text-fill: green; -fx-font-size: 10px;");
                btnUpload.setStyle("-fx-text-fill: blue; -fx-font-size: 10px;");
                btnDown.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
                if (aList.get(i).getStatus() == 0) {
                    hb.getChildren().add(btnDown);
                }
                if (aList.get(i).getStatus() >= 1) {
                    hb.getChildren().add(btnPlay);
                    hb.getChildren().add(btnUpload);
                    if (aList.get(i).getStatus() == 1) {
                        btnUpload.setVisible(false);
                    }
                }
                
                hb.getChildren().add(l);
                vBoxLocal.getChildren().add(hb);
            }
        }
    }

    private void showRemote(String[] arrRemote) {
        if (!remotePath.equals("")) {
            vBoxRemote.getChildren().clear();
            for (String arrRemote1 : arrRemote) {
                Label l = new Label(arrRemote1);
                l.setFont(Font.font("Verdana", 20));
                vBoxRemote.getChildren().add(l);
            }
        }
    }

    private String getPath() {
        String path = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("music", "mp3", "wav", "ogg"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int option = fileChooser.showDialog(null, "Select Directory");

        if (option == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            // if the user accidently click a file, then select the parent directory.
            if (!f.isDirectory()) {
                f = f.getParentFile();
            }
            System.out.println("Selected directory for import " + f);
            path = "" + f;
        }
        return path;
    }

    private void upload(String filename) {
        File output = new File(Paths.get(localPath, filename).toString());
        CopyTask task = new CopyTask(anch, output.length(), progress, localFM, remoteFM, filename);
        progress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
        progress.setVisible(true);
        anch.setDisable(true);
    }

    private void download(String filename) {
        File output = new File(Paths.get(remotePath, filename).toString());
        CopyTask task = new CopyTask(anch, output.length(), progress, remoteFM, localFM, filename);
        progress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
        progress.setVisible(true);
        anch.setDisable(true);
        invalidate();
    }
}