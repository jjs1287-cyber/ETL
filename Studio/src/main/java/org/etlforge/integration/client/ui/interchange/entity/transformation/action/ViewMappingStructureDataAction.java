package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import com.google.common.base.Optional;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.widget.XmlFileViewer;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.structure.mapping.JtrForHumansConverter;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

abstract class ViewMappingStructureDataAction extends AbstractApplicationAction {
   private final ApplicationWindow appWin;
   private final Transformation tx;
   private final ViewMappingStructureDataAction.Mode mode;

   public ViewMappingStructureDataAction(String var1, ApplicationWindow var2, Transformation var3, ViewMappingStructureDataAction.Mode var4) {
      super(var1, TransformationActionDictionary.DICTIONARY);
      this.appWin = var2;
      this.tx = var3;
      this.mode = var4;
   }

   @Override
   public void execute(ActionEvent var1) {
      ViewMappingStructureDataAction.Worker var2 = new ViewMappingStructureDataAction.Worker();
      KongaSwingWorker.runWhileWaiting(var2, this.getWaitService());
   }

   private void displayXml(final File var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            XmlFileViewer.getViewer(ViewMappingStructureDataAction.this.appWin).viewFile(var1);
         }
      });
   }

   protected enum Mode {
      INPUT,
      OUTPUT;
   }

   private class Worker implements Runnable {
      private Worker() {
      }

      @Override
      public void run() {
         try {
            Optional var1 = this.getMappingStructure();
            if (var1.isPresent()) {
               File var2 = this.createFile((MappingTreeStructure)var1.get());
               ViewMappingStructureDataAction.this.displayXml(var2);
            } else {
               Alert.info("There is no structure data available for viewing.", "No Structure Data");
            }
         } catch (Exception var3) {
            ErrorLog.attention(ViewMappingStructureDataAction.class, "An error occurred while opening the data", var3);
         }
      }

      private Optional<MappingTreeStructure> getMappingStructure() throws DataCacheException {
         ManagedProject var1 = ProjectUtils.getManagedProject(ViewMappingStructureDataAction.this.tx);
         if (var1 != null) {
            InputAndOutputMappingTreeStructureCache var2 = var1.getTransformationMappingStructureCache();
            return ViewMappingStructureDataAction.this.mode == ViewMappingStructureDataAction.Mode.INPUT
               ? var2.getInputStructure(ViewMappingStructureDataAction.this.tx.getID())
               : var2.getOutputStructure(ViewMappingStructureDataAction.this.tx.getID());
         } else {
            return null;
         }
      }

      private File createFile(MappingTreeStructure var1) {
         File var2 = ApplicationTempFolder.newFile(ViewMappingStructureDataAction.this.tx.getID() + "_" + ViewMappingStructureDataAction.this.mode);
         var2.deleteOnExit();
         new JtrForHumansConverter().convert(var1, var2);
         return var2;
      }
   }
}
