package org.etlforge.ui.widget.split;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.SplitPaneCanvas;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.util.preference.KongaPreferences;
import org.etlforge.util.preference.PreferencesKey;

public class KongaSplitPane extends JSplitPane {
   private static final String MINIMIZED_KEY = "minimized";
   private static final String MAXIMIZED_KEY = "maximized";
   private static final String LOCATION_KEY = "location";
   private boolean minimized;
   private boolean maximized;
   private KongaPreferences prefs;
   private KongaSplitPane.Listener prefListener;
   private boolean animationEnabled;
   private boolean isProcessingOneTouch;
   private boolean animating;
   private Dimension leftPrefSize;
   private Dimension rightPrefSize;
   private Dimension leftMinSize;
   private Dimension rightMinSize;

   public static KongaSplitPane verticalSplit(UiProvider var0, UiProvider var1) {
      KongaSplitPane var2 = new KongaSplitPane(0);
      var2.split(var0, var1);
      return var2;
   }

   public static KongaSplitPane verticalSplit(JComponent var0, JComponent var1) {
      KongaSplitPane var2 = new KongaSplitPane(0);
      var2.split(var0, var1);
      return var2;
   }

   public static KongaSplitPane horizontalSplit(UiProvider var0, UiProvider var1) {
      KongaSplitPane var2 = new KongaSplitPane(1);
      var2.split(var0, var1);
      return var2;
   }

   public static KongaSplitPane horizontalSplit(JComponent var0, JComponent var1) {
      KongaSplitPane var2 = new KongaSplitPane(1);
      var2.split(var0, var1);
      return var2;
   }

   public KongaSplitPane() {
      this.init();
   }

   public KongaSplitPane(int var1, boolean var2, Component var3, Component var4) {
      super(var1, var2, var3, var4);
      this.init();
   }

   public KongaSplitPane(int var1, boolean var2, UiProvider var3, UiProvider var4) {
      super(var1, var2);
      this.setLeftComponent(var3);
      this.setRightComponent(var4);
      this.init();
   }

   public KongaSplitPane(int var1, boolean var2) {
      super(var1, var2);
      this.init();
   }

   public KongaSplitPane(int var1, Component var2, Component var3) {
      super(var1, var2, var3);
      this.init();
   }

   public KongaSplitPane(int var1, UiProvider var2, UiProvider var3) {
      super(var1);
      this.setLeftComponent(var2);
      this.setRightComponent(var3);
      this.init();
   }

   public KongaSplitPane(int var1) {
      super(var1);
      this.init();
   }

   private void init() {
      this.setupOneTouchAnimation();
      this.removeUnwantedShortcuts();
   }

   public void setTopComponent(UiProvider var1) {
      var1.decorate(SplitPaneCanvas.top(this));
   }

   public void setBottomComponent(UiProvider var1) {
      var1.decorate(SplitPaneCanvas.bottom(this));
   }

   public void setLeftComponent(UiProvider var1) {
      var1.decorate(SplitPaneCanvas.left(this));
   }

   @Override
   public void setLeftComponent(Component var1) {
      if (this.leftMinSize != null) {
         var1.setMinimumSize(this.leftMinSize);
      }

      if (this.leftPrefSize != null) {
         var1.setPreferredSize(this.leftPrefSize);
      }

      super.setLeftComponent(var1);
   }

   public void setRightComponent(UiProvider var1) {
      var1.decorate(SplitPaneCanvas.right(this));
   }

   @Override
   public void setRightComponent(Component var1) {
      if (this.rightMinSize != null) {
         var1.setMinimumSize(this.rightMinSize);
      }

      if (this.rightPrefSize != null) {
         var1.setPreferredSize(this.rightPrefSize);
      }

      super.setRightComponent(var1);
   }

   public void split(Component var1, Component var2) {
      this.setLeftComponent(var1);
      this.setRightComponent(var2);
   }

   public void split(UiProvider var1, UiProvider var2) {
      this.setLeftComponent(var1);
      this.setRightComponent(var2);
   }

   public void setTopPreferredSize(Dimension var1) {
      this.setLeftPreferredSize(var1);
   }

