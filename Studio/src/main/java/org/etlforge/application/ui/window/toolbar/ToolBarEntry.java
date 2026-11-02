package org.etlforge.application.ui.window.toolbar;

import com.google.common.base.Preconditions;
import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.ui.widget.button.KongaButton;

public abstract class ToolBarEntry {
   public static ToolBarEntry of(JButton var0) {
      return new ToolBarEntry.Button(Preconditions.checkNotNull(var0, "button"));
   }

   public static ToolBarEntry of(Action var0) {
      JButton var1 = createButton(Preconditions.checkNotNull(var0, "action"));
      return new ToolBarEntry.Button(var1);
   }

   public static ToolBarEntry of(ApplicationAction var0, ActionDictionary var1) {
      return of(Preconditions.checkNotNull(var0, "action"), var0.getID(), var1);
   }

   public static ToolBarEntry of(Action var0, String var1, ActionDictionary var2) {
      JButton var3 = createButton(Preconditions.checkNotNull(var0, "action"), var1, var2);
      return new ToolBarEntry.Button(var3);
   }

   private static JButton createButton(Action var0, String var1, ActionDictionary var2) {
      JButton var3 = createButton(var0);
      boolean var4 = ToolbarMode.getConfiguredMode() == ToolbarMode.MINIMAL;
      Icon var5 = var4 ? var2.getLargeIcon(var1) : var2.getMediumIcon(var1);
      if (var5 != null) {
         var3.setIcon(var5);
      }

      return var3;
   }

   private static JButton createButton(Action var0) {
      return KongaButton.forToolbar(var0);
   }

   public static ToolBarEntry separator() {
      return new ToolBarEntry.Separator();
   }

   public abstract void addToToolBar(JToolBar var1);

   public abstract void setEnabled(boolean var1);

   private static final class Button extends ToolBarEntry {
      private final JButton button;

      public Button(JButton var1) {
         this.button = var1;
         var1.setFocusPainted(false);
      }

      @Override
      public void addToToolBar(JToolBar var1) {
         var1.add(this.button);
      }

      @Override
      public void setEnabled(boolean var1) {
         this.button.setEnabled(var1);
      }
   }

   private static final class Separator extends ToolBarEntry {
      private Separator() {
      }

      @Override
      public void addToToolBar(JToolBar var1) {
         var1.addSeparator(new Dimension(5, 0));
      }

      @Override
      public void setEnabled(boolean var1) {
      }
   }
}
