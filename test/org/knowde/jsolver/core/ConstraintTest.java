/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.knowde.jsolver.core;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author msriti
 */
public class ConstraintTest {
    
    public ConstraintTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetLength() {
        System.out.println("getLength");
        Constraint instance = new Constraint(new int[]{-1,-2,-3});
        int expResult = 3;
        int result = instance.getLength();
        assertEquals(expResult, result);
    }
}
