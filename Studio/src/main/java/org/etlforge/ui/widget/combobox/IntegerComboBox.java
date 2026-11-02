package org.etlforge.ui.widget.combobox;

import com.google.common.base.Preconditions;
import javax.swing.DefaultComboBoxModel;

public class IntegerComboBox extends KongaComboBox {
   public IntegerComboBox(int var1, int var2) {
      super(getValues(var1, var2));
   }

   public IntegerComboBox(int[] var1) {
      super(getValues(var1));
   }

   private static Integer[] getValues(int var0, int var1) {
      Preconditions.checkArgument(var0 <= var1, "first must be <= last");
      int var2 = var1 - var0 + 1;
      Integer[] var3 = new Integer[var2];
      int var4 = 0;

      for (int var5 = var0; var4 < var2; var5++) {
         var3[var4] = var5;
         var4++;
      }

      return var3;
   }

   private static Integer[] getValues(int[] var0) {
      int var1 = var0.length;
      Integer[] var2 = new Integer[var0.length];

      for (int var3 = 0; var3 < var1; var3++) {
         var2[var3] = var0[var3];
      }

      return var2;
   }

   public Integer getSelectedItem() {
      return (Integer)super.getSelectedItem();
   }

   @Override
   public void addItem(Object var1) {
      checkObject(var1);
      super.addItem(var1);
   }

   @Override
   public void insertItemAt(Object var1, int var2) {
      checkObject(var1);
      super.insertItemAt(var1, var2);
   }

   private static void checkObject(Object var0) {
      Preconditions.checkArgument(var0 instanceof Integer, "anObject must be an Integer");
   }

   public void setNewValues(int var1, int var2) {
      this.setModel(new DefaultComboBoxModel<>(getValues(var1, var2)));
   }

   public void setNewValues(int[] var1) {
      this.setModel(new DefaultComboBoxModel<>(getValues(var1)));
   }
}
