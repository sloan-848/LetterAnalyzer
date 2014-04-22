import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

import components.map.Map;
import components.map.Map1L;
import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Put a short phrase describing the program here.
 * 
 * @author Put your name here
 * 
 */
public final class Analyzer {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Analyzer() {
    }

    private static Map<String, Integer> analyze(File file, String[] key)
            throws IOException {
        Map<String, Integer> stats = new Map1L<String, Integer>();
        String[] letters = new String[key.length];
        int[] counts = new int[key.length];
        for (int i = 0; i < letters.length; i++) {
            letters[i] = key[i];
            counts[i] = 0;
        }

        try (BufferedReader reader = Files.newBufferedReader(file.toPath(),
                Charset.defaultCharset())) {
            String tempLine = null;
            while ((tempLine = reader.readLine()) != null) {
                String[] temp = tempLine.split("");
                for (int i = 0; i < temp.length; i++) {
                    for (int j = 0; j < letters.length; j++) {
                        if (temp[i].toLowerCase().equals(letters[j])) {
                            counts[j]++;
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < letters.length; i++) {
            stats.add(letters[i], counts[i]);
        }

        return stats;
    }

    /**
     * Main method.
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        String[] key = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
                "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
                "x", "y", "z" };
        NaturalNumber[] counts = new NaturalNumber1L[key.length];
        for (int i = 0; i < counts.length; i++) {
            counts[i] = new NaturalNumber1L(0);
        }
        String hosts[] = { "ftp://indian.cse.msu.edu/pub/mirrors/Gutenberg/",
                "ftp://opensource.nchc.org.tw/gutenberg/",
                "http://etext.library.adelaide.edu.au/pg/",
                "http://www.gutenberg.org/dirs/",
                "ftp://ibiblio.org/pub/docs/books/gutenberg",
                "http://library.beau.org/gutenberg",
                "ftp://snowy.arsc.alaska.edu/mirrors/gutenberg",
                "http://snowy.arsc.alaska.edu/gutenberg",
                "ftp://ftp.cise.ufl.edu/pub/mirrors/gutenberg",
                "http://gutenberg.mirrors.tds.net/pub/gutenberg.org",
                "ftp://gutenberg.mirrors.tds.net/pub/gutenberg.org",
                "http://www.bsu.edu/libraries/gutenberg",
                "http://mirrors.xmission.com/gutenberg",
                "ftp://mirrors.xmission.com/gutenberg",
                "http://www.knowledgerush.com/gutenberg",
                "ftp://gutenberg.readingroo.ms/gutenberg",
                "http://gutenberg.cs.uiuc.edu/" };
        int cHost = 0;
        boolean failed = false;
        int failCount = 0;

        String outFN = "data/output.txt";
        /*
         * Get input from user out.print("Put in that filename: "); String fileN
         * = in.nextLine();
         */

        out.println("Starting Analysis");
        //Begin automatic string
        for (int i = 10001; i < 45444; i++) {
            out.println("Processing #" + i);
            File tempFile = new File("data/temp.txt");
            URL tempURL = null;
            try {
                tempURL = new URL(hosts[cHost]
                        + DirectoryBuilder.getDirectory(i));
            } catch (MalformedURLException e) {
                out.println("You suck at putting in URLS.");
                e.printStackTrace();
            }

            if (tempURL != null) {
                try {
                    org.apache.commons.io.FileUtils.copyURLToFile(tempURL,
                            tempFile, 2000, 2000);
                } catch (IOException failure) {
                    out.println("ERROR: " + failure.toString());
                    failCount++;
                    failed = true;
                }
            }

            Map<String, Integer> finalMap = null;
            try {
                finalMap = analyze(tempFile, key);
            } catch (IOException fail) {
                out.println("File cannot be opened");
                failCount++;
            }

            if (finalMap != null) {
                for (int j = 0; j < key.length; j++) {
                    counts[j].add(new NaturalNumber1L(finalMap.value(key[j])));
                }
            }
            out.println("Failures: " + failCount);

            if (failed) { //if the request failed, try to read the same file again
                i--;
                failed = false;
            }

            if (failCount > 30) {
                System.out.println("The host at " + hosts[cHost]
                        + " is tapped out.");
                if (cHost + 1 < hosts.length) {
                    cHost++;
                } else {
                    cHost = 0;
                }
                System.out.println("Moving on to host at " + hosts[cHost]);
                failCount = 0;
                i++;
            }

            //Print statistics to file
            File oldF = new File(outFN);
            oldF.delete();
            SimpleWriter newF = new SimpleWriter1L(outFN);
            newF.println("Statistics");
            newF.println("-----------------------------------");
            for (int j = 0; j < key.length; j++) {
                newF.println(key[j] + ": " + counts[j]);
            }
            newF.println("Files Scanned: " + (i - 10000));
            newF.close();

            tempFile.delete();
        }
        //End auto string

        out.println("Finished analysis.");
        //Print Statistics

        in.close();
        out.close();
    }
}
