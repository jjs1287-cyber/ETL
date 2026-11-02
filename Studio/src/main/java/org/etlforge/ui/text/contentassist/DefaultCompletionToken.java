package org.etlforge.ui.text.contentassist;

import com.google.common.base.Preconditions;
import javax.swing.Icon;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;
import org.apache.commons.lang.StringUtils;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public class DefaultCompletionToken implements CompletionToken {
   private final String value;
   private final String displayValue;
   private final int startPos;
   private final int cursorPosition;
   private final String remainingPart;
   private String additionalInfo;
   private Icon icon;

   public DefaultCompletionToken(String var1, int var2, int var3) {
      this(var1, var2, var3, var1);
   }

   public DefaultCompletionToken(String var1, int var2, int var3, String var4) {
      Preconditions.checkArgument(StringUtils.isNotEmpty(var1), "value cannot be null or empty.");
      Preconditions.checkArgument(var2 >= 0, "startPos must be >= 0 (was " + var2 + ")");
      Preconditions.checkArgument(var3 >= var2, "cursorPosition must be >= startPos");
      Preconditions.checkArgument(var3 <= var2 + var1.length(), "cursor positioned after the end of this token.");
      this.value = var1;
      this.displayValue = var4;
      this.startPos = var2;
      this.cursorPosition = var3;
      this.remainingPart = var1.substring(var3 - var2);
   }

   @Override
   public void complete(Document var1) {
      Preconditions.checkArgument(var1 instanceof AbstractDocument, "The current implementation works only for " + AbstractDocument.class.getName());
      DefaultCompletionToken.Completer var2 = new DefaultCompletionToken.Completer((AbstractDocument)var1);
      var2.complete();
   }

   protected final String getValue() {
      return this.value;
   }

   @Override
   public Interval getInterval(Document var1) {
      return IntervalFactory.fromStartAndLength(this.startPos, this.value.length());
   }

   @Override
   public String getDisplayValue() {
      return this.displayValue;
   }

   @Override
   public Icon getIcon() {
      return this.icon;
   }

   public void setIcon(Icon var1) {
      this.icon = var1;
   }

   @Override
   public String getInfo() {
      return this.additionalInfo;
   }

   public void setInfo(String var1) {
      this.additionalInfo = var1;
   }

   private final class Completer {
      private final AbstractDocument document;

      public Completer(AbstractDocument var2) {
         this.document = var2;
      }

      public void complete() {
         try {
            if (DefaultCompletionToken.this.cursorPosition != DefaultCompletionToken.this.startPos) {
               this.correctTokenCaseIfNeeded();
            }

            if (this.document.getLength() == DefaultCompletionToken.this.cursorPosition) {
               this.applyWhenCursorAtTheEnd();
            } else {
               this.applyWhenCursorWithinToken();
            }
         } catch (BadLocationException var2) {
            var2.printStackTrace();
         }
      }

      private void correctTokenCaseIfNeeded() throws BadLocationException {
         String var1 = this.document
            .getText(DefaultCompletionToken.this.startPos, DefaultCompletionToken.this.cursorPosition - DefaultCompletionToken.this.startPos);
         if (!DefaultCompletionToken.this.value.startsWith(var1)) {
            String var2 = DefaultCompletionToken.this.value.substring(0, var1.length());
            this.document.replace(DefaultCompletionToken.this.startPos, var1.length(), var2, null);
         }
      }

      private void applyWhenCursorAtTheEnd() throws BadLocationException {
         this.insertRemainingPart();
      }

      private void applyWhenCursorWithinToken() throws BadLocationException {
         int var1 = this.document.getLength();
         char[] var2 = DefaultCompletionToken.this.remainingPart.toCharArray();
         int var3 = var2.length;
         int var4 = DefaultCompletionToken.this.cursorPosition;
         int var5 = 0;
         Segment var6 = new Segment();
         this.document.getText(0, var1, var6);

         while (var4 < var1 && var5 < var3 && var6.array[var4] == var2[var5]) {
            var4++;
            var5++;
         }

         if (var5 < var3) {
            String var7 = new String(var2, var5, var3 - var5);
            this.insertRemainingPart(var4, var7);
         }
      }

      private void insertRemainingPart() throws BadLocationException {
         this.insertRemainingPart(DefaultCompletionToken.this.cursorPosition, DefaultCompletionToken.this.remainingPart);
      }

      private void insertRemainingPart(int var1, String var2) throws BadLocationException {
         if (!DefaultCompletionToken.this.remainingPart.isEmpty()) {
            this.document.insertString(var1, var2, null);
         }
      }
   }
}
