package org.etlforge.ui.graph2d;

import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.ui.util.KeyUtils;

public class GraphPrinter {
   private final Graph2D graph;
   private final Action printAction;
   private final Action pageSetupAction;
   private PageFormat pageFormat;

   public GraphPrinter(Graph2D var1) {
      this.graph = var1;
      this.pageFormat = PrinterJob.getPrinterJob().defaultPage();
      this.pageFormat.setOrientation(0);
      this.printAction = new GraphPrinter.PrintGraphAction();
      this.pageSetupAction = new GraphPrinter.PageSetupAction();
   }

   public void print() {
      PrinterJob var1 = PrinterJob.getPrinterJob();
      var1.setPrintable(this.graph.getPrintable(), this.pageFormat);
      if (var1.printDialog()) {
         try {
            var1.print();
         } catch (PrinterException var3) {
            ErrorLog.attention(GraphPrinter.class, "An error occurred while printing: " + var3.getMessage(), var3);
         }
      }
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
         GraphPrinter.this.pageFormat = var2.pageDialog(GraphPrinter.this.pageFormat);
      }
   }

   private class PrintGraphAction extends AbstractAction {
      public PrintGraphAction() {
         super("Print...");
         this.putValue("AcceleratorKey", KeyUtils.PRINT);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         GraphPrinter.this.print();
      }
   }
}
