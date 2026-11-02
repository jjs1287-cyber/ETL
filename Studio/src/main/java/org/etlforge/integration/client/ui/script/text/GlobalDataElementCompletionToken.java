package org.etlforge.integration.client.ui.script.text;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.util.Interval;

public class GlobalDataElementCompletionToken extends ScriptCompletionToken {
   public GlobalDataElementCompletionToken(String var1, int var2, int var3, JTextComponent var4) {
      super(var1, var2, var3, var1, var4);
   }

   @Override
   protected ScriptCompletionToken.PostCompletionHandler createPostCompletionHandler(Document var1) {
      return new GlobalDataElementCompletionToken.PostCompletionHandlerImpl(var1);
   }

   @Override
   protected void doPostCompletionHandling(ScriptCompletionToken.PostCompletionHandler var1) {
      var1.handle();
   }

   private class PostCompletionHandlerImpl extends ScriptCompletionToken.PostCompletionHandler {
      public PostCompletionHandlerImpl(Document var2) {
         super(var2);
      }

      @Override
      public void handle() {
         this.checkBeginTrans();
         this.checkEndTrans();
         Interval var1 = GlobalDataElementCompletionToken.this.getInterval(this.document);
         GlobalDataElementCompletionToken.this.inputComponent.setCaretPosition(var1.startsAt() + var1.length());
      }
   }
}
