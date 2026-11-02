package org.etlforge.application.ui.groovy;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;

final class IndentLevelCalculator {
   private final Segment segment = new Segment();
   private int pos;
   private int end;

   public int getIndentAtPosition(Document var1, int var2) {
      try {
         ((AbstractDocument)var1).getText(0, var2, this.segment);
         return this.countLevels();
      } catch (BadLocationException var4) {
         return -1;
      }
   }

   private int countLevels() {
      int var1 = 0;
      this.pos = this.segment.offset;

      for (this.end = this.segment.getEndIndex(); this.pos < this.end; this.pos++) {
         char var2 = this.segment.array[this.pos];
         switch (var2) {
            case '"':
            case '\'':
               this.consumeText(var2);
               break;
            case '/':
               if (this.pos != this.end - 1) {
                  this.consumePossibleComment();
               }
               break;
            case '{':
               var1++;
               break;
            case '}':
               var1--;
         }
      }

      return var1;
   }

   private void consumePossibleComment() {
      this.pos++;
      switch (this.segment.array[this.pos]) {
         case '*':
            this.pos++;
            this.moveToEndOfBlockComment();
            break;
         case '/':
            this.moveToEndOfLine();
      }
   }

   private void moveToEndOfLine() {
      while (this.pos < this.end && this.segment.array[this.pos] != '\n') {
         this.pos++;
      }
   }

   private void moveToEndOfBlockComment() {
      while (this.pos < this.end - 1) {
         if (this.segment.array[this.pos] == '*' && this.segment.array[this.pos + 1] == '/') {
            this.pos++;
            break;
         }

         this.pos++;
      }
   }

   private void consumeText(char var1) {
      this.pos++;
      boolean var2 = false;

      while (this.pos < this.end - 1) {
         char var3 = this.segment.array[this.pos];
         if (var3 == '\\') {
            var2 = !var2;
         } else {
            if (var3 == var1 && !var2) {
               return;
            }

            var2 = false;
         }

         this.pos++;
      }
   }
}
