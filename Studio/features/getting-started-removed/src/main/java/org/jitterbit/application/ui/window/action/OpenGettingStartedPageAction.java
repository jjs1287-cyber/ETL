package org.jitterbit.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.jitterbit.application.ui.GettingStartedPageFactory;
import org.jitterbit.application.ui.resources.CommonIcons;
import org.jitterbit.application.ui.window.editor.Editor;
import org.jitterbit.application.ui.window.editor.EditorService;

public final class OpenGettingStartedPageAction extends AbstractAction {
   private final EditorService editorService;
   private final GettingStartedPageFactory pageFactory;

   public OpenGettingStartedPageAction(EditorService var1, GettingStartedPageFactory var2) {
      super("&Getting Started", CommonIcons.GETTING_STARTED_16);
      this.editorService = var1;
      this.pageFactory = var2;
      this.putValue("KongaMediumIconKey", CommonIcons.GETTING_STARTED_24);
      this.putValue("SwingLargeIconKey", CommonIcons.GETTING_STARTED_32);
      this.setEnabled(var2.isSupported());
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OpenGettingStartedPageAction.this.openPage();
         }
      });
   }

   private void openPage() {
      if (this.pageFactory.isSupported()) {
         Editor var1 = this.findOpenPage();
         if (var1 == null) {
            var1 = this.pageFactory.newGettingStartedPage();
         }

         this.editorService.openEditor(var1);
      }
   }

   private Editor findOpenPage() {
      String var1 = this.pageFactory.getId();

      for (Editor var5 : this.editorService.getOpenEditors()) {
         if (var5.getId().equals(var1)) {
            return var5;
         }
      }

      return null;
   }
}
