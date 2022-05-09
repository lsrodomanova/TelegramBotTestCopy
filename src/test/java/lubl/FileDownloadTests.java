package lubl;

import com.codeborne.pdftest.PDF;
import com.codeborne.pdftest.matchers.ContainsText;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileDownloadTests extends TestBase {

    ClassLoader cl = FileDownloadTests.class.getClassLoader();

    private static final String URL="https://karelia.tele2.ru/";
    private static final String link="Договор и условия оказания услуг";
    private static final String link2="Лицензии на услуги связи";


    @DisplayName("Поиск и скачивание файла")
    @Test

    public void SearchDownloadFile() {

        step ("Открыть главную страницу", () -> {
            open(URL);
            Selenide.zoom(0.5);
        });

        step ("Найти через поиск раздел с документами", () -> {
            $(".icon-search").click();
            $("#search-text").setValue("условия оказания услуг");
            $(".global-results").shouldHave(text(link));
        });

        step ("Перейти в раздел Договор и условия", () -> {
            $(".global-results").$(byText(link)).click();
            $(".row").shouldHave(text(link2));
        });

        step ("Перейти в Лицензии", () -> {
            $(".row").$(byText(link2)).click();
        });

        step ("Скачать первый в списке файл", () -> {
            File downloadFile = Selenide.$(".btn-small").download();
            try(InputStream is = new FileInputStream(downloadFile)) {
                com.codeborne.pdftest.PDF pdf = new PDF(is);
                org.assertj.core.api.Assertions.assertThat(pdf.numberOfPages).isEqualTo(1);
                assertThat(pdf, new ContainsText("Реквизиты"));
        }
        });
    }
}
