package org.etlforge.ui.widget.text;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

public final class IntegerRangeFilter extends DocumentFilter {
   private final Range<Integer> range;
   private boolean allowEmptyValues = true;

   public IntegerRangeFilter(Range<Integer> var1) {
      this.range = Preconditions.checkNotNull(var1, "range");
   }

   public void setAllowEmptyValues(boolean var1) {
      this.allowEmptyValues = var1;
   }

   @Override
   public void insertString(FilterBypass var1, int var2, String var3, AttributeSet var4) throws BadLocationException {
      if (this.isInsertAllowed(var1, var2, var3)) {
         var1.insertString(var2, var3, var4);
      }
   }

   private boolean isInsertAllowed(FilterBypass var1, int var2, String var3) throws BadLocationException {
      Document var4 = var1.getDocument();
      String var5 = var4.getText(0, var4.getLength());
      String var6 = var5.isEmpty() ? var3 : var5.substring(0, var2) + var3 + var5.substring(var2);
      return this.isAllowed(var6);
   }

   private boolean isAllowed(String var1) {
      try {
         Integer var2 = Integer.parseInt(var1);
         return this.range.contains(var2);
      } catch (NumberFormatException var4) {
         return false;
      }
   }

   @Override
   public void remove(FilterBypass var1, int var2, int var3) throws BadLocationException {
      if (this.isRemoveAllowed(var1, var2, var3)) {
         var1.remove(var2, var3);
      }
   }

   private boolean isRemoveAllowed(FilterBypass var1, int var2, int var3) throws BadLocationException {
      Document var4 = var1.getDocument();
      int var5 = var4.getLength();
      String var6 = var4.getText(0, var5);
      String var7 = var6.substring(0, var2);
      String var8;
      if (var2 + var3 < var5) {
         var8 = var7 + var6.substring(var2 + var3);
      } else {
         var8 = var7;
      }

      return var8.isEmpty() ? this.allowEmptyValues : this.isAllowed(var8);
   }

   @Override
   public void replace(FilterBypass var1, int var2, int var3, String var4, AttributeSet var5) throws BadLocationException {
      if (this.isReplaceAllowed(var1, var2, var3, var4, var5)) {
         var1.replace(var2, var3, var4, var5);
      }
   }

   private boolean isReplaceAllowed(FilterBypass var1, int var2, int var3, String var4, AttributeSet var5) throws BadLocationException {
      Document var6 = var1.getDocument();
      int var7 = var6.getLength();
      String var8 = var6.getText(0, var7);
      String var9;
      if (var2 == var7) {
         var9 = var8 + var4;
      } else {
         var9 = var8.substring(0, var2) + var4 + var8.substring(var2 + var3);
      }

      return var9.isEmpty() ? this.allowEmptyValues : this.isAllowed(var9);
   }
}
