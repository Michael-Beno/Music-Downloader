/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.cit.folders;


import ie.cit.filesystem.Files;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Mick
 */
public class RemoteFolder implements FolderMonitor, Observer {

    private static RemoteFolder INSTANCE;
    private static Files files;

    private RemoteFolder() {}

    public static RemoteFolder getInstance(String path) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteFolder();
            files = new Files(); //Observable
            files.setPath(path);
            files.addObserver(INSTANCE); //adding observer
        }
        return INSTANCE;
    }

    @Override
    public void update(Observable observable, Object arg) {
        System.out.println("Observer updated");
        files = (Files) observable;
    }
    @Override
    public String[] getNames() {return files.getFiles();}
    @Override
    public boolean isEOF() { return files.isEndOfFile();}
    @Override
    public boolean openFileIn(String name) { return files.openFileIn(name); }
    @Override
    public byte getB() { return files.getB(); }
    @Override
    public boolean closeFileIn(String name) {
        System.out.println("Closed fileIN checking status");
        files.checkRemoteFolderStatus();
        return files.closeFileIn(name);
    }
    @Override
    public boolean isChange() { return files.isChange(); }
    @Override
    public void openFileOut(String name) {
        System.out.println("Closed fileOUT checking status");
        files.checkRemoteFolderStatus();
        files.openFileOut(name);
    }
    @Override
    public void putB(byte b) { files.putB(b); }
    @Override
    public void closeFileOut(String name) { files.closeFileOut(name);}
    @Override
    public void setChange(boolean b) { files.setChange(b); }
    @Override
    public void checkRemoteFolderStatus() { files.checkRemoteFolderStatus(); }
    @Override
    public void setPath(String path) { files.setPath(path); }
}