package org.etlforge.integration.data.structure.text;

import com.google.common.base.Optional;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.util.persist.Persistor;

public final class FieldPersistor {
   private static final String NAME = "name";
   private static final String TYPE = "type";
   private static final String DEFAULT = "default";
   private static final String FORMAT = "format";
   private static final String BEGIN_POS = "beginPos";
   private static final String LENGTH = "length";
   private static final String ID = "fieldId";
   private static final String REQUIRED = "required";
   private static final String FIX_WIDTH = "fixWidth";

   public static void save(Field var0, Persistor var1) {
      var1.putString("name", var0.getName());
      var1.putString("type", var0.getDataType().name());
      var1.putString("default", var0.getDefaultValue());
      var1.putString("format", var0.getFormat());
      var1.putInteger("fieldId", var0.getId());
      var1.putInteger("required", var0.getIsRequired());
      var1.putBoolean("fixWidth", var0.isFixedWidth());
      if (var0.isFixedWidth()) {
         var1.putInteger("beginPos", var0.getBeginPosition());
         var1.putInteger("length", var0.getLength());
      }

      Optional var2 = var0.getValidation();
      if (var2.isPresent()) {
         Persistor var3 = var1.createChild("Validation");
         ((FieldValidation)var2.get()).writeTo(var3);
      }
   }

   public static Field restoreFrom(Persistor var0) {
      String var1 = var0.getString("name");
      String var2 = var0.getString("type");
      String var3 = var0.getString("default");
      String var4 = var0.getString("format");
      int var5 = var0.getInteger("fieldId");
      int var6 = var0.getInteger("required");
      boolean var7 = var0.getBoolean("fixWidth");
      Field var8 = new Field(var5, var1, var2, var3, var4, var6, var7);
      if (var7) {
         var8.setBeginPosition(var0.getInteger("beginPos"));
         var8.setLength(var0.getInteger("length"));
      }

      Persistor var9 = var0.getFirstChild("Validation");
      if (var9 != null) {
         var8.setValidation(Optional.of(FieldValidation.readFrom(var9)));
      }

      return var8;
   }

   private FieldPersistor() {
   }
}
