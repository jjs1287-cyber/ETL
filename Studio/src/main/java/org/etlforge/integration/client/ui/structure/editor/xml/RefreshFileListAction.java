package org.etlforge.integration.client.ui.structure.editor.xml;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.functors.function.KongaFunctions;
import org.etlforge.ui.property.ActionEnabledBinding;

public final class RefreshFileListAction extends AbstractAction {
   private final StructureFileDownloader downloader;

   public RefreshFileListAction(StructureFileDownloader var1, StructureFileSelectionModel var2) {
      ActionDecorators.REFRESH.decorate(this);
      this.downloader = var1;
      this.bindEnabledState(var2);
   }

   private void bindEnabledState(StructureFileSelectionModel var1) {
      ActionEnabledBinding var2 = new ActionEnabledBinding<>(this, var1.getProperty("busy"), KongaFunctions.reverseBoolean());
      var2.syncUi();
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RefreshFileListAction.this.downloader.startDownload();
         }
      });
   }
}
