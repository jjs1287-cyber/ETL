package org.etlforge.ui.print;

import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.ui.util.KeyUtils;

public class GenericPrinter {
   private final HasPrintable toPrint;
   private final Action printAction;
   private final Action pageSetupAction;
   private PageFormat pageFormat;

   public GenericPrinter(HasPrintable var1) {
      this.toPrint = var1;
      this.printAction = new GenericPrinter.PrintGraphAction();
      this.pageSetupAction = new GenericPrinter.PageSetupAction();
   }

   public void print() throws PrinterException {
      PrinterJob var1 = PrinterJob.getPrinterJob();
      var1.setPrintable(this.toPrint.getPrintable(), this.getPageFormat());
      if (var1.printDialog()) {
         var1.print();
      }
   }

   private PageFormat getPageFormat() {
      if (this.pageFormat == null) {
         this.pageFormat = PrinterJob.getPrinterJob().defaultPage();
         this.pageFormat.setOrientation(0);
      }

      return this.pageFormat;
   }

   public Action getPrintAction() {
      return this.printAction;
   }

   public Action getPageSetupAction() {
      return this.pageSetupAction;
   }

   private class PageSetupAction extends AbstractAction {
      public PageSetupAction() {
         super("Page Setup...");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         PrinterJob var2 = PrinterJob.getPrinterJob();
         PageFormat var3 = GenericPrinter.this.getPageFormat();
         GenericPrinter.this.pageFormat = var2.pageDialog(var3);
      }
   }

   private class PrintGraphAction extends AbstractAction {
      public PrintGraphAction() {
         super("Print...");
         this.putValue("AcceleratorKey", KeyUtils.PRINT);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         try {
            GenericPrinter.this.print();
         } catch (PrinterException var3) {
            var3.printStackTrace();
         }
      }
   }
}
