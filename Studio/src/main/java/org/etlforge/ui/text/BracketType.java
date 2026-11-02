package org.etlforge.ui.text;

public enum BracketType {
   PARENTHESES('(', ')'),
   SQUARE('[', ']'),
   CURLY('{', '}'),
   ANGLE('<', '>');

   private final char opening;
   private final char closing;

   BracketType(char var3, char var4) {
      this.opening = var3;
      this.closing = var4;
   }

   public char getOpening() {
      return this.opening;
   }

   public char getClosing() {
      return this.closing;
   }
}
