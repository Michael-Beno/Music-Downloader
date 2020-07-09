package ie.cit.folders;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Mick
 */
public interface FolderMonitor {
    
    boolean isEOF();  // have we have reached the end of the file being read?
    String[] getNames();  // returns the names of all the files in folder
    boolean openFileIn(String name);  // opens a file called name
    byte getB();   // Gets a byte from the currently open file
    void putB(byte b);   // Puts byte to the output file
    boolean closeFileIn(String name);  // closes the open file
    boolean isChange();	// has a new file(s) has been added?

    public void openFileOut(String name);

    public void closeFileOut(String name);

    public void setChange(boolean b);

    public void checkRemoteFolderStatus();

    public void setPath(String localPath);
}
