package _selenium

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

fun setupSelenium() {

    var fileName = "chromedriver"
    if (System.getProperty("os.name")?.toLowerCase()?.contains("win") == true) {
        fileName += ".exe"
    }
    System.setProperty(
        "webdriver.chrome.driver",
        "${System.getProperty("user.dir")}/bin/$fileName"
    )

}

fun WebDriver.authenticate(login: String, password: String) {
    val l = findElement(By.cssSelector("input.login-page-login"))
    l.clear()
    l.sendKeys(login)

    val p = findElement(By.cssSelector("input.login-page-password"))
    p.clear()
    p.sendKeys(password)

    findElement(By.xpath("//div[text()='Log in' and contains(@class, 'md-button-content')]"))
        .click()
}