package org.etlforge.integration.client.ui.interchange.entity.document.page;

import com.google.common.base.Preconditions;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.SegmentType;
import org.etlforge.integration.data.structure.text.TextStructureType;

class NewTextDocumentModel {
   public static final String NAME_PROPERTY = "name";
   public static final String STRUCTURE_TYPE_PROPERTY = "structure_type";
   public static final String SEGMENT_TYPE_PROPERTY = "segment_type";
   private final TextDocument textDocument;
   private String documentName;
   private TextStructureType structureType;
   private SegmentType segmentType;
   private final PropertyChangeSupport changeSupport;

   public NewTextDocumentModel(TextDocument var1) {
      this.textDocument = var1;
      TextStructure var2 = var1.getDataStructure();
      if (var2 instanceof ComplexTextStructure) {
         this.structureType = TextStructureType.COMPLEX;
      } else {
         this.structureType = TextStructureType.SIMPLE;
      }

      this.segmentType = var1.isFixedField() ? SegmentType.FIXED_FIELD : SegmentType.DELIMITED;
      this.changeSupport = new PropertyChangeSupport(this);
   }

   public String getDocumentName() {
      return this.documentName;
   }

   public void setDocumentName(String var1) {
      String var2 = this.documentName;
      this.documentName = Preconditions.checkNotNull(var1, "name");
      this.changeSupport.firePropertyChange("name", var2, var1);
   }

   public TextDocument getTextDocument() {
      return this.textDocument;
   }

   public TextStructureType getStructureType() {
      return this.structureType;
   }

   public void setTextStructureType(TextStructureType var1) {
      TextStructureType var2 = this.structureType;
      this.structureType = Preconditions.checkNotNull(var1, "type");
      this.changeSupport.firePropertyChange("structure_type", var2, var1);
   }

   public SegmentType getSegmentType() {
      return this.segmentType;
   }

   public void setSegmentType(SegmentType var1) {
      SegmentType var2 = this.segmentType;
      this.segmentType = Preconditions.checkNotNull(var1, "type");
      this.changeSupport.firePropertyChange("segment_type", var2, var1);
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.changeSupport.addPropertyChangeListener(var1);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.changeSupport.addPropertyChangeListener(var1, var2);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.changeSupport.removePropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.changeSupport.removePropertyChangeListener(var1, var2);
   }

   public void updateDocumentType() {
      int var1 = this.getDocType();
      this.textDocument.setDocType(var1, false);
   }

   private int getDocType() {
      if (this.structureType == TextStructureType.SIMPLE) {
         return this.segmentType == SegmentType.DELIMITED ? 2 : 1;
      } else {
         return this.segmentType == SegmentType.DELIMITED ? 4 : 3;
      }
   }
}
