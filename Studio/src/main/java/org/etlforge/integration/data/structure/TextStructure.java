package org.etlforge.integration.data.structure;

import com.google.common.base.Objects;
import java.util.List;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.Segment;

public abstract class TextStructure extends DataStructure {
   private static final String FIXED_FIELD = "fixedField";
   private transient FieldDelimiter delimiter;
   private transient String stringQualifier;

   public TextStructure(DataStructureType var1, String var2, boolean var3) {
      super(var1, var2);
      this.setBooleanProperty("fixedField", var3);
   }

   public final boolean isFixField() {
      return this.getBooleanProperty("fixedField", false);
   }

   public abstract List<String[]> getFlatDefinition(List<String[]> var1);

   @Override
   public final boolean isCompatibleWith(DataLocation var1) {
      return var1 != null && var1.isFileBased();
   }

   public TextStructure clone() {
      return (TextStructure)super.clone();
   }

   public FieldDelimiter getDelimiter() {
      synchronized (this.getDataLock()) {
         return this.delimiter;
      }
   }

   public void setDelimiter(FieldDelimiter var1) {
      synchronized (this.getDataLock()) {
         this.delimiter = var1;
      }
   }

   public String getStringQualifier() {
      return this.stringQualifier;
   }

   public void setStringQualifier(String var1) {
      synchronized (this.getDataLock()) {
         this.stringQualifier = var1;
      }
   }

   public abstract boolean equals(TextStructure var1);

   public abstract boolean mappingStructureEquals(TextStructure var1);

   protected final boolean isUsingSameDelimiterAndQualifierAs(TextStructure var1) {
      return this.isFixField()
         ? var1.isFixField()
         : !var1.isFixField() && Objects.equal(this.getStringQualifier(), var1.getStringQualifier()) && Objects.equal(this.getDelimiter(), var1.getDelimiter());
   }

   public abstract List<Segment> getSegmentList();
}
