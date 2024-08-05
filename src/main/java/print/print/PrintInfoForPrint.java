package print.print;

import print.print.dto.InfoForPrint;

import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.awt.*;
import java.awt.print.*;

public class PrintInfoForPrint implements Printable {

    private InfoForPrint info;

    public PrintInfoForPrint(InfoForPrint info) {
        this.info = info;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex){
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.scale(0.8, 0.8); // scale to fit on A5 paper if needed

        // Title
        Font titleFont = new Font("Serif", Font.BOLD, 18);
        g2d.setFont(titleFont);
        g2d.drawString("Email: " + info.getEmail(), 100, 50);

        // Subtitle
        Font subTitleFont = new Font("Serif", Font.BOLD, 14);
        g2d.setFont(subTitleFont);
        g2d.drawString("Убакыттар", 100, 80);

        // Main text
        Font font = new Font("Serif", Font.PLAIN, 12);
        g2d.setFont(font);

        int yPosition = 100;
        int lineHeight = 20;

        g2d.drawString("Биринчи сурот печатка кеткен убакыт: " + info.getFirstTimePrint(), 100, yPosition);
        yPosition += lineHeight;
        g2d.drawString("Акыркы сурот печатка кеткен убакыт:  " + info.getLastTimePrint(), 100, yPosition);
        yPosition += lineHeight;
        g2d.drawString("Азыркы убакыт: " + info.getCurrentTime(), 100, yPosition);

        // Adding a gap before the next section
        yPosition += lineHeight * 2;

        // Subtitle for Success
        g2d.setFont(subTitleFont);
        g2d.drawString("Толук чыгып буткондор", 100, yPosition);

        yPosition += lineHeight;

        // Success counts
        g2d.setFont(font);
        g2d.drawString("А4: " + info.getSuccessA4(), 100, yPosition);
        yPosition += lineHeight;
        g2d.drawString("A5: " + info.getSuccessA5(), 100, yPosition);
        yPosition += lineHeight;
        g2d.drawString("A6: " + info.getSuccessA6(), 100, yPosition);
        yPosition += lineHeight;
        g2d.drawString("белгисиз: " + info.getUnknown(), 100, yPosition);

        // Adding a gap before the next section
        yPosition += lineHeight * 2;

        // Subtitle for Unsuccessful
        g2d.setFont(subTitleFont);
        g2d.drawString("Толук чыкпай калгандар", 100, yPosition);

        yPosition += lineHeight;

        // Unsuccessful counts
        g2d.setFont(font);
        g2d.drawString("A4: " + info.getUnSuccessA4(), 100, yPosition);
        yPosition += lineHeight;
        g2d.drawString("A5: " + info.getUnSuccessA5(), 100, yPosition);
        yPosition += lineHeight;
        g2d.drawString("A6: " + info.getUnSuccessA6(), 100, yPosition);
        yPosition += lineHeight;
        g2d.drawString("белгисиз: " + info.getUnUnknown(), 100, yPosition);

        return PAGE_EXISTS;
    }

    public void print() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(MediaSizeName.ISO_A5); // Set paper size to A5

        try {
            // Directly print without showing print dialog
            job.print(attr);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }
}
