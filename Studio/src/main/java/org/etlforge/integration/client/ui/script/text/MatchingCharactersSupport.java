package org.etlforge.integration.client.ui.script.text;

import com.google.common.collect.Sets;
import java.awt.event.ActionEvent;
import java.util.Set;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.DefaultEditorKit.DefaultKeyTypedAction;
import org.etlforge.integration.client.ui.script.partition.ScriptPartitioner;
import org.etlforge.integration.client.ui.script.partition.Tokens;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.partition.TextPartitioner;
import org.etlforge.ui.text.partition.Token;

public class MatchingCharactersSupport {
   private final ScriptPartitioner partitioner;

   public MatchingCharactersSupport(ScriptPartitioner var1) {
      this.partitioner = var1;
   }

   public final void installParenthesisAction(JTextComponent var1) {
      new MatchingCharactersSupport.InsertOpeningCharacterAction(this.partitioner, '(', ')').install(var1);
      new MatchingCharactersSupport.InsertClosingCharacterAction(this.partitioner, ')').install(var1);
   }

   public final void installDoubleQuoteAction(JTextComponent var1) {
      new MatchingCharactersSupport.InsertOpeningCharacterAction(this.partitioner, '"', '"').install(var1);
   }

   public final void installSingleQuoteAction(JTextComponent var1) {
      new MatchingCharactersSupport.InsertOpeningCharacterAction(this.partitioner, '\'', '\'').install(var1);
   }

   private static class ActionBase extends DefaultKeyTypedAction {
      private final ScriptPartitioner partitioner;
      private final Set<Token> excludedTokens;

      private ActionBase(ScriptPartitioner var1) {
         this.partitioner = var1;
         this.excludedTokens = Sets.newHashSet(Tokens.STRING, Tokens.SINGLE_LINE_COMMENT, Tokens.BLOCK_COMMENT);
      }

      protected final boolean isAvailableAt(int var1) {
         Token var2 = this.getTokenAt(var1);
         return var2 != null && !this.excludedTokens.contains(var2);
      }

      protected final Token getTokenAt(int var1) {
         TextPartitioner.Partition var2 = this.partitioner.getPartitionAt(var1);
         return var2 != null ? var2.token : null;
      }

      protected final boolean isNextCharacter(JTextComponent var1, int var2, String var3) {
         Document var4 = var1.getDocument();
         if (var2 >= var4.getLength() - 1) {
            return false;
         }

         String var5 = DocumentUtils.getText(var4, var2, 1);
         return var3.equals(var5);
      }
   }

   private static class InsertClosingCharacterAction extends MatchingCharactersSupport.ActionBase {
      private final String closing;

      private InsertClosingCharacterAction(ScriptPartitioner var1, char var2) {
         super(var1);
         this.closing = String.valueOf(var2);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         assert this.closing.equals(var1.getActionCommand());
         JTextComponent var2 = this.getTextComponent(var1);
         int var3 = var2.getCaretPosition();
         if (this.isAvailableAt(var3) && this.isNextCharacter(var2, var3, this.closing)) {
            var2.setCaretPosition(var3 + 1);
         } else {
            super.actionPerformed(var1);
         }
      }

      public void install(JTextComponent var1) {
         String var2 = "insert-closing-" + this.closing;
         var1.getInputMap(0).put(KeyStroke.getKeyStroke(this.closing.charAt(0)), var2);
         var1.getActionMap().put(var2, this);
      }
   }

   private static class InsertOpeningCharacterAction extends MatchingCharactersSupport.ActionBase {
      private final String opening;
      private final String closing;

      private InsertOpeningCharacterAction(ScriptPartitioner var1, char var2, char var3) {
         super(var1);
         this.opening = String.valueOf(var2);
         this.closing = String.valueOf(var3);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         assert this.opening.equals(var1.getActionCommand());
         JTextComponent var2 = this.getTextComponent(var1);
         int var3 = var2.getCaretPosition();
         if (this.opening.equals(this.closing) && this.getTokenAt(var3) == Tokens.STRING && this.isNextCharacter(var2, var3, this.closing)) {
            var2.setCaretPosition(var3 + 1);
         } else {
            boolean var4 = this.isAvailableAt(var3);
            super.actionPerformed(var1);
            if (var4) {
               var3 = var2.getCaretPosition();
               ActionEvent var5 = new ActionEvent(var1.getSource(), var1.getID(), this.closing);
               super.actionPerformed(var5);
               var2.setCaretPosition(var3);
            }
         }
      }

      public void install(JTextComponent var1) {
         String var2 = "insert-opening-" + this.opening;
         var1.getInputMap(0).put(KeyStroke.getKeyStroke(this.opening.charAt(0)), var2);
         var1.getActionMap().put(var2, this);
      }
   }
}
