package org.etlforge.ui;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.Font;
import java.util.Map;

public final class UiResources {
   private static UiResources defaultInstance = new UiResources();
   private static Map<Object, UiResources> instances = Maps.newHashMap();
   private Map<Object, Color> colors = Maps.newHashMap();
   private Map<Object, Font> fonts = Maps.newHashMap();
   private Map<Object, Object> objects = Maps.newHashMap();

   public static UiResources getDefault() {
      return defaultInstance;
   }

   public static UiResources createInstance(Object var0) {
      UiResources var1 = new UiResources();
      instances.put(var0, var1);
      return var1;
   }

   public static UiResources getInstance(Object var0) {
      return instances.get(var0);
   }

   private UiResources() {
   }

   public Color putColor(Object var1, Color var2) {
      return this.colors.put(var1, var2);
   }

   public Color getColor(Object var1) {
      return this.colors.get(var1);
   }

   public Font putFont(Object var1, Font var2) {
      return this.fonts.put(var1, var2);
   }

   public Font getFont(Object var1) {
      return this.fonts.get(var1);
   }

   public Object putObject(Object var1, Object var2) {
      return this.objects.put(var1, var2);
   }

   public Object getObject(Object var1) {
      return this.objects.get(var1);
   }
}
