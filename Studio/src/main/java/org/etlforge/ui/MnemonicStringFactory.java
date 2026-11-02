package org.etlforge.ui;

public abstract class MnemonicStringFactory {
   public static final MnemonicStringFactory NO_MNEMONIC_FACTORY = new MnemonicStringFactory.NoMnemonicFactory();

   public abstract MnemonicString fromString(String var1);

   public abstract String asText(String var1);

   public abstract String escape(String var1);

   public static MnemonicStringFactory newCharMarkerFactory(char var0) {
      return new MnemonicStringFactory.CharMarkerFactory(var0);
   }

   private static final class CharMarkerFactory extends MnemonicStringFactory {
      private final char marker;

      public CharMarkerFactory(char var1) {
         this.marker = var1;
      }

      @Override
      public MnemonicString fromString(String var1) {
         if (var1.length() < 2 || var1.indexOf(this.marker) == -1) {
            return new MnemonicStringFactory.MnemonicStringImpl(var1, var1, '\u0000', -1);
         }

         if (var1.charAt(0) == this.marker) {
            return new MnemonicStringFactory.MnemonicStringImpl(var1, var1.substring(1), var1.charAt(1), 0);
         }

         StringBuilder var2 = new StringBuilder(var1.length());
         char[] var3 = var1.toCharArray();
         Character var4 = null;
         int var5 = -1;
         int var6 = 0;

         for (int var7 = var3.length; var6 < var7; var6++) {
            char var8 = var3[var6];
            if (var8 == this.marker) {
               if (var6 == var7 - 1) {
                  var2.append(var8);
               } else {
                  char var9 = var3[var6 + 1];
                  if (var9 != this.marker) {
                     var4 = var9;
                     var5 = var2.length();
                     var2.append(var1.substring(var6 + 1));
                     break;
                  }

                  var2.append(var9);
                  var6++;
               }
            } else {
               var2.append(var8);
            }
         }

         return new MnemonicStringFactory.MnemonicStringImpl(var1, var2.toString(), var4 == null ? '\u0000' : var4, var5);
      }

      @Override
      public String escape(String var1) {
         StringBuilder var2 = new StringBuilder();
         boolean var3 = false;
         int var4 = 0;

         for (int var5 = var1.length(); var4 < var5; var4++) {
            char var6 = var1.charAt(var4);
            if (var6 == this.marker) {
               var3 = !var3;
            } else if (var3) {
               var2.append(this.marker);
               var3 = false;
            }

            var2.append(var6);
         }

         return var2.toString();
      }

      @Override
      public String asText(String var1) {
         return this.fromString(var1).getText();
      }
   }

   private static final class MnemonicStringImpl implements MnemonicString {
      private final String raw;
      private final String text;
      private final char mnemonic;
      private final int index;

      public MnemonicStringImpl(String var1, String var2, char var3, int var4) {
         this.raw = var1;
         this.text = var2;
         this.mnemonic = var3;
         this.index = var4;
      }

      @Override
      public String getRaw() {
         return this.raw;
      }

      @Override
      public int getDisplayedMnemonicIndex() {
         return this.index;
      }

      @Override
      public char getMnemonic() {
         return this.mnemonic;
      }

      @Override
      public String getText() {
         return this.text;
      }

      @Override
      public String toString() {
         return "[Text = " + this.text + "] [mnemonic = " + this.mnemonic + "] [index = " + this.index + "]";
      }
   }

   private static final class NoMnemonicFactory extends MnemonicStringFactory {
      private NoMnemonicFactory() {
      }

      @Override
      public MnemonicString fromString(final String var1) {
         return new MnemonicString() {
            @Override
            public String getRaw() {
               return var1;
            }

            @Override
            public int getDisplayedMnemonicIndex() {
               return -1;
            }

            @Override
            public char getMnemonic() {
               return '\u0000';
            }

            @Override
            public String getText() {
               return var1;
            }
         };
      }

      @Override
      public String asText(String var1) {
         return this.fromString(var1).getText();
      }

      @Override
      public String escape(String var1) {
         return var1;
      }
   }
}
