package org.etlforge.ui.text.contentassist;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.text.JTextComponent;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;

public final class CodeEditorAutoCompleter extends AbstractAutoCompleter {
   private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
   private KeyBindingInstaller keyBindings;
   private Action triggerAction;
   private TokenChooser tokenChooser;

   public void setTokenChooser(TokenChooser var1) {
      this.tokenChooser = var1;
   }

   private TokenChooser getTokenChooser() {
      if (this.tokenChooser == null) {
         this.tokenChooser = new DefaultTokenChooser();
      }

      return this.tokenChooser;
   }

   @Override
   public void uninstall() {
      if (this.getTarget() != null) {
         this.keyBindings.remove();
         this.keyBindings = null;
         super.uninstall();
      }
   }

   @Override
   public void install(JTextComponent var1) {
      if (var1 != this.getTarget()) {
         if (this.getTarget() != null) {
            this.uninstall();
         }

         super.install(var1);
         this.installImpl(var1);
      }
   }

   private void installImpl(JTextComponent var1) {
      assert var1 != null;
      this.keyBindings = new KeyBindingInstaller(var1);
      this.keyBindings.addBinding(KeyUtils.CONTENT_ASSIST, "", this.getTriggerAction());
      this.keyBindings.install();
   }

   private Action getTriggerAction() {
      if (this.triggerAction == null) {
         this.triggerAction = new CodeEditorAutoCompleter.TriggerAction();
      }

      return this.triggerAction;
   }

   private class TokenSelector implements Runnable {
      private final Receiver<CompletionToken> tokenAcceptor;

      public TokenSelector(Receiver<CompletionToken> var2) {
         this.tokenAcceptor = var2;
      }

      @Override
      public void run() {
         this.getTokenToComplete();
      }

      private void getTokenToComplete() {
         CompletionToken[] var1 = CodeEditorAutoCompleter.this.getAllAvailableTokens();
         if (var1 != null && var1.length != 0) {
            if (var1.length == 1) {
               this.tokenAcceptor.handle(var1[0]);
            } else {
               CodeEditorAutoCompleter.this.getTokenChooser()
                  .activate(CodeEditorAutoCompleter.this.getCompletionContext(), CodeEditorAutoCompleter.this.getTarget(), var1, this.tokenAcceptor);
            }
         }
      }
   }

   private class TriggerAction extends AbstractAction {
      private TriggerAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (CodeEditorAutoCompleter.this.getTarget().isEditable()) {
            CodeEditorAutoCompleter.TokenSelector var2 = CodeEditorAutoCompleter.this.new TokenSelector(new Receiver<CompletionToken>() {
               public void handle(final CompletionToken var1) {
                  assert var1 != null;
                  EventQueue.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                        CodeEditorAutoCompleter.this.completeToken(var1);
                     }
                  });
               }
            });
            CodeEditorAutoCompleter.THREAD_POOL.submit(var2);
         }
      }
   }
}
