package org.etlforge.ui.widget.button;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.border.Border;
import org.etlforge.system.callback.Acceptor;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.AbstractActionPropertyChangeListener;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.ComponentTraverser;
import org.etlforge.ui.widget.label.KongaLabel;

public class BigButton implements UiProvider {
   private final JComponent ui;
   private Color unselectedBackground;
   private final Border unselectedBorder;
   private final Border selectedBorder;
   private final Color selectedBackground;
   private final Border armedBorder;
   private final Color armedBackground;
   private final Border disabledBorder;
   private final Color disabledBackground;
   private boolean selected;
   private boolean armed;
   private boolean enabled;
   private final List<ActionListener> actionListeners;

   public static BigButton fromAction(Action var0) {
      return fromAction(var0, null);
   }

   public static BigButton fromAction(Action var0, TextStyle var1) {
      Icon var2 = (Icon)var0.getValue("SwingLargeIconKey");
      if (var2 == null) {
         var2 = (Icon)var0.getValue("SmallIcon");
      }

      KongaLabel var3 = KongaLabel.withMnemonic((String)var0.getValue("Name"));
      if (var1 != null) {
         var1.makeOver(var3);
      }

      var3.setIcon(var2);
      var3.setToolTipText((String)var0.getValue("ShortDescription"));
      var3.setBackground(javax.swing.UIManager.getColor("Panel.background"));
      final BigButton var4 = new BigButton(var3);
      var4.addActionListener(var0);
      var0.addPropertyChangeListener(new AbstractActionPropertyChangeListener(var4, var0) {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            if ("enabled".equals(var1.getPropertyName())) {
               var4.setEnabled(this.getActionListenedTo().isEnabled());
            }
         }
      });
      return var4;
   }

   public BigButton(JComponent var1) {
      this(new BorderLayoutBuilder().north(var1).withBackground(var1.getBackground()).container(), true);
   }

   public BigButton(UiProvider var1) {
      this(new BorderLayoutBuilder().north(var1).withBackground(javax.swing.UIManager.getColor("Panel.background")).container(), true);
   }

   private BigButton(JComponent var1, boolean var2) {
      this.ui = var1;
      this.unselectedBackground = var1.getBackground();
      this.unselectedBorder = Empty.border(6);
      this.selectedBorder = createBorder(new Color(185, 211, 238));
      this.selectedBackground = new Color(209, 220, 233);
      this.armedBorder = createBorder(new Color(100, 149, 237));
      this.armedBackground = new Color(192, 212, 242);
      this.disabledBorder = createBorder(Color.LIGHT_GRAY);
      this.disabledBackground = new Color(15790320);
      this.actionListeners = new CopyOnWriteArrayList<>();
      this.enabled = true;
      this.installController(var1);
      this.installFocusListener(var1);
      this.installKeyBindings(var1);
      UiUtils.setOpaqueRecursively(var1, false);
      var1.setOpaque(true);
      this.decorate();
   }

   public JComponent getUi() {
      return this.ui;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocusInWindow();
   }

   public BigButton setUnselectedBackground(Color var1) {
      this.unselectedBackground = var1;
      this.setSelected(this.selected);
      return this;
   }

   public BigButton setSelected(boolean var1) {
      if (var1 != this.selected) {
         this.selected = var1;
         this.decorate();
      }

      return this;
   }

   public BigButton setArmed(boolean var1) {
      if (var1 != this.armed) {
         this.armed = var1;
         this.decorate();
      }

      return this;
   }

   public BigButton setEnabled(boolean var1) {
      if (var1 != this.enabled) {
         UiUtils.setEnabledRecursively(this.ui, var1);
         this.enabled = var1;
         this.decorate();
      }

      return this;
   }

   private void decorate() {
      Border var1 = this.unselectedBorder;
      Color var2 = this.unselectedBackground;
      if (this.enabled) {
         if (this.armed) {
            var1 = this.armedBorder;
            var2 = this.armedBackground;
         } else if (this.selected) {
            var1 = this.selectedBorder;
            var2 = this.selectedBackground;
         }
      } else if (this.selected) {
         var1 = this.disabledBorder;
         var2 = this.disabledBackground;
      }

      this.ui.setBorder(var1);
      this.ui.setBackground(var2);
      Container var3 = this.ui.getParent();

      while (var3 != null && !var3.isOpaque()) {
         var3 = var3.getParent();
      }

      if (var3 != null) {
         UiUtils.revalidate(var3);
      }
   }

   public BigButton addActionListener(ActionListener var1) {
      this.actionListeners.add(var1);
      return this;
   }

   public BigButton removeActionListener(ActionListener var1) {
      this.actionListeners.remove(var1);
      return this;
   }

   private void installFocusListener(JComponent var1) {
      var1.addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent var1) {
            BigButton.this.setSelected(true);
         }

         @Override
         public void focusLost(FocusEvent var1) {
            BigButton.this.setSelected(false);
         }
      });
   }

   private void installKeyBindings(JComponent var1) {
      BigButton.ClickAction var2 = new BigButton.ClickAction();
      KeyBindingInstaller var3 = new KeyBindingInstaller(var1, 1);
      var3.addBinding(KeyUtils.SPACE, "space", var2);
      var3.addBinding(KeyUtils.ENTER, "enter", var2);
      var3.install();
   }

   private void installController(JComponent var1) {
      final BigButton.Controller var2 = new BigButton.Controller();
      ComponentTraverser.traverse(var1, new Acceptor<Component>() {
         public boolean accept(Component var1) {
            var1.addMouseListener(var2);
            var1.addMouseMotionListener(var2);
            return true;
         }
      });
   }

   private void doClick() {
      assert this.enabled;
      this.setArmed(false);
      ActionEvent var1 = new ActionEvent(this, 1001, "");

      for (ActionListener var3 : this.actionListeners) {
         var3.actionPerformed(var1);
      }
   }

   private static Border createBorder(Color var0) {
      return new BorderBuilder().rounded(8, 8, var0).empty(5);
   }

   private class ClickAction extends AbstractAction {
      private ClickAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.enabled) {
            BigButton.this.setArmed(true);
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  BigButton.this.doClick();
               }
            });
         }
      }
   }

   private class Controller extends MouseAdapter {
      private Controller() {
      }

      @Override
      public void mouseEntered(MouseEvent var1) {
         BigButton.this.setSelected(true);
      }

      @Override
      public void mouseExited(MouseEvent var1) {
         BigButton.this.setArmed(false);
         BigButton.this.setSelected(false);
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         if (BigButton.this.armed && BigButton.this.enabled) {
            BigButton.this.doClick();
         }
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         BigButton.this.setArmed(true);
      }
   }
}

