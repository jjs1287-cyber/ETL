package org.etlforge.integration.data.project.path;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

final class ProjectPathTokenizer implements Serializable {
   public static final long serialVersionUID = 1L;
   private static final char SEPARATOR = '/';
   private static final char ESCAPE = '\\';
   private static final char STAR = '*';
   private static final char PLUS = '+';
   private List<String> tokens;
   private String token;
   private boolean lastTokenWasEscape;
   private boolean lastTokenWasSeparator;
   private boolean previousTokenWasAnyLevelWildcard;

   public List<String> tokenize(String var1) {
      this.reset();
      this.process(var1);
      this.checkRemainingParticle();
      this.ensureLastParticleIsNotAnyLevelWildcard();
      return this.tokens;
   }

   private void reset() {
      this.tokens = Lists.newArrayList();
      this.lastTokenWasEscape = false;
      this.token = "";
      this.lastTokenWasSeparator = false;
   }

   private void process(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1, "/*+\\", true);

      while (var2.hasMoreTokens()) {
         String var3 = var2.nextToken();
         switch (var3.charAt(0)) {
            case '*':
               this.handleWildcard('*', "\u0001*");
               break;
            case '+':
               this.handleWildcard('+', "\u0001+");
               break;
            case '/':
               this.handleSeparator();
               break;
            case '\\':
               this.handleEscape();
               break;
            default:
               this.handleToken(var3);
         }
      }
   }

   private void handleSeparator() {
      if (this.lastTokenWasSeparator) {
         if (this.previousTokenWasAnyLevelWildcard) {
            throw new IllegalArgumentException("Invalid path construct: \"///\"");
         }

         this.tokens.add("\u0002");
         this.previousTokenWasAnyLevelWildcard = true;
      } else if (this.lastTokenWasEscape) {
         this.token = this.token + '/';
         this.lastTokenWasEscape = false;
         this.previousTokenWasAnyLevelWildcard = false;
      } else {
         this.tokens.add(this.token);
         this.token = "";
         this.lastTokenWasSeparator = true;
         this.previousTokenWasAnyLevelWildcard = false;
      }
   }

   private void handleWildcard(char var1, String var2) {
      if (this.lastTokenWasEscape) {
         this.token = this.token + var1;
         this.lastTokenWasEscape = false;
      } else {
         this.token = this.token + var2;
      }

      this.lastTokenWasSeparator = false;
      this.previousTokenWasAnyLevelWildcard = false;
   }

   private void handleEscape() {
      if (this.lastTokenWasEscape) {
         this.token = this.token + '\\';
         this.lastTokenWasEscape = false;
      } else {
         this.lastTokenWasEscape = true;
      }

      this.lastTokenWasSeparator = false;
      this.previousTokenWasAnyLevelWildcard = false;
   }

   private void handleToken(String var1) {
      if (this.lastTokenWasEscape) {
         throw new IllegalArgumentException("Incomplete escape sequence");
      }

      this.token = this.token + var1;
      this.lastTokenWasSeparator = false;
      this.previousTokenWasAnyLevelWildcard = false;
   }

   private void checkRemainingParticle() {
      if (this.lastTokenWasEscape) {
         throw new IllegalArgumentException("Trailing escape character");
      }

      if (this.lastTokenWasSeparator && !this.previousTokenWasAnyLevelWildcard || !this.token.isEmpty()) {
         this.tokens.add(this.token);
      }
   }

   private void ensureLastParticleIsNotAnyLevelWildcard() {
      if (this.isLastParticleAnyLevelWildcard()) {
         throw new IllegalArgumentException("The path cannot end with \"//\"");
      }
   }

   private boolean isLastParticleAnyLevelWildcard() {
      return !this.tokens.isEmpty() && this.tokens.get(this.tokens.size() - 1).equals("\u0002");
   }
}
