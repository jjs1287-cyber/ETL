package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.event.Mouse;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;

public final class DefaultActionHandler {
   private final JComponent component;
   private KeyBindingInstaller keyBinding;
   private DefaultActionHandler.ClickHandler clickHandler;
   private final Action action;
   private boolean singleClickSelectionEnabled;
   private final Predicate<MouseEvent> clickLocationCondition;

   public DefaultActionHandler(JComponent var1, Action var2) {
      this.component = Preconditions.checkNotNull(var1, "component");
      this.action = Preconditions.checkNotNull(var2, "action");
      this.clickLocationCondition = createClickLocationFilter(var1);
   }

   private static Predicate<MouseEvent> createClickLocationFilter(JComponent var0) {
      if (var0 instanceof JTree) {
         return Mouse.onTreeRow((JTree)var0);
      } else {
         return var0 instanceof JList ? Mouse.onListRow((JList)var0) : Predicates.alwaysTrue();
      }
   }

   public void install() {
      if (this.keyBinding == null) {
         this.keyBinding = new KeyBindingInstaller(this.component);
         this.keyBinding.addBinding(KeyUtils.ENTER, "enter", this.action);
         this.keyBinding.install();
         this.clickHandler = new DefaultActionHandler.ClickHandler();
         this.component.addMouseListener(this.clickHandler);
      }
   }

   public void uninstall() {
      if (this.keyBinding != null) {
         this.keyBinding.remove();
         this.keyBinding = null;
         this.component.removeMouseListener(this.clickHandler);
         this.clickHandler = null;
      }
   }

   public void setSingleClickSelectionEnabled(boolean var1) {
      this.singleClickSelectionEnabled = var1;
   }

   private class ClickHandler extends MouseActionListener {
      private ClickHandler() {
      }

      @Override
      protected void onSingleClick(MouseEvent var1) {
         if (DefaultActionHandler.this.singleClickSelectionEnabled) {
            this.triggerAction(var1);
         }
      }

      @Override
      protected void onDoubleClick(MouseEvent var1) {
         if (!DefaultActionHandler.this.singleClickSelectionEnabled) {
            this.triggerAction(var1);
         }
      }

      private void triggerAction(MouseEvent var1) {
         if (DefaultActionHandler.this.clickLocationCondition.apply(var1)) {
            ActionUtils.run(DefaultActionHandler.this.action, DefaultActionHandler.this.component, "double-click");
         }
      }
   }
}
