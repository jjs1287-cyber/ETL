package org.etlforge.ui.widget.menu;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.button.ButtonUtils;

public class KongaMenu extends JMenu implements KongaWidget {
   public KongaMenu() {
   }

   public KongaMenu(Action var1) {
      super(var1);
   }

   public KongaMenu(String var1, boolean var2) {
      super(var1, var2);
   }

   public KongaMenu(String var1) {
      super(var1);
   }

   public KongaMenu(String var1, Icon var2) {
      this(var1);
      this.setIcon(var2);
   }

   public KongaMenuItem add(String var1) {
      KongaMenuItem var2 = new KongaMenuItem(var1);
      this.add(var2);
      return var2;
   }

   @Override
   public JMenuItem add(Action var1) {
      JMenuItem var2 = this.createActionComponent(var1);
      var2.setAction(var1);
      this.add(var2);
      return var2;
   }

   public KongaMenu addActions(Iterable<Action> var1) {
      for (Action var3 : var1) {
         if (var3 == null) {
            this.addSeparator();
         } else {
            this.add(var3);
         }
      }

      return this;
   }

   public KongaMenu addActions(Action... var1) {
      return this.addActions(Arrays.asList(var1));
   }

   public KongaMenu addItems(Object... var1) {
      for (Object var5 : var1) {
         if (var5 == null) {
            this.addSeparator();
         } else if (var5 instanceof Action) {
            this.add((Action)var5);
         } else if (var5 instanceof JMenuItem) {
            this.add((JMenuItem)var5);
         } else if (var5 instanceof String) {
            this.add((String)var5);
         } else {
            if (!(var5 instanceof Component)) {
               throw new IllegalArgumentException("Cannot add an item of class " + var5.getClass());
            }

            this.add((Component)var5);
         }
      }

      return this;
   }

   public JMenuItem add(Action var1, int var2) {
      JMenuItem var3 = this.createActionComponent(var1);
      var3.setAction(var1);
      this.add(var3, var2);
      return var3;
   }

   @Override
   protected JMenuItem createActionComponent(Action var1) {
      JMenuItem var2 = var1 instanceof ToggleAction ? new ToggleActionCheckBoxMenuItem((ToggleAction)var1) {
         @Override
         protected PropertyChangeListener createActionPropertyChangeListener(Action var1) {
            PropertyChangeListener var2x = KongaMenu.this.createActionChangeListener(this);
            if (var2x == null) {
               var2x = super.createActionPropertyChangeListener(var1);
            }

            return var2x;
         }
      } : new KongaMenuItem() {
         @Override
         protected PropertyChangeListener createActionPropertyChangeListener(Action var1) {
            PropertyChangeListener var2 = KongaMenu.this.createActionChangeListener(this);
            if (var2 == null) {
               var2 = super.createActionPropertyChangeListener(var1);
            }

            return var2;
         }
      };
      var2.setHorizontalTextPosition(11);
      var2.setVerticalTextPosition(0);
      return var2;
   }

   @Override
   public void setText(String var1) {
      MnemonicString var2 = MNEMONIC_FACTORY.fromString(var1 != null ? var1 : "");
      super.setText(var2.getText());
      ButtonUtils.applyMnemonics(this, var2);
   }

   @Override
   public UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
   }

   public void disableIfAllItemsAreDisabled() {
      MenuDisabler.install(this);
   }
}
