package org.etlforge.integration.data.structure.text;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.structure.crom.CromUtils;
import org.etlforge.integration.structure.reader.CSVReader;
import org.etlforge.integration.structure.reader.CSVUtils;
import org.etlforge.lang.KongaStringUtils;

public final class Segment implements Iterable<Field>, Serializable {
   public static final List<String> oTypes = Collections.unmodifiableList(Arrays.asList("Only Once", "One or More", "Zero or One", "Zero or More"));
   private String segmentName;
   private String parentName;
   private int segmentID;
   private String occurenceType;
   public List<Field> fieldList;
   public Segment alias;
   private int[] dataTypesAssigned = null;

   public static Segment newRootSegment() {
      Segment var0 = new Segment("_Root");
      var0.setOccurrence(SegmentOccurrence.OnlyOnce);
      return var0;
   }

   public Segment(String var1) {
      if (var1 != null && !var1.equals("_Root")) {
         this.parentName = "_Root";
         this.occurenceType = oTypes.get(0);
      } else {
         this.parentName = "";
         this.occurenceType = "";
      }

      this.segmentName = var1;
      this.fieldList = Lists.newArrayList();
   }

   public Segment(Segment var1) {
      this.segmentName = var1.segmentName;
      this.parentName = var1.parentName;
      this.segmentID = var1.segmentID;
      this.occurenceType = var1.occurenceType;
      if (var1.alias == null) {
         this.fieldList = Lists.newArrayList();

         for (Field var3 : var1.fieldList) {
            this.fieldList.add(new Field(var3));
         }
      }
   }

   public boolean isRoot() {
      return "_Root".equals(this.segmentName);
   }

   @Override
   public Iterator<Field> iterator() {
      List var1 = this.getFieldList();
      return var1 == null ? Collections2.emptyIterator() : new ArrayList<>(var1).iterator();
   }

   public boolean hasFields() {
      List var1 = this.getFieldList();
      return var1 != null && !var1.isEmpty();
   }

   public Segment findParent(ComplexTextStructure var1) {
      Segment var2 = null;
      if (this.parentName == "") {
         return var2;
      }

      for (Segment var4 : var1.getSegmentList()) {
         if (this.parentName.equals(var4.segmentName)) {
            var2 = var4;
         }

         if (var4 == this) {
            break;
         }
      }

      return var2;
   }

   public int getOccurenceID() {
      int var1 = 0;

      for (int var2 = oTypes.size(); var1 < var2; var1++) {
         if (this.occurenceType.equals(oTypes.get(var1))) {
            return var1 + 1;
         }
      }

      return 0;
   }

   public Field findIdentField() {
      if (this.fieldList == null) {
         return null;
      }

      for (Field var2 : this.fieldList) {
         if (var2.dataType.equals("Identifier")) {
            return var2;
         }
      }

      return null;
   }

   public List<Field> getFieldList() {
      return this.alias == null ? this.fieldList : this.alias.fieldList;
   }

   public void insertField(Field var1) {
      if (!this.getFieldList().contains(var1)) {
         int var2 = 0;

         for (Field var4 : this.getFieldList()) {
            if (var4.getId() > var1.getId()) {
               this.getFieldList().add(var2, var1);
               return;
            }

            var2++;
         }

         this.getFieldList().add(var1);
      }
   }

   public String getName() {
      return this.segmentName;
   }

   public void setName(String var1) {
      this.segmentName = var1;
   }

   public String getParentName() {
      return this.parentName;
   }

   public void setParentName(String var1) {
      this.parentName = var1;
   }

   public String getOccurrence() {
      return this.occurenceType;
   }

   public void setOccurrence(String var1) {
      Preconditions.checkNotNull(var1, "occurrence");
      this.occurenceType = var1;
   }

   public void setOccurrence(SegmentOccurrence var1) {
      this.setOccurrence(var1.toString());
   }

   public int getId() {
      return this.segmentID;
   }

   public void setId(int var1) {
      this.segmentID = var1;
   }

   public Segment getAlias() {
      return this.alias;
   }

   private String appropriateName(String var1, int var2) {
      if (var1 == null || var1.length() == 0) {
         var1 = "F" + (var2 + 1);
      } else if (Character.isDigit(var1.charAt(0))) {
         var1 = "_" + var1;
      }

      var1 = var1.replace(' ', '_');
      var1 = var1.replace('\r', '_');
      var1 = var1.replace('\n', '_');
      var1 = var1.replace('\t', '_');
      var1 = var1.replace(',', '_');
      var1 = CromUtils.convertIllegalCharacters(var1);
      String var3 = var1;
      int var4 = 0;
      int var5 = 2;

      while (var4 < var2) {
         if (this.fieldList.get(var4).getName().equals(var1)) {
            var1 = var3 + var5;
            var5++;
            var4 = 0;
         }

         var4++;
      }

      return var1;
   }

   private FieldDataType upgradeFieldDataType(FieldDataType var1, FieldDataType var2) {
      if (var1 != FieldDataType.String && var2 != var1) {
         switch (var1) {
            case Integer:
               if (var2 == FieldDataType.Double) {
                  return FieldDataType.Double;
               }
            case Double:
               if (var2 == FieldDataType.Integer) {
                  return var1;
               }
            case Date:
            default:
               return FieldDataType.String;
         }
      } else {
         return var1;
      }
   }

