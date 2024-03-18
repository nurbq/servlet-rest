package lab.andersen.scheduler;

import lab.andersen.service.EmailSenderService;
import lab.andersen.util.PdfGenerator;
import lab.andersen.util.PropertiesUtils;
import lab.andersen.util.TaskExecutor;
import lab.andersen.util.TelegramSender;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.time.LocalDate;
import java.util.logging.Logger;

@WebListener
public class PDFSenderListener implements ServletContextListener {
    private TaskExecutor taskExecutor;
    private static final Logger logger = Logger.getLogger(PDFSenderListener.class.getName());
    private static final String EMAIL_TO = PropertiesUtils.get("mail.send.to");
    private static final String EMAIL_PASSWORD = PropertiesUtils.get("mail.password");
    private static final String EMAIL_FROM = PropertiesUtils.get("mail.send.from");
    private static final String BODY_MESSAGE = "Attaching files";
    private static final String SUBJECT_MESSAGE = "Activity of users";
    private static final String FULL_PATH_PDF = PropertiesUtils.get("pdf.base.url") + LocalDate.now() + ".pdf";
    private static final int TARGET_HOUR = 4;
    private static final int TARGET_MIN = 25;
    private static final int TARGET_SEC = 0;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        taskExecutor = new TaskExecutor(() -> {
            PdfGenerator.getINSTANCE().generate();

            TelegramSender.sendMessage("PDF with daily activities");
            TelegramSender.sendPDF(new File(FULL_PATH_PDF));

            EmailSenderService.getINSTANCE().sendEmail(
                    EMAIL_TO,
                    EMAIL_PASSWORD,
                    EMAIL_FROM,
                    BODY_MESSAGE,
                    SUBJECT_MESSAGE,
                    FULL_PATH_PDF
            );

            logger.info("Messages to Telegram and email have been sent");
        });
        taskExecutor.startExecutionAt(TARGET_HOUR, TARGET_MIN, TARGET_SEC);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        taskExecutor.stop();
    }
}
