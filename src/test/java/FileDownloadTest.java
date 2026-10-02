import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class FileDownloadTest {

    private static final Path DOWNLOAD_DIR =
            Path.of("downloads");

    @BeforeAll
    static void setUp() throws IOException {
        Configuration.browser = "chrome";
        Configuration.downloadsFolder = DOWNLOAD_DIR.toString();
        Configuration.timeout = 150000;

        Files.createDirectories(DOWNLOAD_DIR);
    }

    @Test
    void uploadDownloadAndCheckPng() throws IOException {

        // Указываем режим скачивания файлов
        Configuration.fileDownload = FileDownloadMode.FOLDER;

        // Указываем директорию для сохранения скачанных файлов
        Configuration.downloadsFolder = "src/test/resources";

        // Исходный файл
        Path originalFile =
                Path.of("src/test/resources/test-image.png");

        // Открываем страницу с формой загрузки файла
        open("https://www.resizepixel.com/");

        // Находим элемент <input type="file"> по его атрибуту name
        $("input[name='imageFile']");

        // Загружаем файл, указывая абсолютный путь к файлу
        $("input[type='file']")
                .uploadFile(originalFile.toFile());

        // Находим элемент кнопки для скачивания
        SelenideElement downloadButton = $("a[/download]");
        downloadButton.shouldBe(visible);

        // Кликаем по кнопке для скачивания и сохраняем файл
        File downloadedFile = downloadButton.download();

        //Проверяем, что файл был скачан успешно
        assertTrue(downloadedFile.exists());
    }
}
