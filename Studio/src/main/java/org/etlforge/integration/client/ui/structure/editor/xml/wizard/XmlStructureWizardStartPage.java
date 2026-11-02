package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import com.google.common.collect.Iterables;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.Set;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.GoToNextPageAction;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.ui.structure.editor.xml.FileInputMode;
import org.etlforge.integration.client.ui.structure.editor.xml.RefreshFileListAction;
import org.etlforge.integration.client.ui.structure.editor.xml.StructureFileDownloader;
import org.etlforge.integration.client.ui.structure.editor.xml.StructureFileManager;
import org.etlforge.integration.client.ui.structure.editor.xml.StructureFileSelectionModel;
import org.etlforge.integration.client.ui.structure.editor.xml.StructureFileSelectionUi;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.ui.Empty;
import org.etlforge.ui.property.WaitStateBinding;

final class XmlStructureWizardStartPage extends AbstractWizardPage implements XmlStructureWizardPage {
   private final StructureFileSelectionModel model = StructureFileSelectionModel.forXml();
   private final StructureFileSelectionUi ui;
   private final StructureFileManager structureFileManager;
   private final RootElementManager rootElementManager;

   public XmlStructureWizardStartPage(Wizard var1, XmlStructure var2, XsdFileStore var3) {
      this(var1, var2, var3, PackageResources.StartPage.DEFAULT_SUB_TITLE);
   }

   private XmlStructureWizardStartPage(Wizard var1, XmlStructure var2, XsdFileStore var3, String var4) {
      super(var1, var4);
      this.model.addPropertyChangeListener(new XmlStructureWizardStartPage.ModelListener());
      XmlInfoProvider var5 = IntegrationServer.getInstance().getServerCall(XmlInfoProvider.class);
      StructureFileDownloader var6 = new StructureFileDownloader(this.model, var5);
      if (var2 != null) {
         var6.setCurrentFile(var2.getFilePath());
      }

      this.ui = this.createUi(var6);
      this.structureFileManager = new StructureFileManager(this.model, var6, var3);
      this.rootElementManager = new RootElementManager(this);
      this.updateCanContinue();
   }

   private StructureFileSelectionUi createUi(StructureFileDownloader var1) {
      StructureFileSelectionUi var2 = new StructureFileSelectionUi(this.model);
      var2.setRefreshAction(new RefreshFileListAction(var1, this.model));
      var2.setDefaultAction(new GoToNextPageAction(this));
      return var2;
   }

   private void updateCanContinue() {
      boolean var1;
      switch (this.model.getInputMode()) {
         case EXISTING:
            var1 = this.model.getSelectedServerFile() != null;
            break;
         case LOCAL:
            var1 = this.model.getLocalFile() != null;
            break;
         case FROM_SAMPLE_FILE:
            var1 = true;
            break;
         default:
            throw new RuntimeException("Unexpected mode: " + this.model.getInputMode());
      }

      this.setCanContinue(var1);
   }

   public boolean isSampleFileMode() {
      return this.model.getInputMode() == FileInputMode.FROM_SAMPLE_FILE;
   }

   public final Collection<String> getAvailableRoots() {
      ServerFile var1 = this.getSelectedFile();
      return this.rootElementManager.getRoots(var1);
   }

   public void useForEDI() {
      this.ui.useForEdi();
   }

   @Override
   public void applyTo(XmlStructure var1) {
      if (!this.isSampleFileMode()) {
         ServerFile var2 = this.getSelectedFile();
         var1.setFilePath(var2.getFilePath());
         Collection var3 = this.getAvailableRoots();
         if (var3 != null && var3.size() == 1) {
            var1.setRootNodeName((String)Iterables.get(var3, 0));
         }
      }
   }

   @Override
   public boolean prepareForForwardNavigation() {
      if (this.isSampleFileMode()) {
         return true;
      }

      if (this.doFileTransfer()) {
         ServerFile var1 = this.getSelectedFile();
         if (!this.rootElementManager.isCached(var1)) {
            this.rootElementManager.retrieveRootNames(var1);
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean doFileTransfer() {
      assert !this.isSampleFileMode();
      if (this.isLocalMode()) {
         File var2 = this.model.getLocalFile();
         return this.structureFileManager.doUpload(var2);
      } else {
         ServerFile var1 = this.model.getSelectedServerFile();
         return this.structureFileManager.downloadStructureFile(var1);
      }
   }

   private boolean isLocalMode() {
      return this.model.getInputMode() == FileInputMode.LOCAL;
   }

   public Set<ServerFile> getExistingFiles() {
      return this.model.getAllFiles();
   }

   public ServerFile getSelectedFile() {
      return this.structureFileManager.getStructureFile();
   }

   void updateRootCache(Collection<String> var1) {
      this.rootElementManager.updateRootCache(this.getSelectedFile(), var1);
   }

   @Override
   protected void onFirstDisplay() {
      this.installWaitIndication();
      this.startDownloadOfExistingStructures();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("", this.getSubTitle());
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
      this.ui.requestFocus();
   }

   @Override
   protected void dispose() {
      this.structureFileManager.cancelCurrentJobs();
      this.rootElementManager.cancelCurrentJob();
   }

   private class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               XmlStructureWizardStartPage.this.updateCanContinue();
            }
         });
      }
   }
}
