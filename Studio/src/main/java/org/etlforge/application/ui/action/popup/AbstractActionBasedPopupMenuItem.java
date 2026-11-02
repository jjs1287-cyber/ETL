package org.etlforge.application.ui.action.popup;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.window.action.ActiveObjectsAction;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public abstract class AbstractActionBasedPopupMenuItem implements PopupMenuItem {
   private final Action action;
   private JMenuItem menuItem;
   private boolean enabled;
   private boolean visible;
   private List<?> objectsActedOn;

   public AbstractActionBasedPopupMenuItem(Action var1) {
      Preconditions.checkNotNull(var1, "action");
      this.action = var1;
   }

   @Override
   public final void addToPopupMenu(JPopupMenu var1) {
      if (this.menuItem == null) {
         this.menuItem = new KongaMenuItem();
         this.menuItem.addActionListener(new AbstractActionBasedPopupMenuItem.ActionWrapper());
      }

      ButtonUtils.decorate(this.menuItem, this.action);
      this.menuItem.setEnabled(this.enabled && this.action.isEnabled());
      var1.add(this.menuItem);
   }

   @Override
   public final boolean isVisible() {
      return this.visible;
   }

   @Override
   public void updateState(PopupMenuSite var1, List<?> var2) {
      this.objectsActedOn = var2;
   }

   protected final void setVisible(boolean var1) {
      this.visible = var1;
   }

   protected final void setEnabled(boolean var1) {
      this.enabled = var1;
      if (this.menuItem != null) {
         this.menuItem.setEnabled(this.enabled);
      }
   }

   protected final Action getAction() {
      return this.action;
   }

   protected final void setName(String var1) {
      this.action.putValue("Name", var1);
      if (this.menuItem != null) {
         this.menuItem.setText(var1);
      }
   }

   private class ActionWrapper implements ActionListener {
      private ActionWrapper() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (AbstractActionBasedPopupMenuItem.this.action instanceof ActiveObjectsAction) {
            ((ActiveObjectsAction)AbstractActionBasedPopupMenuItem.this.action).setActiveObjects(AbstractActionBasedPopupMenuItem.this.objectsActedOn);
            this.triggerLater(var1);
         } else {
            this.trigger(var1);
         }
      }

      private void trigger(ActionEvent var1) {
         AbstractActionBasedPopupMenuItem.this.action.actionPerformed(var1);
      }

      private void triggerLater(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ActionWrapper.this.trigger(var1);
            }
         });
      }
   }
}
