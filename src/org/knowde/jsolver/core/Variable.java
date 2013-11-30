/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.knowde.jsolver.core;

/**
 *
 * @author msriti
 */
public class Variable extends Object{
   
   // The value of the variable without sign (+ or -)
   int mValue;
   
   Literal mPositiveLiteral;
   
   Literal mNegativeLiteral;
   
   public Variable(int number){
       setValue(number);
       mPositiveLiteral = new Literal(this,number);
       mNegativeLiteral = new Literal(this,number*(-1));
   }
   
   public Literal getPositiveLiteral(){
       return mPositiveLiteral;
   }
   
   public Literal getNegativeLiteral(){
       return mNegativeLiteral;
   }
   
   private void setValue(int number){
       mValue = number;
   }
   
   public int getValue(){
       return mValue;
   }
   
   public boolean isNegativeLiteralSelected(){
       return mNegativeLiteral.isSelected();
   }  
   
   public boolean isPositiveLiteralSelected(){
       return mPositiveLiteral.isSelected();
   }
   
    public void selectNegativeLiteral(){
       mNegativeLiteral.select();
    }
    
    public void selectPositiveLiteral(){
       mPositiveLiteral.select();
    }
    
    public void unselectNegativeLiteral(){
       mNegativeLiteral.unselect();
    }
    
    public void unselectPositiveLiteral(){
       mPositiveLiteral.unselect();
    }  
    
    public boolean explored(){
       return ( isPositiveLiteralSelected() && isNegativeLiteralSelected() );
    }
    
    public void selectLiterals(){
        selectNegativeLiteral();
        selectPositiveLiteral();
    }
    
    public void unselectLiterals(){
        unselectNegativeLiteral();
        unselectPositiveLiteral();
    }
    
    public void removeNonExistingLiterals(){
        if (!mNegativeLiteral.gotConstraintsToSatisfy()) {
            mNegativeLiteral = null;
        }
        if (!mPositiveLiteral.gotConstraintsToSatisfy()) {
            mPositiveLiteral = null;
        }
        
    }
}