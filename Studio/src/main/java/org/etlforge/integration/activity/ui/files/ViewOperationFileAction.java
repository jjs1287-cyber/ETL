package org.etlforge.integration.activity.ui.files;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.integration.common.server.file.FileCategory;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.integration.common.server.file.OperationFileOwner;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.UiUtils;

public final class ViewOperationFileAction extends AbstractSingleObjectSelectionAction<OperationFileOwner> {
   private final OperationFileController controller;
   private final FileCategory category;

   public ViewOperationFileAction(OperationFileController var1, FileCategory var2) {
      super(OperationFileOwner.class, "View " + (var2 == FileCategory.SFDC_SUCCESS ? "&success" : "&failure") + " file");
      this.controller = Preconditions.checkNotNull(var1);
      this.category = Preconditions.checkNotNull(var2);
   }

   protected boolean isEnabledFor(OperationFileOwner var1) {
      return var1.hasFile(this.category);
   }

   protected void run(final OperationFileOwner var1, final ActionEvent var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewOperationFileAction.this.viewFile(var1, UiUtils.getComponentSource(var2));
         }
      });
   }

   private void viewFile(OperationFileOwner var1, Optional<Component> var2) {
      OperationFile var3 = var1.getFile(this.category);
      if (var3 != null) {
         ResultFileSummaryDialog var4 = new ResultFileSummaryDialog(var3, var2);
         this.controller.openFile(var3, var4);
      }
   }
}
