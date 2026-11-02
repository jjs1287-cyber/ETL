package org.etlforge.integration.client.ui.admin.component.menu.proxy;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.application.ui.view.View;
import org.etlforge.application.ui.view.ViewManager;
import org.etlforge.application.ui.view.ViewManagerEvent;
import org.etlforge.application.ui.view.ViewManagerListener;

final class DefaultProxyAction extends AbstractAction implements ViewManagerListener {
   private final ViewManager viewManager;
   private Action encapsulatedAction;
   private ActionEvent pendingActionEvent;

   protected DefaultProxyAction(Action var1, ViewManager var2) {
      this.encapsulatedAction = var1;
      this.viewManager = var2;
      this.initValues();
      var2.addViewManagerListener(this);
   }

   private void initValues() {
      for (String var4 : new String[]{"Name", "MnemonicKey", "SmallIcon", "SwingLargeIconKey", "ShortDescription", "AcceleratorKey"}) {
         this.putValue(var4, this.encapsulatedAction.getValue(var4));
      }
   }

   @Override
   public void viewIsLoaded(ViewManagerEvent var1) {
   }

   @Override
   public void viewIsDisplayed(ViewManagerEvent var1) {
      View var2 = var1.getView();
      if (var2.getID().equals("administration")) {
         this.transferActionProperties();
         ActionEvent var3 = this.getPendingActionEvent();
         if (var3 != null) {
            this.setPendingActionEvent(null);
            this.encapsulatedAction.actionPerformed(var3);
         }
      }
   }

   @Override
   public void viewWasRemoved(ViewManagerEvent var1) {
      View var2 = var1.getView();
      if (var2.getID().equals("administration")) {
         this.encapsulatedAction = null;
      }
   }

   @Override
   public void actionPerformed(final ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DefaultProxyAction.this.actionImpl(var1);
         }
      });
   }

   private void actionImpl(ActionEvent var1) {
      if (this.encapsulatedAction == null) {
         this.setPendingActionEvent(var1);
         this.viewManager.showView("administration");
      } else {
         this.encapsulatedAction.actionPerformed(var1);
      }
   }

   private synchronized ActionEvent getPendingActionEvent() {
      return this.pendingActionEvent;
   }

   private synchronized void setPendingActionEvent(ActionEvent var1) {
      this.pendingActionEvent = var1;
   }

   private void transferActionProperties() {
      this.setEnabled(this.encapsulatedAction.isEnabled());

      for (String var2 : Lists.newArrayList("Default", "ActionCommandKey", "SwingSelectedKey")) {
         this.putValue(var2, this.encapsulatedAction.getValue(var2));
      }
   }
}
