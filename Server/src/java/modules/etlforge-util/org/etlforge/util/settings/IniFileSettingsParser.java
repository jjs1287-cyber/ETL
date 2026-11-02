package org.etlforge.util.settings;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.lang.KongaStringUtils;

public final class IniFileSettingsParser {
   private static final String DELIMITERS = "\"'=\\#";
   private static final char ESCAPE_CHAR = '\\';
   private static final String ESCAPE_CHAR_STR = "\\";
   private String currentSection = "";
   private LinkedHashSet<String> invalidLines;

   public IniFileSettingsHolder parseSettingsFile(File var1) throws IOException {
      String[] var2 = KongaIoUtils.readLines(var1);
      return this.parseSettings(var2);
   }

   public IniFileSettingsHolder parseSettings(String[] var1) {
      this.invalidLines = Sets.newLinkedHashSet();
      IniFileSettingsHolder var2 = new IniFileSettingsHolder();

      for (String var6 : var1) {
         this.handleLine(var6, var2);
      }

      return var2;
   }

   public Set<String> getInvalidLines() {
      return Sets.newLinkedHashSet(this.invalidLines);
   }

   public boolean hasInvalidLines() {
      return !this.invalidLines.isEmpty();
   }

   private void handleLine(String var1, IniFileSettingsHolder var2) {
      var1 = var1.trim();
      if (this.needsParsing(var1)) {
         IniFileSettingsParser.Tokenizer var3 = new IniFileSettingsParser.Tokenizer(this.currentSection, var1, var2);
         var3.process();
      }
   }

   private boolean needsParsing(String var1) {
      return this.hasData(var1) && !this.isNewSection(var1) && this.isKeyValueEntry(var1);
   }

   private boolean hasData(String var1) {
      return !var1.isEmpty() && !this.isComment(var1);
   }

   private boolean isComment(String var1) {
      return var1.charAt(0) == '#';
   }

   private boolean isNewSection(String var1) {
      if (var1.startsWith("[") && var1.endsWith("]")) {
         this.currentSection = KongaStringUtils.unwrap(var1);
         return true;
      } else {
         return false;
      }
   }

   private boolean isKeyValueEntry(String var1) {
      if (var1.indexOf(61) < 0) {
         System.err.println("Invalid entry: " + var1);
         this.invalidLines.add(var1);
         return false;
      } else {
         return true;
      }
   }

   private final class Tokenizer {
      private final IniFileSettingsHolder settings;
      private final String currentSection;
      private final String line;
      private String token;
      private String previousToken;
      private boolean quoted;
      private boolean dquoted;
      private boolean isName = true;
      private String name = "";
      private String value = "";
      private boolean stop;

      public Tokenizer(String var2, String var3, IniFileSettingsHolder var4) {
         this.currentSection = var2;
         this.line = var3;
         this.settings = var4;
      }

      public void process() {
         this.tokenizeLine();
         this.handleResult();
      }

      private void tokenizeLine() {
         StringTokenizer var1 = new StringTokenizer(this.line, "\"'=\\#", true);

         while (var1.hasMoreTokens() && !this.stop) {
            this.token = var1.nextToken();
            if (this.token.equals("\"")) {
               this.processDoubleQuote();
            } else if (this.token.equals("'")) {
               this.processSingleQuote();
            } else if (this.token.equals("=")) {
               this.processEqualSign();
            } else if (this.token.equals("\\")) {
               if (this.isEscapeChar(this.previousToken)) {
                  this.processNonDelimiter();
                  this.previousToken = null;
                  continue;
               }
            } else if (this.token.equals("#")) {
               this.handleHashMark();
            } else {
               this.processNonDelimiter();
            }

            this.previousToken = this.token;
         }
      }

      private boolean isValidEntry() {
         if (this.quoted || this.dquoted) {
            System.err.println("Invalid entry contains quotes that are not closed: " + this.line);
            return false;
         } else if (this.isName) {
            System.err.println("Invalid entry has no key value (it is probably commented out)" + this.line);
            return false;
         } else if (this.name.length() == 0) {
            System.err.println("Invalid entry has empty key value: " + this.line);
            return false;
         } else {
            return true;
         }
      }

      private void processDoubleQuote() {
         this.processNonDelimiter();
         if (!this.isEscapeChar(this.previousToken)) {
            this.dquoted = !this.dquoted;
         }
      }

      private void processSingleQuote() {
         this.processNonDelimiter();
         if (!this.isEscapeChar(this.previousToken)) {
            this.quoted = !this.quoted;
         }
      }

      private void processEqualSign() {
         if (!this.isEscapeChar(this.previousToken) && !this.quoted && !this.dquoted) {
            this.isName = false;
         } else {
            this.processNonDelimiter();
         }
      }

      private void handleHashMark() {
         if (!this.isEscapeChar(this.previousToken) && !this.quoted && !this.dquoted) {
            this.stop = true;
         } else {
            this.processNonDelimiter();
         }
      }

      private void processNonDelimiter() {
         if (this.isName) {
            this.name = this.name + this.token;
         } else {
            this.value = this.value + this.token;
         }
      }

      private void handleResult() {
         this.name = KongaStringUtils.unquote(this.name.trim());
         this.value = KongaStringUtils.unquote(this.value.trim());
         if (this.isValidEntry()) {
            this.settings.addValue(this.currentSection, this.name, this.value);
         } else {
            IniFileSettingsParser.this.invalidLines.add(this.line);
         }
      }

      private final boolean isEscapeChar(String var1) {
         return var1 != null && var1.length() == 1 && var1.charAt(0) == '\\';
      }
   }
}
