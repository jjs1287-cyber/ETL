package org.etlforge.integration.client.ui.script.partition;

import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.highlight.SyntaxHighlightDocument;
import org.etlforge.ui.text.partition.TextPartitioner;
import org.etlforge.ui.text.partition.Token;
import org.etlforge.ui.text.partition.TokenScanner;

public final class ScriptPartitioner {
   private final ScriptTokenScanner scanner;
   private final TextPartitioner worker;
   private SyntaxHighlightDocument document;

   public ScriptPartitioner() {
      this(true);
   }

   public ScriptPartitioner(boolean var1) {
      this.scanner = new ScriptTokenScanner(var1);
      this.worker = new ScriptPartitioner.Worker(this.scanner);
   }

   public void setDocument(SyntaxHighlightDocument var1) {
      this.document = var1;
      if (var1 != null) {
         var1.setPartitioner(this.worker);
      }

      this.worker.setDocument(var1);
   }

   public void refresh() {
      this.worker.refresh();
   }

   public TextPartitioner.Partition getPartitionAt(int var1) {
      return this.worker.getPartitionAt(var1);
   }

   public boolean isAutoCompletionAvailable(int var1) {
      TextPartitioner.Partition var2 = this.getPartitionAt(var1);
      if (var2 == null) {
         return true;
      }

      Token var3 = var2.token;
      return var3 != Tokens.BEGIN_TRANS
         && var3 != Tokens.END_TRANS
         && var3 != Tokens.STRING
         && var3 != Tokens.SINGLE_LINE_COMMENT
         && var3 != Tokens.BLOCK_COMMENT;
   }

   public boolean isCharacterMatchingAvailable(int var1) {
      return this.isActivePosition(var1);
   }

   public boolean isContextHelpAvailable(int var1) {
      TextPartitioner.Partition var2 = this.getPartitionAt(var1);
      return isFunction(var2);
   }

   public boolean isGlobalDataElement(int var1) {
      TextPartitioner.Partition var2 = this.getPartitionAt(var1);
      return isToken(var2, Tokens.GLOBAL_DATA_ELEMENT);
   }

   public boolean isWhitespaceDecoratingRegion(int var1) {
      TextPartitioner.Partition var2 = this.getPartitionAt(var1);
      if (var2 != null && var2.token.isWhitespace()) {
         for (TextPartitioner.Partition var3 = var2.getPrevious(); var3 != null; var3 = var3.getPrevious()) {
            if (var3.token == Tokens.END_TRANS) {
               return true;
            }

            if (var3.token == Tokens.BEGIN_TRANS) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public String getFunctionNameAt(int var1) {
      if (this.document != null) {
         TextPartitioner.Partition var2 = this.getPartitionAt(var1);
         if (isFunction(var2)) {
            return DocumentUtils.getText(this.document, var2);
         }
      }

      return null;
   }

   public TextPartitioner.Partition getFirst() {
      return this.worker.getFirst();
   }

   private boolean isActivePosition(int var1) {
      TextPartitioner.Partition var2 = this.getPartitionAt(var1);
      if (var2 == null) {
         return true;
      } else {
         Token var3 = var2.token;
         if (var3 == Tokens.NO_CODE || var3 == Tokens.BEGIN_TRANS) {
            return false;
         } else {
            return var3 != Tokens.STRING && var3 != Tokens.SINGLE_LINE_COMMENT && var3 != Tokens.BLOCK_COMMENT && var3 != Tokens.END_TRANS
               ? true
               : isStartOfToken(var3, var2, var1);
         }
      }
   }

   private static boolean isStartOfToken(Token var0, TextPartitioner.Partition var1, int var2) {
      return var1.token == var0 && var1.startsAt() == var2;
   }

   private static boolean isFunction(TextPartitioner.Partition var0) {
      return isToken(var0, Tokens.FUNCTION);
   }

   private static boolean isToken(TextPartitioner.Partition var0, Token var1) {
      return var0 != null && var0.token == var1;
   }

   private static class Worker extends TextPartitioner {
      public Worker(TokenScanner var1) {
         super(var1);
      }

      @Override
      protected TextPartitioner.Partition calculateStartOfDamagedRegion(int var1) {
         TextPartitioner.Partition var2 = this.getPartitionAt(var1);
         if (var2 != null) {
            var2 = var2.getPrevious();
         }

         while (var2 != null) {
            Token var3 = var2.token;
            if (!var3.isWhitespace() && var3 != Tokens.SOURCE_PATH && var3 != Tokens.OPERATOR && var3 != Tokens.WORD_TOKEN) {
               break;
            }

            var2 = var2.getPrevious();
         }

         return var2;
      }

      @Override
      protected int calculateEndOfDamagedRegionOnInsert(int var1, String var2) {
         if (var2.equals("\\")) {
            Document var3 = this.getDocument();
            if (var1 < var3.getLength() - 1) {
               String var4 = DocumentUtils.getText(var3, var1 + 1, 1);
               if (var4.equals("\"")) {
                  return var3.getLength();
               }
            }
         }

         if (isAffectingStringLiterals(var2)) {
            TextPartitioner.Partition var5 = this.getLastStringLiteral(var1);
            if (var5 != null) {
               TextPartitioner.Partition var6 = var5.getNext();
               if (var6 != null) {
                  return var6.offset;
               }
            }

            return this.getDocument().getLength();
         } else {
            return this.getNextTokensEndPosition(var1);
         }
      }

      private TextPartitioner.Partition getLastStringLiteral(int var1) {
         for (TextPartitioner.Partition var2 = this.getLast(); var2 != null; var2 = var2.getPrevious()) {
            if (var2.offset < var1) {
               return null;
            }

            if (var2.token == Tokens.STRING) {
               return var2;
            }
         }

         return null;
      }

      private static boolean isAffectingStringLiterals(String var0) {
         return var0 != null && StringUtils.containsAny(var0, "\"'");
      }

      private int getNextTokensEndPosition(int var1) {
         TextPartitioner.Partition var2 = this.getPartitionAt(var1);
         if (var2 != null) {
            TextPartitioner.Partition var3 = var2.getNext();
            if (var3 != null) {
               return var3.endsAt;
            }
         }

         return this.getDocument().getLength();
      }

      @Override
      protected int calculateEndOfDamagedRegionOnRemoval(int var1, int var2, String var3) {
         return var3 != null && !StringUtils.containsAny(var3, "\"'\\")
            ? this.getNextTokensEndPosition(var1)
            : super.calculateEndOfDamagedRegionOnRemoval(var1, var2, var3);
      }
   }
}
