/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.cit.filesystem;

import ie.cit.folders.FolderMonitor;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Mick
 */
public class CopyTask extends Task<Long> {

    private final FolderMonitor inputFolder;
    private final FolderMonitor outputFolder;
    private final AnchorPane vBoxLocal;
    private final ProgressBar bar;
    private final Long size;
    private final String name;


    public CopyTask(AnchorPane anch, long length, ProgressBar progress,
            FolderMonitor input, FolderMonitor output, String name) {
        this.vBoxLocal = anch;
        size = length;
        bar = progress;
        inputFolder = input;
        outputFolder = output;
        this.name = name;

    }

    @Override
    protected Long call() throws Exception {
        long i = 0;
        inputFolder.openFileIn(name);
        outputFolder.openFileOut(name);
        System.out.println(size);
        while (!inputFolder.isEOF()) {
            i++;
            updateProgress(i, size);
            outputFolder.putB(inputFolder.getB());
            if (isCancelled()) {
                return i;
            }
        }
        inputFolder.closeFileIn(name);
        outputFolder.closeFileOut(name);
        bar.setVisible(false);
        vBoxLocal.setDisable(false);
        return size;
    }

}
