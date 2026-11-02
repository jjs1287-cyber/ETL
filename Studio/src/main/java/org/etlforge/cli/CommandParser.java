package org.etlforge.cli;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.etlforge.lang.KongaStringUtils;

final class CommandParser {
   private static final String VARARG_MARKER = "...";
   private final String syntax;
   private final List<CommandParser.Part> parts;

   public CommandParser(String var1) throws InvalidSyntaxException {
      this.syntax = var1;
      this.checkEllipsisPlacement(var1);
      this.parts = this.tokenize(var1);
      this.verifyAllOptionalArgumentsComeLast();
   }

   private void checkEllipsisPlacement(String var1) {
      int var2 = var1.indexOf("...");
      if (var2 >= 0 && var2 != var1.length() - "...".length()) {
         throw new InvalidSyntaxException("Invalid location for vararg marker ...");
      }
   }

   private List<CommandParser.Part> tokenize(String var1) {
      CommandParser.Tokenizer var2 = new CommandParser.Tokenizer();
      return var2.tokenize(var1);
   }

   private void verifyAllOptionalArgumentsComeLast() throws InvalidSyntaxException {
      boolean var1 = false;

      for (CommandParser.Part var3 : this.parts) {
         if (var3.isRequired()) {
            if (var1) {
               throw new InvalidSyntaxException("All optional arguments must come last.");
            }
         } else {
            var1 = true;
         }
      }
   }

   public String[] parse(String var1) {
      CommandParser.ArgumentExtractor var2 = new CommandParser.ArgumentExtractor(var1);
      return var2.parse();
   }

   public String complete(String var1) {
      String var2 = this.getLiteralStartOfSyntax();
      return StringUtils.startsWithIgnoreCase(var2, var1) && var2.length() > var1.length() ? StringUtils.removeStartIgnoreCase(var2, var1) : null;
   }

   private String getLiteralStartOfSyntax() {
      StringBuilder var1 = new StringBuilder();

      for (CommandParser.Part var3 : this.parts) {
         if (!(var3 instanceof CommandParser.Literal)) {
            break;
         }

         if (var1.length() > 0) {
            var1.append(" ");
         }

         var1.append(((CommandParser.Literal)var3).text);
      }

      return var1.toString();
   }

   @Override
   public String toString() {
      return this.syntax;
   }

   private static final class Argument implements CommandParser.Part {
      private final boolean optional;
      public boolean vararg;
      private int numberOfEscapeChars;

      public Argument(boolean var1) {
         this.optional = var1;
      }

      @Override
      public boolean isRequired() {
         return !this.optional;
      }

      @Override
      public boolean isRepeated() {
         return this.vararg;
      }

      @Override
      public int numberOfEscapeCharacters() {
         return this.numberOfEscapeChars;
      }

      @Override
      public String consume(String var1, int var2) {
         this.numberOfEscapeChars = 0;
         var2 = this.moveToFirstNonWhiteSpace(var1, var2);
         if (var2 >= var1.length()) {
            return null;
         }

         char var3 = var1.charAt(var2);
         if (var3 == '"') {
            return "\"" + this.consumeAllUntilClosingQuote(var1, var2 + 1);
         }

         StringBuilder var4 = new StringBuilder();

         for (int var5 = var2; var5 < var1.length(); var5++) {
            char var6 = var1.charAt(var5);
            if (Character.isWhitespace(var6)) {
               break;
            }

            var4.append(var6);
         }

         return var4.toString();
      }

      private int moveToFirstNonWhiteSpace(String var1, int var2) {
         while (Character.isWhitespace(var1.charAt(var2))) {
            var2++;
         }

         return var2;
      }

