package org.jitterbit.integration.client.ui.interchange.component.start;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jitterbit.application.ui.ApplicationUi;
import org.jitterbit.application.ui.look.WizardStyleSheet;
import org.jitterbit.integration.client.ui.icons.ClientIcons;
import org.jitterbit.ui.DelayedJob;
import org.jitterbit.ui.Empty;
import org.jitterbit.ui.dialog.KongaDialog;
import org.jitterbit.ui.layout.BorderLayoutBuilder;
import org.jitterbit.ui.layout.Grid;
import org.jitterbit.ui.util.border.BorderBuilder;
import org.jitterbit.ui.widget.label.KongaLabel;
import org.jitterbit.ui.widget.overlay.KongaLayeredPane;
import org.jitterbit.ui.widget.overlay.OverlayCenterPositioner;
import org.jitterbit.ui.widget.panel.InvisiblePanel;
import org.jitterbit.ui.widget.panel.KongaPanel;

final class Slogan {
   private final KongaPanel dataComesIn;
   private final KongaPanel dataGoesOut;
   private final KongaPanel cantExplainThat;
   private final Slogan.AnimatorImpl[] animators;
   private final Icon icon = ClientIcons.getIcon("Jitterbit.StartScreen");
   private final KongaPanel logoPanel;

   public Slogan() {
      this.animators = new Slogan.AnimatorImpl[4];
      this.dataComesIn = this.createLine("Data comes in", 0);
      this.dataGoesOut = this.createLine("Data goes out", 1);
      this.cantExplainThat = this.createLine("Can't explain that", 2);
      this.logoPanel = this.createLogoPanel(3);
      this.animators[0].setNext(this.animators[1], 0);
      this.animators[1].setNext(this.animators[2], 1250);
      this.animators[2].setNext(this.animators[3], 0);
   }

   private KongaPanel createLine(String var1, int var2) {
      KongaLabel var3 = WizardStyleSheet.SECTION_STYLE.makeLabel(var1);
      var3.setHorizontalAlignment(10);
      KongaPanel var4 = new KongaPanel();
      InvisiblePanel.convert(var4);
      var4.setOpaque(false);
      var4.add(var3);
      var4.setAlpha(0.0F);
      this.animators[var2] = new Slogan.AnimatorImpl(var4);
      return var4;
   }

   private KongaPanel createLogoPanel(int var1) {
      JLabel var2 = new JLabel(this.icon);
      KongaPanel var3 = new KongaPanel();
      var3.setOpaque(false);
      BorderLayoutBuilder.from(var3).center(var2);
      var3.setAlpha(0.0F);
      Slogan.AnimatorImpl var4 = new Slogan.AnimatorImpl(var3, 1000);
      var4.max = 0.16F;
      this.animators[var1] = var4;
      return var3;
   }

   public void open() {
      DelayedJob var1 = new DelayedJob(1000, new Runnable() {
         @Override
         public void run() {
            Slogan.this.animators[0].start();
         }
      });
      KongaDialog var2 = this.createDialog(var1);
      var2.setVisible(true);
      this.doCleanup(var1);
   }

   private KongaDialog createDialog(final DelayedJob var1) {
      KongaDialog var2 = new KongaDialog(ApplicationUi.getAppWinFrame(), "Inconceivable", true) {
         @Override
         protected void onDialogOpened() {
            super.onDialogOpened();
            var1.schedule();
         }
      };
      var2.standardLayout(this.doLayout(), KongaDialog.CLOSE_ONLY);
      var2.setResizable(false);
      var2.setLocationRelativeTo(null);
      return var2;
   }

   private JComponent doLayout() {
      JPanel var1 = new JPanel();
      var1.setBackground(Color.WHITE);
      var1.add(new JLabel(Empty.iconOfSameSize(this.icon)));
      var1.setBorder(new BorderBuilder().line().empty(24, 16, 24, 16));
      KongaLayeredPane var2 = new KongaLayeredPane();
      var2.setBaseComponent(var1);
      var2.addOverlay(this.logoPanel, new OverlayCenterPositioner());
      Grid var3 = Grid.oneColumn(20).nonOpaque();
      var3.addAll(this.dataComesIn, this.dataGoesOut, this.cantExplainThat);
      var3.container().setSize(var3.container().getPreferredSize());
      var2.addOverlay(var3.container(), new OverlayCenterPositioner());
      var2.setBorder(new BorderBuilder().line().empty(32));
      return var2;
   }

   private void doCleanup(DelayedJob var1) {
      var1.cancel();

      for (Slogan.AnimatorImpl var5 : this.animators) {
         var5.stop();
      }
   }

   private static class AnimatorImpl extends TimingTargetAdapter {
      private final KongaPanel panel;
      private final Animator worker;
      private Slogan.AnimatorImpl next;
      private boolean dead;
      private DelayedJob delayBeforeNext;
      public float max;

      public AnimatorImpl(KongaPanel var1) {
         this(var1, 3000);
      }

      public AnimatorImpl(KongaPanel var1, int var2) {
         this.panel = var1;
         this.worker = new Animator(var2, this);
         this.worker.setAcceleration(0.3F);
         this.worker.setDeceleration(0.2F);
         this.max = 1.0F;
      }

      public void setNext(Slogan.AnimatorImpl var1, int var2) {
         this.next = var1;
         if (var2 > 0) {
            this.delayBeforeNext = new DelayedJob(var2, new Runnable() {
               @Override
               public void run() {
                  AnimatorImpl.this.next.start();
               }
            });
         }
      }

      @Override
      public void timingEvent(float var1) {
         float var2;
         if (this.max < 1.0F) {
            var2 = this.max * var1;
         } else {
            var2 = Math.min(var1, 0.95F);
         }

         this.panel.setAlpha(var2);
      }

      @Override
      public void end() {
         if (!this.dead && this.next != null) {
            if (this.delayBeforeNext != null) {
               this.delayBeforeNext.schedule();
            } else {
               this.next.start();
            }
         }

         this.dead = true;
      }

      public void start() {
         this.worker.start();
      }

      public void stop() {
         this.dead = true;
         this.worker.stop();
         if (this.delayBeforeNext != null) {
            this.delayBeforeNext.cancel();
         }
      }
   }
}
