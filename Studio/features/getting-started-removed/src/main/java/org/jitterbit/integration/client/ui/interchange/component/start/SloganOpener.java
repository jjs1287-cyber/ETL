package org.jitterbit.integration.client.ui.interchange.component.start;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.jitterbit.application.config.ApplicationConfiguration;
import org.jitterbit.application.ui.window.AbstractWindowElementResource;
import org.jitterbit.application.ui.window.ApplicationWindow;
import org.jitterbit.application.ui.window.WindowElement;
import org.jitterbit.application.ui.window.WindowElementStateSpan;
import org.jitterbit.application.ui.window.editor.Editor;
import org.jitterbit.ui.util.MagicPhraseDetector;

class SloganOpener extends AbstractWindowElementResource {
   private final ApplicationWindow appWin;
   private final GettingStartedPage page;
   private SloganOpener.Magic magic;

   public SloganOpener(ApplicationWindow var1, GettingStartedPage var2) {
      super(WindowElementStateSpan.WHEN_ACTIVE);
      this.appWin = var1;
      this.page = var2;
   }

   @Override
   public void create(WindowElement var1) {
      if (this.magic != null) {
         this.dispose(this.page);
      }

      if (this.isSupported()) {
         this.magic = new SloganOpener.Magic();
         this.magic.install();
      }
   }

   private boolean isSupported() {
      return ApplicationConfiguration.getBoolean("GettingStarted.Slogan", true);
   }

   @Override
   public void dispose(WindowElement var1) {
      if (this.magic != null) {
         this.magic.dispose();
         this.magic = null;
      }
   }

   private class Magic extends MagicPhraseDetector {
      public Magic() {
         super("slogan");
      }

      @Override
      protected void doMagic() {
         if (this.isValidState()) {
            Slogan var1 = new Slogan();
            var1.open();
         } else {
            SloganOpener.this.dispose(SloganOpener.this.page);
         }
      }

      private boolean isValidState() {
         Editor var1 = SloganOpener.this.appWin.getEditorService().getActiveEditor();
         return var1 == SloganOpener.this.page;
      }

      @Override
      protected boolean isApplicableFor(Component var1) {
         if (var1 == null) {
            return false;
         }

         JComponent var2 = SloganOpener.this.page.getMainPanel();
         return var2 == null ? false : SwingUtilities.isDescendingFrom(var1, var2);
      }
   }
}
