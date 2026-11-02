package org.etlforge.integration.client.ui.structure.editor.json;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.GoToNextPageAction;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.ui.structure.editor.xml.FileInputMode;
import org.etlforge.integration.client.ui.structure.editor.xml.RefreshFileListAction;
import org.etlforge.integration.client.ui.structure.editor.xml.StructureFileDownloader;
import org.etlforge.integration.client.ui.structure.editor.xml.StructureFileManager;
import org.etlforge.integration.client.ui.structure.editor.xml.StructureFileSelectionModel;
import org.etlforge.integration.client.ui.structure.editor.xml.StructureFileSelectionUi;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.ui.Empty;
import org.etlforge.ui.property.WaitStateBinding;

public final class JsonStructureFileSelectionPage extends AbstractWizardPage implements JsonStructureWizardPage {
   private final StructureFileSelectionModel model = StructureFileSelectionModel.forJson();
   private final StructureFileManager structureFileManager;
   private final StructureFileSelectionUi ui;

   public JsonStructureFileSelectionPage(Wizard var1, XmlInfoProvider var2, XsdFileStore var3) {
      super(var1, "Select an Existing JSON Structure");
      StructureFileDownloader var4 = new StructureFileDownloader(this.model, var2);
      this.ui = this.createUi(var4);
      this.model.addPropertyChangeListener(new JsonStructureFileSelectionPage.ModelListener());
      this.structureFileManager = new StructureFileManager(this.model, var4, var3);
      this.updateCanContinue();
   }

   private StructureFileSelectionUi createUi(StructureFileDownloader var1) {
      StructureFileSelectionUi var2 = new StructureFileSelectionUi(this.model);
      var2.setRefreshAction(new RefreshFileListAction(var1, this.model));
      var2.setDefaultAction(new GoToNextPageAction(this));
      return var2;
   }

   private void updateCanContinue() {
      boolean var1 = this.isSampleFileMode() || this.model.getSelectedServerFile() != null;
      this.setCanContinue(var1);
   }

   public void setStructureToEdit(JsonStructure var1) {
      this.model.setSelectedServerFile(var1 == null ? null : ServerFile.fromPath(var1.getFileName()));
   }

   public boolean isSampleFileMode() {
      return this.model.getInputMode() == FileInputMode.FROM_SAMPLE_FILE;
   }

   public Set<ServerFile> getExistingFiles() {
      return this.model.getAllFiles();
   }

   @Override
   public void applyTo(JsonStructure var1) {
      if (!this.isSampleFileMode()) {
         var1.setFileName(this.model.getSelectedServerFile().getName());
         var1.setRootName("json");
      }
   }

   @Override
   public JsonStructure getDefinedStructure() {
      return this.isSampleFileMode() ? null : new JsonStructure(this.model.getSelectedServerFile().getName(), "json");
   }

   @Override
   public boolean prepareForForwardNavigation() {
      return this.isSampleFileMode() ? true : this.downloadSelectedFile();
   }

   private boolean downloadSelectedFile() {
      ServerFile var1 = this.model.getSelectedServerFile();
      return this.structureFileManager.downloadStructureFile(var1);
   }

   @Override
   protected void dispose() {
      this.structureFileManager.cancelCurrentJobs();
   }

   @Override
   protected void onFirstDisplay() {
      this.installWaitIndication();
      this.startDownloadOfExistingStructures();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("");
      var1.add(this.ui, true, true);
      JPanel var2 = var1.asCenterBorderLayout();
      var2.setBorder(Empty.border(0, 0, 12, 0));
      return var2;
   }

   private void installWaitIndication() {
      WaitStateBinding var1 = new WaitStateBinding(this.model.getProperty("busy"), this.getWaitService());
      var1.syncUi();
   }

   private void startDownloadOfExistingStructures() {
      this.structureFileManager.initialize();
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            JsonStructureFileSelectionPage.this.ui.requestFocus();
         }
      });
   }

   private class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               JsonStructureFileSelectionPage.this.updateCanContinue();
            }
         });
      }
   }
}
