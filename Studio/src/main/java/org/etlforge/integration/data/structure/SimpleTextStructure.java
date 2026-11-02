package org.etlforge.integration.data.structure;

import com.google.common.base.Equivalence;
import com.google.common.base.Equivalences;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Collections2;
import org.etlforge.collections.Pair;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.InvalidTemplateException;
import org.etlforge.integration.data.structure.text.Prototype;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.data.structure.text.SegmentPersistor;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.util.csv.CSVParams;
import org.etlforge.util.persist.Persistor;

public final class SimpleTextStructure extends TextStructure implements Iterable<Field> {
   private static final char hardCodedQualifier = '"';
   private Segment segment;

   public static SimpleTextStructure fromSampleDataFile(String var0, Prototype var1, FieldDelimiter var2, File var3, Charset var4) throws IOException, InvalidTemplateException {
      try {
         SimpleTextStructure var5 = new SimpleTextStructure(var0, false);
         var5.segment = new Segment("");
         boolean var6 = true;
         switch (var1) {
            case SampleFlatFile:
               var6 = false;
            case SampleFlatFileWithHeader:
            default:
               var5.segment.createFromSampleFlatFile(var3, var4, '"', var2, var6);
               return var5;
            case SimpleTextDef:
               throw new IllegalArgumentException("Illegal prototype: " + var1);
         }
      } catch (RuntimeException var7) {
         throw new InvalidTemplateException(var7);
      }
   }

   public static SimpleTextStructure fromDefinitionFile(String var0, boolean var1, BufferedReader var2) throws IOException, InvalidTemplateException {
      try {
         SimpleTextStructure var3 = new SimpleTextStructure(var0, var1);
         var3.segment = new Segment("");
         var3.segment.createFromSimpleTextDef(var2, var1);
         return var3;
      } catch (RuntimeException var4) {
         throw new InvalidTemplateException(var4);
      }
   }

   public SimpleTextStructure(String var1, boolean var2) {
      super(DataStructureType.Text, var1, var2);
      this.segment = new Segment("");
   }

   public SimpleTextStructure(String var1, boolean var2, List<String> var3, List<FieldValidation> var4) {
      super(DataStructureType.Text, var1, var2);
      if (var4 != null && var3 != null && var3.size() != var4.size()) {
         throw new IllegalArgumentException("Number of lines and validations are not the same.");
      }

      this.segment = new Segment("");
      if (var3 != null) {
         this.defineFieldList(var3, var4);
      }
   }

   private void defineFieldList(List<String> var1, List<FieldValidation> var2) {
      int var3 = 0;

      for (String var5 : var1) {
         Field var6 = new Field(var5, this.isFixField());
         FieldValidation var7 = (FieldValidation)var2.get(var3);
         Optional var8 = Optional.fromNullable(var7);
         var6.setValidation(var8);
         this.segment.insertField(var6);
         var3++;
      }
   }

   @Override
   public Iterator<Field> iterator() {
      List var1 = this.getFieldList();
      return var1 == null ? Collections2.emptyIterator() : new ArrayList<>(var1).iterator();
   }

   public List<Field> getFieldList() {
      synchronized (this.getDataLock()) {
         return this.segment.fieldList;
      }
   }

   public void setFieldList(List<Field> var1) {
      synchronized (this.getDataLock()) {
         this.segment.fieldList = var1;
      }
   }

   public int getFieldListSize() {
      synchronized (this.getDataLock()) {
         return this.segment.fieldList.size();
      }
   }

   public Segment getSegment() {
      synchronized (this.getDataLock()) {
         return this.segment;
      }
   }

   public void setSegment(Segment var1) {
      synchronized (this.getDataLock()) {
         this.segment = var1;
      }
   }

   @Override
   public StructureNature getStructureNature() {
      return StructureNature.Flat;
   }

