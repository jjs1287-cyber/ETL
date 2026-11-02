package org.etlforge.ui.widget.panel;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class KongaPanelAlphaAnimator {
   private final KongaPanel panel;
   private Animator animator;
   private int duration = 200;
   private float acceleration = 0.2F;
   private float deceleration = 0.4F;
   private final TimingTarget externalTarget;

   public KongaPanelAlphaAnimator(KongaPanel var1) {
      this(var1, null);
   }

   public KongaPanelAlphaAnimator(KongaPanel var1, TimingTarget var2) {
      this.panel = var1;
      this.externalTarget = var2;
   }

   public void setDuration(int var1) {
      this.duration = var1;
   }

   public void setAcceleration(float var1) {
      this.acceleration = var1;
   }

   public void setDeceleration(float var1) {
      this.deceleration = var1;
   }

   public void stop() {
      if (this.animator != null) {
         this.animator.stop();
         this.animator = null;
      }
   }

   public void show() {
      this.startImpl(true);
   }

   public void hide() {
      this.startImpl(false);
   }

   private void startImpl(boolean var1) {
      this.stop();
      this.panel.setAlpha(0.0F);
      this.animator = this.createAnimator(var1);
      this.animator.start();
   }

   private Animator createAnimator(boolean var1) {
      TimingTargetAdapter var2 = var1 ? new KongaPanelAlphaAnimator.ShowAnimation() : new KongaPanelAlphaAnimator.HideAnimation();
      Animator var3 = new Animator(this.duration, var2);
      if (this.externalTarget != null) {
         var3.addTarget(this.externalTarget);
      }

      var3.setAcceleration(this.acceleration);
      var3.setDeceleration(this.deceleration);
      return var3;
   }

   private class HideAnimation extends TimingTargetAdapter {
      private HideAnimation() {
      }

      @Override
      public void begin() {
         KongaPanelAlphaAnimator.this.panel.setAlpha(1.0F);
      }

      @Override
      public void end() {
         KongaPanelAlphaAnimator.this.panel.setAlpha(0.0F);
      }

      @Override
      public void timingEvent(float var1) {
         KongaPanelAlphaAnimator.this.panel.setAlpha(1.0F - var1);
      }
   }

   private class ShowAnimation extends TimingTargetAdapter {
      private ShowAnimation() {
      }

      @Override
      public void end() {
         KongaPanelAlphaAnimator.this.panel.setAlpha(1.0F);
      }

      @Override
      public void timingEvent(float var1) {
         KongaPanelAlphaAnimator.this.panel.setAlpha(var1);
      }
   }
}
