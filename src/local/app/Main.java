package local.app;


import java.util.List;

import static local.app.CompareScreenshotClass.BrowserType;

public class Main {

    /*
    Dependencies (libraries that should be added to classpath):
    selenium-java-2.45.0.jar   http://selenium-release.storage.googleapis.com/2.45/selenium-java-2.45.0.zip
    selenium-server-standalone-2.45.0.jar  http://selenium-release.storage.googleapis.com/index.html?path=2.45/
    webdriver-factory-1.1.43  http://search.maven.org/#browse%7C-811382175

    and download Selenium webriver executables for according browsers and put them into folder added to PATH
    for webdrivers see
    http://chromedriver.storage.googleapis.com/index.html?path=2.9/
    amd
    http://selenium-release.storage.googleapis.com/index.html?path=2.45/
    http://www.seleniumhq.org/download/
     */

    private static final String protocol = "https://";
//    private static final String prodHost = "en.wikipedia.org";
    private static final String prodHost = "sites.google.com";
//    private static final String testHost = "en.wikipedia.org";
    private static final String testHost = "sites.google.com";
    private static final String filename = "res/urls.txt";
    private static final boolean runIncognitoMode = false;


    private static void compareScreenshots(BrowserType type) throws Exception{
              /*
        You can use BrowserType enum as a parameter for the CompareScreenshotClass constructor to choose appropriate browser
         */

        CompareScreenshotClass helper = new CompareScreenshotClass(type, runIncognitoMode);

        List<String> URLs = helper.LoadURLs(filename);
        String prodAddr;
        String testAddr;

        for ( String tail : URLs ) {

            prodAddr = protocol + prodHost + "/" + tail;
            System.out.println("production address: " + prodAddr);

            int prodHash = helper.getScreenshotHashForUrl(prodAddr);
            System.out.println("#hash: " + prodHash);

            testAddr =  protocol + testHost + "/" + tail;
            System.out.println("qa address: " + testAddr);

            int testHash = helper.getScreenshotHashForUrl(testAddr);
            System.out.println("#hash: " + testHash);

            if (testHash != prodHash) {
                System.out.println("Error: Screenshots do not match");
//                helper.exitAll();
//                throw new RuntimeException("Error: Screenshots do not match");
            }
        }
        System.out.println();
        helper.exitAll();
    }

    public static void main(String[] args) throws Exception {

        BrowserType[] browsersToRun = {
                BrowserType.SAFARI,
                BrowserType.CHROME,
                BrowserType.IE,
                BrowserType.FIREFOX
        };

//        for (BrowserType browser : BrowserType.values()) {   // to run through all supported browser types
        for (BrowserType browser : browsersToRun) {
            System.out.println("-----------------------------------------------------------");
            compareScreenshots(browser);
        }
    }

}
