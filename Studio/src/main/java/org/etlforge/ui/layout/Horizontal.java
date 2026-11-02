package org.etlforge.ui.layout;

public final class Horizontal {
   public static BoxBuilder box() {
      return BoxBuilder.horizontal();
   }

   public static Grid grid() {
      return Grid.oneRow();
   }

   public static Grid grid(int var0) {
      return Grid.oneRow(var0);
   }

   private Horizontal() {
   }

   public static final class Align {
      public static BorderLayoutBuilder east(Object var0) {
         return new BorderLayoutBuilder().east(var0);
      }

      public static BorderLayoutBuilder west(Object var0) {
         return new BorderLayoutBuilder().west(var0);
      }

      public static BoxBuilder center(Object var0) {
         return Horizontal.box().glue().add(var0).glue();
      }

      private Align() {
      }
   }
}
