package org.etlforge.integration.client.ui.script.partition;

import javax.swing.JTextPane;
import org.etlforge.integration.client.ui.script.text.ScriptDocument;

public final class ScriptSyntaxHighlighter {
   public static void install(JTextPane var0) {
      ScriptDocument var1 = new ScriptDocument();
      var0.setDocument(var1);
      ScriptPartitioner var2 = new ScriptPartitioner();
      var2.setDocument(var1);
   }

   public static void installWithoutTransTags(JTextPane var0) {
      ScriptDocument var1 = new ScriptDocument();
      var0.setDocument(var1);
      ScriptPartitioner var2 = new ScriptPartitioner(false);
      var2.setDocument(var1);
   }

   private ScriptSyntaxHighlighter() {
   }
}
