/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.cit.folders;

import ie.cit.filesystem.Files;



/**
 *
 * @author Mick
 */
public class LocalFolder implements FolderMonitor{

    private static LocalFolder INSTANCE;
    private Files files;
    
    private LocalFolder(){}
    
    private LocalFolder(String path){
        files = new Files();
        files.setPath(path);
    }
    
    public static LocalFolder getInstance(String path) {
        if(INSTANCE == null) {
            INSTANCE = new LocalFolder(path);
        }
        return INSTANCE;
    }
    
    @Override
    public String[] getNames() { return files.getFiles(); }
    @Override
    public boolean isEOF() { return files.isEndOfFile(); }
    @Override
    public boolean openFileIn(String name) { return files.openFileIn(name);}
    @Override
    public byte getB() { return files.getB(); }
    @Override
    public boolean closeFileIn(String name) { return files.closeFileIn(name); }
    @Override
    public boolean isChange() { return files.isChange(); }
    @Override
    public void openFileOut(String name) { files.openFileOut(name); }
    @Override
    public void putB(byte b) { files.putB(b); }
    @Override
    public void closeFileOut(String name) { files.closeFileOut(name); }
    @Override
    public void setChange(boolean b) { files.setChange(b); }
    @Override
    public void checkRemoteFolderStatus() { throw new UnsupportedOperationException("Not supported yet."); }
    @Override
    public void setPath(String path) { files.setPath(path); }
}
