package lab.andersen.scheduler;

import lab.andersen.service.EmailSenderService;
import lab.andersen.util.PdfGenerator;
import lab.andersen.util.PropertiesUtils;
import lab.andersen.util.TelegramSender;

import java.io.File;
import java.time.LocalDate;

public class Test {

    public static void main(String[] args) {
        PdfGenerator.getINSTANCE().generate();

        EmailSenderService.getINSTANCE().sendEmail(
                PropertiesUtils.get("mail.send.to"),
                PropertiesUtils.get("mail.password"),
                PropertiesUtils.get("mail.send.from"),
                "Attaching files",
                "Activity of users",
                PropertiesUtils.get("pdf.base.url") + LocalDate.now() + ".pdf"
        );

        TelegramSender.sendPDF(new File(PropertiesUtils.get("pdf.base.url") + LocalDate.now() + ".pdf"));
    }
}
