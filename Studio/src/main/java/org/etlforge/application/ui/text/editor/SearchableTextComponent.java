package org.etlforge.application.ui.text.editor;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.util.KeyUtils;

public class SearchableTextComponent {
   private final JTextComponent textComponent;
   private final FindTextAction action;

   public SearchableTextComponent(JTextComponent var1, String var2, boolean var3) {
      this(var1, var2, false, var3);
   }

   public SearchableTextComponent(JTextComponent var1, String var2, boolean var3, boolean var4) {
      Preconditions.checkNotNull(var1, "tc");
      this.textComponent = var1;
      this.action = new FindTextAction(var1, var2, var3);
      if (var4) {
         this.installSearchShortcut(var1, this.action);
      }

      if (var3) {
         this.installReplaceShortcut(var1);
      }
   }

   private void installSearchShortcut(JTextComponent var1, Action var2) {
      String var3 = "findText";
      var1.getInputMap(2).put(KeyUtils.FIND, var3);
      var1.getInputMap(0).put(KeyUtils.FIND, var3);
      var1.getActionMap().put(var3, var2);
   }

   private void installReplaceShortcut(JTextComponent var1) {
      String var2 = "replaceText";
      var1.getInputMap(2).put(KeyUtils.FIND_AND_REPLACE, var2);
      var1.getInputMap(0).put(KeyUtils.FIND_AND_REPLACE, var2);
      var1.getActionMap().put(var2, new SearchableTextComponent.ReplaceAction());
   }

   public SearchableTextComponent(JTextComponent var1, boolean var2) {
      this(var1, null, var2);
   }

   public JTextComponent getTextComponent() {
      return this.textComponent;
   }

   public Action getFindAction() {
      return this.action;
   }

   public void setActionDescription(String var1) {
      this.action.putValue("ShortDescription", var1);
   }

   private class ReplaceAction extends AbstractAction {
      private ReplaceAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ReplaceAction.this.replace();
            }
         });
      }

      private void replace() {
         SearchableTextComponent.this.action.openReplaceDialog();
      }
   }
}
