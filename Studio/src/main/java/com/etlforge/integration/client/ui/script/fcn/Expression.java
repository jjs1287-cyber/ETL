package com.etlforge.integration.client.ui.script.fcn;

import java.awt.EventQueue;
import java.util.concurrent.Future;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.script.builder.FunctionTester;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.ui.text.contentassist.DefaultCompletionToken;

public final class Expression {
   private final String text;
   private final int caretPosition;

   public Expression(String var1) {
      this(var1, var1.length());
   }

   public Expression(String var1, int var2) {
      this.text = var1;
      this.caretPosition = var2;
   }

   public void insertInto(final JTextComponent var1) {
      var1.requestFocusInWindow();
      final int var2 = var1.getSelectionStart() + this.caretPosition;
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.replaceSelection(Expression.this.text);
            var1.setCaretPosition(var2);
            var1.requestFocusInWindow();
         }
      });
   }

   public void autoComplete(final JTextComponent var1, int var2) {
      DefaultCompletionToken var3 = new DefaultCompletionToken(this.text, var2, var1.getCaretPosition());
      var3.complete(var1.getDocument());
      final int var4 = var2 + this.caretPosition;
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.setCaretPosition(var4);
         }
      });
   }

   public ExpressionParser.Result parse(ExpressionParser var1) {
      String var2 = ScriptConstants.wrapInTags(this.text, false);
      return var1.parse(var2);
   }

   public Future<?> test(FunctionTester var1, ScriptDataElementsModel var2, ServerTestCallback var3) {
      return var1.testFunction(this.text, var2, var3);
   }
}
