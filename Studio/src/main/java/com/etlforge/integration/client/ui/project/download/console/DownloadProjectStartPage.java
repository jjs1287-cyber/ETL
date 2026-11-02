package com.etlforge.integration.client.ui.project.download.console;

import com.etlforge.integration.client.ui.project.download.resources.ProjectDownloadResources;
import java.awt.EventQueue;
import javax.swing.Icon;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public class DownloadProjectStartPage extends GenericEditor {
   private final DownloadProjectConsole console;

   public DownloadProjectStartPage(DownloadProjectConsole var1) {
      super(EditorIdGenerator.get(DownloadProjectStartPage.class), "Download Project");
      this.console = var1;
      this.setMementoPath(new String[]{"Project", "Download"});
      Icon var2 = ProjectDownloadResources.getIcon("Download.16");
      this.setIcon(var2);
      this.setTitleIcon(var2);
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.createTitlePanel();
      this.setToolBar(this.createToolBar());
      this.setMainPanel(BorderLayoutBuilder.fillWith(this.console));
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DownloadProjectStartPage.this.console.prepareForDisplay();
         }
      });
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      boolean var2 = super.removeFromWindow(var1);
      if (var2) {
         this.console.dispose();
      }

      return var2;
   }
}
