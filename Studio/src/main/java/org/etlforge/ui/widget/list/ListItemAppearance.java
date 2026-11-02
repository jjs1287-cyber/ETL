package org.etlforge.ui.widget.list;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.Component;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public final class ListItemAppearance {
   private Function<Object, String> textFunction = new ListItemAppearance.DefaultTextFunction();
   private Function<Object, String> tooltipFunction = Functions.constant(null);
   private final ListItemAppearance.IconFunction iconFunction = new ListItemAppearance.IconFunction();

   public ListItemAppearance withTextFunction(Function<Object, String> var1) {
      this.textFunction = Preconditions.checkNotNull(var1);
      return this;
   }

   public ListItemAppearance withToolTipFunction(Function<Object, String> var1) {
      this.tooltipFunction = Preconditions.checkNotNull(var1);
      return this;
   }

   public ListItemAppearance withIcon(Class<?> var1, Icon var2) {
      this.iconFunction.addMapping(var1, var2);
      return this;
   }

   public String getDisplayValue(Object var1) {
      return this.textFunction.apply(var1);
   }

   public void configure(KongaListFactory var1) {
      var1.setNextMatchFunction(this.textFunction);
      var1.setCellRenderer(this.asCellRenderer());
   }

   public ListCellRenderer asCellRenderer() {
      return new ListItemAppearance.Renderer();
   }

   private static class DefaultTextFunction implements Function<Object, String> {
      private DefaultTextFunction() {
      }

      public String apply(Object var1) {
         return var1 == null ? "" : var1.toString();
      }
   }

   private static class IconFunction implements Function<Object, Icon> {
      private final Map<Class, Icon> iconMap = Maps.newHashMap();
      private Icon nullIcon;

      private IconFunction() {
      }

      public void addMapping(Class var1, Icon var2) {
         if (var1 == null) {
            this.nullIcon = var2;
         } else {
            this.iconMap.put(var1, var2);
         }
      }

      public Icon apply(Object var1) {
         return var1 == null ? this.nullIcon : this.iconMap.get(var1.getClass());
      }
   }

   private class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         String var6 = ListItemAppearance.this.textFunction.apply(var2);
         if (var6 != null) {
            this.setText(var6);
         }

         String var7 = ListItemAppearance.this.tooltipFunction.apply(var2);
         this.setToolTipText(var7);
         Icon var8 = ListItemAppearance.this.iconFunction.apply(var2);
         this.setIcon(var8);
         return this;
      }
   }
}
