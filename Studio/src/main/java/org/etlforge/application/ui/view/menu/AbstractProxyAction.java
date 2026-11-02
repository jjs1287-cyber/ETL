package org.etlforge.application.ui.view.menu;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.view.View;
import org.etlforge.application.ui.view.ViewManagerAdapter;
import org.etlforge.application.ui.view.ViewManagerEvent;
import org.etlforge.application.ui.window.ApplicationWindow;

public abstract class AbstractProxyAction extends AbstractAction {
   private final String viewId;
   private final String actionId;
   private ApplicationAction encapsulatedAction;
   private PropertyChangeListener changeDispatcher;

   public AbstractProxyAction(ApplicationWindow var1, String var2, String var3, ActionDictionary var4) {
      this.viewId = var2;
      this.actionId = var3;
      ActionDecorators.decorateFromDictionary(this, var4, var3);
      super.setEnabled(false);
      var1.getViewManager().addViewManagerListener(new AbstractProxyAction.Listener());
   }

   @Override
   public final void setEnabled(boolean var1) {
      if (this.encapsulatedAction == null) {
         super.setEnabled(false);
      } else {
         this.encapsulatedAction.setEnabled(var1);
         super.setEnabled(var1);
      }
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.encapsulatedAction != null) {
         this.encapsulatedAction.actionPerformed(var1);
      }
   }

   protected abstract ApplicationAction createAction(String var1, View var2);

   private final class ChangeDispatcher implements PropertyChangeListener {
      private ChangeDispatcher() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if ("enabled".equals(var1.getPropertyName())) {
            AbstractProxyAction.super.setEnabled((Boolean)var1.getNewValue());
         }

         AbstractProxyAction.this.firePropertyChange(var1.getPropertyName(), var1.getOldValue(), var1.getNewValue());
      }
   }

   private final class Listener extends ViewManagerAdapter {
      private Listener() {
      }

      @Override
      public void viewIsDisplayed(ViewManagerEvent var1) {
         View var2 = var1.getView();
         if (var2.getID().equals(AbstractProxyAction.this.viewId)) {
            AbstractProxyAction.this.encapsulatedAction = AbstractProxyAction.this.createAction(AbstractProxyAction.this.actionId, var2);
            this.transferActionProperties();
            this.listenToActionChanges();
         }
      }

      @Override
      public void viewWasRemoved(ViewManagerEvent var1) {
         View var2 = var1.getView();
         if (var2.getID().equals(AbstractProxyAction.this.viewId)) {
            this.stopListeningToActionChanges();
            AbstractProxyAction.this.encapsulatedAction = null;
            AbstractProxyAction.this.setEnabled(false);
         }
      }

      private void transferActionProperties() {
         AbstractProxyAction.this.setEnabled(AbstractProxyAction.this.encapsulatedAction.isEnabled());

         for (String var2 : Lists.newArrayList("Default", "ActionCommandKey", "SwingSelectedKey")) {
            AbstractProxyAction.this.putValue(var2, AbstractProxyAction.this.encapsulatedAction.getValue(var2));
         }
      }

      private void listenToActionChanges() {
         if (AbstractProxyAction.this.changeDispatcher == null) {
            AbstractProxyAction.this.changeDispatcher = AbstractProxyAction.this.new ChangeDispatcher();
         }

         AbstractProxyAction.this.encapsulatedAction.addPropertyChangeListener(AbstractProxyAction.this.changeDispatcher);
      }

      private void stopListeningToActionChanges() {
         if (AbstractProxyAction.this.encapsulatedAction != null) {
            AbstractProxyAction.this.encapsulatedAction.removePropertyChangeListener(AbstractProxyAction.this.changeDispatcher);
         }
      }
   }
}
