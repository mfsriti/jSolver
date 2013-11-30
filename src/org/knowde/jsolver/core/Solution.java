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
public class Solution {
    
    String mProblemName;
    
    //Only problem literals provided in the solution
    int [] mSolutionLiterals;
    
    // All problem literals
    int [] mProblemLiterals;
    
    public Solution(String problem, int [] lits){
        setProblemName(problem);
        setLiterals(lits);
    }
    
    private void setProblemName(String name){
        mProblemName = name;
    }

    private void setLiterals(int[] literals) {
        mSolutionLiterals = new int[literals.length];
        System.arraycopy(literals, 0, mSolutionLiterals, 0, literals.length);
    }
    
    public String getProblemName(){
        return mProblemName;
    }
    
    @Override
    public String toString(){
        String str = "";
        for (int i=0; i<mSolutionLiterals.length; i++){
            str += mSolutionLiterals[i] + " ";
        }
        return str;
    }
    /*
    public int[][] computeDerivedSolutions(){
        int combinatory = factorial(mProblemLiterals.length - mSolutionLiterals.length)
        int solutions = new int[combinatory][mProblemLiterals.length]
                
    }
    */
    
}
