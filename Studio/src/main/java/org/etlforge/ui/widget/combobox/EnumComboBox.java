package org.etlforge.ui.widget.combobox;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Component;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;

public class EnumComboBox<T extends Enum<T>> extends KongaComboBox {
   private Map<T, String> displayNames;
   private Map<T, Icon> icons;

   public static <E extends Enum<E>> EnumComboBox<E> of(E var0, E var1, E... var2) {
      return of(Lists.asList((E)var0, (E)var1, (E[])var2));
   }

   public static <E extends Enum<E>> EnumComboBox<E> of(Iterable<E> var0) {
      return of(EnumSet.copyOf(Lists.newArrayList(var0)));
   }

   public static <E extends Enum<E>> EnumComboBox<E> of(E[] var0) {
      return of(EnumSet.copyOf((Collection<E>)Arrays.asList(var0)));
   }

   public static <E extends Enum<E>> EnumComboBox<E> of(Class<E> var0) {
      return new EnumComboBox<>(var0);
   }

   public static <E extends Enum<E>> EnumComboBox<E> of(EnumSet<E> var0) {
      return new EnumComboBox<>(var0);
   }

   public EnumComboBox(Class<T> var1) {
      super(var1.getEnumConstants());
      this.init();
   }

   public EnumComboBox(Set<T> var1) {
      super(var1.toArray());
      this.init();
   }

   private void init() {
      this.displayNames = Maps.newHashMap();
      this.icons = Maps.newHashMap();
      this.setRenderer(new EnumComboBox.Renderer());
   }

   public final void adjustPrototypeValue() {
      Object var1 = this.createPrototype();
      this.setPrototypeDisplayValue(var1);
   }

   private Object createPrototype() {
      StringBuilder var1 = new StringBuilder();

      for (int var2 = 0; var2 < this.getItemCount(); var2++) {
         Object var3 = this.getItemAt(var2);
         String var4 = this.displayNames.get(var3);
         if (var4 == null) {
            var4 = var3.toString();
         }

         for (int var5 = var4.length(); var5 > var1.length(); var5--) {
            var1.append("W");
         }
      }

      return var1.toString();
   }

   public T getSelectedItem() {
      return (T)super.getSelectedItem();
   }

   public void setDisplayStrings(Map<T, String> var1) {
      Preconditions.checkNotNull(var1, "displayStrings");
      this.displayNames = Maps.newHashMap(var1);
   }

   public void generateDisplayStrings(Function<? super T, String> var1) {
      int var2 = 0;

      for (int var3 = this.getItemCount(); var2 < var3; var2++) {
         T var4 = (T)this.getItemAt(var2);
         String var5 = (String)var1.apply(var4);
         this.displayNames.put((T)var4, var5);
      }
   }

   public void setIcons(Map<T, Icon> var1) {
      Preconditions.checkNotNull(var1, "icons");
      this.icons = Maps.newHashMap(var1);
   }

   private class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         String var6 = EnumComboBox.this.displayNames.get(var2);
         if (var6 != null) {
            this.setText(var6);
         }

         Icon var7 = EnumComboBox.this.icons.get(var2);
         if (var7 != null) {
            this.setIcon(var7);
         }

         this.setEnabled(EnumComboBox.this.isEnabled());
         return this;
      }
   }
}
