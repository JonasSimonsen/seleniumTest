package com.mycompany.seleniumtest2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author jonassimonsen
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CarsTest1 {

    private static final int WAIT_MAX = 4;
    static WebDriver driver;

    @BeforeClass
    public static void setup() {
        /*########################### IMPORTANT ###############################*/
        /*## Change this, according to your own OS and location of driver(s) ##*/
        /*#####################################################################*/
        //System.setProperty("webdriver.gecko.driver", "/Users/jonassimonsen/NetBeansProjects/drivers/geckodriver");
        System.setProperty("webdriver.chrome.driver", "/Users/jonassimonsen/NetBeansProjects/drivers/chromedriver");

        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
        //Reset Database 
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
    }

    @Test
    //Verify that page is loaded and all expected data are visible
    public void test1() throws Exception {
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.tagName("tbody"));
            List<WebElement> rows = e.findElements(By.tagName("tr"));
            Assert.assertThat(rows.size(), is(5));
            return true;
        });
    }

    @Test
    //Verify the filter functionality 
    public void test2() throws Exception {
        //No need to WAIT, since we are running test in a fixed order, we know the DOM is ready (because of the wait in test1)
        WebElement element = driver.findElement(By.id("filter"));
        //Complete this
        element.sendKeys("2002");
        element.click();

        (new WebDriverWait(driver, WAIT_MAX)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement e = d.findElement(By.tagName("tbody"));
                List<WebElement> rows = e.findElements(By.tagName("tr"));
                Assert.assertThat(rows.size(), is(2));
                return true;
            }
        });
    }

    @Test
    public void test3() throws Exception {
        driver.manage().timeouts().implicitlyWait(WAIT_MAX, TimeUnit.SECONDS);

        WebElement element = driver.findElement(By.id("filter"));
//        element.clear();
//        element.click();
        element.sendKeys(Keys.BACK_SPACE);

        (new WebDriverWait(driver, WAIT_MAX)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement e = d.findElement(By.tagName("tbody"));
                List<WebElement> rows = e.findElements(By.tagName("tr"));
                Assert.assertThat(rows.size(), is(5));
                return true;
            }
        });
    }

    @Test
    public void test4() throws Exception {
        WebElement element = driver.findElement(By.tagName("thead")).findElement(By.tagName("tr"));
        List<WebElement> rows = element.findElements(By.tagName("th"));
        rows.get(1).findElement(By.tagName("a")).click();

        (new WebDriverWait(driver, WAIT_MAX)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement e = d.findElement(By.tagName("tbody"));
                List<WebElement> rows = e.findElements(By.tagName("tr"));
                String firstRow = rows.get(0).findElements(By.tagName("td")).get(0).getText();
                String lastRow = rows.get(4).findElements(By.tagName("td")).get(0).getText();

                Assert.assertThat(firstRow, is("938"));
                Assert.assertThat(lastRow, is("940"));
                return true;
            }
        });
    }
    
    @Test
    public void test5() throws Exception {
        List<WebElement> rows = driver.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        WebElement element = null;
        //find element with id 938 in the tbody
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).findElements(By.tagName("td")).get(0).getText().equalsIgnoreCase("938")) {
                element = rows.get(i);
                break;
            }
        }

        element = element.findElements(By.tagName("td")).get(7).findElements(By.tagName("a")).get(0);
        //click edit button
        element.click();
        //clear description input field
        driver.findElement(By.id("description")).clear();
        
        
        //WAIT for DOM to be executed
        driver.manage().timeouts().implicitlyWait(WAIT_MAX, TimeUnit.SECONDS);
        //edit description in input field
        driver.findElement(By.id("description")).sendKeys("cool cars");

        //click the save button
        driver.findElement(By.id("save")).click();

        //check that the tbody td, id 938 description is cool cars(check DOM works)
        (new WebDriverWait(driver, WAIT_MAX)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement e = d.findElement(By.tagName("tbody"));
                List<WebElement> rows = e.findElements(By.tagName("tr"));
                String result = null;
                for (int i = 0; i < rows.size(); i++) {
                    if (rows.get(i).findElements(By.tagName("td")).get(0).getText().equalsIgnoreCase("938")) {
                        result = rows.get(i).findElements(By.tagName("td")).get(5).getText();
                        break;
                    }
                }
                Assert.assertThat(result, is("cool cars"));
                return true;
            }
        });
    }
    
    @Test
    public void test6() throws Exception {
        driver.findElement(By.id("new")).click();
        driver.findElement(By.id("save")).click();
        
        (new WebDriverWait(driver, WAIT_MAX)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String result = d.findElement(By.id("submiterr")).getText();
                Assert.assertThat(result, is("All fields are required"));
                return true;
            }
        });
    }

    
    @Test
    public void test7() throws Exception {
        driver.findElement(By.id("new")).click();
        driver.findElement(By.id("year")).sendKeys("2008");
        driver.findElement(By.id("registered")).sendKeys("2002-05-05");
        driver.findElement(By.id("make")).sendKeys("Kia");
        driver.findElement(By.id("model")).sendKeys("Rio");
        driver.findElement(By.id("description")).sendKeys("As new");
        driver.findElement(By.id("price")).sendKeys("31000");
        
        driver.findElement(By.id("save")).click();
        
         //WAIT for DOM to be executed
          (new WebDriverWait(driver, WAIT_MAX)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement e = d.findElement(By.tagName("tbody"));
                List<WebElement> rows = e.findElements(By.tagName("tr"));
                Assert.assertThat(rows.size(), is(6)); 
                Assert.assertThat(rows.get(5).findElements(By.tagName("td")).get(1).getText(), is("2008"));
                Assert.assertThat(rows.get(5).findElements(By.tagName("td")).get(6).getText(), is("31.000,00 kr."));
                return true;
            }
        });
    }
}