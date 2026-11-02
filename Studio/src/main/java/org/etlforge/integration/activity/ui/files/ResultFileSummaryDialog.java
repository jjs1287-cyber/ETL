package org.etlforge.integration.activity.ui.files;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.util.OpenInExternalProgramAction;
import org.etlforge.integration.activity.client.files.ResultFileSummary;
import org.etlforge.integration.common.server.file.FileCategory;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.LayeredKongaDialog;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.wait.WaitService;

final class ResultFileSummaryDialog implements Receiver<ResultFileSummary> {
   private final OperationFile file;
   private final SaveCopyOfResultFileAction copyAction = new SaveCopyOfResultFileAction();
   private final OpenInExternalProgramAction openAction = new OpenInExternalProgramAction();
   private final Optional<Component> invoker;

   public ResultFileSummaryDialog(OperationFile var1, Optional<Component> var2) {
      this.file = var1;
      this.invoker = var2;
      this.copyAction.setInvoker(var2);
   }

   public void handle(ResultFileSummary var1) {
      KongaDialog var2 = this.createDialog(var1);
      File var3 = var1.getLocalFile();
      this.copyAction.setFile(var3);
      this.copyAction.setWaitService(var2);
      this.openAction.setFile(var3);
      this.openAction.setWaitService(var2);
      var2.setVisible(true);
      this.cleanupAfterClose();
   }

   private void cleanupAfterClose() {
      this.copyAction.setWaitService(WaitService.NONE);
      this.openAction.setWaitService(WaitService.NONE);
   }

   private KongaDialog createDialog(ResultFileSummary var1) {
      ResultFileSummaryView var2 = this.createView(var1);
      LayeredKongaDialog var3 = new LayeredKongaDialog(ApplicationUi.getWindowAncestor(this.invoker), "Result");
      var3.getDefaultLayout().center(var2);
      JPanel var4 = this.createButtons(var3);
      var3.getDefaultLayout().south(var4);
      var3.getDefaultLayout().withBorder(Empty.border(10));
      var3.pack();
      var3.setCloseButtonAsDefault();
      var3.setFocusedComponent(var2);
      var3.manageLocation(this.getClass());
      return var3;
   }

   private ResultFileSummaryView createView(ResultFileSummary var1) {
      ResultFileSummaryView var2 = new ResultFileSummaryView(this.file, var1);
      ArrayList var3 = Lists.newArrayList();
      CopyRecordsAction var4 = new CopyRecordsAction();
      var4.setSelectionSource(var2.asSelectionSource());
      var3.add(var4);
      if (this.file.getCategory() == FileCategory.SFDC_FAILURE) {
         CopyErrorAction var5 = new CopyErrorAction();
         var5.setSelectionSource(var2.asSelectionSource());
         var3.add(var5);
      }

      var2.installContextMenu(var3);
      return var2;
   }

   private JPanel createButtons(LayeredKongaDialog var1) {
      JButton var2 = var1.createCloseButton();
      return Horizontal.box().withEmptyBorder(10, 0, 10, 0).addAllWithSpace(10, this.copyAction, this.openAction).glue().add(var2).nonOpaque().container();
   }
}