   public void setLeftPreferredSize(Dimension var1) {
      this.leftPrefSize = defensiveCopy(var1);
   }

   private static Dimension defensiveCopy(Dimension var0) {
      return var0 == null ? null : new Dimension(var0);
   }

   public void setBottomPreferredSize(Dimension var1) {
      this.setRightPreferredSize(var1);
   }

   public void setRightPreferredSize(Dimension var1) {
      this.rightPrefSize = defensiveCopy(var1);
   }

   public void setTopMinimumSize(Dimension var1) {
      this.setLeftMinimumSize(var1);
   }

   public void setLeftMinimumSize(Dimension var1) {
      this.leftMinSize = defensiveCopy(var1);
   }

   public void setBottomMinimumSize(Dimension var1) {
      this.setRightMinimumSize(var1);
   }

   public void setRightMinimumSize(Dimension var1) {
      this.rightMinSize = defensiveCopy(var1);
   }

   public void setPreferenceStore(PreferencesKey var1) {
      this.setPreferenceStore(var1, 0.5);
   }

   public void setPreferenceStore(PreferencesKey var1, double var2) {
      if (var1 != null) {
         this.prefs = KongaPreferences.getPreferences(var1);
         if (this.prefListener == null) {
            this.prefListener = new KongaSplitPane.Listener();
            this.addPropertyChangeListener("dividerLocation", this.prefListener);
         }

         this.updateStateFromPrefs(var2);
      } else {
         this.prefs = null;
         if (this.prefListener != null) {
            this.removePropertyChangeListener("dividerLocation", this.prefListener);
            this.prefListener = null;
         }
      }
   }

   protected final void updateStateFromPrefs(double var1) {
      if (this.prefs != null) {
         this.minimized = this.prefs.getBoolean("minimized", false);
         this.maximized = this.prefs.getBoolean("maximized", false);
         if (this.minimized) {
            this.setMinimizedState();
         } else if (this.maximized) {
            this.setMaximizedState();
         } else {
            double var3 = this.prefs.getDouble("location", var1);
            if (var3 >= 0.0) {
               int var5 = this.getLocationRatioBaseLine();
               if (var5 > 1) {
                  this.setDividerLocation(var3);
               } else {
                  this.tryToSetPropertionalDividerLocation(var3, 1);
               }
            }
         }
      }
   }

   private void setMinimizedState() {
      this.setDividerLocation(0);
      if (this.ui instanceof BasicSplitPaneUI) {
         this.doSplitPaneUiHack((BasicSplitPaneUI)this.ui);
      }
   }

   private void setMaximizedState() {
      this.setDividerLocation(Integer.MAX_VALUE);
      if (this.ui instanceof BasicSplitPaneUI) {
         this.doSplitPaneUiHack((BasicSplitPaneUI)this.ui);
      }
   }

   private void doSplitPaneUiHack(BasicSplitPaneUI var1) {
      try {
         Method var2 = BasicSplitPaneUI.class.getDeclaredMethod("setKeepHidden", boolean.class);
         var2.setAccessible(true);
         var2.invoke(var1, Boolean.TRUE);
      } catch (Exception var3) {
      }
   }

   @Override
   public void setDividerLocation(int var1) {
      if (this.isAnimationEnabled() && this.isProcessingOneTouch && !this.animating) {
         this.startAnimation(var1);
      } else {
         super.setDividerLocation(var1);
      }
   }

   public void setAnimationEnabled(boolean var1) {
      this.animationEnabled = var1;
   }

   public boolean isAnimationEnabled() {
      return this.animationEnabled;
   }

   public void setProportionalDividerLocation(double var1) {
      this.tryToSetPropertionalDividerLocation(var1, 1);
   }