      private String consumeAllUntilClosingQuote(String var1, int var2) {
         StringBuilder var3 = new StringBuilder();
         String var4 = var1.substring(var2);
         StringTokenizer var5 = new StringTokenizer(var4, "\"\\", true);
         boolean var6 = false;

         while (var5.hasMoreElements()) {
            String var7 = var5.nextToken();
            switch (var7.charAt(0)) {
               case '"':
                  var3.append("\"");
                  if (!var6) {
                     return var3.toString();
                  }

                  this.numberOfEscapeChars++;
                  var6 = false;
                  break;
               case '\\':
                  if (var6) {
                     var6 = false;
                     var3.append("\\");
                     this.numberOfEscapeChars++;
                  } else {
                     var6 = true;
                  }
                  break;
               default:
                  var6 = false;
                  var3.append(var7);
            }
         }

         return null;
      }
   }

   private class ArgumentExtractor {
      private final String expression;
      private final List<String> args = Lists.newArrayList();
      private int currentPos;
      private CommandParser.Part lastPart;
      private boolean isPotentialMatch;
      private boolean reachedEndOfExpression;

      public ArgumentExtractor(String var2) {
         this.expression = var2;
         this.isPotentialMatch = true;
      }

      public String[] parse() {
         for (CommandParser.Part var2 : CommandParser.this.new PartOrder()) {
            this.processPart(var2);
            if (!this.isPotentialMatch) {
               return null;
            }

            if (this.reachedEndOfExpression) {
               break;
            }
         }

         this.doesAllRequiredArgsHaveValue();
         return this.reachedEndOfExpression && this.isPotentialMatch ? this.args.toArray(new String[this.args.size()]) : null;
      }

      private void processPart(CommandParser.Part var1) {
         this.lastPart = var1;
         String var2 = var1.consume(this.expression, this.currentPos);
         if (var2 == null) {
            this.partDidNotMatch(var1);
         } else {
            this.moveCursorForward(var2);
         }

         if (var2 != null && var1 instanceof CommandParser.Argument) {
            this.addArgumentValue(var2);
         }

         this.checkIfWeHaveReachedTheEnd();
      }

      private void partDidNotMatch(CommandParser.Part var1) {
         if (var1.isRequired()) {
            this.isPotentialMatch = false;
         }
      }

      private void moveCursorForward(String var1) {
         this.currentPos = this.currentPos + var1.length() + this.lastPart.numberOfEscapeCharacters();
         this.consumeWhitespace();
      }

      private void consumeWhitespace() {
         while (this.currentPos < this.expression.length() && Character.isWhitespace(this.expression.charAt(this.currentPos))) {
            this.currentPos++;
         }
      }

      private void addArgumentValue(String var1) {
         this.args.add(KongaStringUtils.removeStartAndEndIfBothPresent(var1, "\""));
      }

      private void checkIfWeHaveReachedTheEnd() {
         if (this.currentPos >= this.expression.length()) {
            this.reachedEndOfExpression = true;
         }
      }

      private void doesAllRequiredArgsHaveValue() {
         ListIterator var1 = CommandParser.this.parts.listIterator(CommandParser.this.parts.size());

         while (var1.hasPrevious() && this.isPotentialMatch) {
            CommandParser.Part var2 = (CommandParser.Part)var1.previous();
            if (var2 == this.lastPart) {
               return;
            }

            this.partDidNotMatch(var2);
         }
      }
   }

   private static final class Literal implements CommandParser.Part {
      public final String text;

      public Literal(String var1) {
         this.text = var1;
      }

      @Override
      public boolean isRequired() {
         return true;
      }

      @Override
      public boolean isRepeated() {
         return false;
      }

      @Override
      public int numberOfEscapeCharacters() {
         return 0;
      }

      @Override
      public String consume(String var1, int var2) {
         int var3 = var2 + this.text.length();
         return var1.length() < var3
               || !var1.substring(var2, var3).equalsIgnoreCase(this.text)
               || !this.isEndOfExpression(var1, var3) && !this.nextCharIsWhitespace(var1, var3)
            ? null
            : this.text;
      }

      private boolean isEndOfExpression(String var1, int var2) {
         return var1.length() <= var2;
      }

      private boolean nextCharIsWhitespace(String var1, int var2) {
         return Character.isWhitespace(var1.charAt(var2));
      }

