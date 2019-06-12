/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.media.j3d.BranchGroup;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Daria
 */
public class mainWindowNGTest {
    
    public mainWindowNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of utworzScene method, of class mainWindow.
     */
    @Test
    public void testUtworzScene() {
        System.out.println("utworzScene");
        mainWindow instance = new mainWindow();
        BranchGroup expResult = null;
        BranchGroup result = instance.utworzScene();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of swiatla method, of class mainWindow.
     */
    @Test
    public void testSwiatla() {
        System.out.println("swiatla");
        mainWindow instance = new mainWindow();
        instance.swiatla();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of robot method, of class mainWindow.
     */
    @Test
    public void testRobot() {
        System.out.println("robot");
        mainWindow instance = new mainWindow();
        instance.robot();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of obrot_A method, of class mainWindow.
     */
    @Test
    public void testObrot_A() {
        System.out.println("obrot_A");
        mainWindow instance = new mainWindow();
        instance.obrot_A();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of obrot_B method, of class mainWindow.
     */
    @Test
    public void testObrot_B() {
        System.out.println("obrot_B");
        mainWindow instance = new mainWindow();
        instance.obrot_B();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class mainWindow.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        mainWindow.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
