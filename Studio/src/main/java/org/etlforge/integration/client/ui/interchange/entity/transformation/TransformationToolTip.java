package org.etlforge.integration.client.ui.interchange.entity.transformation;

import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class TransformationToolTip extends IntegrationEntityToolTip {
   public TransformationToolTip(Transformation var1) {
      super(var1);
      this.addSourceType(var1);
      this.addTargetType(var1);
   }

   private void addSourceType(Transformation var1) {
      if (var1.getSourceJmsMessageId() != null) {
         this.addEntry(getTypeLabel(SourceTarget.Source), "JMS");
      } else {
         DataStructure var2 = var1.getInputStructure();
         if (var2 != null) {
            TransformationToolTip.StructureDetailsWriter var3 = new TransformationToolTip.StructureDetailsWriter(SourceTarget.Source);
            var3.write(var2);
         } else {
            this.addEntry(getTypeLabel(SourceTarget.Source), "[unknown]");
         }
      }
   }

   private void addTargetType(Transformation var1) {
      DataStructure var2 = var1.getOutputStructure();
      if (var2 != null) {
         TransformationToolTip.StructureDetailsWriter var3 = new TransformationToolTip.StructureDetailsWriter(SourceTarget.Target);
         var3.write(var2);
      } else {
         this.addEntry(getTypeLabel(SourceTarget.Target), "[unknown]");
      }
   }

   private static String getTypeLabel(SourceTarget var0) {
      return var0 + " type:";
   }

   private class StructureDetailsWriter {
      private final SourceTarget type;

      public StructureDetailsWriter(SourceTarget var2) {
         this.type = var2;
      }

      public void write(DataStructure var1) {
         if (var1 instanceof DatabaseStructure) {
            this.visit((DatabaseStructure)var1);
         } else if (var1 instanceof XmlStructure) {
            this.visit((XmlStructure)var1);
         } else if (var1 instanceof JsonStructure) {
            this.visit((JsonStructure)var1);
         } else {
            this.writeDefault(var1);
         }
      }

      private void visit(DatabaseStructure var1) {
         StringBuilder var2 = new StringBuilder(DataStructureType.Database.toString());
         DatabaseLocation var3 = var1.getDbLocation();
         if (var3 != null) {
            StringBuilder var4 = new StringBuilder();
            String var5 = var3.getDatabase();
            if (var5 != null && var5.length() > 0) {
               var4.append(var5).append(" on ");
            }

            String var6 = var3.getServer();
            if (var6 != null && var6.length() > 0) {
               var4.append(var6);
            } else if (var4.length() > 0) {
               var4.append("?");
            }

            if (var4.length() > 0) {
               var2.append(" (").append(var4).append(")");
            }
         }

         TransformationToolTip.this.addEntry(this.getTypeLabel(), var2.toString());
      }

      private void visit(XmlStructure var1) {
         StringBuilder var2 = new StringBuilder(DataStructureType.XML.toString());
         String var3 = var1.getFilePath();
         if (var3 != null && var3.length() > 0) {
            var2.append(" (").append(var3).append(")");
         }

         TransformationToolTip.this.addEntry(this.getTypeLabel(), var2.toString());
      }

      private void visit(JsonStructure var1) {
         StringBuilder var2 = new StringBuilder(DataStructureType.JSON.toString());
         String var3 = var1.getFileName();
         if (var3 != null && var3.length() > 0) {
            var2.append(" (").append(var3).append(")");
         }

         TransformationToolTip.this.addEntry(this.getTypeLabel(), var2.toString());
      }

      private void writeDefault(DataStructure var1) {
         TransformationToolTip.this.addEntry(this.getTypeLabel(), var1.getStructureType());
      }

      private String getTypeLabel() {
         return TransformationToolTip.getTypeLabel(this.type);
      }
   }
}
