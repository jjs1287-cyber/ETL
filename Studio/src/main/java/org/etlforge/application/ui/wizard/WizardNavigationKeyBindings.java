package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;

public final class WizardNavigationKeyBindings {
   private final Wizard wizard;
   private final Map<KeyStroke, Action> originalActions;

   private WizardNavigationKeyBindings(Wizard var1, JComponent var2) {
      this.wizard = var1;
      this.originalActions = Maps.newHashMap();
      KeyBindingInstaller var3 = new KeyBindingInstaller(var2);
      this.installMoveNext(var3, var2);
      this.installMovePrevious(var3, var2);
      var3.install();
   }

   public static void install(Wizard var0, JComponent var1) {
      Preconditions.checkNotNull(var0, "wizard");
      Preconditions.checkNotNull(var1, "c");
      new WizardNavigationKeyBindings(var0, var1);
   }

   private void installMoveNext(KeyBindingInstaller var1, JComponent var2) {
      AbstractAction var3 = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            WizardNavigationKeyBindings.this.wizard.nextPageRequested();
         }
      };
      KeyStroke[] var4 = new KeyStroke[]{KeyUtils.getCtrlDown(39)};
      this.bindAction(var3, var4, var1, var2);
   }

   private void installMovePrevious(KeyBindingInstaller var1, JComponent var2) {
      AbstractAction var3 = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            WizardNavigationKeyBindings.this.wizard.previousPageRequested();
         }
      };
      KeyStroke[] var4 = new KeyStroke[]{KeyUtils.getCtrlDown(37)};
      this.bindAction(var3, var4, var1, var2);
   }

   private void bindAction(Action var1, KeyStroke[] var2, KeyBindingInstaller var3, JComponent var4) {
      for (KeyStroke var8 : var2) {
         this.storeOriginalActions(var8, var4);
         String var9 = var8.toString();
         var3.addBinding(var8, var9, new WizardNavigationKeyBindings.NavigationAction(var8, var1));
      }
   }

   private void storeOriginalActions(KeyStroke var1, JComponent var2) {
      InputMap var3 = var2.getInputMap(0);
      Object var4 = var3.get(var1);
      if (var4 != null) {
         this.originalActions.put(var1, var2.getActionMap().get(var4));
      }
   }

   private class NavigationAction extends AbstractAction {
      private final KeyStroke keyStroke;
      private final Action action;

      public NavigationAction(KeyStroke var2, Action var3) {
         this.keyStroke = var2;
         this.action = var3;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Action var2 = WizardNavigationKeyBindings.this.originalActions.get(this.keyStroke);
         if (var2 != null) {
            var2.actionPerformed(var1);
         }

         this.action.actionPerformed(var1);
      }
   }
}