      @Override
      public String toString() {
         return this.text;
      }
   }

   private interface Part {
      boolean isRequired();

      boolean isRepeated();

      int numberOfEscapeCharacters();

      String consume(String var1, int var2);
   }

   private class PartOrder implements Iterable<CommandParser.Part> {
      private PartOrder() {
      }

      @Override
      public Iterator<CommandParser.Part> iterator() {
         final Iterator var1 = CommandParser.this.parts.iterator();
         return new Iterator<CommandParser.Part>() {
            private CommandParser.Part last;

            @Override
            public boolean hasNext() {
               return var1.hasNext() || this.last != null && this.last.isRepeated();
            }

            public CommandParser.Part next() {
               if (this.last != null && this.last.isRepeated()) {
                  return this.last;
               }

               this.last = (CommandParser.Part)var1.next();
               return this.last;
            }

            @Override
            public void remove() {
               throw new UnsupportedOperationException();
            }
         };
      }
   }

   private static class Tokenizer {
      private List<CommandParser.Part> parts;
      private boolean insideArg;
      private boolean nextArgOptional;
      private char lastToken;
      private final StringBuilder literal = new StringBuilder();

      private Tokenizer() {
      }

      public List<CommandParser.Part> tokenize(String var1) throws InvalidSyntaxException {
         this.parts = Lists.newArrayList();
         this.consumeTokens(var1);
         this.finishLastLiteral();
         this.checkEndStateIsValid();
         this.checkIfLastArgIsVararg(var1);
         return this.parts;
      }

      private void consumeTokens(String var1) {
         StringTokenizer var2 = new StringTokenizer(var1, "[]{}", true);

         while (var2.hasMoreElements()) {
            String var3 = var2.nextToken();
            switch (var3.charAt(0)) {
               case '[':
                  this.handleStartOptional();
                  break;
               case ']':
                  this.handleEndOptional();
                  break;
               case '{':
                  this.handleStartArg();
                  break;
               case '}':
                  this.handleEndArg();
                  break;
               default:
                  this.handleLiteral(var3);
            }

            this.lastToken = var3.charAt(0);
         }
      }

      private void handleStartOptional() {
         if (this.insideArg) {
            throw new InvalidSyntaxException("Invalid placement of optional opening marker [");
         }

         this.nextArgOptional = true;
      }

      private void handleEndOptional() {
         if (this.lastToken != '}') {
            throw new InvalidSyntaxException("Invalid placement of optional closing marker ]");
         }

         this.nextArgOptional = false;
      }

      private void handleStartArg() {
         if (this.insideArg) {
            throw new InvalidSyntaxException("Nested opening argument marker {");
         }

         if (this.literal.length() > 0) {
            this.parts.add(new CommandParser.Literal(this.literal.toString()));
            this.literal.setLength(0);
         }

         this.insideArg = true;
      }

      private void handleEndArg() {
         if (!this.insideArg) {
            throw new InvalidSyntaxException("Invalid placement of closing argument marker }");
         }

         this.insideArg = false;
         this.parts.add(new CommandParser.Argument(this.nextArgOptional));
      }

      private void handleLiteral(String var1) {
         if (!this.insideArg && !var1.equals("...")) {
            this.literal.append(var1.trim());
         }
      }

      private void finishLastLiteral() {
         if (this.literal.length() > 0) {
            this.parts.add(new CommandParser.Literal(this.literal.toString()));
         }
      }

      private void checkEndStateIsValid() {
         if (this.insideArg) {
            throw new InvalidSyntaxException("Missing closing argument marker }");
         }

         if (this.parts.isEmpty()) {
            throw new InvalidSyntaxException();
         }
      }

      private void checkIfLastArgIsVararg(String var1) {
         if (var1.endsWith("...")) {
            CommandParser.Part var2 = this.parts.get(this.parts.size() - 1);
            if (var2 instanceof CommandParser.Argument) {
               ((CommandParser.Argument)var2).vararg = true;
            }
         }
      }
   }
}
