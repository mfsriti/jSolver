/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.knowde.jsolver.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author msriti
 */
public class Problem {
    String mName;
    Variable[] mVariables;
    //List <Constraint> mConstraints;
    //List<Constraint>[] mConstraints;
    Map<String,List<Constraint>> mConstraints;
    static Problem sProblem;
    
    public static Problem getInstance(){
        return sProblem;
    }
    
    protected Problem(String name){
        setName(name);
        //mConstraints = new ArrayList<>();
        //mConstraints = new ArrayList[4];
        //mConstraints[0] = new ArrayList<>();
        //mConstraints[1] = new ArrayList<>();
        //mConstraints[2] = new ArrayList<>();
        //mConstraints[3] = new ArrayList<>();
        mConstraints = new HashMap<>();
        setInstance(this);
    }

    private static void setInstance(Problem problem){
        sProblem = problem;
    }
    private void setName(String name){
        mName = name;
    }
    
    public String getName(){
        return mName;
    }
    
    public Literal findLiteral(int number){
        Literal lit;
        Variable var = mVariables[(number>=0?number:-number)-1];
        if (number>=0){
            lit = var.getPositiveLiteral();
        } else {
            lit = var.getNegativeLiteral();
        }
        return lit;
    } 

    //public Constraint findConstraint(int number){
       // return mConstraints.get(Object.VARIABLE_PREFIX+number);
    //}
    
    public void setVariables(int[] vars) {
        mVariables = new Variable [vars.length];
        for (int i=0; i<vars.length; i++){
            Variable var = new Variable(vars[i]);
            mVariables[i]= var;
        }
    }

    /*public Map<String,Constraint> getConstraints() {
        return mConstraints;
    }*/
    
    public Variable[] getVariables() {
        return mVariables;
    }

    // used initially when adding constraints
    public void addConstraint(int[] literals) {
        Constraint con = new Constraint(this,literals);
        addConstraint(con);
    }
    
    // used when exploring
    public void removeConstraint(Constraint con) {
        mConstraints.get(Integer.toString(con.getLength())).remove(con);
        //mConstraints[con.getLength()].remove(con);
    }
    
    // used when exploring
    public void addConstraint(Constraint con) {
        String key = Integer.toString(con.getLength());
        List<Constraint> list = mConstraints.get(key);
        if (list==null){
            list = new ArrayList<>();
            mConstraints.put(key, list);
        }
        list.add(con);
        //mConstraints[con.getLength()].add(con);
    }
    
    public boolean solved(){
        for (List l: mConstraints.values()){
            if (!l.isEmpty()){
                return false;
            }
        }
        return true;
        //return (mConstraints[0].isEmpty() && mConstraints[1].isEmpty()&& mConstraints[2].isEmpty()&& mConstraints[3].isEmpty());
    }

    public Constraint getSmallestConstraint() {
        String[] strKeys = mConstraints.keySet().toArray(new String[0]);
        int[] keys = new int[strKeys.length];
        for (int i=0; i < strKeys.length; i++) {
            keys[i] = Integer.parseInt(strKeys[i]);
        }
        Arrays.sort(keys);
        for (int k: keys){
            List l = mConstraints.get(Integer.toString(k));
            if (l!=null && !l.isEmpty()){
                return (Constraint)l.remove(0);
            }        
        }
        return null;
    }
    
    protected List<Constraint> getSmallestConstraints() {
        String[] strKeys = mConstraints.keySet().toArray(new String[0]);
        int[] keys = new int[strKeys.length];
        for (int i=0; i < strKeys.length; i++) {
            keys[i] = Integer.parseInt(strKeys[i]);
        }
        Arrays.sort(keys);
        for (int k: keys){
            List l = mConstraints.get(Integer.toString(k));
            if (l!=null && !l.isEmpty()){
                return l;
            }        
        }
        return null;
    }
    
    public Literal selectLiteral(){
        Literal selectedLiteral = null;
        // list des contraintes le plus petites
        List<Constraint> smallestCons = getSmallestConstraints();
        // calculer le nombre de literaux
        List<Literal> literals = getRemainingLiteralsFromConstraints(smallestCons,true);
        // calculer les formules: au meme temps la valeur max
        // choisir un literal
        selectedLiteral = getLiteralFromMOM(literals);
        // remettre les compteurs a zero      
        resetLiteralsCounters(literals);
        return selectedLiteral;
    } 

    private List<Literal> getRemainingLiteralsFromConstraints(List<Constraint> constraints, boolean withCounting) {
       List<Literal> result = new ArrayList<>();
       for (Constraint con: constraints){
           Literal[] literals = con.getLiterals();
           for(Literal lit: literals){
               if (!lit.isSelected() && !lit.isInverseLiteralSelected()){
                    if (!result.contains(lit)){
                        result.add(lit);
                    } 
                    if (withCounting){
                        lit.incrementCounter();
                    }
               }
           }
       }
       return result;
    }
    
    private void resetLiteralsCounters(List<Literal> literals){
        for (Literal lit: literals){
            lit.resetCounter();
        }
    }

    private Literal getLiteralFromMOM(List<Literal> literals) {
        Literal literalWithMaxValue = null;
        int maxValue=0;
        for (Literal lit: literals){
            int formulaValue =(lit.getCounter() + lit.getInverseLiteral().getCounter()) * 1
                    + (lit.getCounter() * lit.getInverseLiteral().getCounter()) ; 
            if (formulaValue>maxValue){
                maxValue = formulaValue;
                literalWithMaxValue = lit;
            }
        }
        literalWithMaxValue = (literalWithMaxValue.getCounter() > 
                literalWithMaxValue.getInverseLiteral().getValue() ?
                literalWithMaxValue : literalWithMaxValue.getInverseLiteral());
        return literalWithMaxValue;
        
    }
}
