package org.etlforge.integration.client.ui.interchange.component.launcher;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.UiProviderEditor;

public final class ProjectLauncherViewer {
   private final InterchangeLauncher launcher;

   public ProjectLauncherViewer(InterchangeLauncher var1) {
      Preconditions.checkNotNull(var1, "launcher");
      this.launcher = var1;
   }

   public void open() {
      this.open(this.getConfiguredMode());
   }

   public void open(ProjectLauncherViewer.Mode var1) {
      var1.showLauncher(this.launcher);
   }

   private ProjectLauncherViewer.Mode getConfiguredMode() {
      String var1 = ApplicationConfiguration.getString("ProjectLauncher.Mode", null);
      if (var1 != null) {
         try {
            return ProjectLauncherViewer.Mode.valueOf(var1.toUpperCase());
         } catch (Exception var3) {
            ErrorLog.log(ProjectLauncherViewer.class, "Invalid value of the ProjectLauncher.Mode key in the application resources: " + var1, var3);
         }
      }

      return ProjectLauncherViewer.Mode.DIALOG;
   }

   private static class EditorDisplayer {
      private static final String EDITOR_ID = "Project.Launcher";
      private final InterchangeLauncher launcher;
      private ActionListener launcherListener;

      public EditorDisplayer(InterchangeLauncher var1) {
         this.launcher = var1;
      }

      public void open() {
         Editor var1 = this.findOpenEditor();
         if (var1 == null) {
            this.launcher.wakeUp();
            var1 = this.createNewEditor(this.launcher);
            this.attachLauncherListener(this.launcher, var1);
         }

         EditorService var2 = this.getEditorService();
         var2.openEditor(var1);
      }

      private EditorService getEditorService() {
         ApplicationWindow var1 = this.launcher.getWindow();
         return var1.getEditorService();
      }

      private Editor createNewEditor(final InterchangeLauncher var1) {
         UiProviderEditor var2 = new UiProviderEditor(var1.getUi(), "Project.Launcher", "Open Project") {
            @Override
            public boolean removeFromWindow(boolean var1x) {
               this.getObject().removeLaunchListener(EditorDisplayer.this.launcherListener);
               this.getObject().hibernate();
               return super.removeFromWindow(var1x);
            }

            public InterchangeLauncher getObject() {
               return var1;
            }
         };
         var2.setIcon(CommonIcons.OPEN_16);
         return var2;
      }

      private void attachLauncherListener(InterchangeLauncher var1, final Editor var2) {
         this.launcherListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               var2.close();
            }
         };
         var1.addLaunchListener(this.launcherListener);
      }

      private Editor findOpenEditor() {
         EditorService var1 = this.getEditorService();
         return var1.getEditor("Project.Launcher");
      }
   }

   public enum Mode {
      DIALOG {
         @Override
         protected void showLauncher(InterchangeLauncher var1) {
            LauncherDialog.publish(var1);
         }
      },
      EDITOR {
         @Override
         protected void showLauncher(InterchangeLauncher var1) {
            ProjectLauncherViewer.EditorDisplayer var2 = new ProjectLauncherViewer.EditorDisplayer(var1);
            var2.open();
         }
      };

      Mode() {
      }

      protected abstract void showLauncher(InterchangeLauncher var1);
   }
}
