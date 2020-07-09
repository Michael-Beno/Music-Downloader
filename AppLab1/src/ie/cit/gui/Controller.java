/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.cit.gui;

import ie.cit.folders.FolderMonitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;

/**
 *
 * @author Mick
 */
public class Controller {

    protected FolderMonitor remoteFM, localFM;
    protected String remotePath = "", localPath = "";
    protected String[] arrLocal;
    protected String[] arrRemote;
    protected ArrayList<MyFile> aList;

    public Controller() {
        aList = new ArrayList<>();
        Timeline time = new Timeline();
        time.setCycleCount(Timeline.INDEFINITE);
        time.getKeyFrames().add(new KeyFrame(Duration.millis(5000), (ActionEvent event) -> {
            remoteFM.checkRemoteFolderStatus();
            if (remoteFM.isChange()) {
                invalidate();
            }
        }));time.setCycleCount(Timeline.INDEFINITE);
        time.playFromStart();
    }

    public void invalidate() {

        aList.clear();
        arrLocal = localFM.getNames();
        arrRemote = remoteFM.getNames();
        if (arrLocal.length == 0) {
            for (String arrRemote1 : arrRemote) {
                aList.add(new MyFile(arrRemote1, 0)); // Download status 0
            }
        } else if (arrRemote.length == 0) {
            for (String arrLocal1 : arrLocal) {
                aList.add(new MyFile(arrLocal1, 2)); // Upload, Play status 2
            }
        } else {
            for (String arrLocal1 : arrLocal) {
                aList.add(new MyFile(arrLocal1, 2)); // Upload, Play status 2
            }

            boolean q;
            for (String arrRemote1 : arrRemote) {
                q = false;
                for (int a = 0; a < aList.size(); a++) {
                    if (arrRemote1.equals(aList.get(a).getName())) {
                        aList.get(a).setStatus(1);// = 1; // Play status 1
                        q = true;
                        break;
                    }
                }
                if (!q) {
                    aList.add(new MyFile(arrRemote1, 0)); // Download status 0
                }
            }
        }
        Collections.sort(aList, new SortByName());
    }

    class SortByName implements Comparator<MyFile> {
        
        @Override
        public int compare(MyFile a, MyFile b) {
            return a.getName().compareTo(b.getName());
        }
    }

    class MyFile {

        private final String filename;
        private int status;

        public MyFile(String filename, int status) {
            this.filename = filename;
            this.status = status;
        }
        public String getName() {
            return filename;
        }

        public int getStatus() {
            return status;
        }

        private void setStatus(int i) {
            status = i;
        }
    }
}
