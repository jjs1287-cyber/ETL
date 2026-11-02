package org.etlforge.xml.sax;

import java.lang.reflect.Method;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

abstract class StartElementCallback extends KongaSaxParser.Callback {
   public static StartElementCallback create(Object var0, Method var1) {
      Class[] var2 = var1.getParameterTypes();
      switch (var2.length) {
         case 0:
            return new StartElementCallback.StartElementCallbackWithoutAttributes(var0, var1);
         case 1:
            if (var2[0].equals(Attributes.class)) {
               return new StartElementCallback.StartElementCallbackWithAttributes(var0, var1);
            } else if (var2[0].equals(StartOfElement.class)) {
               return new StartElementCallback.StartElementCallbackWithAllInfo(var0, var1);
            }
         default:
            throw new RuntimeException(
               "Invalid signature for StartElementHandler method "
                  + var1.getName()
                  + ". The method must either take no arguments, or a single argument of type "
                  + Attributes.class.getName()
                  + " or "
                  + StartElementCallback.StartElementCallbackWithAllInfo.class.getName()
                  + "."
            );
      }
   }

   public StartElementCallback(Object var1, Method var2) {
      super(var1, var2);
   }

   public abstract void call(String var1, String var2, String var3, Attributes var4) throws SAXException;

   private static class StartElementCallbackWithAllInfo extends StartElementCallback {
      public StartElementCallbackWithAllInfo(Object var1, Method var2) {
         super(var1, var2);
      }

      @Override
      public void call(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.invokeMethod(new StartOfElement(var1, var2, var3, var4));
      }
   }

   private static class StartElementCallbackWithAttributes extends StartElementCallback {
      public StartElementCallbackWithAttributes(Object var1, Method var2) {
         super(var1, var2);
      }

      @Override
      public void call(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.invokeMethod(var4);
      }
   }

   private static class StartElementCallbackWithoutAttributes extends StartElementCallback {
      public StartElementCallbackWithoutAttributes(Object var1, Method var2) {
         super(var1, var2);
      }

      @Override
      public void call(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.invokeMethod();
      }
   }
}
