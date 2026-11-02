package org.etlforge.ui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public final class GraphicsState {
   public static final GraphicsState.Property COLOR = new GraphicsState.Property() {
      @Override
      public Object get(Graphics2D var1) {
         return var1.getColor();
      }

      @Override
      public void set(Graphics2D var1, Object var2) {
         var1.setColor((Color)var2);
      }
   };
   public static final GraphicsState.Property RENDERING_HINTS = new GraphicsState.Property() {
      @Override
      public Object get(Graphics2D var1) {
         return var1.getRenderingHints();
      }

      @Override
      public void set(Graphics2D var1, Object var2) {
         var1.setRenderingHints((RenderingHints)var2);
      }
   };
   public static final GraphicsState.Property STROKE = new GraphicsState.Property() {
      @Override
      public Object get(Graphics2D var1) {
         return var1.getStroke();
      }

      @Override
      public void set(Graphics2D var1, Object var2) {
         var1.setStroke((Stroke)var2);
      }
   };
   public static final GraphicsState.Property COMPOSITE = new GraphicsState.Property() {
      @Override
      public Object get(Graphics2D var1) {
         return var1.getComposite();
      }

      @Override
      public void set(Graphics2D var1, Object var2) {
         var1.setComposite((Composite)var2);
      }
   };
   public static final GraphicsState.Property FONT = new GraphicsState.Property() {
      @Override
      public Object get(Graphics2D var1) {
         return var1.getFont();
      }

      @Override
      public void set(Graphics2D var1, Object var2) {
         var1.setFont((Font)var2);
      }
   };
   private final LinkedHashMap<GraphicsState.Property, Object> values = Maps.newLinkedHashMap();
   private final Graphics2D g2;

   public GraphicsState(Graphics var1, GraphicsState.Property... var2) {
      this((Graphics2D)var1, var2);
   }

   public GraphicsState(Graphics2D var1, GraphicsState.Property... var2) {
      this.g2 = var1;

      for (GraphicsState.Property var6 : var2) {
         this.values.put(var6, var6.get(var1));
      }
   }

   public void restore() {
      ArrayList var1 = Lists.newArrayList(this.values.entrySet());
      Collections.reverse(var1);

      for (Object __etlItem92 : var1) {
         Entry var3 = (Entry) __etlItem92;
         GraphicsState.Property var4 = (GraphicsState.Property)var3.getKey();
         Object var5 = var3.getValue();
         var4.set(this.g2, var5);
      }
   }

   public interface Property {
      Object get(Graphics2D var1);

      void set(Graphics2D var1, Object var2);
   }
}
