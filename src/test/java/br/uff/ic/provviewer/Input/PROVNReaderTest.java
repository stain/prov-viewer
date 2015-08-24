/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.provviewer.BasePath;
import br.uff.ic.provviewer.GUI.GuiRun;
import br.uff.ic.provviewer.GraphObject;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kohwalter
 */
public class PROVNReaderTest {
    
    public PROVNReaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of readFile method, of class PROVNReader.
     */
    @Test
    public void testReadFile() throws Exception {
        System.out.println("readFile");
        testFiles("bundles1.provn");
        testFiles("bundles2.provn");
        testFiles("container0.provn");
        testFiles("container1.provn");
        testFiles("container2.provn");
        testFiles("example-blog1.provn");
        testFiles("example-blog2.provn");
        testFiles("prov-family.provn");
        testFiles("prov-family-graphics.provn");
        testFiles("file-example1.asn");
        testFiles("file-example2.asn");
        testFiles("file-example3.asn");
        testFiles("compound-1HHB.pn");
        testFiles("paolo-test.pn");
        testFiles("primer.pn");
        testFiles("container3.prov");
        testFiles("prov-dm-example1.prov-asn");
        testFiles("sculpture.prov-asn");
        testFiles("w3c-publication1.prov-asn");
        testFiles("w3c-publication2.prov-asn");
        testFiles("w3c-publication3.prov-asn");
    }
    
    public void testFiles(String file) throws Exception {
        String path = File.separator + "Graph" + File.separator + "Test" + File.separator + file;
        File f = new File(BasePath.getBasePathForClass(GuiRun.class) + path);
        System.out.println("=================================================");
        System.out.println("Path: " + path);
        PROVNReader instance = new PROVNReader(f);
        instance.readFile();
    }

    /**
     * Test of Read method, of class PROVNReader.
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("Read");
        String line = "wasGeneratedBy(e2, a1, -, [ex:fct=\\\"save\\\"])";
        PROVNReader instance = new PROVNReader(null);
        instance.Read(line);
    }
}
