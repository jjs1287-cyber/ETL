package org.etlforge.integration.client.ui.structure.editor.json;

import com.google.common.base.Optional;
import java.io.File;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.mapping.serverapi.json.JsonInfoProvider;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

public final class JsonPageChain implements WizardPageChain {
   private final Wizard wizard;
   private final XsdFileStore clientXsdFiles;
   private final JsonStructureFileSelectionPage startPage;
   private CreateJsonStructureFromSamplePage sampleFilePage;
   private Optional<File> sampleFile = Optional.absent();

   public JsonPageChain(Wizard var1, XsdFileStore var2) {
      this.wizard = var1;
      this.clientXsdFiles = var2;
      this.startPage = this.createStartPage();
   }

   private JsonStructureFileSelectionPage createStartPage() {
      XmlInfoProvider var1 = IntegrationServer.getInstance().getServerCall(JsonInfoProvider.class);
      return new JsonStructureFileSelectionPage(this.wizard, var1, this.clientXsdFiles);
   }

   public void setStructureToEdit(JsonStructure var1) {
      this.startPage.setStructureToEdit(var1);
   }

   public void setPreselectedSampleFile(File var1) {
      this.sampleFile = Optional.of(var1);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.startPage;
   }

   @Override
   public WizardPage getLastPage() {
      if (this.isSampleFileMode()) {
         assert this.sampleFilePage != null;
         return this.sampleFilePage;
      } else {
         return this.startPage;
      }
   }

   private boolean isSampleFileMode() {
      return this.startPage.isSampleFileMode();
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return var1 == this.startPage ? this.isSampleFileMode() : false;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.startPage && this.isSampleFileMode()) {
         if (this.sampleFilePage == null) {
            this.sampleFilePage = new CreateJsonStructureFromSamplePage(this.wizard, this.clientXsdFiles);
            if (this.sampleFile.isPresent()) {
               this.sampleFilePage.setPreselectedFile(this.sampleFile.get());
            }
         }

         this.sampleFilePage.setExistingFiles(this.startPage.getExistingFiles());
         return this.sampleFilePage;
      } else {
         return null;
      }
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      if (var1 == this.sampleFilePage) {
         assert this.isSampleFileMode();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.sampleFilePage) {
         assert this.isSampleFileMode();
         return this.startPage;
      } else {
         return null;
      }
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      boolean var2 = var1.canContinue();
      if (!var2) {
         return false;
      } else {
         return var1 == this.startPage ? !this.isSampleFileMode() : true;
      }
   }

   public JsonStructure getDefinedStructure() {
      return this.isSampleFileMode() ? this.sampleFilePage.getDefinedStructure() : this.startPage.getDefinedStructure();
   }
}
