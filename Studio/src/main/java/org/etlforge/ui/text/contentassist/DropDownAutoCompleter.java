package org.etlforge.ui.text.contentassist;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.widget.list.ListFactory;

public final class DropDownAutoCompleter extends AbstractAutoCompleter {
   private TokenChooserListUi listUi;
   private DropDownAutoCompleter.TokenAcceptor tokenAcceptor;
   private final DropDownAutoCompleter.TextFieldListener textListener;
   private DelayedJob updater;
   private ListFactory listFactory;
   private int maxCharWidth = -1;
   private boolean isCompleting;
   private boolean enabled;
   private boolean displayMatchingTokensAfterCompletion;
   private Predicate<KeyEvent> typingFilter;
   private int clickCountToTrigger = 2;

   public DropDownAutoCompleter() {
      this((CompletionContext)null);
   }

   public DropDownAutoCompleter(StringValueHistory var1) {
      this(new StringValueHistoryCompletionContext(var1));
   }

   public DropDownAutoCompleter(CompletionContext var1) {
      this.enabled = true;
      this.textListener = new DropDownAutoCompleter.TextFieldListener();
      this.setGrabFocusAfterCompletion(true);
      this.setGrabFocusDelay(0);
      this.setCompletionContext(var1);
      this.setTypingFilter(null);
   }

   @Override
   public void install(JTextComponent var1) {
      if (this.getTarget() != var1) {
         if (this.getTarget() != null) {
            this.uninstall();
         }

         super.install(var1);
         var1.addKeyListener(this.textListener);
         var1.addFocusListener(this.textListener);
         var1.addMouseListener(this.textListener);
         this.installKeyBindings(var1);
      }
   }

