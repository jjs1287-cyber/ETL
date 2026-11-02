package org.etlforge.ui.event;

import com.google.common.base.Predicate;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;

public final class Mouse {
   public static Predicate<MouseEvent> onTreeRow(JTree var0) {
      return new Mouse.OnTreeRow(var0);
   }

   public static Predicate<MouseEvent> onListRow(JList var0) {
      return new Mouse.OnListRow(var0);
   }

   public static Predicate<MouseEvent> onTableRow(JTable var0) {
      return new Mouse.OnTableRow(var0);
   }

   public static Predicate<MouseEvent> inTableColumn(KongaTable var0, TableColumnMetaData var1) {
      return new Mouse.InTableColumn(var0, var1);
   }

   private Mouse() {
   }

   private static class InTableColumn implements Predicate<MouseEvent> {
      private final KongaTable table;
      private final TableColumnMetaData column;

      public InTableColumn(KongaTable var1, TableColumnMetaData var2) {
         this.table = var1;
         this.column = var2;
      }

      public boolean apply(MouseEvent var1) {
         return this.table.isInColumn(var1.getPoint(), this.column);
      }

      @Override
      public String toString() {
         return "In table column \"" + this.column.getName() + "\"";
      }
   }

   private static class OnListRow implements Predicate<MouseEvent> {
      private final JList list;

      public OnListRow(JList var1) {
         this.list = var1;
      }

      public boolean apply(MouseEvent var1) {
         Point var2 = var1.getPoint();
         int var3 = this.list.locationToIndex(var2);
         if (var3 < 0) {
            return false;
         }

         Rectangle var4 = this.list.getCellBounds(var3, var3);
         return var4.contains(var2);
      }

      @Override
      public String toString() {
         return "On a JList row";
      }
   }

   private static class OnTableRow implements Predicate<MouseEvent> {
      private final JTable table;

      public OnTableRow(JTable var1) {
         this.table = var1;
      }

      public boolean apply(MouseEvent var1) {
         int var2 = this.table.rowAtPoint(var1.getPoint());
         return var2 != -1;
      }

      @Override
      public String toString() {
         return "On a JTable row";
      }
   }

   private static class OnTreeRow implements Predicate<MouseEvent> {
      private final JTree tree;

      public OnTreeRow(JTree var1) {
         this.tree = var1;
      }

      public boolean apply(MouseEvent var1) {
         int var2 = this.tree.getRowForLocation(var1.getX(), var1.getY());
         return var2 >= 0;
      }

      @Override
      public String toString() {
         return "On a JTree row";
      }
   }
}
