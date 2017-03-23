package com.mycompany.seleniumtest2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
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
import org.openqa.selenium.safari.SafariDriver;
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
    /*########################### IMPORTANT ######################*/
    /*## Change this, according to your own OS and location of driver(s) ##*/    
    /*############################################################*/    
    //System.setProperty("webdriver.gecko.driver", "/Users/jonassimonsen/NetBeansProjects/drivers/geckodriver");
    System.setProperty("webdriver.chrome.driver","/Users/jonassimonsen/NetBeansProjects/drivers/chromedriver");
    
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
  }


}