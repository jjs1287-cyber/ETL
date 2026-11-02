package org.etlforge.ui.widget.selection;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTextField;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.util.KeyUtils;

public final class SelectFirstAction extends AbstractAction {
   private final SelectionWidget widget;

   public static final SelectFirstAction installInField(SelectionWidget var0, JTextField var1) {
      SelectFirstAction var2 = new SelectFirstAction(var0);
      ActionKeyBinding.install(var2, var1, KeyUtils.DOWN);
      return var2;
   }

   public SelectFirstAction(SelectionWidget var1) {
      this.widget = Preconditions.checkNotNull(var1, "widget");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (!this.widget.isEmpty()) {
         this.widget.selectFirst();
         this.widget.requestFocus();
      }
   }
}
