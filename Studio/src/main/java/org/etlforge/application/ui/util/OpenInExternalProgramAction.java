package org.etlforge.application.ui.util;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.Callable;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;

public final class OpenInExternalProgramAction extends AbstractAction {
   private File file;
   private final NativeFileEditor editor = NativeFileEditor.getEditor();
   private WaitService waitService = WaitService.NONE;

   public OpenInExternalProgramAction() {
      this(null);
   }

   public OpenInExternalProgramAction(File var1) {
      super("Open in External Program");
      this.file = var1;
      this.setEnabled(var1 != null);
   }

   public final void setFile(File var1) {
      this.file = Preconditions.checkNotNull(var1);
      this.setEnabled(true);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.editor.isSupported()) {
         this.edit();
      } else {
         Optional var2 = UiUtils.getComponentSource(var1);
         notSupported(var2);
      }
   }

   private void edit() {
      Callable var1 = new Callable<Void>() {
         public Void call() throws Exception {
            OpenInExternalProgramAction.this.editor.edit(OpenInExternalProgramAction.this.file);
            return null;
         }
      };
      KongaSwingWorker.runCallableWhileWaiting(var1, this.waitService);
   }

   private static void notSupported(Optional<Component> var0) {
      Component var1 = var0.isPresent() ? (Component)var0.get() : null;
      NonBlockingAlert.error(var1, "This action is not supported on this platform.", "Not supported.");
   }
}
