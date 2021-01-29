package _selenium

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.test.assertTrue

class SeleniumViewTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            setupSelenium()
        }
    }

    lateinit var driver: WebDriver
    val options = ChromeOptions().apply {
        addArguments("--headless")
        addArguments("--disable-gpu")
    }

    @BeforeEach
    fun beforeEach() {
        driver = ChromeDriver(options)
        driver.manage().window().maximize()
        driver.get("http://localhost:8080")
    }

    @AfterEach
    fun afterEach() {
        driver.close()
    }

    @Test
    fun checkRegisterUnavailable() {
        driver
            .findElement(By.xpath("//div[text()='Register' and contains(@class, 'md-button-content')]"))
            .click()
        assertTrue(driver.currentUrl.endsWith("/register"))

        val form = driver.findElement(By.cssSelector(".md-error"))
        assertTrue(form.text.contains("unavailable"))

        driver
            .findElement(By.xpath("//div[text()='Go back' and contains(@class, 'md-button-content')]"))
            .click()
        assertFalse(driver.currentUrl.endsWith("/register"))
    }

    @Test
    fun failLoginAttempt() {
        driver.get("http://localhost:8080/#/login")

        driver.authenticate("not-admin", "not-password")
        WebDriverWait(driver, 1).until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(text(), 'Unknown login') and contains(@class, 'md-error')]")
            )
        )

        driver.authenticate("admin", "password")
        WebDriverWait(driver, 1).until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(text(), 'Wrong password') and contains(@class, 'md-error')]")
            )
        )
    }

}