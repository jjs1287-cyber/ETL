package org.etlforge.integration.client.ui.script.text;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import org.etlforge.integration.client.ui.script.partition.ScriptPartitioner;
import org.etlforge.integration.client.ui.script.partition.Tokens;
import org.etlforge.ui.text.partition.TextPartitioner;
import org.etlforge.ui.text.partition.Token;
import org.etlforge.ui.widget.text.TextComponentUtils;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public class TokenSelector {
   private final JTextComponent component;
   private final ScriptPartitioner partitioner;
   private boolean selectCompleteToken;

   public TokenSelector(JTextComponent var1, ScriptPartitioner var2) {
      this.component = var1;
      this.partitioner = var2;
   }

   public void setSelectCompleteToken(boolean var1) {
      this.selectCompleteToken = var1;
   }

   public void selectTokenAtCaretPosition() {
      this.selectToken(this.component.getCaretPosition());
   }

   public void selectToken(int var1) {
      Interval var2 = this.getIntervalToSelect(var1);
      TextComponentUtils.select(this.component, var2);
   }

   private Interval getIntervalToSelect(int var1) {
      TextPartitioner.Partition var2 = this.partitioner.getPartitionAt(var1);
      return this.isSelectableToken(var2) ? this.getTokenInterval(var2) : this.getWordInterval(var1);
   }

   private boolean isSelectableToken(TextPartitioner.Partition var1) {
      if (var1 == null) {
         return false;
      }

      if (this.selectCompleteToken) {
         return true;
      }

      Token var2 = var1.token;
      return var2 != Tokens.SINGLE_LINE_COMMENT && var2 != Tokens.BLOCK_COMMENT && var2 != Tokens.NO_CODE;
   }

   private Interval getTokenInterval(TextPartitioner.Partition var1) {
      Interval var2 = var1;
      if (var1.token == Tokens.STRING) {
         var2 = this.getIntervalForString(var1);
      } else if (var1.token == Tokens.SOURCE_PATH) {
         var2 = this.getIntervalForSourcePath(var1);
      }

      return var2;
   }

   private Interval getIntervalForString(TextPartitioner.Partition var1) {
      assert var1.token == Tokens.STRING : "getIntervalForString should be used only for string tokens";
      if (this.component.getCaretPosition() == var1.startsAt()) {
         return IntervalFactory.fromStartAndLength(var1.startsAt(), 0);
      } else {
         return this.selectCompleteToken ? var1 : IntervalFactory.fromStartAndLength(var1.startsAt() + 1, var1.length() - 2);
      }
   }

   private Interval getIntervalForSourcePath(TextPartitioner.Partition var1) {
      assert var1.token == Tokens.SOURCE_PATH : "getIntervalForSourcePath should be used only for source path tokens";

      try {
         int var2 = var1.startsAt();
         int var3 = var1.endsAt;
         if (var2 > 0 && var3 < this.component.getDocument().getLength() - 1) {
            String var4 = this.component.getDocument().getText(var2 - 1, 1);
            String var5 = this.component.getDocument().getText(var3, 1);
            if ("<".equals(var4) && ">".equals(var5)) {
               return IntervalFactory.fromStartAndEnd(var2 - 1, var3 + 1);
            }
         }
      } catch (BadLocationException var6) {
         var6.printStackTrace();
      }

      return var1;
   }

   private Interval getWordInterval(int var1) {
      try {
         int var2 = Utilities.getWordStart(this.component, var1);
         int var3 = Utilities.getWordEnd(this.component, var1);
         return IntervalFactory.fromStartAndEnd(var2, var3);
      } catch (Exception var4) {
         var4.printStackTrace();
         return IntervalFactory.fromStartAndLength(var1, 0);
      }
   }
}