   @Override
   public List<String[]> getFlatDefinition(List<String[]> var1) {
      synchronized (this.getDataLock()) {
         ArrayList var3 = Lists.newArrayList();
         List var4 = this.segment.getFieldList();
         if (var4.size() == 0) {
            return var3;
         }

         int var5 = 0;

         for (Object __etlItem148 : var4) {
            Field var7 = (Field) __etlItem148;
            var7.setId(++var5);
            var3.add(var7.getFlatDefinition(this.segment));
            Optional var8 = var7.getValidation();
            if (var8.isPresent()) {
               FieldValidation var9 = (FieldValidation)var8.get();
               var1.add(var9.getFlatDefinition(var5, this.segment.getId()));
            }
         }

         return var3;
      }
   }

   public SimpleTextStructure clone() {
      synchronized (this.getDataLock()) {
         SimpleTextStructure var2 = (SimpleTextStructure)super.clone();
         var2.segment = new Segment(this.segment);
         return var2;
      }
   }

   @Override
   public void writeTo(Persistor var1) {
      synchronized (this.getDataLock()) {
         super.writeTo(var1);
         Persistor var3 = var1.createChild("Segment");
         SegmentPersistor var4 = new SegmentPersistor();
         var4.save(this.segment, var3);
      }
   }

   @Override
   public void restoreFrom(Persistor var1) {
      Persistor var2 = var1.getFirstChild("Segment");
      if (var2 == null) {
         throw new IllegalArgumentException("Invalid persistor: the Segment field is missing");
      }

      synchronized (this.getDataLock()) {
         super.restoreFrom(var1);
         SegmentPersistor var4 = new SegmentPersistor();
         this.segment = var4.restoreFrom(var2);
         this.segment.setName("");
      }
   }

   @Override
   public boolean equals(TextStructure var1) {
      return this.equalFields(var1, Equivalences.equals());
   }

   @Override
   public boolean mappingStructureEquals(TextStructure var1) {
      return this.equalFields(var1, new Equivalence<Object>() {
         @Override
         protected boolean doEquivalent(Object var1, Object var2) {
            return ((Field)var1).mappingStructureEquals((Field)var2);
         }

         @Override
         protected int doHash(Object var1) {
            return 0;
         }
      });
   }

   private boolean equalFields(TextStructure var1, Equivalence<Object> var2) {
      if (!(var1 instanceof SimpleTextStructure)) {
         return false;
      }

      if (!this.isUsingSameDelimiterAndQualifierAs(var1)) {
         return false;
      }

      List var3 = this.getFieldList();
      List var4 = ((SimpleTextStructure)var1).getFieldList();
      if (var3.size() != var4.size()) {
         return false;
      }

      Iterator var5 = var3.iterator();
      Iterator var6 = var4.iterator();

      while (var5.hasNext()) {
         Field var7 = (Field)var5.next();
         Field var8 = (Field)var6.next();
         if (!var2.equivalent(var7, var8)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public List<Segment> getSegmentList() {
      synchronized (this.getDataLock()) {
         return Collections.singletonList(this.segment);
      }
   }

   public Optional<CSVParams> getCSVParams() {
      synchronized (this.getDataLock()) {
         if (this.isFixField()) {
            return Optional.absent();
         }

         FieldDelimiter var2 = this.getDelimiter();
         String var3 = this.getStringQualifier();
         return Optional.of(
            new CSVParams(var2 == null ? CSVParams.DEFAULT.delimiter : var2.asChar(), StringUtils.isBlank(var3) ? CSVParams.DEFAULT.qualifier : var3.charAt(0))
         );
      }
   }

   public FluentIterable<Pair<Field, FieldValidation>> getFieldsWithValidation(final Predicate<? super FieldValidation> var1) {
      return FluentIterable.from(this).filter(new Predicate<Field>() {
         public boolean apply(Field var1x) {
            Optional<FieldValidation> var2 = var1x.getValidation();
            return var2.isPresent() && var1.apply(var2.get());
         }
      }).transform(new Function<Field, Pair<Field, FieldValidation>>() {
         public Pair<Field, FieldValidation> apply(Field var1) {
            return Pair.of(var1, var1.getValidation().get());
         }
      });
   }
}
