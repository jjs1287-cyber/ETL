package org.etlforge.integration.client.ui.jitterpack;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.action.ActionInvocationListener;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.jitterpack.Exporter;
import org.etlforge.integration.client.ui.jitterpack.resources.JitterpackResources;
import org.etlforge.integration.client.ui.project.loading.LoadingIssuesPresenter;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.system.callback.CallbackResultWrapper;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.wait.ResourceReleasingCallback;

public final class DoExportAction extends AbstractApplicationAction {
   private final ApplicationWindow window;
   private final ExportJitterPackUi exportUi;
   private final Exporter exporter;
   private final ActionInvocationListener invokationListener;
   private final CallbackResult externalCallback;

   public DoExportAction(ApplicationWindow var1, ExportJitterPackUi var2, Exporter var3, ActionInvocationListener var4, CallbackResult var5) {
      super("do_export", Strings.get("Export"), JitterpackResources.EXPORT_16);
      this.putValue("ShortDescription", Strings.getJitterPackString("Export.ToolTip"));
      this.setEnabled(false);
      this.window = var1;
      this.exportUi = var2;
      this.exporter = var3;
      this.invokationListener = var4;
      this.externalCallback = new CallbackResultWrapper(var5);
   }

   @Override
   public void execute(ActionEvent var1) {
      this.setEnabled(false);
      if (this.invokationListener != null) {
         this.invokationListener.actionInvoked(this);
      }

      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DoExportAction.this.executeImpl();
         }
      });
   }

   private void executeImpl() {
      DoExportAction.PackerCallback var1 = new DoExportAction.PackerCallback();
      if (this.checkFile()) {
         JitterPackerUi var2 = new JitterPackerUi();
         var2.setLoadingIssuesHandler(new LoadingIssuesPresenter());
         var2.pack(this.exportUi, this.exporter, var1);
      } else {
         var1.cancelled(null);
      }
   }

   private boolean checkFile() {
      File var1 = this.exportUi.getDestination();
      if (var1.exists()) {
         DefaultFileChooser.DefaultConfirmer var2 = new DefaultFileChooser.DefaultConfirmer() {
            @Override
            protected JComponent getMessageObject(File var1) {
               return new JLabel(this.getMessage(var1));
            }

            @Override
            protected Component getDialogOwner() {
               return DoExportAction.this.window.getFrame();
            }
         };
         return var2.confirmOverwrite(var1);
      } else {
         return true;
      }
   }

   private final class PackerCallback extends ResourceReleasingCallback {
      private PackerCallback() {
      }

      @Override
      protected void onCancel(Object var1) {
         DoExportAction.this.externalCallback.cancelled(var1);
      }

      @Override
      protected void onFailure(Object var1) {
         DoExportAction.this.externalCallback.failed(var1);
         Throwable var2 = null;
         String var3 = Strings.getJitterPackString("Export.Error.General");
         if (var1 instanceof Throwable) {
            var2 = (Throwable)var1;
         } else {
            var2 = new Exception(var3 + " " + var1);
         }

         ErrorLog.attention(DoExportAction.class, var3, var2);
      }

      @Override
      protected void onSuccess(Object var1) {
         DoExportAction.this.externalCallback.succeeded(var1);
         DoExportAction.this.window.postApplicationMessageEvent(new ApplicationMessageEvent(this, Strings.getJitterPackString("Export.Success"), 0));
      }

      @Override
      protected void releaseResource() {
         ActionUtils.setEnabledSafely(DoExportAction.this, true);
      }
   }
}

