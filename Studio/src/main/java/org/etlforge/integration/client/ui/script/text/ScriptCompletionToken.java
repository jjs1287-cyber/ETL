package org.etlforge.integration.client.ui.script.text;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.ui.text.contentassist.DefaultCompletionToken;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public abstract class ScriptCompletionToken extends DefaultCompletionToken {
   protected final JTextComponent inputComponent;
   protected int tokenStartsAt;

   public ScriptCompletionToken(String var1, int var2, int var3, String var4, JTextComponent var5) {
      super(var1, var2, var3, var4);
      this.inputComponent = var5;
      this.tokenStartsAt = 0;
   }

   @Override
   public void complete(Document var1) {
      super.complete(var1);
      ScriptCompletionToken.PostCompletionHandler var2 = this.createPostCompletionHandler(var1);
      this.doPostCompletionHandling(var2);
   }

   @Override
   public Interval getInterval(Document var1) {
      Interval var2 = super.getInterval(var1);
      return this.tokenStartsAt > 0 ? IntervalFactory.fromStartAndLength(var2.startsAt() + this.tokenStartsAt, var2.length()) : var2;
   }

   protected abstract void doPostCompletionHandling(ScriptCompletionToken.PostCompletionHandler var1);

   protected abstract ScriptCompletionToken.PostCompletionHandler createPostCompletionHandler(Document var1);

   protected abstract class PostCompletionHandler {
      protected final Document document;
      protected final Interval interval;
      protected final String allText;

      public PostCompletionHandler(Document var2) {
         this.document = var2;
         this.allText = ScriptCompletionToken.this.inputComponent.getText();
         this.interval = ScriptCompletionToken.this.getInterval(var2);
      }

      public abstract void handle();

      public final void checkBeginTrans() {
         if (org.etlforge.integration.data.script.JavaScriptFunctions.isInside(this.allText, this.interval.startsAt())) return;
         if (this.allText.indexOf("<trans>") == -1) {
            this.insert("<trans>\n", this.interval.startsAt());
            ScriptCompletionToken.this.tokenStartsAt = ScriptConstants.BEGIN_TRANS_LENGTH + 1;
         }
      }

      public final void checkEndTrans() {
         if (org.etlforge.integration.data.script.JavaScriptFunctions.isInside(this.allText, this.interval.startsAt())) return;
         if (this.allText.indexOf("</trans>") == -1) {
            this.insert("\n</trans>", ScriptCompletionToken.this.tokenStartsAt + this.interval.startsAt() + this.interval.length());
         }
      }

      private void insert(String var1, int var2) {
         try {
            this.document.insertString(var2, var1, null);
         } catch (BadLocationException var4) {
            var4.printStackTrace();
         }
      }
   }
}
