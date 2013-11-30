/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.knowde.jsolver.core;

/**
 *
 * @author msriti
 */
public class Model {
    
    String mProblemName;
    
    //Only problem literals provided in the model
    int [] mModelLiterals;
    
    // All problem literals
    int [] mProblemLiterals;
    
    long mTime;
    
    public Model(String problem, int [] lits){
        setProblemName(problem);
        setLiterals(lits);
    }
    
    private void setProblemName(String name){
        mProblemName = name;
    }

    private void setLiterals(int[] literals) {
        mModelLiterals = new int[literals.length];
        System.arraycopy(literals, 0, mModelLiterals, 0, literals.length);
    }
    
    public String getProblemName(){
        return mProblemName;
    }
    
    @Override
    public String toString(){
        String str = "";
        for (int i=0; i<mModelLiterals.length; i++){
            str += mModelLiterals[i] + " ";
        }
        return str;
    }
    /*
    public int[][] computeDerivedModels(){
        int combinatory = factorial(mProblemLiterals.length - mModelLiterals.length)
        int models = new int[combinatory][mProblemLiterals.length]
                
    }
    */
    
    public void setTime(long time){
        mTime = time;
    }
    
    public long getTime(){
        return mTime;
    }
    
}
