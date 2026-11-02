package org.etlforge.integration.client.ui.script.text;

import java.awt.EventQueue;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.script.prefs.AutoSuggestFunctionArgumentsPreference;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.ui.text.contentassist.DefaultCompletionToken;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public class FunctionCompletionToken extends DefaultCompletionToken {
   private final Function function;
   private final JTextComponent inputComponent;
   private int tokenStartsAt;
   private FunctionArgumentSuggestion argumentSuggestion;

   public FunctionCompletionToken(Function var1, int var2, int var3, JTextComponent var4) {
      super(var1.getInsertValue(), var2, var3, var1.getName());
      this.function = var1;
      this.inputComponent = var4;
      this.tokenStartsAt = 0;
      this.argumentSuggestion = FunctionArgumentSuggestion.NONE;
   }

   public void setArgumentSuggestion(FunctionArgumentSuggestion var1) {
      this.argumentSuggestion = var1;
   }

   @Override
   public void complete(Document var1) {
      super.complete(var1);
      FunctionCompletionToken.PostCompletionHandler var2 = new FunctionCompletionToken.PostCompletionHandler(var1);
      if (!var2.allText.equals("<SQLIDENTITY>") && !var2.allText.equals("<SEQUENCE>") && !var2.allText.equals("<UDF>")) {
         var2.handle();
      }
   }

   @Override
   public Interval getInterval(Document var1) {
      Interval var2 = super.getInterval(var1);
      return this.tokenStartsAt > 0 ? IntervalFactory.fromStartAndLength(var2.startsAt() + this.tokenStartsAt, var2.length()) : var2;
   }

   private class PostCompletionHandler {
      private final Document document;
      private final Interval interval;
      private final String allText;

      public PostCompletionHandler(Document var2) {
         this.document = var2;
         this.allText = FunctionCompletionToken.this.inputComponent.getText();
         this.interval = FunctionCompletionToken.this.getInterval(var2);
      }

      public void handle() {
         this.checkBeginTrans();
         int var1 = this.getRelativeIndexOfFirstArgument();
         if (var1 >= 0) {
            this.highlightFirstArgument(var1);
         }

         this.checkEndTrans();
         this.suggestArgument();
      }

      private void checkBeginTrans() {
         if (org.etlforge.integration.data.script.JavaScriptFunctions.isJavaScript(FunctionCompletionToken.this.function)) {
            if (!org.etlforge.integration.data.script.JavaScriptFunctions.isInside(this.allText, this.interval.startsAt())) {
               this.insert("<javascript>\n", this.interval.startsAt());
               FunctionCompletionToken.this.tokenStartsAt = 13;
            }
            return;
         }
         if (org.etlforge.integration.data.script.JavaScriptFunctions.isInside(this.allText, this.interval.startsAt())) return;
         if (this.allText.indexOf("<trans>") == -1) {
            this.insert("<trans>\n", this.interval.startsAt());
            FunctionCompletionToken.this.tokenStartsAt = ScriptConstants.BEGIN_TRANS_LENGTH + 1;
         }
      }

      private void checkEndTrans() {
         if (org.etlforge.integration.data.script.JavaScriptFunctions.isJavaScript(FunctionCompletionToken.this.function)) {
            if (FunctionCompletionToken.this.tokenStartsAt > 0)
               this.insert(";\n</javascript>", FunctionCompletionToken.this.tokenStartsAt + this.interval.startsAt() + this.interval.length());
            return;
         }
         if (org.etlforge.integration.data.script.JavaScriptFunctions.isInside(this.allText, this.interval.startsAt())) return;
         if (this.allText.indexOf("</trans>") == -1) {
            this.insert("\n</trans>", FunctionCompletionToken.this.tokenStartsAt + this.interval.startsAt() + this.interval.length());
         }
      }

      private void suggestArgument() {
         if (AutoSuggestFunctionArgumentsPreference.get()) {
            EventQueue.invokeLater(
               new Runnable() {
                  @Override
                  public void run() {
                     FunctionCompletionToken.this.argumentSuggestion
                        .insertArgument(FunctionCompletionToken.this.function, FunctionCompletionToken.this.inputComponent);
                  }
               }
            );
         }
      }

      private void insert(String var1, int var2) {
         try {
            this.document.insertString(var2, var1, null);
         } catch (BadLocationException var4) {
            var4.printStackTrace();
         }
      }

      private int getRelativeIndexOfFirstArgument() {
         String var1 = FunctionCompletionToken.this.getValue();
         int var2 = var1.indexOf(40);
         return var2 >= 0 ? var2 + 1 : -1;
      }

      private void highlightFirstArgument(int var1) {
         int var2 = this.findFirstArgumentEnd(var1);
         if (var2 > var1) {
            int var3 = FunctionCompletionToken.this.tokenStartsAt + this.interval.startsAt();
            this.setSelection(var1 + var3, var2 + var3);
         }
      }

      private int findFirstArgumentEnd(int var1) {
         for (int var2 = var1 + 1; var2 < FunctionCompletionToken.this.getValue().length(); var2++) {
            char var3 = FunctionCompletionToken.this.getValue().charAt(var2);
            if (var3 == ',' || var3 == ')') {
               return var2;
            }
         }

         return -1;
      }

      private void setSelection(final int var1, final int var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               FunctionCompletionToken.this.inputComponent.select(var1, var2);
            }
         });
      }
   }
}
