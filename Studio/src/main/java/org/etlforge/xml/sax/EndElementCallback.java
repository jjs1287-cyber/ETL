package org.etlforge.xml.sax;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.etlforge.xml.KongaXmlUtils;
import org.xml.sax.SAXException;

abstract class EndElementCallback extends KongaSaxParser.Callback {
   public static EndElementCallback create(Object var0, Method var1) {
      Class[] var2 = var1.getParameterTypes();
      switch (var2.length) {
         case 0:
            return new EndElementCallback.EndElementCallbackWithoutValue(var0, var1);
         case 1:
            Class var3 = var2[0];
            if (var3.equals(String.class)) {
               return new EndElementCallback.EndElementCallbackWithValue<>(var0, var1, Functions.identity());
            } else if (var3.equals(Boolean.class)) {
               return new EndElementCallback.EndElementCallbackWithValue<>(var0, var1, new Function<String, Boolean>() {
                  public Boolean apply(@Nullable String var1) {
                     return var1 == null ? null : KongaXmlUtils.isTrue(var1);
                  }
               });
            } else if (var3.equals(Integer.class)) {
               return new EndElementCallback.EndElementCallbackWithValue<>(var0, var1, new Function<String, Integer>() {
                  public Integer apply(@Nullable String var1) {
                     return var1 == null ? null : Integer.valueOf(var1);
                  }
               });
            } else if (var3.equals(Long.class)) {
               return new EndElementCallback.EndElementCallbackWithValue<>(var0, var1, new Function<String, Long>() {
                  public Long apply(@Nullable String var1) {
                     return var1 == null ? null : Long.valueOf(var1);
                  }
               });
            } else if (var3.equals(Double.class)) {
               return new EndElementCallback.EndElementCallbackWithValue<>(var0, var1, new Function<String, Double>() {
                  public Double apply(@Nullable String var1) {
                     return var1 == null ? null : Double.valueOf(var1);
                  }
               });
            } else if (var3.equals(EndOfElement.class)) {
               return new EndElementCallback.EndElementCallbackWithAllInfo(var0, var1);
            }
         default:
            throw new RuntimeException(
               "Invalid signature for EndElementHandler method "
                  + var1.getName()
                  + ". The method must either take no arguments, or a single argument of type java.lang.String, java.lang.Boolean, java.lang.Integer, java.lang.Long, java.lang.Double, or"
                  + EndOfElement.class.getName()
                  + "."
            );
      }
   }

   public EndElementCallback(Object var1, Method var2) {
      super(var1, var2);
   }

   public abstract void call(String var1, String var2, String var3, String var4) throws SAXException;

   private static class EndElementCallbackWithAllInfo extends EndElementCallback {
      public EndElementCallbackWithAllInfo(Object var1, Method var2) {
         super(var1, var2);
      }

      @Override
      public void call(String var1, String var2, String var3, String var4) throws SAXException {
         this.invokeMethod(new EndOfElement(var1, var2, var3, var4));
      }
   }

   private static class EndElementCallbackWithValue<T> extends EndElementCallback {
      private final Function<String, T> valueTransform;

      public EndElementCallbackWithValue(Object var1, Method var2, Function<String, T> var3) {
         super(var1, var2);
         this.valueTransform = var3;
      }

      @Override
      public void call(String var1, String var2, String var3, String var4) throws SAXException {
         Object var5 = this.valueTransform.apply(var4);
         this.invokeMethod(var5);
      }
   }

   private static class EndElementCallbackWithoutValue extends EndElementCallback {
      public EndElementCallbackWithoutValue(Object var1, Method var2) {
         super(var1, var2);
      }

      @Override
      public void call(String var1, String var2, String var3, String var4) throws SAXException {
         this.invokeMethod();
      }
   }
}
