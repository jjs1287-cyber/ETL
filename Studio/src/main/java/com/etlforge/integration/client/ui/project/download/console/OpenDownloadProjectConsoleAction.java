package com.etlforge.integration.client.ui.project.download.console;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.admin.project.ProjectDownloaderCallback;
import com.etlforge.integration.client.ui.project.download.resources.ProjectDownloadResources;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModel;
import org.etlforge.integration.client.ui.project.loading.ProjectLoader;

public final class OpenDownloadProjectConsoleAction extends AbstractAction {
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;
   private final DownloadProjectConsoleFactory consoleFactory;
   private String currentPageId;

   public OpenDownloadProjectConsoleAction(ApplicationWindow var1, ProjectManager var2, DownloadProjectConsoleFactory var3) {
      super(ProjectDownloadResources.getString("OpenConsoleAction.Name"), ProjectDownloadResources.getIcon("Download.16"));
      this.putValue("AcceleratorKey", ProjectDownloadResources.getKeyStroke("OpenConsoleAction.Accelerator"));
      this.putValue("ShortDescription", ProjectDownloadResources.getString("OpenConsoleAction.ToolTip"));
      this.appWin = var1;
      this.projectManager = var2;
      this.consoleFactory = var3;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      new BusyWorker(this.appWin, new OpenDownloadProjectConsoleAction.ConsoleOpener()).run();
   }

   private Editor locateOpenPage() {
      return this.currentPageId != null ? this.appWin.getEditorService().getEditor(this.currentPageId) : null;
   }

   private final class Callback extends ProjectDownloaderCallback.Adapter {
      private Callback() {
      }

      @Override
      public void downloadCompleted(ProjectLocation var1) {
         this.closePage();
         this.loadProject(var1);
      }

      private void closePage() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Editor var1 = OpenDownloadProjectConsoleAction.this.locateOpenPage();
               if (var1 != null) {
                  var1.close();
               }
            }
         });
      }

      private void loadProject(ProjectLocation var1) {
         ProjectLoader var2 = new ProjectLoader(OpenDownloadProjectConsoleAction.this.projectManager);
         var2.setWaitService(OpenDownloadProjectConsoleAction.this.appWin);
         var2.loadAndDisplay(var1);
      }
   }

   private final class ConsoleOpener implements Runnable {
      private ConsoleOpener() {
      }

      @Override
      public void run() {
         Editor var1 = OpenDownloadProjectConsoleAction.this.locateOpenPage();
         if (var1 == null) {
            var1 = this.createNewEditor();
         }

         OpenDownloadProjectConsoleAction.this.appWin.getEditorService().openEditor(var1);
      }

      private Editor createNewEditor() {
         DownloadProjectConsole var1 = this.createConsole();
         DownloadProjectStartPage var2 = new DownloadProjectStartPage(var1);
         OpenDownloadProjectConsoleAction.this.currentPageId = var2.getId();
         return var2;
      }

      private DownloadProjectConsole createConsole() {
         ProjectConsoleModel var1 = new ProjectConsoleModel();
         OpenDownloadProjectConsoleAction.Callback var2 = OpenDownloadProjectConsoleAction.this.new Callback();
         OpenDownloadProjectConsoleAction.PreDownloadCondition var3 = OpenDownloadProjectConsoleAction.this.new PreDownloadCondition();
         DownloadProjectConsole var4 = OpenDownloadProjectConsoleAction.this.consoleFactory.newConsole(var1, var2, var3);
         var4.setWaitService(OpenDownloadProjectConsoleAction.this.appWin);
         return var4;
      }
   }

   private final class PreDownloadCondition implements Supplier<Boolean> {
      private PreDownloadCondition() {
      }

      public Boolean get() {
         try {
            OpenDownloadProjectConsoleAction.this.projectManager.closeCurrentProject();
            return true;
         } catch (CloseProjectVetoException var2) {
            return false;
         }
      }
   }
}
