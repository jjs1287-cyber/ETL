package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.ui.wait.WaitLock;

public final class PrintMappingsDataAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.Print";
   private final TransformationPage page;

   public PrintMappingsDataAction(TransformationPage var1) {
      super("action.ic.tx.Print", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            PrintMappingsDataAction.this.print();
         }
      });
   }

   private void print() {
      WaitLock var1 = this.page.getWaitService().startWait();
      TreeMapper var2 = this.page.getTreeMapper();
      PrintMappingsDataAction.Worker var3 = new PrintMappingsDataAction.Worker(var2, var1);
      var3.execute();
   }

   private static class Worker extends KongaSwingWorker<Void, Void> {
      private final TreeMapper treeMapper;
      private final WaitLock waitLock;

      public Worker(TreeMapper var1, WaitLock var2) {
         this.treeMapper = var1;
         this.waitLock = var2;
      }

      protected Void doInBackground() throws Exception {
         PrinterJob var1 = PrinterJob.getPrinterJob();
         PageFormat var2 = var1.defaultPage();
         var2.setOrientation(0);
         var1.setPrintable(this.treeMapper.getPrintable(), var2);
         if (var1.printDialog()) {
            try {
               this.treeMapper.startPrinting();
               var1.print();
            } catch (PrinterException var7) {
               ErrorLog.attention(PrintMappingsDataAction.class, "An error occurred while printing: " + var7.getMessage(), var7);
            } finally {
               this.treeMapper.finishPrinting();
            }
         }

         return null;
      }

      @Override
      protected void finished() {
         this.waitLock.release();
      }
   }
}
