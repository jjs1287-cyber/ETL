package org.etlforge.integration.client.main;

import com.google.common.base.Preconditions;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.runtime.StartupProgressMonitor;
import org.etlforge.ui.util.UiUtils;

final class SplashscreenProgressMonitor implements StartupProgressMonitor {
   private SplashScreen ss;
   private Graphics2D g2;

   public SplashscreenProgressMonitor(SplashScreen var1) {
      this.ss = Preconditions.checkNotNull(var1);
   }

   @Override
   public void publishMessage(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SplashscreenProgressMonitor.this.setMessageEDT(var1);
         }
      });
   }

   private void setMessageEDT(String var1) {
      if (!this.isDisposed()) {
         if (!this.ss.isVisible()) {
            this.dispose();
         } else {
            if (this.g2 == null) {
               this.g2 = this.ss.createGraphics();
            }

            Rectangle var2 = prepareGraphics(this.ss, this.g2);
            this.g2.drawString(var1, 12, var2.height - 10);
            this.ss.update();
         }
      }
   }

   private boolean isDisposed() {
      return this.ss == null;
   }

   private void dispose() {
      this.ss = null;
      this.g2 = null;
   }

   private static Rectangle prepareGraphics(SplashScreen var0, Graphics2D var1) {
      Rectangle var2 = var0.getBounds();
      var1.setComposite(AlphaComposite.Clear);
      var1.fillRect(0, 0, var2.width - 1, var2.height - 1);
      var1.setPaintMode();
      var1.setColor(Color.DARK_GRAY);
      var1.setFont(TextStyles.AdminHint.getFont());
      UiUtils.makeAntiAliased(var1);
      return var2;
   }
}
