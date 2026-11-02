package org.etlforge.ui.util;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import org.etlforge.system.Disposable;

public abstract class MagicPhraseDetector implements KeyEventDispatcher {
   private final String phrase;
   private int position = -1;
   private Disposable installed;

   public MagicPhraseDetector(String var1) {
      this.phrase = var1;
   }

   public final void dispose() {
      if (this.installed != null) {
         this.installed.dispose();
         this.installed = null;
      }
   }

   public final Disposable install() {
      this.dispose();
      KeyboardFocusManager var1 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      var1.addKeyEventDispatcher(this);
      this.installed = new Disposable() {
         @Override
         public void dispose() {
            KeyboardFocusManager var1x = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            var1x.removeKeyEventDispatcher(MagicPhraseDetector.this);
            MagicPhraseDetector.this.installed = null;
         }
      };
      return this.installed;
   }

   @Override
   public final boolean dispatchKeyEvent(KeyEvent var1) {
      if (var1.getID() == 400) {
         try {
            this.checkKeyTyped(var1);
         } catch (Exception var3) {
         }
      }

      return false;
   }

   private void checkKeyTyped(KeyEvent var1) {
      if (!this.isApplicableFor(var1.getComponent())) {
         this.position = -1;
      } else {
         char var2 = var1.getKeyChar();
         if (this.position >= 0) {
            char var3 = this.phrase.charAt(this.position + 1);
            if (var2 == var3) {
               this.position++;
               if (this.position == this.phrase.length() - 1) {
                  this.position = -1;
                  this.doMagic();
               }

               return;
            }
         }

         if (var2 == this.phrase.charAt(0)) {
            this.position = 0;
         } else {
            this.position = -1;
         }
      }
   }

   protected boolean isApplicableFor(Component var1) {
      return true;
   }

   protected abstract void doMagic();
}
