package com.mistno.minewrap.backup;

import java.io.*;
import java.util.*;

import org.apache.commons.compress.archivers.zip.*;
import org.hamcrest.Matchers;
import org.junit.*;

import com.mistno.minewrap.StateHolder;
import com.mistno.minewrap.config.Config;

public class ZipBackupTest {

    private static final String TEST_DATA_DIR = "/ZipBackupTestData/";
    private static final String RANDOM_STRING = "bivNPiEWgf";
    private static final String CONFIG = "server.jar=" + RANDOM_STRING + "\n"
            + "server.xmx=" + RANDOM_STRING + "\n"
            + "server.xms=" + RANDOM_STRING + "\n"
            + "backup.zip.enabled=true\n"
            + "backup.zip.exclude=excluded_folder\n"
            + "backup.external.enabled=" + RANDOM_STRING + "\n"
            + "backup.external.path=" + RANDOM_STRING;

    private ZipBackup tested;
    private StateHolder stateHolder;

    @Before
    public void setupDefaults() throws Exception {
        String testPath = getResourcePath();
        String config = CONFIG + "\n"
                + "backup.zip.sourceDir=" + testPath + TEST_DATA_DIR + "\n"
                + "backup.zip.targetDir=" + testPath;

        ByteArrayInputStream bais = new ByteArrayInputStream(config.getBytes());
        Config.initialize(bais);

        stateHolder = new StateHolder();
    }

    @Test
    public void addOnlyIncludedFilesToZip() throws Exception {
        tested = new ZipBackup(stateHolder);
        tested.performBackup();

        ZipFile zipArchive = new ZipFile(findZipFile());
        List<String> filesInArchive = getFilesInZipArchive(zipArchive);
        zipArchive.close();

        Assert.assertThat(filesInArchive, Matchers.contains("file.1", "included_folder/file.2"));
        Assert.assertThat(filesInArchive, Matchers.not(Matchers.contains("excluded_folder/file.3")));
    }

    private List<String> getFilesInZipArchive(ZipFile zipArchive) {
        List<String> files = new ArrayList<>();
        Enumeration<ZipArchiveEntry> entries = zipArchive.getEntries();
        while (entries.hasMoreElements()) {
            files.add(entries.nextElement().getName());
        }
        return files;
    }

    private File findZipFile() throws Exception {
        for (File file : new File(getResourcePath()).listFiles()) {
            if (file.getName().contains(".zip")) {
                return file;
            }
        }
        return null;
    }

    @After
    public void tearDown() throws Exception {
        for (File file : new File(getResourcePath()).listFiles()) {
            if (file.getName().contains(".zip")) {
                file.delete();
            }
        }
    }

    private String getResourcePath() throws Exception {
        ClassLoader loader = getClass().getClassLoader();
        String file = loader.getResource("anchor").getFile();
        return file.substring(1, file.indexOf("/anchor"));
    }
}
