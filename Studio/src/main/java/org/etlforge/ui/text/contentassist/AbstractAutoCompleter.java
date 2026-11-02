package org.etlforge.ui.text.contentassist;

import com.google.common.base.Preconditions;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.util.Interval;

public abstract class AbstractAutoCompleter implements AutoCompleter {
   private JTextComponent target;
   private CompletionContext completionContext;
   private boolean grabFocusAfterCompletion = true;
   private int grabFocusDelay = 100;

   @Override
   public void install(JTextComponent var1) {
      Preconditions.checkNotNull(var1, "text");
      this.target = var1;
   }

   @Override
   public void uninstall() {
      this.target = null;
   }

   @Override
   public final void setCompletionContext(CompletionContext var1) {
      this.completionContext = var1;
   }

   protected final CompletionContext getCompletionContext() {
      return this.completionContext;
   }

   protected final JTextComponent getTarget() {
      return this.target;
   }

   public final void setGrabFocusAfterCompletion(boolean var1) {
      this.grabFocusAfterCompletion = var1;
   }

   public final void setGrabFocusDelay(int var1) {
      this.grabFocusDelay = var1;
   }

   protected final void completeToken(CompletionToken var1) {
      Document var2 = this.target.getDocument();
      var1.complete(var2);
      Interval var3 = var1.getInterval(var2);
      int var4 = Math.min(var3.startsAt() + var3.length(), var2.getLength());
      this.target.setCaretPosition(var4);
      if (this.grabFocusAfterCompletion) {
         if (this.grabFocusDelay > 0) {
            FocusUtils.changeFocusAcrossWindows(this.target, 100);
         } else {
            this.target.requestFocusInWindow();
         }
      }

      this.postCompletion();
   }

   protected void postCompletion() {
   }

   protected final CompletionToken[] getAllAvailableTokens() {
      if (this.completionContext == null) {
         return null;
      }

      Document var1 = this.target.getDocument();
      int var2 = Utils.getCaretPosition(this.target);
      return var2 < 0 ? new CompletionToken[0] : this.completionContext.getAvailableTokens(var1, var2);
   }
}
