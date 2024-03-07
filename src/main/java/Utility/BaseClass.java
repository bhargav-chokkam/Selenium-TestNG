package Utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.lang3.time.DateUtils;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BaseClass {
    protected static Common com;
    private static Common suite;
    protected static Map<String, String> data;
    private static ExtentReports report;
    protected ExtentTest spark;

    private static void deleteOldFiles() {
        Date oldestAllowedFileDate = DateUtils.addDays(new Date(), -5); //minus days from current date
        File targetDir = new File(System.getProperty("user.dir") + "//reportBackup");
        Iterator<File> filesToDelete = FileUtils.iterateFiles(targetDir, new AgeFileFilter(oldestAllowedFileDate), null);
        //if deleting subdirs, replace null above with TrueFileFilter.INSTANCE
        while (filesToDelete.hasNext()) {
            FileUtils.deleteQuietly(filesToDelete.next());
        }  //I don't want an exception if a file is not deleted. Otherwise use filesToDelete.next().delete() in a try/catch
    }

    private static void deleteFolder() throws IOException {
        FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + "//report"));
        System.out.println("Report Folder Deleted");
    }

    private static void zipFolder() {
        String reportFile = System.getProperty("user.dir") + "//report";
        checkFolderIfExists(reportFile);
        String currentDateTime = getCurrentTime();
        String reportBackup = System.getProperty("user.dir") + "//reportBackup";
        checkFolderIfExists(reportBackup);
        try {
            FileOutputStream fos = new FileOutputStream(reportBackup + "//" + currentDateTime + ".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(reportFile);
            zipFile(fileToZip, fileToZip.getName(), zipOut);
            zipOut.close();
            fos.close();
            System.out.println("Old Test Result Report has Zipped and moved to reportBackUp Folder");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private static void checkFolderIfExists(String sourceFile) {
        File folder = new File(sourceFile);
        if (!folder.exists() && !folder.isDirectory()) {
            boolean msg = folder.mkdir();
            if (msg) {
                System.out.println(sourceFile + " does not exists. Created new directory!");
            }
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    private static String getCurrentTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String parsedDateTime = String.valueOf(localDateTime);
        String formatDateTime = parsedDateTime.replace(':', '-');
        return formatDateTime.replace('.', '-');
    }

    private static void setSystemOutToFile() throws IOException {
        File newFolder = new File(System.getProperty("user.dir") + "//report");
        File newFile = new File(newFolder + "//console.txt");
        if (newFile.createNewFile()) {
            System.out.println(newFile + " file is created");
        }
        PrintStream file = new PrintStream(newFile);
        try {
            System.setOut(file);
        } catch (Exception ignored) {
        } finally {
            file.close();
        }
    }

    public static void createSparkReport() {
        if (report == null) {
            report = getExtendReport();
        }
    }

    public static ExtentReports getExtendReport() {
        String reportPath = System.getProperty("user.dir") + "//report//SparkReport.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setDocumentTitle("Test Results");
        spark.config().setReportName(suite.readProperty("TesterName"));
        spark.config().setTheme(Theme.STANDARD);
        ExtentReports extent2 = new ExtentReports();
        extent2.attachReporter(spark);
        return extent2;
    }

    @BeforeMethod(alwaysRun = true)
    public void setup(ITestResult result) throws IOException {
        spark = report.createTest(result.getMethod().getMethodName());
        com = new Common(spark);
        com.browserInit();
    }

    @AfterMethod(alwaysRun = true)
    public void closeSetup() {
        com.quitBrowser();
    }

    @Parameters({"Project", "dataFile"})
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(String Project, String dataFile) throws IOException {
        zipFolder();//Will ZIP LastRun report and ZIP in to resultBackup Folder
        deleteFolder();//Deleting Report Folder
        deleteOldFiles();//Deleting 7 days before backup results
        setSystemOutToFile();
        suite = new Common(Project, dataFile);
        data = suite.getDataObject();
        createSparkReport();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        report.flush();
        System.out.println("Extent Report Flushed");
        com.quitBrowser();
    }
}