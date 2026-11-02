package org.etlforge.ui.widget.button;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.lang.Arrays2;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public class StaticOptionButton {
   public static final String CURRENT_ACTION_PROPERTY = "StaticOptionButton#CurrentAction";
   private static final ImageIcon ARROW = (ImageIcon)UiResourceBundle.getOptionButtonArrow16();
   private final Action defaultMainAction;
   private final Action[] optionActions;
   private Action currentMainAction;
   private Action arrowPressedAction;
   private JButton mainActionButton;
   private JButton optionActionsButton;
   private JPopupMenu popupMenu;
   private final ActionListener actionListener = new StaticOptionButton.ActionListenerImpl();
   private final PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);
   private boolean keepDefaultActionFixed;

   public StaticOptionButton(Action var1, Action[] var2, boolean var3) {
      this.defaultMainAction = var1;
      this.optionActions = (Action[])var2.clone();
      this.createButtons(var3);
      this.setMainButtonAction();
      this.defaultMainAction.addPropertyChangeListener(new StaticOptionButton.DefaultActionPropertyChangeListener());
   }

   private void createButtons(final boolean var1) {
      this.mainActionButton = new JButton();
      Fonts.convertToBold(this.mainActionButton);
      this.mainActionButton.addActionListener(this.actionListener);
      this.optionActionsButton = new JButton(ARROW) {
         @Override
         public Dimension getPreferredSize() {
            return var1
               ? new Dimension(super.getPreferredSize().width, StaticOptionButton.this.mainActionButton.getPreferredSize().height)
               : super.getPreferredSize();
         }

         @Override
         public Dimension getMinimumSize() {
            return var1 ? this.getPreferredSize() : super.getMinimumSize();
         }

         @Override
         public Dimension getMaximumSize() {
            return var1 ? this.getPreferredSize() : super.getMaximumSize();
         }
      };
      this.optionActionsButton.addActionListener(this.actionListener);
      this.arrowPressedAction = new StaticOptionButton.ArrowPressedAction();
      StaticOptionButton.ButtonChangeListener var2 = new StaticOptionButton.ButtonChangeListener();
      this.mainActionButton.addChangeListener(var2);
      this.optionActionsButton.addChangeListener(var2);
   }

   public void setMainButtonIcon(Icon var1) {
      this.mainActionButton.setIcon(var1);
   }

   public void setMainButtonText(String var1) {
      this.mainActionButton.setText(var1);
   }

   public void addToToolBar(JToolBar var1) {
      var1.add(this.mainActionButton);
      var1.add(this.optionActionsButton);
   }

   public JButton getMainActionButton() {
      return this.mainActionButton;
   }

   public Action getMainAction() {
      return this.currentMainAction;
   }

   public JButton getArrowButton() {
      return this.optionActionsButton;
   }

   public Action getArrowAction() {
      return this.arrowPressedAction;
   }

   public void setKeepDefaultActionFixed(boolean var1) {
      this.keepDefaultActionFixed = var1;
   }

   protected JPopupMenu createPopupMenu() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      var1.addActions(this.optionActions);
      this.registerMenuItemListener(var1);
      return var1;
   }

   protected void registerMenuItemListener(JPopupMenu var1) {
      if (var1 != null) {
         ActionListener var2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               JMenuItem var2x = (JMenuItem)var1.getSource();
               Action var3 = var2x.getAction();
               if (var3 != StaticOptionButton.this.currentMainAction && !StaticOptionButton.this.keepDefaultActionFixed) {
                  Action var4 = StaticOptionButton.this.currentMainAction;
                  StaticOptionButton.this.currentMainAction = var3;
                  StaticOptionButton.this.firePropertyChange("StaticOptionButton#CurrentAction", var4, var3);
               }
            }
         };

         for (Component var6 : var1.getComponents()) {
            if (var6 instanceof JMenuItem) {
               ((JMenuItem)var6).addActionListener(var2);
            }
         }
      }
   }

   protected void setMainButtonAction() {
      Action var1 = null;
      if (this.defaultMainAction.isEnabled()) {
         var1 = this.defaultMainAction;
      } else {
         var1 = Arrays2.first(this.optionActions, ActionUtils.ENABLED);
      }

      if (var1 != this.currentMainAction) {
         this.mainActionButton.removeActionListener(this.currentMainAction);
         String var2 = var1 != null ? (String)var1.getValue("ShortDescription") : null;
         this.mainActionButton.setToolTipText(var2);
         Action var3 = this.currentMainAction;
         this.currentMainAction = var1;
         this.firePropertyChange("StaticOptionButton#CurrentAction", var3, var1);
      }
   }

   public final void addPropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.addPropertyChangeListener(var1);
   }

   public final void removePropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.removePropertyChangeListener(var1);
   }

   public final void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.addPropertyChangeListener(var1, var2);
   }

   public final void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.removePropertyChangeListener(var1, var2);
   }

   protected final void firePropertyChange(String var1, Object var2, Object var3) {
      this.propChangeSupport.firePropertyChange(var1, var2, var3);
   }

   public final void setEnabled(boolean var1) {
      this.mainActionButton.setEnabled(var1);
      this.optionActionsButton.setEnabled(var1);
   }

   static {
      Image var0 = ARROW.getImage();
      int var1 = var0.getHeight(null);
      var0 = var0.getScaledInstance(8, var1, 4);
      ARROW.setImage(var0);
   }

   private class ActionListenerImpl implements ActionListener {
      private ActionListenerImpl() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (var1.getSource() == StaticOptionButton.this.mainActionButton) {
            if (StaticOptionButton.this.currentMainAction != null) {
               StaticOptionButton.this.currentMainAction.actionPerformed(var1);
            }
         } else if (var1.getSource() == StaticOptionButton.this.optionActionsButton) {
            StaticOptionButton.this.arrowPressedAction.actionPerformed(var1);
         }
      }
   }

   private class ArrowPressedAction extends AbstractAction {
      private ArrowPressedAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (StaticOptionButton.this.popupMenu == null) {
            StaticOptionButton.this.popupMenu = StaticOptionButton.this.createPopupMenu();
         }

         StaticOptionButton.this.popupMenu
            .show(
               StaticOptionButton.this.optionActionsButton,
               StaticOptionButton.this.optionActionsButton.getWidth() / 2,
               StaticOptionButton.this.optionActionsButton.getHeight() / 2
            );
      }
   }

   private class ButtonChangeListener implements ChangeListener {
      private ButtonChangeListener() {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         if (var1.getSource() == StaticOptionButton.this.mainActionButton) {
            StaticOptionButton.this.optionActionsButton.getModel().setRollover(StaticOptionButton.this.mainActionButton.getModel().isRollover());
         } else if (var1.getSource() == StaticOptionButton.this.optionActionsButton) {
            StaticOptionButton.this.mainActionButton.getModel().setRollover(StaticOptionButton.this.optionActionsButton.getModel().isRollover());
         }
      }
   }

   private class DefaultActionPropertyChangeListener implements PropertyChangeListener {
      private DefaultActionPropertyChangeListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if ("enabled".equals(var1.getPropertyName())) {
            StaticOptionButton.this.setMainButtonAction();
         }
      }
   }
}
