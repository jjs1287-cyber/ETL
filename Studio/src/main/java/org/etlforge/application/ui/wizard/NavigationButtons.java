package org.etlforge.application.ui.wizard;

import com.google.common.collect.Iterables;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.StaticFocusTraversalPolicy;
import org.etlforge.ui.widget.ComponentPredicates;
import org.etlforge.ui.widget.button.KongaButton;

final class NavigationButtons implements UiProvider {
   private final Map<Action, JButton> buttonMap = new LinkedHashMap<>();
   private JButton defaultButton;

   public NavigationButtons(Action var1, Action var2, Action var3, Action var4) {
      this(createButton(var1), createButton(var2), createButton(var3), createButton(var4));
   }

   public NavigationButtons(JButton... var1) {
      for (JButton var5 : var1) {
         this.buttonMap.put(var5.getAction(), var5);
      }
   }

   public boolean contains(Component var1) {
      return this.buttonMap.values().contains(var1);
   }

   public JButton getFirstEnabledButton() {
      return Iterables.getFirst(this.enabledButtons(), null);
   }

   private Iterable<JButton> enabledButtons() {
      return Iterables.filter(this.buttonMap.values(), ComponentPredicates.ENABLED);
   }

   public JButton getLastEnabledButton() {
      return Iterables.getLast(this.enabledButtons(), null);
   }

   public void setDefaultAction(Action var1) {
      this.defaultButton = this.buttonMap.get(var1);
   }

   public void setDefaultButton(JButton var1) {
      for (JButton var3 : this.buttonMap.values()) {
         if (var3 == var1) {
            this.defaultButton = var1;
            return;
         }
      }

      throw new IllegalArgumentException("Unknown default button");
   }

   public void addToParent(Container var1, Object var2) {
      JComponent var3 = this.doLayout();
      var1.add(var3, var2);
   }

   private JComponent doLayout() {
      Collection var1 = this.buttonMap.values();
      return Horizontal.grid().of(var1).withHGap(10).nonOpaque().container();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.doLayout());
   }

   @Override
   public void requestFocus() {
      if (this.defaultButton != null) {
         this.defaultButton.requestFocusInWindow();
      }
   }

   public FocusTraversalPolicy getFocusTraversalPolicy() {
      return new StaticFocusTraversalPolicy(this.buttonMap.values());
   }

   public void applyDefaultButtonToRootContainer() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            JRootPane var1 = NavigationButtons.this.getRootPane();
            if (var1 != null) {
               var1.setDefaultButton(NavigationButtons.this.defaultButton);
            }
         }
      });
   }

   public void resetDefaultButtonInRootContainer() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            JRootPane var1 = NavigationButtons.this.getRootPane();
            if (var1 != null) {
               var1.setDefaultButton(null);
            }
         }
      });
   }

   private JRootPane getRootPane() {
      for (JButton var2 : this.buttonMap.values()) {
         Window var3 = SwingUtilities.getWindowAncestor(var2);
         if (var3 instanceof RootPaneContainer) {
            return ((RootPaneContainer)var3).getRootPane();
         }
      }

      return null;
   }

   private static JButton createButton(Action var0) {
      KongaButton var1 = new KongaButton(var0);
      var1.setIcon(null);
      var1.setOpaque(false);
      return var1;
   }
}
