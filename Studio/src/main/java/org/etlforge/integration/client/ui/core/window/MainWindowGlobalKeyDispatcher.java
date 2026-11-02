package org.etlforge.integration.client.ui.core.window;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.Set;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;

final class MainWindowGlobalKeyDispatcher implements KeyEventDispatcher {
   private final ApplicationWindow window;
   private final Multimap<KeyStroke, MainWindowGlobalKeyDispatcher.SingleKeyDispatcher> dispatchers;
   private final Set<String> dispatcherIds;

   public MainWindowGlobalKeyDispatcher(ApplicationWindow var1) {
      this.window = var1;
      this.dispatchers = HashMultimap.create();
      this.dispatcherIds = Sets.newHashSet();
   }

   public void install() {
      KeyboardFocusManager var1 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      var1.addKeyEventDispatcher(this);
   }

   public void uninstall() {
      KeyboardFocusManager var1 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      var1.removeKeyEventDispatcher(this);
   }

   public void addDispatcher(final KeyStroke var1, final WindowKeyDispatcher var2) {
      if (var1 != null && var2 != null) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               MainWindowGlobalKeyDispatcher.this.addDispatcherImpl(var1, var2);
            }
         });
      }
   }

   private void addDispatcherImpl(KeyStroke var1, WindowKeyDispatcher var2) {
      String var3 = var2.getId();
      if (!this.dispatcherIds.contains(var3)) {
         this.dispatcherIds.add(var3);
         this.dispatchers.put(var1, new MainWindowGlobalKeyDispatcher.SingleKeyDispatcher(this.window, var1, var2));
      }
   }

   public void removeDispatcher(final KeyStroke var1, final WindowKeyDispatcher var2) {
      if (var1 != null && var2 != null) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               MainWindowGlobalKeyDispatcher.this.removeDispatcherImpl(var1, var2);
            }
         });
      }
   }

   private void removeDispatcherImpl(KeyStroke var1, WindowKeyDispatcher var2) {
      String var3 = var2.getId();
      if (this.dispatcherIds.contains(var3)) {
         this.dispatcherIds.remove(var3);
         MainWindowGlobalKeyDispatcher.SingleKeyDispatcher var4 = null;

         for (MainWindowGlobalKeyDispatcher.SingleKeyDispatcher var6 : this.dispatchers.get(var1)) {
            if (var6.getId().equals(var2.getId())) {
               var4 = var6;
               break;
            }
         }

         this.dispatchers.remove(var1, var4);
      }
   }

   @Override
   public boolean dispatchKeyEvent(KeyEvent var1) {
      KeyStroke var2 = KeyStroke.getKeyStrokeForEvent(var1);

      for (MainWindowGlobalKeyDispatcher.SingleKeyDispatcher var4 : this.dispatchers.get(var2)) {
         if (var4.dispatchKeyEvent(var1)) {
            return true;
         }
      }

      return false;
   }

   private static final class SingleKeyDispatcher implements KeyEventDispatcher {
      private final ApplicationWindow window;
      private final KeyStroke keyStroke;
      private final WindowKeyDispatcher delegate;

      protected SingleKeyDispatcher(ApplicationWindow var1, KeyStroke var2, WindowKeyDispatcher var3) {
         this.window = var1;
         this.keyStroke = var2;
         this.delegate = var3;
      }

      private boolean isDispatcherFor(KeyEvent var1) {
         return KeyStroke.getKeyStrokeForEvent(var1).equals(this.keyStroke);
      }

      @Override
      public boolean dispatchKeyEvent(KeyEvent var1) {
         return this.isDispatcherFor(var1) ? this.delegate.dispatch(var1, this.window) : false;
      }

      public String getId() {
         return this.delegate.getId();
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 instanceof MainWindowGlobalKeyDispatcher.SingleKeyDispatcher) {
            MainWindowGlobalKeyDispatcher.SingleKeyDispatcher var2 = (MainWindowGlobalKeyDispatcher.SingleKeyDispatcher)var1;
            return this.getId().equals(var2.getId());
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return this.getId().hashCode();
      }
   }
}
