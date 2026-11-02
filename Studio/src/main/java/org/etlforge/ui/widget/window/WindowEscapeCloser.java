package org.etlforge.ui.widget.window;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

public final class WindowEscapeCloser {
   private final Window window;
   private final WindowEscapeCloser.EscapeListener escapeListener = new WindowEscapeCloser.EscapeListener();
   private final WindowListener closeListener = new WindowEscapeCloser.CloseListener();
   private boolean consumeEscapeWhileVisible = true;
   private final List<Runnable> runOnClose = Lists.newArrayList();

   public static WindowEscapeCloser bind(Window var0) {
      return new WindowEscapeCloser(var0);
   }

   private WindowEscapeCloser(Window var1) {
      this.window = Preconditions.checkNotNull(var1);
      this.escapeListener.install();
      var1.addWindowListener(this.closeListener);
   }

   public void addRunToJobOnClose(Runnable var1) {
      this.runOnClose.add(Preconditions.checkNotNull(var1));
   }

   public void dispose() {
      this.escapeListener.uninstall();
      this.window.removeWindowListener(this.closeListener);
   }

   public void setConsumeEscapeWhileVisible(boolean var1) {
      this.consumeEscapeWhileVisible = var1;
   }

   private class CloseListener extends WindowAdapter {
      private CloseListener() {
      }

      @Override
      public void windowClosed(WindowEvent var1) {
         WindowEscapeCloser.this.dispose();
      }
   }

   private class EscapeListener implements KeyEventDispatcher {
      private EscapeListener() {
      }

      public void install() {
         KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
      }

      public void uninstall() {
         KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
      }

      @Override
      public boolean dispatchKeyEvent(KeyEvent var1) {
         if (this.isEscape(var1)) {
            WindowEscapeCloser.this.dispose();
            WindowEscapeCloser.this.window.dispose();
            this.runPostCloseJobs();
            return WindowEscapeCloser.this.consumeEscapeWhileVisible;
         } else {
            return false;
         }
      }

      private boolean isEscape(KeyEvent var1) {
         return var1.getID() == 401 && var1.getKeyCode() == 27;
      }

      private void runPostCloseJobs() {
         for (Runnable var2 : WindowEscapeCloser.this.runOnClose) {
            var2.run();
         }
      }
   }
}
