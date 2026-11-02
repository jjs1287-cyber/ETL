package org.etlforge.ui.widget.popup;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.menu.KongaMenuItem;
import org.etlforge.ui.widget.menu.ToggleActionCheckBoxMenuItem;

public class KongaPopupMenu extends JPopupMenu implements KongaWidget {
   public KongaPopupMenu() {
   }

   public KongaPopupMenu(String var1) {
      super(var1);
   }

   public static KongaPopupMenu fromItems(Object... var0) {
      return fromItems(Arrays.asList(var0));
   }

   public static KongaPopupMenu fromItems(Iterable<?> var0) {
      KongaPopupMenu var1 = new KongaPopupMenu();
      var1.addItems(var0);
      return var1;
   }

   @Override
   public JMenuItem add(Action var1) {
      JMenuItem var2 = this.createActionComponent(var1);
      var2.setAction(var1);
      this.add(var2);
      return var2;
   }

   public KongaPopupMenu addActions(Action... var1) {
      return this.addActions(Arrays.asList(var1));
   }

   public KongaPopupMenu addActions(Iterable<? extends Action> var1) {
      for (Action var3 : var1) {
         if (var3 == null) {
            this.addSeparator();
         } else {
            this.add(var3);
         }
      }

      return this;
   }

   public KongaMenuItem add(String var1) {
      KongaMenuItem var2 = new KongaMenuItem(var1);
      this.add(var2);
      return var2;
   }

   public KongaPopupMenu addItems(Object... var1) {
      for (Object var5 : var1) {
         this.addObject(var5);
      }

      return this;
   }

   private void addObject(Object var1) {
      if (var1 == null) {
         this.addSeparator();
      } else if (var1 instanceof Action) {
         this.add((Action)var1);
      } else if (var1 instanceof JMenuItem) {
         this.add((JMenuItem)var1);
      } else if (var1 instanceof String) {
         this.add((String)var1);
      } else {
         if (!(var1 instanceof Component)) {
            throw new IllegalArgumentException("Cannot add an item of class " + var1.getClass());
         }

         this.add((Component)var1);
      }
   }

   public KongaPopupMenu addItems(Iterable<?> var1) {
      for (Object var3 : var1) {
         this.addObject(var3);
      }

      return this;
   }

   @Override
   protected JMenuItem createActionComponent(Action var1) {
      JMenuItem var2 = var1 instanceof ToggleAction ? new ToggleActionCheckBoxMenuItem((ToggleAction)var1) {
         @Override
         protected PropertyChangeListener createActionPropertyChangeListener(Action var1) {
            PropertyChangeListener var2x = KongaPopupMenu.this.createActionChangeListener(this);
            if (var2x == null) {
               var2x = super.createActionPropertyChangeListener(var1);
            }

            return var2x;
         }
      } : new KongaMenuItem() {
         @Override
         protected PropertyChangeListener createActionPropertyChangeListener(Action var1) {
            PropertyChangeListener var2 = KongaPopupMenu.this.createActionChangeListener(this);
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
   public UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
   }

   public void showCentered(Component var1) {
      WidgetUtils.centerContextMenu(this, var1);
   }
}
