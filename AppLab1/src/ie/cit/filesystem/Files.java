/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.cit.filesystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mick
 */
public class Files extends Observable {

    private String path;
    private String[] lastArr;
    private String nameOfFile = "";
    private DataInputStream din = null;
    private DataOutputStream dout = null;
    private boolean isEOF = false;
    private boolean isChange = false;

    public Files() {
        path = "";
        lastArr = new String[0];
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String[] getFiles() {
        if (path == null || path.equals("")) {
            return new String[0];
        }

        ArrayList<String> lof = new ArrayList();
        File folder = new File(path);
        try {
            File[] listOfFiles = folder.listFiles();
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile() && !listOfFile.toString().contains(".tmp")) {
                    lof.add(listOfFile.getName());
                }
            }
        } catch (Exception e) {
        }
        //TODO generate from Arrays in one line
        String[] arr = new String[lof.size()];
        for (int i = 0; i < lof.size(); i++) {
            arr[i] = lof.get(i);
        }
        return arr;
    }

    public void checkRemoteFolderStatus() {
        isChange = false;
        String[] tmpArr = getFiles();
        if (tmpArr.length != lastArr.length) {
            lastArr = tmpArr;
            isChange = true;
            setChanged();
            notifyObservers();
            System.out.println("check1");
        } else {
            for (int i = 0; i < lastArr.length; i++) {
                if (!lastArr[i].equals(tmpArr[i])) {
                    lastArr = tmpArr;
                    isChange = true;
                    setChanged();
                    notifyObservers();
                    System.out.println("check2");
                    return;
                }
            }
        }
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean b) {
        isChange = b;
    }
// Methods for opening/closing streams, copy(put/get), end of file

    public boolean isEndOfFile() {
        return isEOF;
    }

    public boolean openFileIn(String name) {
        din = null;
        try {
            din = new DataInputStream(new FileInputStream(new File(Paths.get(path, name).toString())));
            nameOfFile = name;
            isEOF = false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
            din = null;
            return false;
        }

        return true;
    }

    public byte getB() {
        int c = -1;
        try {
            isEOF = (c = din.read()) == -1;
        } catch (IOException ex) {
            Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (byte) c;
    }

    public boolean closeFileIn(String name) {

        if (name.equals(nameOfFile)) {
            try {
                din.close();
                nameOfFile = "";
            } catch (IOException ex) {
                Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public void putB(byte b) {
        try {
            dout.write(b);
        } catch (IOException ex) {
            Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean openFileOut(String name) {
        dout = null;
        try {
            dout = new DataOutputStream(new FileOutputStream(new File(Paths.get(path, name).toString())));
            nameOfFile = name;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
            din = null;
            return false;
        }

        return true;
    }

    public boolean closeFileOut(String name) {

        if (name.equals(nameOfFile)) {
            try {
                dout.close();
                nameOfFile = "";
            } catch (IOException ex) {
                Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
