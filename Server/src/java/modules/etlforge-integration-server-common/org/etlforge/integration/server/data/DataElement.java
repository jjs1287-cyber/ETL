package org.etlforge.integration.server.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.etlforge.io.KongaIoUtils;

public class DataElement {
   private DataElement.Type type = DataElement.Type.UNKNOWN;
   private String value = null;

   public static DataElement serializedJavaObject(Serializable var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      ObjectOutputStream var2 = null;

      try {
         var2 = new ObjectOutputStream(var1);
         var2.writeObject(var0);
         var2.flush();
         byte[] var3 = Base64.encodeBase64Chunked(var1.toByteArray());
         DataElement var4 = new DataElement(new String(var3));
         var4.type = DataElement.Type.JAVA_OBJECT;
         return var4;
      } catch (UnsupportedEncodingException var9) {
         var9.printStackTrace();
         throw new RuntimeException("Failed to get Base64 encoded bytes as a UTF-8 encoded string. Is UTF-8 not supported?", var9);
      } finally {
         if (var2 != null) {
            KongaIoUtils.close(var2);
            Object var11 = null;
         }
      }
   }

   public DataElement() {
      this.type = DataElement.Type.UNKNOWN;
      this.value = "";
   }

   public DataElement(DataElement.Type var1, String var2) throws NumberFormatException {
      this.set(var1, var2);
   }

   public DataElement(Object var1) {
      this.set(var1);
   }

   public DataElement(int var1) {
      this.set(var1);
   }

   public DataElement(long var1) {
      this.set(var1);
   }

   public DataElement(float var1) {
      this.set(var1);
   }

   public DataElement(double var1) {
      this.set(var1);
   }

   public DataElement(String var1) {
      this.set(var1);
   }

   public DataElement(boolean var1) {
      this.set(var1);
   }

   public DataElement.Type getType() {
      return this.type;
   }

   @Override
   public String toString() {
      return this.value;
   }

   public void set(int var1) {
      this.type = DataElement.Type.INT;
      this.value = Integer.toString(var1);
   }

   public void set(long var1) {
      this.type = DataElement.Type.LONG;
      this.value = Long.toString(var1);
   }

   public void set(String var1) {
      this.type = DataElement.Type.STRING;
      this.value = var1;
   }

   public void set(float var1) {
      this.type = DataElement.Type.FLOAT;
      this.value = Float.toString(var1);
   }

   public void set(double var1) {
      this.type = DataElement.Type.DOUBLE;
      this.value = Double.toString(var1);
   }

   public void set(boolean var1) {
      this.type = DataElement.Type.BOOL;
      this.value = var1 ? "1" : "0";
   }

   public void setBit(boolean var1) {
      this.type = DataElement.Type.BIT;
      this.value = var1 ? "1" : "0";
   }

   public void set(Object var1) {
      if (var1 == null) {
         this.type = DataElement.Type.UNKNOWN;
         this.value = "";
      } else if (var1 instanceof Integer) {
         this.set(((Integer)var1).intValue());
      } else if (var1 instanceof Long) {
         this.set(((Long)var1).longValue());
      } else if (var1 instanceof Double) {
         this.set(((Double)var1).doubleValue());
      } else if (var1 instanceof Float) {
         this.set(((Float)var1).floatValue());
      } else if (var1 instanceof Boolean) {
         this.set(((Boolean)var1).booleanValue());
      } else if (var1 instanceof String) {
         this.set((String)var1);
      } else {
         this.type = DataElement.Type.UNKNOWN;
         this.value = "";
         System.err.println("Object of unknown type '" + var1.getClass() + "' passed to DataElement.set()");
      }
   }

   public Object getValue() {
      switch (this.type) {
         case INT:
            return Integer.valueOf(this.value);
         case LONG:
            return Long.valueOf(this.value);
         case FLOAT:
            return new Float(this.value);
         case DOUBLE:
            return new Double(this.value);
         case STRING:
            return this.value;
         case BOOL:
         case BIT:
            if (this.value.equals("1")) {
               return Boolean.TRUE;
            }

            return Boolean.FALSE;
         case JAVA_OBJECT:
            try {
               return this.deserializeJavaObject();
            } catch (ClassNotFoundException var2) {
               var2.printStackTrace();
               return null;
            } catch (IOException var3) {
               var3.printStackTrace();
               return null;
            }
         default:
            return null;
      }
   }

