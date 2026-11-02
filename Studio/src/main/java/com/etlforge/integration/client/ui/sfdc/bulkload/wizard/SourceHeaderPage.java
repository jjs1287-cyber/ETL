package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.JPanel;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiThreadSupplier;

final class SourceHeaderPage extends AbstractWizardPage {
   private final SourceHeaderPageUi ui = new SourceHeaderPageUi(new Receiver<Boolean>() {
      public void handle(Boolean var1) {
         SourceHeaderPage.this.setCanContinue(var1);
      }
   });

   public SourceHeaderPage(Wizard var1) {
      super(var1, "Select a Source File");
   }

   public void setSampleFile(SampleFile var1) {
      this.ui.setSampleFile(var1);
   }

   public List<String> getInputFileHeader() {
      return this.ui.getHeaders();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Source File Header");
      var1.add(this.ui, true, true);
      return var1.asNorthBorderLayout();
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SourceHeaderPage.this.ui.requestFocus();
         }
      });
   }

   @Override
   public boolean prepareForForwardNavigation() {
      List var1 = UiThreadSupplier.call(new Supplier<List<String>>() {
         public List<String> get() {
            return SourceHeaderPage.this.getInputFileHeader();
         }
      });
      if (var1 == null) {
         NonBlockingAlert.error("Support for source files without header has not been implemented yet.", "Not Implemented Yet");
         return false;
      } else {
         return true;
      }
   }
}
