package org.etlforge.integration.dataelement;

import java.util.Stack;
import org.apache.commons.lang.StringUtils;
import org.etlforge.lang.KongaStringUtils;

public final class DataElementResolver {
   private DataElementContext context;
   private String input;
   private int delimiterPosition;
   private StringBuilder output;
   private Stack<DataElementResolver.DataElement> deStack;
   private boolean invalidSyntax;
   private StringBuilder backslashes;
   private final DataElementResolver.CharacterRule openingSquareBracket = new DataElementResolver.CharacterRule() {
      @Override
      public boolean isSpecialInCurrentPosition() {
         return true;
      }

      @Override
      public void takeAction() {
         DataElementResolver.DataElement var1 = new DataElementResolver.DataElement();
         DataElementResolver.this.deStack.push(var1);
      }
   };
   private final DataElementResolver.CharacterRule closingSquareBracket = new DataElementResolver.CharacterRule() {
      @Override
      public boolean isSpecialInCurrentPosition() {
         if (DataElementResolver.this.deStack.isEmpty()) {
            return false;
         }

         DataElementResolver.DataElement var1 = DataElementResolver.this.deStack.peek();
         return !var1.inDefaultValue;
      }

      @Override
      public void takeAction() {
         DataElementResolver.DataElement var1 = DataElementResolver.this.deStack.pop();
         this.closeDataElement(var1);
      }

      private void closeDataElement(DataElementResolver.DataElement var1) {
         String var2 = var1.resolve(DataElementResolver.this.context);
         DataElementResolver.this.append(var2);
      }
   };
   private final DataElementResolver.CharacterRule openingCurlyBracket = new DataElementResolver.CharacterRule() {
      @Override
      public boolean isSpecialInCurrentPosition() {
         if (DataElementResolver.this.deStack.isEmpty()) {
            return false;
         }

         DataElementResolver.DataElement var1 = DataElementResolver.this.deStack.peek();
         return !var1.inDefaultValue;
      }

      @Override
      public void takeAction() {
         this.startDefaultValue();
      }

      private void startDefaultValue() {
         DataElementResolver.DataElement var1 = DataElementResolver.this.deStack.peek();
         if (var1.hasName()) {
            var1.startDefaultValue();
         } else {
            DataElementResolver.this.invalidSyntax = true;
         }
      }
   };
   private final DataElementResolver.CharacterRule closingCurlyBracket = new DataElementResolver.CharacterRule() {
      @Override
      public boolean isSpecialInCurrentPosition() {
         if (DataElementResolver.this.deStack.isEmpty()) {
            return false;
         }

         DataElementResolver.DataElement var1 = DataElementResolver.this.deStack.peek();
         return var1.inDefaultValue;
      }

      @Override
      public void takeAction() {
         this.endDefaultValue();
      }

      private void endDefaultValue() {
         DataElementResolver.this.deStack.peek().endDefaultValue();
         if (!this.ensureNextCharacterIs(']', DataElementResolver.this.input, DataElementResolver.this.delimiterPosition)) {
            DataElementResolver.this.invalidSyntax = true;
         }
      }

      private boolean ensureNextCharacterIs(char var1, String var2, int var3) {
         if (var3 < var2.length() - 1) {
            char var4 = var2.charAt(var3 + 1);
            return var4 == var1;
         } else {
            return false;
         }
      }
   };

   public static String resolveDefaultValues(String var0) {
      DataElementResolver var1 = new DataElementResolver();
      return var1.resolve(var0);
   }

   public void setContext(DataElementContext var1) {
      this.context = var1;
   }

   public String resolve(String var1) {
      if (StringUtils.isBlank(var1)) {
         return var1;
      }

      this.init(var1);
      this.tokenize();
      return this.getResult();
   }

   private void init(String var1) {
      this.input = var1;
      this.delimiterPosition = 0;
      this.output = new StringBuilder();
      this.deStack = new Stack<>();
      this.backslashes = new StringBuilder();
      this.invalidSyntax = false;
   }

   private void tokenize() throws RuntimeException {
      int var1 = 0;

      while (!this.invalidSyntax && var1 < this.input.length()) {
         this.delimiterPosition = this.moveToNextPosition(this.input, var1);
         if (this.delimiterPosition == -1) {
            return;
         }

         char var2 = this.input.charAt(this.delimiterPosition);
         if (var2 == '\\') {
            this.backslashes.append("\\");
         } else {
            DataElementResolver.CharacterRule var3 = this.getRule(var2);
            this.handleSpecialCharacter(var2, var3);
         }

         var1 = this.delimiterPosition + 1;
      }
   }

