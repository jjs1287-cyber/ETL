package org.etlforge.util.exception;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationUtils;
import org.etlforge.lang.KongaClassUtils;
import org.etlforge.util.persist.Persistor;

public final class ExceptionPersistor {
   private static final String BYTES = "Bytes";
   private static final String EXCEPTION_CLASS = "Class";
   private static final String MESSAGE = "Message";
   private static final String STACK_TRACE_ELEMENT = "StackTraceElement";
   private static final String STACKTRACE_CLASSNAME = "Class";
   private static final String METHOD = "Method";
   private static final String FILENAME = "FileName";
   private static final String LINENUMBER = "LineNumber";

   public static Optional<Exception> restore(Persistor var0) {
      ExceptionPersistor.Restorer var1 = new ExceptionPersistor.Restorer();
      return var1.restore(var0);
   }

   public static void persist(Exception var0, Persistor var1) {
      try {
         var1.putString("Bytes", serializeAndBase64Encode(var0));
      } catch (Exception var3) {
         var3.printStackTrace();
         var1.putString("Class", var0.getClass().getName());
         persistMessage(var0, var1);
         persistStackTrace(var0, var1);
      }
   }

   private static void persistMessage(Exception var0, Persistor var1) {
      String var2 = var0.getMessage();
      if (var2 == null) {
         var2 = "";
      }

      var1.putString("Message", var2);
   }

   private static void persistStackTrace(Exception var0, Persistor var1) {
      for (StackTraceElement var5 : var0.getStackTrace()) {
         persistStackTraceElement(var5, var1);
      }
   }

   private static void persistStackTraceElement(StackTraceElement var0, Persistor var1) {
      Persistor var2 = var1.createChild("StackTraceElement");
      var2.putString("Class", var0.getClassName());
      var2.putString("Method", var0.getMethodName());
      var2.putString("FileName", var0.getFileName());
      var2.putInteger("LineNumber", var0.getLineNumber());
   }

   private static String serializeAndBase64Encode(Exception var0) throws Exception {
      byte[] var1 = SerializationUtils.serialize(var0);
      return new String(Base64.encodeBase64(var1), Charsets.UTF_8);
   }

   private ExceptionPersistor() {
   }

   private static class Restorer {
      private Restorer() {
      }

      public Optional<Exception> restore(Persistor var1) {
         String var2 = var1.getString("Bytes");
         if (var2 != null) {
            try {
               return Optional.of(this.decodeAndDeserialize(var2));
            } catch (Exception var5) {
               var5.printStackTrace();
               return Optional.of(new Exception("[Failed to restore the logged exception: " + var5.getMessage(), var5));
            }
         } else {
            Optional var3 = this.restoreExceptionWithoutStackTrace(var1);
            if (var3.isPresent()) {
               StackTraceElement[] var4 = this.restoreStackTrace(var1);
               ((Exception)var3.get()).setStackTrace(var4);
            }

            return var3;
         }
      }

      private Optional<Exception> restoreExceptionWithoutStackTrace(Persistor var1) {
         String var2 = var1.getString("Class");
         if (Strings.isNullOrEmpty(var2)) {
            return Optional.absent();
         }

         String var3 = var1.getString("Message");

         try {
            Class var4 = Class.forName(var2);
            Constructor var5 = KongaClassUtils.getSingleArgumentConstructor(var4, String.class);
            if (var5 != null) {
               return Optional.of((Exception)var5.newInstance(var3));
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         return Optional.of(new Exception(var3 + " [" + var2 + "]"));
      }

      private Exception decodeAndDeserialize(String var1) throws Exception {
         byte[] var2 = Base64.decodeBase64(var1.getBytes(Charsets.UTF_8));
         return (Exception)SerializationUtils.deserialize(var2);
      }

      private StackTraceElement[] restoreStackTrace(Persistor var1) {
         ArrayList var2 = new ArrayList();

         for (Persistor var4 : var1.getChildren("StackTraceElement")) {
            StackTraceElement var5 = new StackTraceElement(
               var4.getString("Class"), var4.getString("Method"), var4.getString("FileName"), var4.getInteger("LineNumber")
            );
            var2.add(var5);
         }

         return (StackTraceElement[]) var2.toArray(new StackTraceElement[var2.size()]);
      }
   }
}
