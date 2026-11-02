package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.dnd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import javax.swing.JViewport;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.TestTransformationService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.loadsource.client.LoadSourceDataFileInput;
import org.etlforge.integration.loadsource.client.SourceDataService;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.system.callback.CallbackResultAdapter;

public final class SourceTreeFileDropStrategy implements DropStrategy {
   private static final Color REJECTION_BACKGROUND = new Color(255, 104, 104, 40);
   private final TreeMapper treeMapper;
   private File file;
   private final DropZone[] supportedDropZones;
   private DropZone rejectionZone;

   public SourceTreeFileDropStrategy(TreeMapper var1) {
      this.treeMapper = var1;
      this.supportedDropZones = new DropZone[]{
         new DropZone("Load Source", "Drop here to do Load Source", new Color(178, 204, 255, 40)),
         new DropZone("Test Transformation", "Drop here to do Test Transformation", new Color(178, 255, 178, 40))
      };
   }

   @Override
   public DropZone[] getDropZones(DropTargetDragEvent var1) {
      this.file = this.getDraggedFile(var1);
      if (this.file != null) {
         return this.supportedDropZones;
      } else {
         return this.rejectionZone != null ? new DropZone[]{this.rejectionZone} : NO_ZONES;
      }
   }

   private File getDraggedFile(DropTargetDragEvent var1) {
      this.rejectionZone = null;

      try {
         if (!this.isFileDropSupported()) {
            this.createRejectionZone("Not a file based source type.");
            return null;
         }

         if (!var1.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            this.createRejectionZone("Only files can be dropped.");
            return null;
         }

         List var2 = (List)var1.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
         if (var2.size() == 1) {
            return (File)var2.get(0);
         }

         this.createRejectionZone("Only one file can be dropped at the time.");
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return null;
   }

   private boolean isFileDropSupported() {
      DataStructure var1 = this.treeMapper.getTransformation().getInputStructure();
      DataStructureType var2 = var1.getStructureType();
      return var2.isFileBased();
   }

   private void createRejectionZone(String var1) {
      this.rejectionZone = new DropZone("Drop Not Allowed", var1, REJECTION_BACKGROUND);
      this.rejectionZone.setSupportsDrops(false);
   }

   @Override
   public boolean handleDrop(DropTargetDropEvent var1) {
      if (this.file == null) {
         return true;
      }

      boolean var2 = this.isTestTransformation(var1);
      SourceTreeFileDropStrategy.Workflow var3 = new SourceTreeFileDropStrategy.Workflow(this.treeMapper, this.file, var2);
      KongaSwingWorker.runCallable(var3);
      return true;
   }

   private boolean isTestTransformation(DropTargetDropEvent var1) {
      Component var2 = var1.getDropTargetContext().getComponent();
      JViewport var3 = (JViewport)var2.getParent();
      Rectangle var4 = var3.getViewRect();
      int var5 = var1.getLocation().y - var4.y;
      int var6 = var4.height / 2;
      return var5 > var6;
   }

   private static class Workflow implements Callable<Void> {
      private final TreeMapper treeMapper;
      private final File file;
      private final boolean isTestTransformation;

      public Workflow(TreeMapper var1, File var2, boolean var3) {
         this.treeMapper = var1;
         this.file = var2;
         this.isTestTransformation = var3;
      }

      public Void call() throws Exception {
         LoadSourceDataFileInput var1 = new LoadSourceDataFileInput(this.file);
         SourceDataService var2 = this.getDataService().getSourceDataService();
         CallbackResult var3 = this.isTestTransformation ? this.getTestTransformationTrigger() : null;
         var2.loadSourceData(var1, var3);
         return null;
      }

      private MappingTreeDataService getDataService() {
         return this.treeMapper.getTreeMapperContainer().getTreeDataService();
      }

      private CallbackResult getTestTransformationTrigger() {
         return new CallbackResultAdapter() {
            @Override
            public void succeeded(Object var1) {
               this.startTest();
            }

            private void startTest() {
               TestTransformationService var1 = Workflow.this.getDataService().getTestTransformationService();
               var1.testTransformation();
            }
         };
      }
   }
}
