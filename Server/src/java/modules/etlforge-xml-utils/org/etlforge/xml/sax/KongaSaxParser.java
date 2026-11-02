package org.etlforge.xml.sax;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.io.InputSupplier;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Stack;
import org.etlforge.xml.KongaXmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class KongaSaxParser {
   private final KongaSaxParser.Callbacks<StartElementCallback> startElementCallbacks = KongaSaxParser.Callbacks.create();
   private final KongaSaxParser.Callbacks<EndElementCallback> endElementCallbacks = KongaSaxParser.Callbacks.create();

   public KongaSaxParser(Object... var1) {
      for (Object var5 : var1) {
         this.registerHandler(var5);
      }
   }

   public void parseString(String var1) throws SAXException {
      try {
         SaxParsers.parseString(var1, new KongaSaxParser.Handler(), true);
      } catch (StopParsingException var3) {
      }
   }

   public void parseResource(String var1) throws SAXException, IOException {
      try {
         SaxParsers.parseResource(var1, new KongaSaxParser.Handler(), true);
      } catch (StopParsingException var3) {
      }
   }

   public void parseFile(File var1) throws SAXException, IOException {
      try {
         SaxParsers.parseFile(var1, new KongaSaxParser.Handler(), true);
      } catch (StopParsingException var3) {
      }
   }

   public void parseInput(InputSupplier<? extends InputStream> var1) throws SAXException, IOException {
      try {
         SaxParsers.parseInput(var1, new KongaSaxParser.Handler(), true);
      } catch (StopParsingException var3) {
      }
   }

   public void registerHandler(Object var1) {
      String var2 = Strings.nullToEmpty(getRelativePathRoot(var1));
      if (!var2.isEmpty() && !var2.endsWith("/")) {
         var2 = var2 + "/";
      }

      this.locateElementHandlers(var1, var2);
   }

   private static String getRelativePathRoot(Object var0) {
      Class var1 = var0.getClass();
      if (var1.isAnnotationPresent(RelativePathRoot.class)) {
         RelativePathRoot var3 = var0.getClass().getAnnotation(RelativePathRoot.class);
         return var3.value();
      }

      String var2 = lookForRelativePathRootMethod(var0, var1);
      if (var2 == null) {
         var2 = lookForRelativePathRootField(var0, var1);
      }

      return var2;
   }

   private static String lookForRelativePathRootMethod(Object var0, Class<? extends Object> var1) {
      for (Method var5 : var1.getDeclaredMethods()) {
         if (var5.isAnnotationPresent(RelativePathRoot.class)) {
            if (var5.getReturnType().equals(String.class) && var5.getParameterTypes().length == 0) {
               try {
                  if (!var5.isAccessible()) {
                     var5.setAccessible(true);
                  }

                  return (String)var5.invoke(var0);
               } catch (Exception var7) {
                  throw new RuntimeException(var7);
               }
            }

            throw new RuntimeException(
               "Invalid RelativePathRoot method: " + var5.getName() + ". The method cannot take input arguments, and must return a java.lang.String."
            );
         }
      }

      return null;
   }

   private static String lookForRelativePathRootField(Object var0, Class<? extends Object> var1) {
      for (Field var5 : var1.getFields()) {
         if (var5.isAnnotationPresent(RelativePathRoot.class)) {
            Class var6 = var5.getType();
            if (var6.equals(String.class)) {
               if (!var5.isAccessible()) {
                  var5.setAccessible(true);
               }

               try {
                  return (String)var5.get(var0);
               } catch (Exception var8) {
                  throw new RuntimeException(var8);
               }
            }

            throw new RuntimeException("Invalid RelativePathRoot field: " + var5.getName() + ". Only fields of type java.lang.String are allowed.");
         }
      }

      return null;
   }

   private void locateElementHandlers(Object var1, String var2) {
      for (Method var6 : var1.getClass().getMethods()) {
         if (var6.isAnnotationPresent(StartElementHandler.class)) {
            this.registerStartElementCallback(var1, var2, var6);
         } else if (var6.isAnnotationPresent(EndElementHandler.class)) {
            this.registerEndElementCallback(var1, var2, var6);
         }
      }
   }

   private void registerStartElementCallback(Object var1, String var2, Method var3) {
      StartElementHandler var4 = var3.getAnnotation(StartElementHandler.class);
      String var5 = var4.value();
      if (Strings.isNullOrEmpty(var5)) {
         var5 = var3.getName();
      } else if (var5.equals("/")) {
         var5 = "";
      }

      String var6 = var2 + var5;
      if (var6.endsWith("/")) {
         var6 = var6.substring(0, var6.length() - 1);
      }

      StartElementCallback var7 = StartElementCallback.create(var1, var3);
      this.startElementCallbacks.add(var6, var7);
   }

   private void registerEndElementCallback(Object var1, String var2, Method var3) {
      EndElementHandler var4 = var3.getAnnotation(EndElementHandler.class);
      String var5 = var4.value();
      if (Strings.isNullOrEmpty(var5)) {
         var5 = var3.getName();
      } else if (var5.equals("/")) {
         var5 = "";
      }

      String var6 = var2 + var5;
      if (var6.endsWith("/")) {
         var6 = var6.substring(0, var6.length() - 1);
      }

      EndElementCallback var7 = EndElementCallback.create(var1, var3);
      this.endElementCallbacks.add(var6, var7);
   }

   abstract static class Callback {
      private final Object object;
      private final Method method;

      public Callback(Object var1, Method var2) {
         this.object = var1;
         this.method = var2;
         if (!var2.isAccessible()) {
            var2.setAccessible(true);
         }
      }

      protected final void invokeMethod(Object... var1) throws SAXException {
         try {
            this.method.invoke(this.object, var1);
         } catch (IllegalArgumentException var4) {
            throw new SAXException(var4);
         } catch (IllegalAccessException var5) {
            throw new SAXException(var5);
         } catch (InvocationTargetException var6) {
            Throwable var3 = var6.getCause();
            if (var3 instanceof Exception && !(var3 instanceof StopParsingException)) {
               throw new SAXException((Exception)var3);
            }

            Throwables.propagate(var3);
         }
      }
   }

   private static class Callbacks<T> {
      private final Map<String, T> specific = Maps.newHashMap();
      private final KongaSaxParser.Wildcards<T> wildcards = new KongaSaxParser.Wildcards<>();

      public static <E> KongaSaxParser.Callbacks<E> create() {
         return new KongaSaxParser.Callbacks();
      }

      public void add(String var1, T var2) {
         if (this.isWildcard(var1)) {
            this.wildcards.add(new PathWildcard(var1), (T)var2);
         } else {
            this.specific.put(var1, (T)var2);
         }
      }

      private boolean isWildcard(String var1) {
         return var1.endsWith("*");
      }

      public T get(String var1) {
         Object var2 = this.specific.get(var1);
         if (var2 == null) {
            var2 = this.wildcards.findMatchingHandler(var1);
         }

         return (T)var2;
      }
   }

   private class Handler extends DefaultHandler {
      private final Stack<String> stack = new Stack<>();
      private StringBuilder elementValue;
      private boolean nil;

      private Handler() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         String var5 = var4.getValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
         this.nil = KongaXmlUtils.isTrue(var5);
         this.elementValue = new StringBuilder();
         String var6 = this.pushPath(var2);
         StartElementCallback var7 = KongaSaxParser.this.startElementCallbacks.get(var6);
         if (var7 == null) {
            var7 = KongaSaxParser.this.startElementCallbacks.get(var2);
         }

         if (var7 != null) {
            var7.call(var1, var2, var3, var4);
         }
      }

      private String pushPath(String var1) {
         String var2;
         if (this.stack.isEmpty()) {
            var2 = var1;
         } else {
            String var3 = this.stack.peek();
            var2 = var3 + "/" + var1;
         }

         this.stack.push(var2);
         return var2;
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         String var4 = this.stack.pop();
         EndElementCallback var5 = KongaSaxParser.this.endElementCallbacks.get(var4);
         if (var5 == null) {
            var5 = KongaSaxParser.this.endElementCallbacks.get(var2);
         }

         if (var5 != null) {
            String var6 = this.nil ? null : this.elementValue.toString();
            var5.call(var1, var2, var3, var6);
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         String var4 = new String(var1, var2, var3);
         this.elementValue.append(var4);
      }
   }

   private static class Wildcards<T> {
      private final Map<PathWildcard, T> handlers = Maps.newHashMap();
      private ImmutableList<PathWildcard> order = ImmutableList.of();

      private Wildcards() {
      }

      public void add(PathWildcard var1, T var2) {
         this.handlers.put(var1, (T)var2);
         this.order = PathWildcard.ORDER_OF_SPECIFICITY.immutableSortedCopy(this.handlers.keySet());
      }

      public T findMatchingHandler(String var1) {
         for (PathWildcard var3 : this.order) {
            if (var3.matches(var1)) {
               return this.handlers.get(var3);
            }
         }

         return null;
      }
   }
}