   private void installKeyBindings(JTextComponent var1) {
      String var2 = "showMatches";
      var1.getInputMap().put(KeyStroke.getKeyStroke(32, 128), var2);
      var1.getActionMap().put(var2, new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            DropDownAutoCompleter.this.findAndDisplayMatchingTokens(false);
         }
      });
   }

   private void findAndDisplayMatchingTokens(boolean var1) {
      assert EventQueue.isDispatchThread() : "This method must be called from the EDT to guarantee thread correctness";
      if (this.updater != null) {
         this.updater.cancel();
      }

      new DropDownAutoCompleter.Worker(var1).run();
   }

   @Override
   public void uninstall() {
      if (this.getTarget() != null) {
         this.getTarget().removeKeyListener(this.textListener);
         this.getTarget().removeFocusListener(this.textListener);
         this.getTarget().removeMouseListener(this.textListener);
         super.uninstall();
      }
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
      if (!var1) {
         this.hide();
      }
   }

   private void hide() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (DropDownAutoCompleter.this.listUi != null) {
               DropDownAutoCompleter.this.listUi.hide(false);
            }
         }
      });
   }

   public void setListFactory(ListFactory var1) {
      this.listFactory = var1;
   }

   public void setMaxWidth(int var1) {
      this.maxCharWidth = var1;
   }

   public final void setDisplayMatchingTokensAfterCompletion(boolean var1) {
      this.displayMatchingTokensAfterCompletion = var1;
   }

   public void setTypingFilter(Predicate<KeyEvent> var1) {
      if (var1 == null) {
         this.typingFilter = Predicates.alwaysTrue();
      } else {
         this.typingFilter = var1;
      }
   }

   public void setClickCountToTrigger(int var1) {
      this.clickCountToTrigger = var1;
   }

   public void displayValueDropDown() {
      if (EventQueue.isDispatchThread()) {
         this.findAndDisplayMatchingTokens(true);
      } else {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DropDownAutoCompleter.this.findAndDisplayMatchingTokens(true);
            }
         });
      }
   }

   @Override
   protected void postCompletion() {
      if (this.displayMatchingTokensAfterCompletion) {
         this.findAndDisplayMatchingTokens(true);
      }
   }

   private class TextFieldListener extends MouseAdapter implements KeyListener, FocusListener {
      private TextFieldListener() {
      }

      @Override
      public void keyPressed(KeyEvent var1) {
         if (var1.getKeyCode() == 27) {
            DropDownAutoCompleter.this.hide();
         }
      }

      @Override
      public void keyReleased(KeyEvent var1) {
      }

      @Override
      public void keyTyped(KeyEvent var1) {
         if (!this.isIgnored(var1) && DropDownAutoCompleter.this.getTarget().isEditable()) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  if (DropDownAutoCompleter.this.updater != null) {
                     DropDownAutoCompleter.this.updater.cancel();
                  }

                  DropDownAutoCompleter.this.updater = DelayedJob.createAndSchedule(75, DropDownAutoCompleter.this.new Worker(true));
               }
            });
         }
      }

      @Override
      public void focusGained(FocusEvent var1) {
      }

      @Override
      public void focusLost(FocusEvent var1) {
         DropDownAutoCompleter.this.hide();
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         if (DropDownAutoCompleter.this.clickCountToTrigger >= 1 && !var1.isPopupTrigger()) {
            if (var1.getClickCount() == DropDownAutoCompleter.this.clickCountToTrigger && DropDownAutoCompleter.this.getTarget().getDocument().getLength() == 0
               )
             {
               DropDownAutoCompleter.this.findAndDisplayMatchingTokens(false);
            }
         }
      }

      private boolean isIgnored(KeyEvent var1) {
         return this.isTerminatingKey(var1)
            || var1.isControlDown()
            || var1.isAltDown()
            || this.isDelete(var1)
            || DropDownAutoCompleter.this.isCompleting
            || !DropDownAutoCompleter.this.enabled
            || this.isFilteredOut(var1);
      }

      private boolean isTerminatingKey(KeyEvent var1) {
         char var2 = var1.getKeyChar();
         return var2 == 27 || var2 == '\t' || var2 == '\n';
      }

      private boolean isDelete(KeyEvent var1) {
         return var1.getKeyChar() == 127;
      }

      private boolean isFilteredOut(KeyEvent var1) {
         return !DropDownAutoCompleter.this.typingFilter.apply(var1);
      }
   }

   private class TokenAcceptor implements Receiver<CompletionToken> {
      private TokenAcceptor() {
      }

      public void handle(CompletionToken var1) {
         if (var1 != null) {
            this.insertToken(var1);
         }
      }

      private void insertToken(final CompletionToken var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DropDownAutoCompleter.this.isCompleting = true;

               try {
                  DropDownAutoCompleter.this.completeToken(var1);
               } finally {
                  DropDownAutoCompleter.this.isCompleting = false;
               }
            }
         });
      }
   }

   private class Worker implements Runnable {
      private final boolean hideWhenEmpty;

      public Worker(boolean var2) {
         this.hideWhenEmpty = var2;
      }

      @Override
      public void run() {
         if (this.hideWhenEmpty && DropDownAutoCompleter.this.getTarget().getDocument().getLength() == 0) {
            DropDownAutoCompleter.this.hide();
         } else {
            this.checkTokens();
         }
      }

      private void checkTokens() {
         CompletionToken[] var1 = DropDownAutoCompleter.this.getAllAvailableTokens();
         if (var1.length == 0) {
            DropDownAutoCompleter.this.hide();
         } else {
            this.displayTokenList(var1);
         }
      }

      private void displayTokenList(final CompletionToken[] var1) {
         assert var1.length > 0 : "Should not display empty token list";
         if (DropDownAutoCompleter.this.listUi == null) {
            DropDownAutoCompleter.this.tokenAcceptor = DropDownAutoCompleter.this.new TokenAcceptor();
            DropDownAutoCompleter.this.listUi = new TokenChooserListUi(DropDownAutoCompleter.this.getTarget(), DropDownAutoCompleter.this.tokenAcceptor);
            DropDownAutoCompleter.this.listUi.setTransient(true);
            DropDownAutoCompleter.this.listUi.setListFactory(DropDownAutoCompleter.this.listFactory);
            DropDownAutoCompleter.this.listUi.setMaxWidth(DropDownAutoCompleter.this.maxCharWidth);
            DropDownAutoCompleter.this.listUi.setTrackWidthOfTrigger(true);
         } else {
            DropDownAutoCompleter.this.listUi.setTokens(var1);
         }

         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DropDownAutoCompleter.this.listUi.display(DropDownAutoCompleter.this.getCompletionContext(), var1);
            }
         });
      }
   }
}
