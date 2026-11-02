package org.etlforge.integration.data.structure.text;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.Serializable;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.system.annotations.InternalMethod;

public final class Field implements Serializable {
   public static String[] Variable_TypeTab = new String[]{
      "Unknown", "String", "Integer", "Date", "Long", "Float", "Double", "Boolean", "Segment", "Identifier"
   };
   public String fieldName;
   public String dataType;
   public String defaultValue;
   public String format;
   private int isRequired;
   private int beginPos;
   private int endPos;
   private int length;
   private int fieldId;
   private boolean isFixedWidth;
   private Optional<FieldValidation> validation = Optional.absent();

   public static Field newDelimitedIdentifier(String var0) {
      return newDelimitedIdentifier("ID", var0);
   }

   public static Field newDelimitedIdentifier(String var0, String var1) {
      return new Field(-1, var0, FieldDataType.Identifier.name(), var1, var1, 1, false);
   }

   public Field(String var1, boolean var2) {
      String[] var3 = KongaStringUtils.split(var1);
      this.fieldId = Integer.parseInt(var3[1]);
      this.fieldName = var3[3];
      int var4 = KongaStringUtils.parseInt(var3[5], 0);
      this.dataType = Variable_TypeTab[var4];
      if (var2) {
         this.length = 1;
         this.setBeginPosition(KongaStringUtils.parseInt(var3[6], 0));
         this.setEndPosition(KongaStringUtils.parseInt(var3[7], 1));
      } else {
         this.beginPos = -1;
         this.length = -1;
         this.endPos = -1;
      }

      this.isRequired = KongaStringUtils.parseInt(var3[8], 1);
      this.defaultValue = var3[9];
      this.format = var3[10];
      this.isFixedWidth = var2;
   }

   public Field(int var1, String var2, String var3, String var4, String var5, int var6, int var7, int var8, Field var9, boolean var10) {
      if (var10) {
         this.fieldId = var1;
         this.fieldName = var2;
         this.dataType = var3;
         this.defaultValue = var4;
         this.format = var5;
         this.isRequired = var6;
         if (var7 > 0) {
            this.beginPos = var7;
         } else if (var9 == null) {
            this.beginPos = 1;
         } else {
            this.beginPos = var9.endPos + 1;
         }

         this.length = var8;
         this.endPos = this.beginPos + this.length - 1;
      } else {
         this.beginPos = -1;
         this.length = -1;
         this.endPos = -1;
         this.fieldId = var1;
         this.fieldName = var2;
         this.dataType = var3;
         this.defaultValue = var4;
         this.format = var5;
         this.isRequired = var6;
      }

      this.isFixedWidth = var10;
   }

   public Field(int var1, String var2, String var3, String var4, String var5, int var6, int var7, int var8, Field var9) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, true);
   }

   public Field(int var1, String var2, String var3, String var4, String var5, int var6, boolean var7) {
      this.fieldId = var1;
      this.fieldName = var2;
      this.dataType = var3;
      this.defaultValue = var4;
      this.format = var5;
      this.isRequired = var6;
      if (var7) {
         this.beginPos = 0;
         this.length = 1;
         this.endPos = 1;
      } else {
         this.beginPos = -1;
         this.length = -1;
         this.endPos = -1;
      }

      this.isFixedWidth = var7;
   }

   public Field(Field var1) {
      this.fieldName = var1.fieldName;
      this.dataType = var1.dataType;
      this.defaultValue = var1.defaultValue;
      this.format = var1.format;
      this.isRequired = var1.isRequired;
      this.beginPos = var1.beginPos;
      this.endPos = var1.endPos;
      this.length = var1.length;
      this.fieldId = var1.fieldId;
      this.isFixedWidth = var1.isFixedWidth;
      this.validation = var1.validation;
   }

   public int getBeginPosition() {
      return this.beginPos;
   }

   public int getEndPosition() {
      return this.endPos;
   }

   public int getLength() {
      return this.length;
   }

   public void setBeginPosition(int var1) {
      this.beginPos = var1;
      this.endPos = this.beginPos + this.length - 1;
   }

   public void setEndPosition(int var1) {
      this.endPos = var1;
      this.length = this.endPos - this.beginPos + 1;
   }

   public void setLength(int var1) {
      this.length = var1;
      this.endPos = this.beginPos + this.length - 1;
   }

   public String getName() {
      return this.fieldName;
   }

   @InternalMethod
   public void makeFixedWidth() {
      this.isFixedWidth = true;
   }

   public void setName(String var1) {
      this.fieldName = Strings.nullToEmpty(var1);
   }

   public FieldDataType getDataType() {
      return this.dataType != null && this.dataType.length() > 0 ? FieldDataType.valueOf(this.dataType) : null;
   }

   public void setDataType(FieldDataType var1) {
      this.dataType = var1.toString();
   }

   public boolean isIdentifier() {
      return this.getDataType() == FieldDataType.Identifier;
   }

   public boolean isString() {
      return this.getDataType() == FieldDataType.String;
   }

   public boolean isDate() {
      return this.getDataType() == FieldDataType.Date;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public void setDefaultValue(String var1) {
      this.defaultValue = var1;
   }

   public String getFormat() {
      return this.format;
   }

   public void setFormat(String var1) {
      this.format = var1;
   }

   public int getId() {
      return this.fieldId;
   }

   public void setId(int var1) {
      this.fieldId = var1;
   }

   public int getIsRequired() {
      return this.isRequired;
   }

   public boolean isFixedWidth() {
      return this.isFixedWidth;
   }

   public Optional<FieldValidation> getValidation() {
      return this.validation;
   }

   public void setValidation(Optional<FieldValidation> var1) {
      this.validation = Preconditions.checkNotNull(var1);
   }

   public void setValidation(FieldValidation var1) {
      this.validation = Optional.of(var1);
   }

   public String[] getFlatDefinition(Segment var1) {
      return new String[]{
         null,
         Integer.toString(this.getId()),
         Integer.toString(var1.getId()),
         this.getName(),
         "0",
         Integer.toString(this.getDataType().intVal()),
         Integer.toString(this.getBeginPosition()),
         Integer.toString(this.getEndPosition()),
         Integer.toString(this.getIsRequired()),
         this.getDefaultValue(),
         this.getFormat(),
         ""
      };
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Field)) {
         return false;
      }

      Field var2 = (Field)var1;
      return this.mappingStructureEquals(var2) && this.validation.equals(var2.validation);
   }

   public boolean mappingStructureEquals(Field var1) {
      return this.isFixedWidth == var1.isFixedWidth
         && Objects.equal(this.fieldName, var1.fieldName)
         && Objects.equal(this.dataType, var1.dataType)
         && Objects.equal(this.defaultValue, var1.defaultValue)
         && Objects.equal(this.format, var1.format)
         && this.isRequired == var1.isRequired
         && this.beginPos == var1.beginPos
         && this.endPos == var1.endPos;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(
         this.isFixedWidth, this.fieldName, this.dataType, this.defaultValue, this.format, this.isRequired, this.beginPos, this.endPos, this.validation
      );
   }
}
