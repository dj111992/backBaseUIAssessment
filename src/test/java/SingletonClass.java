import com.assessment.ui.choices.Browser;
import com.assessment.ui.choices.Host;
import com.assessment.ui.config.EnvFactory;
import com.assessment.ui.factories.CapabilitiesFactory;
import com.typesafe.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;

@Slf4j
public class SingletonClass {

    // instance of singleton class
    private static SingletonClass instanceOfSingletonClass=null;
    private WebDriver driver;

    private static Config config = EnvFactory.getInstance().getConfig();
    private static final Host HOST = Host.parse(config.getString("HOST"));
    private static final Browser BROWSER = Browser.parse(config.getString("BROWSER"));


    // Constructor
    private SingletonClass(){
        log.info("Getting driver for browser: {}", BROWSER);
        switch (BROWSER) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(CapabilitiesFactory.getChromeOptions());
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(CapabilitiesFactory.getFirefoxOptions());
                break;
            case EDGE:
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case OPERA:
                WebDriverManager.operadriver().setup();
                driver = new OperaDriver();
                break;
            default:
                throw new IllegalStateException(String.format("%s is not a valid browser choice. Pick your browser from %s.", BROWSER, java.util.Arrays.asList(BROWSER.values())));
        }
    }

    // TO create instance of class
    public static SingletonClass getInstanceOfSingletonClass(){
        if(instanceOfSingletonClass==null){
            instanceOfSingletonClass = new SingletonClass();
        }
        return instanceOfSingletonClass;
    }

    // To get driver
    public WebDriver getDriver()
    {
        return driver;
    }



}
