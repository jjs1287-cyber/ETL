package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Supplier;
import java.io.IOException;
import javax.swing.JPanel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.structure.text.InvalidTemplateException;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiThreadSupplier;

public final class SampleFileTypePage extends AbstractWizardPage {
   private final SampleFile sampleFile;
   private final SampleFileTypePageUi ui = new SampleFileTypePageUi();
   private volatile SourceFileStructure fileStructure;
   private String name = "New " + EntityType.Document.getDisplayName();

   public SampleFileTypePage(Wizard<?> var1, SampleFile var2) {
      super(var1, "Select File Type");
      this.sampleFile = var2;
      this.setCanContinue(true);
   }

   public void setDocumentName(String var1) {
      this.name = ArgCheck.checkNotBlank(var1);
   }

   public SourceFileStructure getFileStructure() {
      return this.fileStructure;
   }

   public boolean isStructureReviewRequested() {
      return this.ui.isStructureReviewRequested();
   }

   @Override
   protected JPanel layoutPage() {
      this.ui.setBorder(Empty.border(0, 16, 0, 0));
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(
         this.getTitle(), "Select an option for creating a " + EntityType.Document.getDisplayName() + " based on the selected source file:"
      );
      var1.add(this.ui, true, false);
      return var1.asNorthBorderLayout();
   }

   @Override
   public boolean prepareForForwardNavigation() {
      this.fileStructure = null;
      SampleFileTypeOption var1 = this.getSelectedOption();
      if (var1.isValid()) {
         return this.createStructure(var1);
      }

      NonBlockingAlert.error(this.getMainPanel(), "Must specify a delimiter.", "Invalid input.");
      return false;
   }

   private SampleFileTypeOption getSelectedOption() {
      return UiThreadSupplier.call(new Supplier<SampleFileTypeOption>() {
         public SampleFileTypeOption get() {
            return SampleFileTypePage.this.ui.getSelectedOption();
         }
      });
   }

   private boolean createStructure(SampleFileTypeOption var1) {
      try {
         SampleFileType var2 = var1.getFileType();
         this.fileStructure = var2.createFileStructure(this.sampleFile, this.name);
         return true;
      } catch (IOException var3) {
         return this.handleFactoryError(var3);
      } catch (InvalidTemplateException var4) {
         return this.handleFactoryError(var4);
      }
   }

   private boolean handleFactoryError(Exception var1) {
      return UiThreadSupplier.call(
         new Supplier<Boolean>() {
            public Boolean get() {
               String var1x = "The file structure could not be created from the sample file. If you continue you will be asked to define the file structure manually.\n\nDo you want to continue?";
               Answer var2 = Ask.continueOrCancel(SampleFileTypePage.this.getMainPanel(), var1x, "Error", Alert.Option.CANCEL, Alert.Type.ERROR);
               return var2.isContinue();
            }
         }
      );
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }
}
