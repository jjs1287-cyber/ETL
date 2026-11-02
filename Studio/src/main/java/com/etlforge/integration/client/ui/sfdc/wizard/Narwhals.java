package com.etlforge.integration.client.ui.sfdc.wizard;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.MagicPhraseDetector;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;

final class Narwhals implements Disposable {
   private final Wizard<?> wizard;
   private final MagicPhraseDetector detector;

   public Narwhals(Wizard<?> var1) {
      this.wizard = var1;
      this.detector = new Narwhals.DetectorImpl();
   }

   public void install() {
      KeyboardFocusManager var1 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      var1.addKeyEventDispatcher(this.detector);
   }

   @Override
   public void dispose() {
      KeyboardFocusManager var1 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      var1.removeKeyEventDispatcher(this.detector);
   }

   private static class Animator implements ActionListener {
      private Timer timer;
      private final Narwhals.Overlay overlay;
      private final OverlayContainer container;

      public Animator(Narwhals.Overlay var1, OverlayContainer var2) {
         this.overlay = var1;
         this.container = var2;
      }

      public void setTimer(Timer var1) {
         this.timer = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.overlay.getStep() == 10) {
            this.timer.stop();
            this.container.removeOverlay(this.overlay);
         } else {
            this.overlay.update();
         }
      }
   }

   private class DetectorImpl extends MagicPhraseDetector {
      public DetectorImpl() {
         super("narwhal");
      }

      @Override
      protected boolean isApplicableFor(Component var1) {
         return !(var1 instanceof JTextComponent);
      }

      @Override
      protected void doMagic() {
         WizardPage var1 = Narwhals.this.wizard.getCurrentPage();
         if (var1 instanceof AbstractWizardPage) {
            OverlayContainer var2 = ((AbstractWizardPage)var1).getOverlayContainer();
            this.addOverlay(var2);
         }
      }

      private void addOverlay(OverlayContainer var1) {
         Narwhals.Overlay var2 = new Narwhals.Overlay();
         var2.putClientProperty("konga.overlay.blocking", true);
         var2.setPreferredSize(var1.getSize());
         var1.addOverlay(var2, new OverlayCenterPositioner());
         Narwhals.Animator var3 = new Narwhals.Animator(var2, var1);
         Timer var4 = new Timer(1000, var3);
         var3.setTimer(var4);
         var4.start();
      }
   }

   private static class Overlay extends JComponent {
      private final Icon icon = ApplicationResources.ICONS.getIcon("DanOnANarwhal");
      private int step;

      private Overlay() {
      }

      @Override
      protected void paintComponent(Graphics var1) {
         int var2 = this.getWidth() - (int)(this.getWidth() / 8.0) * this.step;
         int var3 = (this.getHeight() - this.icon.getIconHeight()) / 2;
         this.icon.paintIcon(this, var1, var2, var3);
      }

      public void update() {
         this.step++;
         this.repaint();
      }

      public int getStep() {
         return this.step;
      }
   }
}
