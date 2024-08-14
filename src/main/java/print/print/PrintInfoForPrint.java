package print.print;

import print.print.dto.InfoForPrint;

import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.awt.*;
import java.awt.print.*;
import java.util.Locale;

public class PrintInfoForPrint implements Printable {

    private InfoForPrint info;

    public PrintInfoForPrint(InfoForPrint info) {
        this.info = info;
        System.out.println(info);
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Масштабирование для корректного отображения на A5
        double scaleX = pageFormat.getImageableWidth() / 200.0; // A5 ширина в пунктах
        double scaleY = pageFormat.getImageableHeight() / 500.0; // A5 высота в пунктах
        double scale = Math.min(scaleX, scaleY);
        g2d.scale(scale, scale);

        // Title
        Font titleFont = new Font("Serif", Font.BOLD, 18);
        g2d.setFont(titleFont);
        g2d.drawString("Email: " + info.getEmail(), 10, 20);

        // Subtitle
        Font subTitleFont = new Font("Serif", Font.BOLD, 14);
        g2d.setFont(subTitleFont);
        g2d.drawString("Убакыттар", 10, 40);

        // Main text
        Font font = new Font("Serif", Font.PLAIN, 12);
        g2d.setFont(font);

        int yPosition = 60;
        int lineHeight = 20;

        g2d.drawString("Биринчи сурот : " + info.getFirstTimePrint(), 10, yPosition);
        yPosition += lineHeight;
        g2d.drawString("Акыркы сурот :  " + info.getLastTimePrint(), 10, yPosition);
        yPosition += lineHeight;
        g2d.drawString("Азыркы убакыт: " + info.getCurrentTime(), 10, yPosition);
        yPosition += lineHeight;
        g2d.drawString("Кечеки акыркы : " + info.getYesterdayLastTimePrint(), 10, yPosition);

        // Adding a gap before the next section
        yPosition += lineHeight * 2;

        // Subtitle for Success
        g2d.setFont(subTitleFont);
        g2d.drawString("Толук чыгып буткондор", 10, yPosition);

        yPosition += lineHeight;

        // Success counts
        g2d.setFont(font);
        g2d.drawString("А4: " + info.getSuccessA4(), 10, yPosition);
        yPosition += lineHeight;
        g2d.drawString("A5: " + info.getSuccessA5(), 10, yPosition);
        yPosition += lineHeight;
        g2d.drawString("A6: " + info.getSuccessA6(), 10, yPosition);
        yPosition += lineHeight;
        g2d.drawString("белгисиз: " + info.getUnknown(), 10, yPosition);

        // Adding a gap before the next section
        yPosition += lineHeight * 2;

        // Subtitle for Unsuccessful
        g2d.setFont(subTitleFont);
        g2d.drawString("Толук чыкпай калгандар", 10, yPosition);

        yPosition += lineHeight;

        // Unsuccessful counts
        g2d.setFont(font);
        g2d.drawString("A4: " + info.getUnSuccessA4(), 10, yPosition);
        yPosition += lineHeight;
        g2d.drawString("A5: " + info.getUnSuccessA5(), 10, yPosition);
        yPosition += lineHeight;
        g2d.drawString("A6: " + info.getUnSuccessA6(), 10, yPosition);
        yPosition += lineHeight;
        g2d.drawString("белгисиз: " + info.getUnUnknown(), 10, yPosition);

        return PAGE_EXISTS;
    }

    public void print() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(MediaSizeName.ISO_A5); // Set paper size to A5

        // Установка локали на русскую
        Locale.setDefault(new Locale("ru", "RU"));

        // Отображение диалогового окна печати
        if (job.printDialog(attr)) {
            try {
                job.print(attr);
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }
}
