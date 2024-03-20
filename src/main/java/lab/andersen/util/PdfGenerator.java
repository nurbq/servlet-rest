package lab.andersen.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lab.andersen.dao.UserActivityDao;
import lab.andersen.dto.UserActivityShortDto;
import lab.andersen.service.UserActivityService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PdfGenerator {

    private static final Logger logger = Logger.getLogger(PdfGenerator.class.getName());
    private static final String BASE_PATH = PropertiesUtils.get("pdf.base.url");
    @Getter
    private static final PdfGenerator INSTANCE = new PdfGenerator();
    private static final UserActivityService userActivityService = new UserActivityService(new UserActivityDao());


    @SneakyThrows
    public void generate() {

        List<UserActivityShortDto> allTodayActivitiesFromDb = userActivityService.findAllTodayActivities();

        Path pdfFullPath = Path.of(BASE_PATH, LocalDate.now() + ".pdf");
        Files.createDirectories(pdfFullPath.getParent());
        Document document = new Document();
        PdfWriter.getInstance(document,
                new FileOutputStream(pdfFullPath.toString()));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("Users information " + "date " + LocalDate.now(), font);

        com.itextpdf.text.List listToPdf = new com.itextpdf.text.List();

        allTodayActivitiesFromDb.forEach(userActivity -> {
                    String userInformationText = userActivity.getUserName() + " " +
                                                 userActivity.getDescription() + " " + userActivity.getDateTime().toLocalTime();
                    listToPdf.add(userInformationText);
                }
        );

        document.add(chunk);
        document.add(listToPdf);
        document.close();
        logger.info("File generated at: " + pdfFullPath);
    }
}
