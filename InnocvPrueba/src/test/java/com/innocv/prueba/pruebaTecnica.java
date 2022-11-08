package com.innocv.prueba;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
public class pruebaTecnica {
	
	private WebDriver driver;
	By contactoLink = By.linkText("Contacto");
	By telefonoContactoXpath = By.xpath("//*[@id=\"root\"]/div/div/div[1]/div/div[1]/div/div/div/div/p[2]/span/span[2]");
	By legalButtonXpath = By.linkText("AVISO LEGAL");
	By closeCookiesButton = By.id("rcc-decline-button");
	By telefonoLegalXpath = By.xpath("//*[@id=\"root\"]/div/div/div[1]/div/div/p[3]/span/span");
	By enviarFormularioXpath = By.xpath("//*[@id=\"maps\"]/div/div/form/button/span[1]");
	By errorFormulario = By.className("MuiFormHelperText-root");
	By fechaXpath = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div[2]/div[2]/article/section[1]/div[2]/div[1]/p[2]");
	
	
	@Before
	public void setUp() {
		
		System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.innocv.com/");
	}
	
	@After
	public void tearDown() throws Exception{
		//driver.quit();
	}
	
	@Test
	public void prueba1() throws InterruptedException {
		//Variables
		String telefonoContacto;
		String telefonoLegal;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		//Home Page
		wait.until(ExpectedConditions.visibilityOfElementLocated(closeCookiesButton));
		driver.findElement(closeCookiesButton).click();
		driver.findElement(contactoLink).click();
		
		//Contacto Page
		wait.until(ExpectedConditions.visibilityOfElementLocated(telefonoContactoXpath));
		telefonoContacto = driver.findElement(telefonoContactoXpath).getText().substring(6);
		//Scroll down 
		Thread.sleep(2000);
		js.executeScript("window.scrollBy(0,4500)");
		Thread.sleep(1000);
		driver.findElement(legalButtonXpath).click();
		
		//Aviso legal Page
		wait.until(ExpectedConditions.visibilityOfElementLocated(closeCookiesButton));
		driver.findElement(closeCookiesButton).click();
		telefonoLegal = driver.findElement(telefonoLegalXpath).getText();
		
		//Asserts
		Assert.assertTrue(telefonoLegal.contains(telefonoContacto));
	}
	
	@Test
	public void prueba2() throws InterruptedException {
		//Declarations
		int contador = 0;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		
		//Home Page
		wait.until(ExpectedConditions.visibilityOfElementLocated(closeCookiesButton));
		driver.findElement(closeCookiesButton).click();
		driver.findElement(contactoLink).click();
		
		//Contacto Page
		wait.until(ExpectedConditions.visibilityOfElementLocated(telefonoContactoXpath));
		WebElement l=driver.findElement(By.tagName("body"));
		//Esto deberia ser un metodo a parte
		String t = l.getText().toLowerCase();
		StringTokenizer tok = new StringTokenizer(t);
		while(tok.hasMoreTokens()) {
			if(tok.nextElement().equals("faraday,")) {
				contador++;
			}
		}
		System.out.println(contador);
		System.out.println(t);
	}
	
	@Test
	public void prueba3() throws InterruptedException {
		//Variables
		String mensajeErrorFormulario;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		//Home Page
		wait.until(ExpectedConditions.visibilityOfElementLocated(closeCookiesButton));
		driver.findElement(closeCookiesButton).click();
		driver.findElement(contactoLink).click();
		
		//Contacto Page
		wait.until(ExpectedConditions.visibilityOfElementLocated(telefonoContactoXpath));
		//Scroll down 
		js.executeScript("window.scrollBy(0,1200)", "");
		Thread.sleep(1000);
		driver.findElement(enviarFormularioXpath).click();
		mensajeErrorFormulario = driver.findElement(errorFormulario).getText();
		
		//Asserts
		Assert.assertEquals(mensajeErrorFormulario, "Campo requerido");
	}
	
	@Test
	public void prueba4()throws InterruptedException, ParseException {
		//Variables
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		LocalDate fechaNoticia = null;
		LocalDate fechaLimite = null;
		String fecha="null"; 
		boolean fechaValida;
				
		//Home Page
		wait.until(ExpectedConditions.visibilityOfElementLocated(closeCookiesButton));
		driver.findElement(closeCookiesButton).click();
		driver.findElement(contactoLink).click();
		
		//Contacto Page
		Thread.sleep(2000);
		//Scroll down 
		js.executeScript("window.scrollBy(0,3300)", "");
		fecha = driver.findElement(fechaXpath).getText();
		
		//Esto deberia ser un metodo a parte
		fechaLimite = (LocalDate.now().minusMonths(2));
		fechaNoticia = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		fechaValida = fechaLimite.isBefore(fechaNoticia);
		
		Assert.assertEquals(fechaValida, true);
	}
	
}
