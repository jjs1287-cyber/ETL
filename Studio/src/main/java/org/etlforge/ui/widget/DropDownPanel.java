package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.event.ChangeListeners;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.Focusable;
import org.etlforge.ui.util.focus.FocusableComponent;
import org.etlforge.ui.util.focus.MuteFocusable;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.panel.KongaPanel;
import org.etlforge.ui.widget.panel.KongaPanelAlphaAnimator;
import org.etlforge.util.preference.KongaPreferences;
import org.etlforge.util.preference.PreferencesKey;

public class DropDownPanel {
   private final DropDownPanel.Displayer displayer;
   private final JComponent component;
   private Focusable focusWhenExpanded;
   private final ChangeListeners changeListeners;
   private KongaPreferences preferenceStore;
   private PreferencesKey preferenceKey;
   private boolean animated;
   private boolean repackDialogWhenExpansionChanges;

   public DropDownPanel(JComponent var1, String var2, boolean var3) {
      this(var1, var2, DropDownPanel.ButtonOrientation.TRAILING, var3);
   }

   public DropDownPanel(JComponent var1, String var2, DropDownPanel.ButtonOrientation var3, boolean var4) {
      this.component = Preconditions.checkNotNull(var1);
      this.displayer = new DropDownPanel.Displayer(var2, var3, var4);
      this.changeListeners = new ChangeListeners();
      this.addChangeListener(new DropDownPanel.RepackListener());
   }

   public JComponent getDisplayer() {
      return this.displayer;
   }

   public void setTitleIcon(Icon var1) {
      this.displayer.titlePanel.titleLabel.setIcon(var1);
   }

   public void setTitleFont(Font var1) {
      this.displayer.titlePanel.titleLabel.setFont(var1);
   }

   public void setTitleColor(Color var1) {
      this.displayer.titlePanel.titleLabel.setForeground(var1);
   }

   public void setTitleToolTip(String var1) {
      this.displayer.titlePanel.titleLabel.setToolTipText(var1);
   }

   public void decorateTitle(TextStyle var1) {
      this.setTitleFont(var1.getFont());
      this.setTitleColor(var1.getColor());
   }

   public boolean isShowing() {
      return this.displayer.isShowing;
   }

   public void setOpaque(boolean var1) {
      this.displayer.setOpaque(var1);
      this.displayer.titlePanel.setOpaque(var1);
   }

   public void setAnimated(boolean var1) {
      this.animated = var1;
   }

   public void addChangeListener(ChangeListener var1) {
      this.changeListeners.add(var1);
   }

   public void removeChangeListener(ChangeListener var1) {
      this.changeListeners.remove(var1);
   }

   private void fireChange() {
      this.notifyListeners();
      this.storeStateInPreferenceStore();
   }

   private void notifyListeners() {
      this.changeListeners.call().stateChanged(new ChangeEvent(this));
   }

   private void storeStateInPreferenceStore() {
      if (this.preferenceStore != null) {
         this.preferenceStore.putBoolean(this.preferenceKey.keyName(), this.isShowing());
      }
   }

   public void setFocusWhenExpanded(Component var1) {
      this.focusWhenExpanded = new FocusableComponent(var1);
   }

   public void setFocusWhenExpanded(UiProvider var1) {
      this.focusWhenExpanded = MuteFocusable.fromUiProvider(var1);
   }

   public void expand() {
      this.displayer.titlePanel.onShow();
   }

   public void collapse() {
      this.displayer.titlePanel.onHide();
   }

   public void setRepackDialogWhenExpansionStateChanges(boolean var1) {
      this.repackDialogWhenExpansionChanges = var1;
   }

   public void setPreferenceStore(PreferencesKey var1) {
      this.preferenceKey = var1;
      if (var1 != null) {
         this.preferenceStore = KongaPreferences.getPreferences(var1);
         this.updateStateFromPreferenceStore();
      } else {
         this.preferenceStore = null;
      }
   }

   public void setEnabled(boolean var1) {
      if (!var1) {
         this.collapse();
      }

      this.displayer.titlePanel.setEnabled(var1);
   }

   public void setCollapsedIcon(Icon var1) {
      this.displayer.titlePanel.setCollapsedIcon(var1);
   }

   public void setExpandedIcon(Icon var1) {
      this.displayer.titlePanel.setExpandedIcon(var1);
   }

   private void updateStateFromPreferenceStore() {
      boolean var1 = this.preferenceStore.getBoolean(this.preferenceKey.keyName(), this.isShowing());
      if (var1) {
         this.expand();
      } else {
         this.collapse();
      }
   }

   private static TextStyle getTitleStyle() {
      Object var0 = UIManager.get("konga.dropdownpanel.title-style");
      return var0 instanceof TextStyle ? (TextStyle)var0 : new TextStyle(new JLabel());
   }

   public enum ButtonOrientation {
      LEADING,
      TRAILING;
   }

   private class Displayer extends JPanel {
      private final DropDownPanel.Displayer.TitlePanel titlePanel;
      private final KongaPanel contentPanel;
      private boolean isShowing;
      private KongaPanelAlphaAnimator animator;

      public Displayer(String var2, DropDownPanel.ButtonOrientation var3, boolean var4) {
         this.titlePanel = new DropDownPanel.Displayer.TitlePanel(var2, var3, var4);
         this.contentPanel = new KongaPanel(new BorderLayout());
         this.contentPanel.add(DropDownPanel.this.component, "Center");
         this.layoutUi(var4);
         this.isShowing = var4;
      }

