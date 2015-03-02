package local.app;


import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;
import ru.stqa.selenium.factory.WebDriverFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class CompareScreenshotClass {

    private static final String webDriverHome = "D:\\webdriver";

    public static enum BrowserType {
        IE,
        SAFARI,
        CHROME,
        FIREFOX,
//        OPERA
    }

    private WebDriver driver;

    public CompareScreenshotClass(BrowserType browser, boolean incognitoMode) {

        Properties props = System.getProperties();
        props.setProperty("gate.home", webDriverHome);
        Capabilities capabilities;
        switch (browser) {
            case FIREFOX:
                capabilities = DesiredCapabilities.firefox();
                break;

            case CHROME:
                props.setProperty("webdriver.chrome.driver", webDriverHome + "\\chromedriver.exe");
                capabilities = DesiredCapabilities.chrome();
                break;

            case IE:
                props.setProperty("webdriver.ie.driver", webDriverHome + "\\IEDriverServer.exe");
                capabilities = DesiredCapabilities.internetExplorer();
                break;

//            case OPERA:
//                capabilities = DesiredCapabilities.operaBlink();
//                break;

            case SAFARI:
                capabilities = DesiredCapabilities.safari();
                break;

            default:
                throw new IllegalArgumentException("Invalid browser type");
//                break;
        }
        System.out.println("Used browser: " + browser.toString());
        try {
            this.driver = WebDriverFactory.getDriver(capabilities);
        } catch (UnreachableBrowserException ube) {
            if (browser == BrowserType.SAFARI) {
                System.out.println("Trying to open Safari browser again...");
                try {Thread.sleep(3000);} catch (InterruptedException ie) {}
                this.driver = WebDriverFactory.getDriver(capabilities);
            } else {
                throw ube;
            }
        }

    }

    public CompareScreenshotClass(BrowserType browser) {
        this(browser, false);
    }

    public List<String> LoadURLs(String filename)
    {
        List<String> records = new ArrayList<String>();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (!line.startsWith("#")) {
                    records.add(line);
                }
            }
            reader.close();
            return records;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }

    public int getScreenshotHashForUrl(String url) {

        driver.manage().window().maximize();
        driver.navigate().to(url);
        try {Thread.sleep(2000);} catch (InterruptedException e) {System.err.println(e.toString());}

        String scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);

        return scrFile.hashCode();
    }

    public void exitBrowser() {
        driver.quit();
    }

    public void exitAll() {
        WebDriverFactory.dismissAll();
    }

}
