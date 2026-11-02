package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import com.google.common.base.Charsets;
import java.io.File;
import java.nio.charset.Charset;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.application.ui.text.editor.TextEditor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;
import org.etlforge.integration.debug.client.file.DataFileType;
import org.etlforge.integration.debug.client.file.DataFilesInfo;
import org.etlforge.system.callback.Receiver;

public final class FileViewer {
   private final DataFileType fileType;
   private final EditorService editorService;
   private final TransformationFileStore dataFileStore;
   private final Transformation tx;
   private Charset charset = Charsets.UTF_8;

   public FileViewer(EditorService var1, Transformation var2, TransformationFileStore var3, DataFileType var4) {
      this.editorService = var1;
      this.dataFileStore = var3;
      this.fileType = var4;
      this.tx = var2;
   }

   public void setCharset(Charset var1) {
      this.charset = var1;
   }

   public void openFile(DataFilesInfo var1) {
      OperationId var2 = var1.getOperationId();
      DataFileIdentifier var3 = new DataFileIdentifier(var2, this.tx.getID(), this.fileType, 1, 1);
      DataFileOpener var4 = new DataFileOpener(this.dataFileStore, var3);
      FileViewer.Callback var5 = new FileViewer.Callback();
      Computation var6 = Computation.create(var4, var5);
      var6.swingWorkerBuilder().withErrorLog(ErrorLevel.ATTENTION, this.getClass()).buildAndExecute();
   }

   private class Callback implements Receiver<File> {
      private Callback() {
      }

      public void handle(File var1) {
         if (var1 == null) {
            Alert.error("The requested file was not found.", "File not found");
         } else {
            TextEditor var2 = this.createEditor(var1);
            FileViewer.this.editorService.openEditor(var2);
         }
      }

      private TextEditor createEditor(File var1) {
         String var2 = StringUtils.capitalize(FileViewer.this.fileType.toString().toLowerCase()) + " data";
         String var3 = FileViewer.this.tx.getName();
         int var4 = 100000;
         return DataFileEditorFactory.newEditor(var1, FileViewer.this.charset, var2, var3, var4);
      }
   }
}
