package org.etlforge.util;

import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.etlforge.io.KongaIoUtils;

public final class KongaSerializationUtils {
   public static void serializeToFile(Serializable var0, File var1) throws IOException {
      serializeToStream(var0, new FileOutputStream(var1));
   }

   public static void serializeToStream(Serializable var0, OutputStream var1) throws IOException {
      ObjectOutputStream var2 = var1 instanceof ObjectOutputStream ? (ObjectOutputStream)var1 : new ObjectOutputStream(var1);
      serializeToStreamImpl(var0, var2);
   }

   private static void serializeToStreamImpl(Serializable var0, ObjectOutputStream var1) throws IOException {
      boolean var2 = false;

      try {
         var1.writeObject(var0);
         var2 = true;
      } finally {
         Closeables.close(var1, !var2);
      }
   }

   public static void serializeToCompressedFile(Serializable var0, File var1) throws IOException {
      ObjectOutputStream var2 = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(var1)));
      serializeToStreamImpl(var0, var2);
   }

   public static <T> T deserializeFromFile(File var0, Class<T> var1) throws IOException, ClassNotFoundException {
      return deserializeFromStream(new FileInputStream(var0), var1);
   }

   public static <T> T deserializeFromStream(InputStream var0, Class<T> var1) throws IOException, ClassNotFoundException {
      ObjectInputStream var2 = var0 instanceof ObjectInputStream ? (ObjectInputStream)var0 : new ObjectInputStream(var0);
      return deserializeFromStreamImpl(var1, var2);
   }

   private static <T> T deserializeFromStreamImpl(Class<T> var0, ObjectInputStream var1) throws IOException, ClassNotFoundException {
      try {
         Object var2 = var1.readObject();
         return (T)var0.cast(var2);
      } finally {
         KongaIoUtils.close(var1);
      }
   }

   public static <T> T deserializeFromCompressedFile(File var0, Class<T> var1) throws IOException, ClassNotFoundException {
      return deserializeFromCompressedInput(Files.newInputStreamSupplier(var0), var1);
   }

   public static <T> T deserializeFromCompressedInput(InputSupplier<? extends InputStream> var0, Class<T> var1) throws IOException, ClassNotFoundException {
      InputStream var2 = (InputStream)var0.getInput();

      try {
         return deserializeFromStreamImpl(var1, new ObjectInputStream(new GZIPInputStream(var2)));
      } finally {
         Closeables.closeQuietly(var2);
      }
   }

   private KongaSerializationUtils() {
   }
}
