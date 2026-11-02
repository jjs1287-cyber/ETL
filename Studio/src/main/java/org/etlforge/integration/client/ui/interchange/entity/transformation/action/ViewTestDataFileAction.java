package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import com.google.common.base.Charsets;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.nio.charset.Charset;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.FileViewer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataServiceEDTListener;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataServiceListener;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.TransformationFileStore;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;
import org.etlforge.integration.debug.client.file.DataFileType;
import org.etlforge.integration.debug.client.file.DataFilesInfo;

public class ViewTestDataFileAction extends AbstractApplicationAction {
   private final TransformationPage page;
   private final DataFileType fileType;
   private final MappingTreeDataServiceListener listener;

   public ViewTestDataFileAction(String var1, TransformationPage var2, DataFileType var3) {
      super(var1, TransformationActionDictionary.DICTIONARY);
      this.page = var2;
      this.fileType = var3;
      this.listener = new MappingTreeDataServiceEDTListener(new ViewTestDataFileAction.Listener());
      var2.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            ViewTestDataFileAction.this.listenToFileStore();
            DataFilesInfo var1x = ViewTestDataFileAction.this.getCurrentFileInfo();
            ViewTestDataFileAction.this.updateEnabledState(var1x);
         }
      });
   }

   private void listenToFileStore() {
      MappingTreeDataService var1 = this.page.getTreeDataService();
      var1.addListener(this.listener);
   }

   private DataFilesInfo getCurrentFileInfo() {
      MappingTreeDataService var1 = this.page.getTreeDataService();
      TransformationFileStore var2 = var1.getDataFileStore();
      return var2.getCurrentFilesInfo();
   }

   private void updateEnabledState(DataFilesInfo var1) {
      boolean var2;
      if (var1 == null) {
         var2 = false;
      } else {
         DataFileIdentifier var3 = new DataFileIdentifier(var1.getOperationId(), this.page.getObject().getID(), this.fileType, 1, 1);
         var2 = var1.contains(var3);
      }

      this.setEnabledSafely(var2);
   }

   @Override
   public final void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewTestDataFileAction.this.openFile();
         }
      });
   }

   private void openFile() {
      EditorService var1 = this.page.getWindow().getEditorService();
      TransformationFileStore var2 = this.page.getTreeDataService().getDataFileStore();
      FileViewer var3 = new FileViewer(var1, this.page.getObject(), var2, this.fileType);
      var3.setCharset(this.getCharset(this.page.getObject()));
      DataFilesInfo var4 = var2.getCurrentFilesInfo();
      var3.openFile(var4);
   }

   @Override
   public final void dispose() {
      MappingTreeDataService var1 = this.page.getTreeDataService();
      var1.removeListener(this.listener);
   }

   protected Charset getCharset(Transformation var1) {
      return Charsets.UTF_8;
   }

   private class Listener implements MappingTreeDataServiceListener {
      private Listener() {
      }

      @Override
      public void allDataRemoved() {
         ViewTestDataFileAction.this.updateEnabledState(null);
      }

      @Override
      public void loadSourceDataAvailable(DataFilesInfo var1) {
         ViewTestDataFileAction.this.updateEnabledState(var1);
      }

      @Override
      public void testOperationDataAvailable(DataFilesInfo var1) {
         ViewTestDataFileAction.this.updateEnabledState(var1);
      }

      @Override
      public void targetDataRemoved() {
         DataFilesInfo var1 = ViewTestDataFileAction.this.getCurrentFileInfo();
         ViewTestDataFileAction.this.updateEnabledState(var1);
      }

      @Override
      public void testTransformationDataAvailable(DataFilesInfo var1) {
         ViewTestDataFileAction.this.updateEnabledState(var1);
      }
   }
}
