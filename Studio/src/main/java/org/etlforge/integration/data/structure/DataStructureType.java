package org.etlforge.integration.data.structure;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.LdapLocation;

public enum DataStructureType {
   Text(1, "Text", true) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return DataLocationType.getFileBasedTypes();
      }
   },
   Database(3, "Database", false) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return EnumSet.of(DataLocationType.Database);
      }

      @Override
      public boolean isExternalSourceAccepted() {
         return false;
      }

      @Override
      public boolean isTiedToSpecificLocationType() {
         return true;
      }

      @Override
      public void updateInputStructureWithNewSourceLocation(Transformation var1, Source var2) {
         DatabaseStructure var3 = (DatabaseStructure)var1.getInputStructure();
         DatabaseStructure var4 = var3.withNewSourceLocation(var2);
         var1.setInputStructure(var4);
         var1.setSourceId(var2.getID());
      }

      @Override
      public void updateOutputStructureWithNewTargetLocation(Transformation var1, Target var2) {
         DatabaseStructure var3 = (DatabaseStructure)var1.getOutputStructure();
         DatabaseStructure var4 = var3.withNewTargetLocation(var2);
         var1.setOutputStructure(var4);
         var1.setTargetId(var2.getID());
      }
   },
   XML(4, "XML", true) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return DataLocationType.getFileBasedTypes();
      }
   },
   ComplexText(10, "Complex Text", true) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return DataLocationType.getFileBasedTypes();
      }
   },
   WebServiceRequest(12, "Web Service Request", true) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return DataLocationType.getFileBasedTypes();
      }
   },
   None(13, "(None)", false) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return EnumSet.noneOf(DataLocationType.class);
      }
   },
   WebServiceResponse(14, "Web Service Response", true) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return DataLocationType.getFileBasedTypes();
      }
   },
   LDAP(16, "LDAP", false) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return EnumSet.of(DataLocationType.LDAP);
      }

      @Override
      public boolean isExternalSourceAccepted() {
         return false;
      }

      @Override
      public boolean isTiedToSpecificLocationType() {
         return true;
      }

      @Override
      public void updateInputStructureWithNewSourceLocation(Transformation var1, Source var2) {
         LdapStructure var3 = (LdapStructure)var1.getInputStructure();
         LdapStructure var4 = var3.withNewLocation((LdapLocation)var2.getLocation());
         var1.setInputStructure(var4);
         var1.setSourceId(var2.getID());
      }

      @Override
      public void updateOutputStructureWithNewTargetLocation(Transformation var1, Target var2) {
         LdapStructure var3 = (LdapStructure)var1.getOutputStructure();
         LdapStructure var4 = var3.withNewLocation((LdapLocation)var2.getLocation());
         var1.setOutputStructure(var4);
         var1.setTargetId(var2.getID());
      }
   },
   EDI(2, "EDI", true) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return DataLocationType.getFileBasedTypes();
      }
   },
   JSON(17, "JSON", true) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return DataLocationType.getFileBasedTypes();
      }
   },
   ConnectorCallRequest(101, "Connector Call Request", true) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return DataLocationType.getFileBasedTypes();
      }
   },
   ConnectorCallResponse(102, "Connector Call Response", true) {
      @Override
      public EnumSet<DataLocationType> getCompatibleLocationTypes() {
         return DataLocationType.getFileBasedTypes();
      }
   };

   private final int id;
   private final String string;
   private final boolean fileBased;

   DataStructureType(int var3, String var4, boolean var5) {
      this.id = var3;
      this.string = var4;
      this.fileBased = var5;
   }

   public static DataStructureType fromId(int var0) {
      for (DataStructureType var4 : values()) {
         if (var4.id == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Unknown DataStructureType ID: " + var0);
   }

   public int getId() {
      return this.id;
   }

   @Override
   public String toString() {
      return this.string;
   }

   public boolean isXmlBased() {
      switch (this) {
         case XML:
         case WebServiceRequest:
         case WebServiceResponse:
         case ConnectorCallRequest:
         case ConnectorCallResponse:
            return true;
         default:
            return false;
      }
   }

   public boolean isTextBased() {
      return this == Text || this == ComplexText;
   }

   public boolean isSourceDataRequired() {
      return this != None;
   }

   public final boolean isFileBased() {
      return this.fileBased;
   }

   public boolean isExternalSourceAccepted() {
      return true;
   }

   public abstract EnumSet<DataLocationType> getCompatibleLocationTypes();

   public boolean isTiedToSpecificLocationType() {
      return false;
   }

   public void updateInputStructureWithNewSourceLocation(Transformation var1, Source var2) {
      throw new UnsupportedOperationException();
   }

   public void updateOutputStructureWithNewTargetLocation(Transformation var1, Target var2) {
      throw new UnsupportedOperationException();
   }

   static Set<Integer> reservedIds() {
      return Sets.newHashSet(0, 5, 6, 7, 8, 9, 11, 15);
   }
}
