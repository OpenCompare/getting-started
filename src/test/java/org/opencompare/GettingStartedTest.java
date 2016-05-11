package org.opencompare;

import org.junit.Test;
import org.opencompare.api.java.*;
import org.opencompare.api.java.extractor.CellContentInterpreter;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.CSVExporter;
import org.opencompare.api.java.io.CSVLoader;
import org.opencompare.api.java.io.PCMLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by gbecan on 02/12/15.
 */
public class GettingStartedTest {


    private final static String csvTargetFolder = "csvs";

    @Test
    public void testPokemonCSV() throws IOException {

        CSVLoader csvL = new CSVLoader(
                new PCMFactoryImpl(),
                new CellContentInterpreter(new PCMFactoryImpl()));

        List<PCMContainer> pcms = csvL.load(new File("pcms/pokemon.csv"));
        assertEquals(pcms.size(), 1);
        PCM pcm = pcms.get(0).getPcm();
        assertNotNull(pcm);
        System.err.println("pcm:" + pcm);
        List<Feature> cfs = pcm.getConcreteFeatures();
        for (Feature ft : cfs) {
            System.err.println("" + ft.getName());
        }


        assertEquals(pcm.getProducts().size(), 811);


    }

    @Test
    public void testCSVarchive() throws IOException {

        File dir = new File("csvs/");
        assertTrue(dir.isDirectory());

        File[] pcms = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".csv");
            }
        });

        assertTrue(pcms.length > 0);

        int i = 0;
        for (File pcmFile : pcms) {

            CSVLoader loader = new CSVLoader(
                    new PCMFactoryImpl(),
                    new CellContentInterpreter(new PCMFactoryImpl()));
            List<PCMContainer> pcmContainers = loader.load(pcmFile);
            for (PCMContainer pcmContainer : pcmContainers) {
                PCM pcm = pcmContainer.getPcm();
                assertNotNull(pcm);
                i++;
            }
        }

        assertEquals(i, pcms.length);

        System.err.println("#pcms (CSVS)=" + i);



    }

    @Test
    public void testPCMarchive() throws IOException {

        File dir = new File("/Users/macher1/Downloads/model/");
        assertTrue(dir.isDirectory());

        File[] pcms = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".pcm");
            }
        });

        assertTrue(pcms.length > 0);

        int i = 0;
        for (File pcmFile : pcms) {

            PCMLoader loader = new KMFJSONLoader();
            List<PCMContainer> pcmContainers = loader.load(pcmFile);
            for (PCMContainer pcmContainer : pcmContainers) {
                PCM pcm = pcmContainer.getPcm();
                assertNotNull(pcm);
                i++;

                String csv = new CSVExporter().export(pcmContainer);
                FileWriter fw = new FileWriter(new File(csvTargetFolder + "/" + pcmFile.getName().replace(".pcm", ".csv")));
                fw.write(csv);
                fw.close();
            }
        }

        assertEquals(i, pcms.length);

        System.err.println("#pcms=" + i);



    }

    @Test
    public void testGettingStarted() throws IOException {

        // Define a file representing a PCM to load
        File pcmFile = new File("pcms/example.pcm");

        // Create a loader that can handle the file format
        PCMLoader loader = new KMFJSONLoader();

        // Load the file
        // A loader may return multiple PCM containers depending on the input format
        // A PCM container encapsulates a PCM and its associated metadata
        List<PCMContainer> pcmContainers = loader.load(pcmFile);

        for (PCMContainer pcmContainer : pcmContainers) {

            // Get the PCM
            PCM pcm = pcmContainer.getPcm();

            // Browse the cells of the PCM
            for (Product product : pcm.getProducts()) {
                for (Feature feature : pcm.getConcreteFeatures()) {

                    // Find the cell corresponding to the current feature and product
                    Cell cell = product.findCell(feature);

                    // Get information contained in the cell
                    String content = cell.getContent();
                    String rawContent = cell.getRawContent();
                    Value interpretation = cell.getInterpretation();

                    // Print the content of the cell
                    System.out.println("(" + product.getKeyContent() + ", " + feature.getName() + ") = " + content);
                }
            }

            // Export the PCM container to CSV
            CSVExporter csvExporter = new CSVExporter();
            String csv = csvExporter.export(pcmContainer);

            // Write CSV content to file
            Path outputFile = Files.createTempFile("oc-", ".csv");
            Files.write(outputFile, csv.getBytes());
            System.out.println("PCM exported to " + outputFile);
        }

    }
}
