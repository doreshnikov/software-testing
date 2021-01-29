package _selenium

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class SeleniumModifyTest {

    @BeforeEach
    fun beforeAll() {
        setupSelenium()
        driver = ChromeDriver(ChromeOptions().apply {
            addArguments("--headless")
            addArguments("--disable-gpu")
        })
        driver.manage().window().maximize()
        driver.get("http://localhost:8080")
    }

    lateinit var driver: WebDriver

    @AfterEach
    fun afterAll() {
        driver.close()
    }

    @Test
    fun largeScenarioOfActions() {
        driver.get("http://localhost:8080/#/login")
        driver.authenticate("admin", "")
        WebDriverWait(driver, 1).until(
            ExpectedConditions.not(ExpectedConditions.urlContains("/login"))
        )

        (1..3).forEach { id ->
            driver.get("http://localhost:8080/#/title/add")
            WebDriverWait(driver, 1).until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("input"))
            )
            driver.findElement(By.cssSelector("input"))
                .sendKeys("http://myanimelist.net/manga/$id")

            WebDriverWait(driver, 5).until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[contains(@class, 'title-preview-name')]")
                )
            )
            assertEquals(0, driver.findElements(By.cssSelector(".md-error")).size)

            driver.findElement(By.xpath("//div[text()='Add' and contains(@class, 'md-button-content')]"))
                .click()
            WebDriverWait(driver, 1).until(
                ExpectedConditions.not(ExpectedConditions.urlContains("/add"))
            )
            assertEquals(id + 1, driver.findElements(By.cssSelector(".md-card")).size)
        }

        assertDoesNotThrow {
            driver.findElements(By.xpath("//span[contains(@class, 'delete-icon')]")).forEach {
                it.findElement(By.xpath("./..")).click()
            }
        }
        WebDriverWait(driver, 1).until(
            ExpectedConditions.numberOfElementsToBe(
                By.cssSelector(".md-card"), 1
            )
        )

        driver.findElement(By.xpath("//div[text()='admin']")).click()
        driver.findElement(By.xpath("//div[contains(text(), 'Log out')]")).click()

        WebDriverWait(driver, 1).until(
            ExpectedConditions.numberOfElementsToBe(
                By.cssSelector(".md-card"), 0
            )
        )
    }

}