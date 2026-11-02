package org.etlforge.ui.text;

import com.google.common.base.Preconditions;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public class DefaultCharacterPairFinder implements CharacterPairFinder {
   private final char left;
   private final char right;

   public DefaultCharacterPairFinder(char var1, char var2) {
      Preconditions.checkArgument(var1 != 0, "left cannot be 0");
      Preconditions.checkArgument(var2 != 0, "right cannot be 0");
      this.left = var1;
      this.right = var2;
   }

   public boolean isValidOffset(Document var1, int var2) {
      return true;
   }

   @Override
   public Interval findMatch(Document var1, int var2) {
      int var3 = var1.getLength();
      if (var3 != 0 && var2 <= var3) {
         DefaultCharacterPairFinder.Anchor var4 = this.locateAnchor(var1, var2, var3);
         return var4 != null ? var4.findMatch(var1, var3) : null;
      } else {
         return null;
      }
   }

   private DefaultCharacterPairFinder.Anchor locateAnchor(Document var1, int var2, int var3) {
      DefaultCharacterPairFinder.Anchor var4 = null;
      if (var2 == 0) {
         var4 = this.locateAnchorAtZeroOffset(var1, var2);
      } else if (var2 == var3) {
         var4 = this.locateAnchorAtMaxOffset(var1, var2);
      } else {
         var4 = this.locateAnchorAtMiddleOffset(var1, var2);
      }

      return var4;
   }

   private DefaultCharacterPairFinder.Anchor locateAnchorAtZeroOffset(Document var1, int var2) {
      char var3 = getCharAt(var1, var2);
      return var3 == this.left ? DefaultCharacterPairFinder.Anchor.newStart(this, var2, this.left, this.right) : null;
   }

   private DefaultCharacterPairFinder.Anchor locateAnchorAtMaxOffset(Document var1, int var2) {
      char var3 = getCharAt(var1, var2 - 1);
      return var3 == this.right ? DefaultCharacterPairFinder.Anchor.newEnd(this, var2 - 1, this.right, this.left) : null;
   }

   private DefaultCharacterPairFinder.Anchor locateAnchorAtMiddleOffset(Document var1, int var2) {
      char var3 = getCharAt(var1, var2);
      if (var3 == this.left) {
         return DefaultCharacterPairFinder.Anchor.newStart(this, var2, this.left, this.right);
      }

      if (var3 == this.right) {
         return DefaultCharacterPairFinder.Anchor.newEnd(this, var2 - 1, this.right, this.left);
      }

      char var4 = getCharAt(var1, var2 - 1);
      return var4 == this.right ? DefaultCharacterPairFinder.Anchor.newEnd(this, var2 - 1, this.right, this.left) : null;
   }

   private static char getCharAt(Document var0, int var1) {
      try {
         String var2 = var0.getText(var1, 1);
         return var2.charAt(0);
      } catch (BadLocationException var3) {
         return '\u0000';
      }
   }

   private static String getText(Document var0, int var1, int var2) {
      try {
         return var0.getText(var1, var2 - var1);
      } catch (BadLocationException var4) {
         return null;
      }
   }

   private static final class Anchor {
      private final DefaultCharacterPairFinder finder;
      private final int offset;
      private final char character;
      private final char other;
      private final DefaultCharacterPairFinder.Direction direction;
      private int nested;

      private Anchor(DefaultCharacterPairFinder var1, int var2, char var3, char var4, DefaultCharacterPairFinder.Direction var5) {
         this.finder = var1;
         this.offset = var2;
         this.character = var3;
         this.other = var4;
         this.direction = var5;
      }

      public static DefaultCharacterPairFinder.Anchor newStart(DefaultCharacterPairFinder var0, int var1, char var2, char var3) {
         return new DefaultCharacterPairFinder.Anchor(var0, var1, var2, var3, DefaultCharacterPairFinder.Direction.FORWARD);
      }

      public static DefaultCharacterPairFinder.Anchor newEnd(DefaultCharacterPairFinder var0, int var1, char var2, char var3) {
         return new DefaultCharacterPairFinder.Anchor(var0, var1, var2, var3, DefaultCharacterPairFinder.Direction.BACKWARD);
      }

      public Interval findMatch(Document var1, int var2) {
         return this.direction == DefaultCharacterPairFinder.Direction.FORWARD ? this.findForwardMatch(var1, var2) : this.findBackwardMatch(var1);
      }

      private Interval findForwardMatch(Document var1, int var2) {
         int var3 = this.offset + 1;
         int var4 = Math.min(var3 + 50, var2);

         while (true) {
            String var5 = DefaultCharacterPairFinder.getText(var1, var3, var4);
            if (var5 == null || var5.length() == 0) {
               break;
            }

            int var6 = 0;

            for (int var7 = var5.length(); var6 < var7; var6++) {
               char var8 = var5.charAt(var6);
               Interval var9 = this.handleCharacter(var1, var8, var3, var6);
               if (var9 != null) {
                  return var9;
               }
            }

            var3 = var4;
            if (var3 >= var2) {
               break;
            }

            var4 = Math.min(var3 + 50, var2);
         }

         return null;
      }

      private Interval findBackwardMatch(Document var1) {
         int var2 = this.offset;
         int var3 = Math.max(0, var2 - 50);

         while (true) {
            String var4 = DefaultCharacterPairFinder.getText(var1, var3, var2);
            if (var4 == null || var4.length() == 0) {
               return null;
            }

            for (int var5 = var4.length() - 1; var5 >= 0; var5--) {
               char var6 = var4.charAt(var5);
               Interval var7 = this.handleCharacter(var1, var6, var3, var5);
               if (var7 != null) {
                  return var7;
               }
            }

            var2 = var3;
            if (var2 < 0) {
               return null;
            }

            var3 = Math.max(0, var2 - 50);
         }
      }

      private Interval handleCharacter(Document var1, char var2, int var3, int var4) {
         int var5 = var3 + var4;
         this.updateNested(var1, var5, var2);
         if (var2 == this.other && this.isValidOffset(var1, var5)) {
            if (!this.isNested()) {
               return this.createMatchingInterval(var5);
            }

            this.unnestOneLevel();
         }

         return null;
      }

      private Interval createMatchingInterval(int var1) {
         return this.direction == DefaultCharacterPairFinder.Direction.FORWARD ? this.startToEnd(var1) : this.endToStart(var1);
      }

      private Interval startToEnd(int var1) {
         int var2 = var1 - this.offset + 1;
         return IntervalFactory.fromStartAndLength(this.offset, var2);
      }

      private Interval endToStart(int var1) {
         int var2 = this.offset - var1 + 1;
         return IntervalFactory.fromStartAndLength(var1, var2);
      }

      private boolean isValidOffset(Document var1, int var2) {
         return this.finder.isValidOffset(var1, var2);
      }

      private void updateNested(Document var1, int var2, char var3) {
         if (this.character != this.other && var3 == this.character && this.isValidOffset(var1, var2)) {
            this.nested++;
         }
      }

      private boolean isNested() {
         return this.nested > 0;
      }

      private void unnestOneLevel() {
         this.nested--;
      }
   }

   private enum Direction {
      FORWARD,
      BACKWARD;
   }
}
