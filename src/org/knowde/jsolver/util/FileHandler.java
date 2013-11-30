package org.knowde.jsolver.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileHandler {
    
    int counter;
    
    File mFile = null;
    
    BufferedReader mReader = null;
    
    BufferedWriter mWriter = null;
    
    InputStreamReader mInputStream = null;
    
    OutputStreamWriter mOutputStream = null;

    public FileHandler(File file) {
        mFile = file;
    }

    public FileHandler(File file, boolean forRead) throws IOException {
        this(file);
        if(!file.exists()){
            new File(file.getAbsolutePath().replace(file.getName(),"")).mkdirs();
            file.createNewFile();
        }

        if (mFile.isFile())
                open (forRead);
    }

    public FileHandler(String filepath) throws IOException {
            this(new File(filepath));
    }

    public FileHandler(String filepath, boolean forRead) throws IOException {
            this(new File(filepath), forRead);
    }

    private void open(boolean forRead) throws FileNotFoundException, UnsupportedEncodingException {
            if(forRead)
                openForRead();
            else 
                openForWrite();
    }

    private void openForRead() throws FileNotFoundException, UnsupportedEncodingException {
        FileInputStream fis = new FileInputStream(mFile);
        mInputStream = new InputStreamReader(fis, "UTF-8");
        mReader = new BufferedReader(mInputStream);
    }

    private void openForWrite() throws FileNotFoundException, UnsupportedEncodingException {
        FileOutputStream fos = new FileOutputStream(mFile);
        mOutputStream = new OutputStreamWriter(fos , "UTF-8");
        mWriter = new BufferedWriter(mOutputStream);
    }

    public String nextLine() throws IOException {
        return (mReader!=null? mReader.readLine() : null);
    }

    public void writeLine(String line) throws IOException {
        if (mWriter !=null) {
            mWriter.write(line);
            mWriter.newLine();
        }
    }
    public void close() throws IOException{
        if (mInputStream != null)
            mInputStream.close();
        
        if (mOutputStream != null){
            if (mWriter!=null) 
                mWriter.flush();
            mOutputStream.close();    
        }
    }

    public List<File> listChildren(boolean recursively){
        if (recursively)
            return listChildren(mFile);
        else
            return Arrays.asList(mFile.listFiles());
    }

    private List<File> listChildren(File file){
        ArrayList<File> list = new ArrayList<>();
        if (file.isFile()){
            list.add(file);
                //System.out.println("[FILE:"+ ++counter+"] " + file.getName());
        }
        else if (file.isDirectory()) {
            //System.out.println("[DIR] " + file.getName());
            File[] listOfFiles = file.listFiles();
            if (listOfFiles != null) {
                for (File childFile : listOfFiles) {
                    list.addAll(listChildren(childFile));
                }
            } else {
                System.out.println(" [ACCESS DENIED]");
            }
        }
        return list;
    }
    
    public String getFilePath(){
        return mFile.getAbsolutePath();
    }
    
    public String getFileName(){
        return mFile.getName();
    }

}
