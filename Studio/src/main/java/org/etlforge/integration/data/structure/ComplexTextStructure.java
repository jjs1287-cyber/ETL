package org.etlforge.integration.data.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.data.structure.text.SegmentPersistor;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.util.persist.Persistor;

public final class ComplexTextStructure extends TextStructure implements Iterable<Segment> {
   public static final String c_rootName = "_Root";
   private List<Segment> segmentList = Lists.newArrayList();

   public ComplexTextStructure(String var1, boolean var2) {
      super(DataStructureType.ComplexText, var1, var2);
   }

   public ComplexTextStructure(String var1, boolean var2, List<String> var3) {
      super(DataStructureType.ComplexText, var1, var2);
      if (var3 != null) {
         this.defineSegmentList(var3);
      }
   }

   @Override
   public List<Segment> getSegmentList() {
      synchronized (this.getDataLock()) {
         return this.segmentList;
      }
   }

   @Override
   public Iterator<Segment> iterator() {
      synchronized (this.getDataLock()) {
         return Lists.newArrayList(this.segmentList).iterator();
      }
   }

   public void removeAllSegments() {
      synchronized (this.getDataLock()) {
         this.segmentList.clear();
      }
   }

   public void addSegment(Segment var1) {
      synchronized (this.getDataLock()) {
         this.segmentList.add(var1);
      }
   }

   private Segment getSegment(int var1) {
      if (var1 == 0) {
         return null;
      }

      synchronized (this.getDataLock()) {
         for (Segment var4 : this.segmentList) {
            if (var4.getId() == var1) {
               return var4;
            }
         }

         return null;
      }
   }

   private void defineSegmentList(List<String> var1) {
      for (String var4 : var1) {
         String[] var2 = KongaStringUtils.split(var4);
         String var7 = var2[3];
         String var8 = var2[9];
         String var9 = var2[10];
         int var10 = Integer.parseInt(var2[1]);
         int var11 = Integer.parseInt(var2[2]);
         int var12 = KongaStringUtils.parseInt(var2[4], 0);
         int var13 = KongaStringUtils.parseInt(var2[5], 0);
         int var14 = KongaStringUtils.parseInt(var2[6], 0);
         int var15 = KongaStringUtils.parseInt(var2[7], 0);
         int var16 = KongaStringUtils.parseInt(var2[8], 0);
         String var17 = Field.Variable_TypeTab[var13];
         if (var17.equals("Segment")) {
            Segment var5 = new Segment(var7);
            var5.setId(var10);
            if (var11 > 0) {
               Segment var18 = this.getSegment(var11);
               if (var18 != null) {
                  var5.setParentName(var18.getName());
               } else {
                  var5.setParentName("_Root");
                  boolean var20 = true;
               }
            }

            if (var16 > 1 && var16 <= 4) {
               var5.setOccurrence(Segment.oTypes.get(var16 - 1));
            }

            this.addSegment(var5);
            if (var5.getName().equals("_Root") && !var5.getOccurrence().equals(Segment.oTypes.get(0))) {
               var5.setOccurrence(Segment.oTypes.get(0));
            }

            if (var12 != 0) {
               var5.alias = this.getSegment(var12);
               var5.fieldList = null;
            }
         } else {
            Segment var19 = this.getSegment(var11);
            if (var19 == null) {
               throw new IllegalStateException(
                  "Failed to parse document definition line \""
                     + var4
                     + "\" for text document \""
                     + this.getName()
                     + "\". Parent_ID = "
                     + Long.toString(var11)
                     + " was not found."
               );
            }

            int var21 = var15 - var14 + 1;
            Field var6 = new Field(var10, var7, var17, var8, var9, var16, var14, var21, null, this.isFixField());
            var19.insertField(var6);
         }
      }
   }

   @Override
   public StructureNature getStructureNature() {
      return StructureNature.Tree;
   }

   @Override
   public List<String[]> getFlatDefinition(List<String[]> var1) {
      synchronized (this.getDataLock()) {
         ArrayList var3 = Lists.newArrayList();
         if (this.segmentList.size() == 0) {
            return var3;
         }

         int var4 = 0;
         int var5 = -1;
         int var6 = -1;

         for (Segment var8 : this.segmentList) {
            Field var9 = var8.findIdentField();
            if (var9 != null) {
               var5 = var9.getBeginPosition();
               var6 = var9.getEndPosition();
               break;
            }
         }

         for (Segment var17 : this.segmentList) {
            var17.setId(++var4);
            String[] var18 = new String[12];
            var18[1] = Integer.toString(var17.getId());
            Segment var10 = var17.findParent(this);
            if (var10 == null) {
               var18[2] = "0";
            } else {
               var18[2] = Integer.toString(var10.getId());
            }

            var18[3] = var17.getName();
            Segment var11 = var17.getAlias();
            if (var11 != null) {
               var18[4] = Integer.toString(var11.getId());
            } else {
               var18[4] = "0";
            }

            var18[5] = "8";
            var18[6] = String.valueOf(var5);
            var18[7] = String.valueOf(var6);
            var18[8] = Integer.toString(var17.getOccurenceID());
            var18[9] = "";
            var18[10] = "";
            var18[11] = "";
            var3.add(var18);

            for (Field var13 : var17.getFieldList()) {
               var13.setId(++var4);
               var3.add(var13.getFlatDefinition(var17));
            }
         }

         return var3;
      }
   }

   public ComplexTextStructure clone() {
      synchronized (this.getDataLock()) {
         ComplexTextStructure var2 = (ComplexTextStructure)super.clone();
         var2.segmentList = Lists.newArrayList();

         for (Segment var4 : this.segmentList) {
            Segment var5 = new Segment(var4);
            var5.alias = var4.alias;
            var2.segmentList.add(var5);
         }

         return var2;
      }
   }

   @Override
   public boolean equals(TextStructure var1) {
      if (!(var1 instanceof ComplexTextStructure)) {
         return false;
      } else {
         return !this.isUsingSameDelimiterAndQualifierAs(var1) ? false : this.segmentList.equals(((ComplexTextStructure)var1).segmentList);
      }
   }

   @Override
   public boolean mappingStructureEquals(TextStructure var1) {
      return this.equals(var1);
   }

   @Override
   public void writeTo(Persistor var1) {
      synchronized (this.getDataLock()) {
         super.writeTo(var1);
         this.writeSegmentsTo(var1);
      }
   }

   private void writeSegmentsTo(Persistor var1) {
      SegmentPersistor var2 = new SegmentPersistor();
      Persistor var3 = var1.createChild("Segments");

      for (Segment var5 : this.segmentList) {
         var2.save(var5, var3.createChild("Segment"));
      }
   }

   @Override
   public void restoreFrom(Persistor var1) {
      synchronized (this.getDataLock()) {
         super.restoreFrom(var1);
         this.restoreSegmentsFrom(var1);
      }
   }

   private void restoreSegmentsFrom(Persistor var1) {
      Persistor var2 = var1.getFirstChild("Segments");
      if (var2 == null) {
         throw new IllegalArgumentException("Invalid persistor: the Segments root is missing");
      }

      this.segmentList = Lists.newArrayList();
      SegmentPersistor var3 = new SegmentPersistor();

      for (Persistor var5 : var2.getChildren("Segment")) {
         this.segmentList.add(var3.restoreFrom(var5));
      }
   }
}
