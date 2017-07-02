package edu.rice.cs.caper.bayou.core.synthesizer;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SynthesizerTest {

   // String testDir = "/Users/vijay/Work/bayou/src/test/resources/synthesizer";
   // String classpath = "/Users/vijay/Work/bayou/tool_files/build_scripts/out/resources/artifacts/classes:/Users/vijay/Work/bayou/tool_files/build_scripts/out/resources/artifacts/jar/android.jar";

    void testExecute(String test) throws IOException
    {
        File projRoot = new File(System.getProperty("user.dir")).getParentFile().getParentFile().getParentFile();
        File srcFolder = new File(projRoot.getAbsolutePath() + File.separator + "src");
        File mainResourcesFolder = new File(srcFolder.getAbsolutePath() + File.separator + "main" + File.separator +
                "resources");

        File artifactsFolder = new File(mainResourcesFolder + File.separator + "artifacts");

        String classpath;
        {
            File classesFolder = new File(artifactsFolder.getAbsolutePath() + File.separator + "classes");
            File androidJar = new File(artifactsFolder.getAbsolutePath() + File.separator + "jar" + File.separator +
                    "android.jar");

            if(!classesFolder.exists())
                throw new IllegalStateException();

            if(!androidJar.exists())
                throw new IllegalStateException();

            classpath = classesFolder.getAbsolutePath() + File.pathSeparator + androidJar.getAbsolutePath();
        }

        String testDir = srcFolder.getAbsolutePath() + File.separator + "test" + File.separator + "resources" +
                File.separator + "synthesizer";


        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bout);
        Synthesizer synthesizer = new Synthesizer(out);

        String code = new String(Files.readAllBytes(Paths.get(String.format("%s/%s.java", testDir, test))));
        String asts = new String(Files.readAllBytes(Paths.get(String.format("%s/%s.json", testDir, test))));

        synthesizer.execute(code, asts, classpath);
        String content = new String(bout.toByteArray(), StandardCharsets.UTF_8);

        Assert.assertTrue(content.contains("public class")); // some code was synthesized
    }

    @Test
    public void testIO1() throws IOException {
        testExecute("TestIO1");
    }

    @Test
    public void testIO2() throws IOException {
        testExecute("TestIO2");
    }

    @Test
    public void testBluetooth() throws IOException {
        testExecute("TestBluetooth");
    }

    @Test
    public void testDialog() throws IOException {
        testExecute("TestDialog");
    }

    @Test
    public void testCamera() throws IOException {
        testExecute("TestCamera");
    }

    @Test
    public void testWifi() throws IOException {
        testExecute("TestWifi");
    }

    @Test
    public void testSpeech() throws IOException {
        testExecute("TestSpeech");
    }
}