   public static FieldDataType getFieldDataTypeByParsing(String var0) {
      boolean var1 = false;
      boolean var2 = false;
      boolean var3 = false;

      try {
         Double.parseDouble(var0);
         var1 = var0.indexOf(46) < 0;
         var2 = !var1;
      } catch (NumberFormatException var7) {
      }

      int[] var4 = new int[]{0, 3, 1, 2};

      for (int var5 = 0; var5 < var4.length; var5++) {
         try {
            DateFormat var6 = DateFormat.getDateInstance(var4[var5]);
            var6.parse(var0);
            var3 = true;
            break;
         } catch (ParseException var8) {
         }
      }

      FieldDataType var9 = null;
      if (var1) {
         var9 = FieldDataType.Integer;
      } else if (var2) {
         var9 = FieldDataType.Double;
      } else if (var3) {
         var9 = FieldDataType.Date;
      } else {
         var9 = FieldDataType.String;
      }

      return var9;
   }

   private void createDataTypesAssigned(int var1) {
      if (var1 == 0) {
         this.dataTypesAssigned = new int[256];
      }

      if (var1 > this.dataTypesAssigned.length) {
         int[] var2 = this.dataTypesAssigned;
         this.dataTypesAssigned = new int[var1];

         for (int var3 = 0; var3 < var2.length; var3++) {
            this.dataTypesAssigned[var3] = var2[var3];
         }
      }
   }

   private void addDefaultField(String var1, char var2) {
      int var3 = this.fieldList.size();
      if (var3 > this.dataTypesAssigned.length) {
         this.createDataTypesAssigned(this.dataTypesAssigned.length + 100);
      }

      int var4 = var1.indexOf(40);
      int var5 = var1.indexOf(41, var4);
      if (var4 > 0 && var5 > var4) {
         var1 = var1.substring(0, var4);
      }

      if (CSVUtils.isQualified(var1, var2)) {
         var1 = CSVUtils.stripQualifierandTrim(var1, var2);
      }

      Field var6 = new Field(var3, this.appropriateName(var1, var3), "String", "", "", 0, false);
      this.fieldList.add(var6);
   }

   private void processDataTypeFromSampleData(List<String> var1, char var2) {
      for (int var3 = 0; var3 < var1.size(); var3++) {
         if (var3 >= this.fieldList.size()) {
            this.addDefaultField("", var2);
         }

         String var4 = CSVUtils.stripQualifierandTrim((String)var1.get(var3), var2);
         if (!var4.isEmpty()) {
            this.dataTypesAssigned[var3]++;
            FieldDataType var5 = getFieldDataTypeByParsing(var4);
            if (this.dataTypesAssigned[var3] > 1) {
               FieldDataType var6 = this.fieldList.get(var3).getDataType();
               var5 = this.upgradeFieldDataType(var5, var6);
            }

            this.fieldList.get(var3).setDataType(var5);
         }
      }
   }

   public void createFromSampleFlatFile(File var1, Charset var2, char var3, FieldDelimiter var4, boolean var5) throws IOException {
      this.fieldList.clear();
      CSVReader var6 = new CSVReader(var1, var2, var3, var4.asChar());

      try {
         var6.open();
         ArrayList var7 = Lists.newArrayList();
         this.createDataTypesAssigned(0);
         if (var5) {
            if (!var6.nextRecord()) {
               return;
            }

            for (String var9 : var6.getRecordFields()) {
               this.addDefaultField(var9, var3);
            }
         }

         int var14 = 0;

         while (var6.nextRecord() && 200 >= var14++) {
            this.processDataTypeFromSampleData(var6.getRecordFields(), var3);
         }
      } finally {
         var6.close();
      }
   }

   private FieldDataType findDataType(String var1) {
      String var2 = var1.toLowerCase();

      for (FieldDataType var4 : FieldDataType.allValueTypes()) {
         if (var4.name().toLowerCase().startsWith(var2)) {
            return var4;
         }
      }

      return FieldDataType.String;
   }

   public void createFromSimpleTextDef(BufferedReader var1, boolean var2) throws IOException {
      String var4 = ",\t;";
      int var5 = 0;
      int var6 = 1;

      String var3;
      while ((var3 = var1.readLine()) != null) {
         String[] var7 = KongaStringUtils.split(var3, var4, '\\');
         String var8 = var7[0];
         String var9 = var7[1];
         Field var10 = new Field(var5, this.appropriateName(var8, var5), this.findDataType(var9).name(), "", "", 0, false);
         this.fieldList.add(var10);
         if (var2) {
            String var11 = var7[2];
            if (var11 != null) {
               int var12 = Integer.parseInt(var11);
               var10.setBeginPosition(var6);
               var10.setLength(var12);
               var6 += var12;
               var10.setEndPosition(var6 - 1);
            }
         }

         var5++;
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Segment)) {
         return false;
      }

      Segment var2 = (Segment)var1;
      return Objects.equal(this.segmentName, var2.segmentName)
         && Objects.equal(this.parentName, var2.parentName)
         && Objects.equal(this.occurenceType, var2.occurenceType)
         && Objects.equal(this.getFieldList(), var2.getFieldList());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.segmentName, this.parentName, this.occurenceType, this.getFieldList());
   }

   public String asXsdComplexType(String var1) {
      return new SegmentToXsdComplexTypeBuilder(var1).createType(this);
   }
}
