package org.etlforge.application.ui.widget;

import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import org.etlforge.ui.widget.split.KongaSplitPane;

public class HideableSplitPane extends KongaSplitPane {
   public static final String HIDDEN = "hidden";
   private boolean dividerHidden;
   private final int sign;
   private int savedDividerSize;

   public HideableSplitPane(int var1, int var2) {
      super(var1);
      this.sign = var2;
      this.setupOneTouchAnimation();
      this.savedDividerSize = this.getDividerSize();
      this.setDividerHidden(true);
      this.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent var1) {
            if (HideableSplitPane.this.dividerHidden) {
               HideableSplitPane.this.setHiddenDividerLocation();
            }
         }
      });
   }

   @Override
   public void setBounds(int var1, int var2, int var3, int var4) {
      super.setBounds(var1, var2, var3, var4);
      if (this.dividerHidden && var3 > 0 && var4 > 0) {
         this.setHiddenDividerLocation();
      }
   }

   @Override
   public void setDividerSize(int var1) {
      if (this.dividerHidden) {
         this.savedDividerSize = var1;
      } else {
         super.setDividerSize(var1);
      }
   }

   public void setDividerHidden(boolean var1) {
      if (var1 != this.dividerHidden) {
         if (var1) {
            this.hideDivider();
         } else {
            this.showDivider();
         }

         this.firePropertyChange("hidden", !var1, var1);
      }
   }

   private void hideDivider() {
      this.setPreferencesListenerBlocked(true);
      this.savedDividerSize = this.getDividerSize();
      this.setDividerSize(0);
      this.dividerHidden = true;
      this.setHiddenDividerLocation();
   }

   private void showDivider() {
      this.setPreferencesListenerBlocked(true);
      this.dividerHidden = false;
      this.setDividerSize(this.savedDividerSize);
      this.updateStateFromPrefs(0.5);
      this.setPreferencesListenerBlocked(false);
   }

   private void setHiddenDividerLocation() {
      assert this.dividerHidden;
      int var1 = this.getHiddenDividerLocation();
      this.setDividerLocation(var1);
   }

   private int getHiddenDividerLocation() {
      if (this.sign == -1) {
         return 0;
      } else {
         return this.getOrientation() == 1 ? this.getWidth() : this.getHeight();
      }
   }

   @Override
   public void setDividerLocation(int var1) {
      if (!this.dividerHidden || var1 == this.getHiddenDividerLocation()) {
         super.setDividerLocation(var1);
      }
   }

   @Override
   public void tryToSetPropertionalDividerLocation(double var1, int var3) {
      if (!this.dividerHidden) {
         super.tryToSetPropertionalDividerLocation(var1, var3);
      }
   }

   @Override
   protected void storeCurrentStateInPreferences() {
      if (!this.dividerHidden) {
         super.storeCurrentStateInPreferences();
      }
   }

   @Override
   public boolean isAnimationEnabled() {
      return AnimationPreference.INSTANCE.get();
   }

   public void ensureBothComponentsVisible() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (this.needsToBeReset()) {
               this.reset();
            }
         }

         private void reset() {
            double var1 = this.getDefaultDividerProportion();
            HideableSplitPane.this.setDividerLocation(var1);
         }

         private boolean needsToBeReset() {
            int var1 = HideableSplitPane.this.getDividerLocation();
            if (HideableSplitPane.this.sign == -1) {
               return var1 < 20;
            }

            int var2 = HideableSplitPane.this.getOrientation() == 1 ? HideableSplitPane.this.getWidth() : HideableSplitPane.this.getHeight();
            return var2 - var1 < 20;
         }

         private double getDefaultDividerProportion() {
            return HideableSplitPane.this.getOrientation() == 1 ? this.getDefaultDividerProportionHorizontal() : this.getDefaultDividerProportionVertical();
         }

         private double getDefaultDividerProportionHorizontal() {
            return HideableSplitPane.this.sign == 1 ? 0.75 : 0.25;
         }

         private double getDefaultDividerProportionVertical() {
            return HideableSplitPane.this.sign == 1 ? 0.6 : 0.4;
         }
      });
   }

   public boolean isDividerHidden() {
      return this.dividerHidden;
   }

   @Override
   public double getResizeWeight() {
      return this.dividerHidden ? 1.0 : super.getResizeWeight();
   }

   int getSign() {
      return this.sign;
   }
}