      private void layoutUi(boolean var1) {
         BoxBuilder var2 = BoxBuilder.vertical(this);
         var2.add(this.titlePanel).strut(5);
         if (var1) {
            var2.add(this.contentPanel);
         }
      }

      public void showContents() {
         if (DropDownPanel.this.animated) {
            this.startAnimation(true);
         }

         this.isShowing = true;
         this.add(this.contentPanel);
         this.revalidate();
         if (DropDownPanel.this.focusWhenExpanded != null) {
            DropDownPanel.this.focusWhenExpanded.requestFocus();
         }
      }

      public void hideContents() {
         this.isShowing = false;
         if (DropDownPanel.this.animated) {
            this.startAnimation(false);
         } else {
            this.removeContent();
         }
      }

      private void startAnimation(boolean var1) {
         if (this.animator != null) {
            this.animator.stop();
         }

         if (var1) {
            this.animator = new KongaPanelAlphaAnimator(this.contentPanel);
            this.animator.show();
         } else {
            this.animator = new KongaPanelAlphaAnimator(this.contentPanel, new TimingTargetAdapter() {
               @Override
               public void end() {
                  Displayer.this.removeContent();
               }
            });
            this.animator.hide();
         }
      }

      private void removeContent() {
         this.remove(this.contentPanel);
         this.revalidate();
         this.repaint();
      }

      @Override
      public Dimension getMaximumSize() {
         return this.getPreferredSize();
      }

      @Override
      public void setOpaque(boolean var1) {
         super.setOpaque(var1);
         if (this.contentPanel != null) {
            UiUtils.setOpaqueRecursivelySkipDefaults(this.contentPanel, var1);
         }
      }

      private class TitlePanel extends JPanel {
         private final JButton titleLabel;
         private final JButton button;
         private final Action showAction = new DropDownPanel.Displayer.TitlePanel.Show();
         private final Action hideAction = new DropDownPanel.Displayer.TitlePanel.Hide();

         public TitlePanel(String var2, DropDownPanel.ButtonOrientation var3, boolean var4) {
            this.titleLabel = this.createTitleLabel(var2);
            DropDownPanel.getTitleStyle().makeOver(this.titleLabel);
            Action var5 = var4 ? this.hideAction : this.showAction;
            this.button = new KongaButton(var5);
            this.titleLabel.addActionListener(var5);
            ButtonUtils.configureButtonForToolBar(this.button);
            BoxBuilder var6 = BoxBuilder.horizontal(this);
            if (var3 == DropDownPanel.ButtonOrientation.LEADING) {
               var6.addAll(this.button, this.titleLabel);
            } else {
               var6.addAll(this.titleLabel, this.button);
            }

            var6.glue();
         }

         private KongaButton createTitleLabel(String var1) {
            KongaButton var2 = new KongaButton(var1);
            var2.setOpaque(false);
            var2.setContentAreaFilled(false);
            var2.setFocusPainted(false);
            ButtonUtils.enableRolloverForButton(var2);
            return var2;
         }

         public void onShow() {
            if (!Displayer.this.isShowing) {
               this.button.setAction(this.hideAction);
               this.titleLabel.removeActionListener(this.showAction);
               this.titleLabel.addActionListener(this.hideAction);
               Displayer.this.showContents();
               DropDownPanel.this.fireChange();
            }
         }

         public void onHide() {
            if (Displayer.this.isShowing) {
               this.button.setAction(this.showAction);
               this.titleLabel.removeActionListener(this.hideAction);
               this.titleLabel.addActionListener(this.showAction);
               Displayer.this.hideContents();
               DropDownPanel.this.fireChange();
            }
         }

         public void setCollapsedIcon(Icon var1) {
            this.showAction.putValue("SmallIcon", var1);
         }

         public void setExpandedIcon(Icon var1) {
            this.hideAction.putValue("SmallIcon", var1);
         }

         @Override
         public void setEnabled(boolean var1) {
            super.setEnabled(var1);
            this.titleLabel.setEnabled(var1);
            this.button.setEnabled(var1);
            this.showAction.setEnabled(var1);
            this.hideAction.setEnabled(var1);
         }

         private class Hide extends AbstractAction {
            public Hide() {
               super("", UiResourceBundle.getIcon("DropDownExpanded.16"));
            }

            @Override
            public void actionPerformed(ActionEvent var1) {
               TitlePanel.this.onHide();
            }
         }

         private class Show extends AbstractAction {
            public Show() {
               super("", UiResourceBundle.getIcon("DropDownCollapsed.16"));
            }

            @Override
            public void actionPerformed(ActionEvent var1) {
               TitlePanel.this.onShow();
            }
         }
      }
   }

   private class RepackListener implements ChangeListener {
      private RepackListener() {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         this.checkForLayeredPaneAncestor();
         if (DropDownPanel.this.repackDialogWhenExpansionChanges) {
            Window var2 = SwingUtilities.getWindowAncestor(DropDownPanel.this.displayer);
            if (var2 instanceof JDialog) {
               var2.pack();
            }
         }
      }

      private void checkForLayeredPaneAncestor() {
         KongaLayeredPane var1 = (KongaLayeredPane)SwingUtilities.getAncestorOfClass(KongaLayeredPane.class, DropDownPanel.this.displayer);
         if (var1 != null) {
            var1.baseComponentsPreferredSizeMayHaveChanged();
         }
      }
   }
}