   private int moveToNextPosition(String var1, int var2) {
      int var3 = KongaStringUtils.indexOfAny(var1, "[]{}\\", var2);
      if (var3 == -1) {
         this.appendAllBackslashes();
         this.append(var1.substring(var2));
      } else {
         if (this.isNewBackslashAfterOneOrMoreNonSpecialCharacters(var1, var2, var3)) {
            this.appendAllBackslashes();
         }

         this.append(var1.substring(var2, var3));
      }

      return var3;
   }

   private void appendAllBackslashes() {
      this.append(this.backslashes.toString());
      this.backslashes.setLength(0);
   }

   private boolean isNewBackslashAfterOneOrMoreNonSpecialCharacters(String var1, int var2, int var3) {
      char var4 = var1.charAt(var3);
      return var4 == '\\' && var3 > var2 && this.backslashes.length() > 0;
   }

   private DataElementResolver.CharacterRule getRule(char var1) {
      switch (var1) {
         case '[':
            return this.openingSquareBracket;
         case ']':
            return this.closingSquareBracket;
         case '{':
            return this.openingCurlyBracket;
         case '}':
            return this.closingCurlyBracket;
         default:
            throw new RuntimeException("There is no rule for the character " + var1);
      }
   }

   private void handleSpecialCharacter(char var1, DataElementResolver.CharacterRule var2) {
      if (var2.isSpecialInCurrentPosition()) {
         this.enforceRule(var1, var2);
      } else {
         this.append(this.backslashes.toString());
         this.append(String.valueOf(var1));
      }

      this.backslashes.setLength(0);
   }

   private void enforceRule(char var1, DataElementResolver.CharacterRule var2) {
      this.appendEscapedBackslashes();
      if (this.isSpecialCharacterEscaped()) {
         this.append(String.valueOf(var1));
      } else {
         var2.takeAction();
      }
   }

   private void appendEscapedBackslashes() {
      int var1 = this.backslashes.length() / 2;

      for (int var2 = 0; var2 < var1; var2++) {
         this.append("\\");
      }
   }

   private boolean isSpecialCharacterEscaped() {
      int var1 = this.backslashes.length();
      return var1 % 2 != 0;
   }

   private void append(String var1) {
      if (this.deStack.isEmpty()) {
         this.output.append(var1);
      } else {
         DataElementResolver.DataElement var2 = this.deStack.peek();
         var2.append(var1);
      }
   }

   private String getResult() {
      if (!this.deStack.isEmpty()) {
         this.invalidSyntax = true;
      }

      if (this.invalidSyntax) {
         return this.input;
      }

      this.output.append(this.backslashes);
      return this.output.toString();
   }

   private interface CharacterRule {
      boolean isSpecialInCurrentPosition();

      void takeAction();
   }

   private static class DataElement {
      private final StringBuilder name = new StringBuilder();
      private StringBuilder defaultValue;
      public boolean inDefaultValue;

      private DataElement() {
      }

      public boolean hasName() {
         return this.name.length() > 0;
      }

      public void startDefaultValue() {
         this.defaultValue = new StringBuilder();
         this.inDefaultValue = true;
      }

      public void endDefaultValue() {
         this.inDefaultValue = false;
      }

      public void append(String var1) {
         if (this.defaultValue != null) {
            this.defaultValue.append(var1);
         } else {
            this.name.append(var1);
         }
      }

      public String resolve(DataElementContext var1) {
         String var2 = this.lookupValueFromContext(var1);
         if (var2 == null) {
            if (this.defaultValue != null) {
               var2 = this.defaultValue.toString();
            } else {
               var2 = "[" + this.name + "]";
            }
         }

         return var2;
      }

      private String lookupValueFromContext(DataElementContext var1) {
         String var2 = null;
         if (var1 != null) {
            var2 = var1.getValue(this.name.toString());
         }

         return var2;
      }

      @Override
      public String toString() {
         String var1 = "[" + this.name;
         if (this.defaultValue != null) {
            var1 = var1 + "{" + this.defaultValue + "}";
         }

         return var1 + "]";
      }
   }
}
