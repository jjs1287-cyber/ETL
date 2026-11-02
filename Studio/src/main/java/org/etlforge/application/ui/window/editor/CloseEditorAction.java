package org.etlforge.application.ui.window.editor;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.action.WindowActionDictionary;

public final class CloseEditorAction extends AbstractAction {
   private final Editor editor;

   public CloseEditorAction(Editor var1) {
      this.editor = var1;
      ActionDictionary var2 = WindowActionDictionary.DICTIONARY;
      String var3 = "action.Close";
      this.putValue("Name", var2.getName(var3).getRaw());
      this.putValue("SmallIcon", CommonIcons.CLOSE_16);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.editor.close();
   }
}
