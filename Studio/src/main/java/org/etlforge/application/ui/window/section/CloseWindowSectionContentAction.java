package org.etlforge.application.ui.window.section;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;

final class CloseWindowSectionContentAction extends AbstractAction {
   private final WindowSection section;
   private final WindowSectionContent content;

   public CloseWindowSectionContentAction(WindowSection var1, WindowSectionContent var2) {
      super("Close", CommonIcons.CLOSE_16);
      this.section = var1;
      this.content = var2;
      this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(81, 128));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CloseWindowSectionContentAction.this.section.removeContent(CloseWindowSectionContentAction.this.content);
         }
      });
   }
}