   public void tryToSetPropertionalDividerLocation(final double var1, int var3) {
      int var4 = this.getLocationRatioBaseLine();
      if (var4 > 1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               KongaSplitPane.this.setDividerLocation(var1);
            }
         });
      } else if (var3 < 10) {
         final int var5 = var3 + 1;
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               KongaSplitPane.this.tryToSetPropertionalDividerLocation(var1, var5);
            }
         });
      }
   }

   public double getProportionalDividerLocation() {
      int var1 = this.getDividerLocation();
      int var2 = this.getLocationRatioBaseLine();
      double var3 = 1.0 * var1 / var2;
      return Math.min(var3, 1.0);
   }

   private int getLocationRatioBaseLine() {
      int var1;
      if (this.getOrientation() == 0) {
         var1 = this.getHeight() - this.getDividerSize();
      } else {
         var1 = this.getWidth() - this.getDividerSize();
      }

      return Math.max(var1, 1);
   }

   protected final void setupOneTouchAnimation() {
      BasicSplitPaneDivider var1 = ((BasicSplitPaneUI)this.getUI()).getDivider();

      for (Component var5 : var1.getComponents()) {
         if (var5 instanceof JButton) {
            this.configureOneTouchButton((JButton)var5);
         }
      }

      var1.addContainerListener(new ContainerAdapter() {
         @Override
         public void componentAdded(ContainerEvent var1) {
            Component var2 = var1.getChild();
            if (var2 instanceof JButton) {
               KongaSplitPane.this.configureOneTouchButton((JButton)var2);
            }
         }
      });
   }

   private void configureOneTouchButton(JButton var1) {
      final ActionListener[] var2 = var1.getActionListeners();

      for (ActionListener var6 : var2) {
         var1.removeActionListener(var6);
      }

      ActionListener var7 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            KongaSplitPane.this.isProcessingOneTouch = true;

            for (ActionListener var5 : var2) {
               var5.actionPerformed(var1);
            }
         }
      };
      var1.addActionListener(var7);
   }

   private void startAnimation(int var1) {
      this.animating = true;
      final int var2 = this.getDividerLocation();
      Animator var3 = new Animator(250);
      var3.setResolution(15);
      var3.setAcceleration(0.3F);
      var3.setDeceleration(0.2F);
      var3.addTarget(new PropertySetter(this, "dividerLocation", var1));
      var3.addTarget(new TimingTargetAdapter() {
         @Override
         public void end() {
            KongaSplitPane.this.animating = false;
            KongaSplitPane.this.isProcessingOneTouch = false;
            KongaSplitPane.this.setLastDividerLocation(var2);
         }
      });
      var3.start();
   }

   private void removeUnwantedShortcuts() {
      InputMap var1 = this.getInputMap(1);
      KeyStroke[] var2 = new KeyStroke[]{KeyUtils.F6, KeyUtils.F8};

      for (KeyStroke var6 : var2) {
         var1.put(var6, null);
      }
   }

   @Override
   protected boolean processKeyBinding(KeyStroke var1, KeyEvent var2, int var3, boolean var4) {
      return !var1.equals(KeyUtils.F6) && !var1.equals(KeyUtils.F8) ? super.processKeyBinding(var1, var2, var3, var4) : false;
   }

   protected void storeCurrentStateInPreferences() {
      if (this.prefs != null) {
         this.prefs.putBoolean("minimized", this.minimized);
         this.prefs.putBoolean("maximized", this.maximized);
         this.prefs.putDouble("location", this.getProportionalDividerLocation());
      }
   }

   protected final void setPreferencesListenerBlocked(boolean var1) {
      if (this.prefListener != null) {
         this.prefListener.blocked = var1;
      }
   }

   public KongaSplitPane withDividerSize(int var1) {
      this.setDividerSize(var1);
      return this;
   }

   public KongaSplitPane withResizeWeight(double var1) {
      this.setResizeWeight(var1);
      return this;
   }

   private class Listener implements PropertyChangeListener {
      public boolean blocked;

      private Listener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (!this.blocked) {
            int var2 = KongaSplitPane.this.getDividerLocation();
            if (var2 < KongaSplitPane.this.getMinimumDividerLocation()) {
               KongaSplitPane.this.minimized = true;
               KongaSplitPane.this.maximized = false;
            } else if (var2 > KongaSplitPane.this.getMaximumDividerLocation()) {
               KongaSplitPane.this.minimized = false;
               KongaSplitPane.this.maximized = true;
            } else {
               KongaSplitPane.this.minimized = false;
               KongaSplitPane.this.maximized = false;
            }

            KongaSplitPane.this.storeCurrentStateInPreferences();
         }
      }
   }
}