   public int toInt() {
      return Integer.parseInt(this.value);
   }

   public long toLong() {
      return Long.parseLong(this.value);
   }

   public double toDouble() {
      return Double.parseDouble(this.value);
   }

   private Object deserializeJavaObject() throws ClassNotFoundException, IOException {
      if (this.type != DataElement.Type.JAVA_OBJECT) {
         throw new IllegalStateException("Internal Error: deserializeJavaObject() can only be called on a DataElement of type JAVA_OBJECT!");
      }

      byte[] var1 = Base64.decodeBase64(this.value.getBytes());
      ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
      ObjectInputStream var3 = new ObjectInputStream(var2);
      return var3.readObject();
   }

   public void set(DataElement.Type var1, String var2) throws NumberFormatException {
      switch (var1) {
         case INT:
            Integer.parseInt(var2);
            this.value = var2;
            break;
         case LONG:
            Long.parseLong(var2);
            this.value = var2;
            break;
         case FLOAT:
            Float.parseFloat(var2);
            this.value = var2;
            break;
         case DOUBLE:
            Double.parseDouble(var2);
            this.value = var2;
            break;
         case STRING:
            this.value = var2;
            break;
         case BOOL:
            if (var2.equalsIgnoreCase("true") || var2.equals("1")) {
               this.value = "1";
            } else if (!var2.equalsIgnoreCase("false") && !var2.equals("0")) {
               this.value = "0";
            } else {
               this.value = "0";
            }
            break;
         case BIT:
            if (var2.equals("0")) {
               this.value = "0";
            } else if (var2.equals("1")) {
               this.value = "1";
            } else {
               this.value = "0";
            }
            break;
         case JAVA_OBJECT:
            this.value = var2;
            break;
         case DATE:
            this.value = var2;
            break;
         case TIMESPAN:
            this.value = var2;
            break;
         case COLLECTION:
            this.value = var2;
            break;
         default:
            this.value = var2;
      }

      this.type = var1;
   }

   public enum Type {
      UNKNOWN(-1, "unknown"),
      INT(0, "int"),
      LONG(1, "long"),
      FLOAT(2, "float"),
      DOUBLE(3, "double"),
      DATE(4, "date"),
      STRING(5, "string"),
      BOOL(6, "bool"),
      TIMESPAN(7, "timespan"),
      BIT(8, "bit"),
      COLLECTION(9, "collection"),
      JAVA_OBJECT(10, "javaobject");

      private final int id;
      private final String name;

      public static final DataElement.Type fromString(String var0) {
         if (var0.equalsIgnoreCase(INT.toString())) {
            return INT;
         } else if (var0.equalsIgnoreCase(LONG.toString())) {
            return LONG;
         } else if (var0.equalsIgnoreCase(FLOAT.toString())) {
            return FLOAT;
         } else if (var0.equalsIgnoreCase(DOUBLE.toString())) {
            return DOUBLE;
         } else if (var0.equalsIgnoreCase(DATE.toString())) {
            return DATE;
         } else if (var0.equalsIgnoreCase(STRING.toString())) {
            return STRING;
         } else if (var0.equalsIgnoreCase(BOOL.toString())) {
            return BOOL;
         } else if (var0.equalsIgnoreCase(TIMESPAN.toString())) {
            return TIMESPAN;
         } else if (var0.equalsIgnoreCase(BIT.toString())) {
            return BIT;
         } else if (var0.equalsIgnoreCase(COLLECTION.toString())) {
            return COLLECTION;
         } else {
            return var0.equalsIgnoreCase(JAVA_OBJECT.toString()) ? JAVA_OBJECT : UNKNOWN;
         }
      }

      Type(int var3, String var4) {
         this.id = var3;
         this.name = var4;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public int getId() {
         return this.id;
      }
   }
}
