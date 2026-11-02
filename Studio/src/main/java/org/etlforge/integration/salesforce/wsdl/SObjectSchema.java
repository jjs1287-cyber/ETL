package org.etlforge.integration.salesforce.wsdl;

import com.google.common.collect.ImmutableSet;
import java.io.Serializable;
import java.util.List;
import org.etlforge.util.name.Nameable;
import org.etlforge.xml.ExpandedName;

public interface SObjectSchema extends SchemaEntity, Serializable {
   @Override
   String getName();

   ExpandedName getExpandedName();

   @Override
   String getLabel();

   String getLabelPlural();

   boolean isCustom();

   List<SObjectSchema.Field> getFields();

   Iterable<SObjectSchema.Parent> getParents();

   Iterable<SObjectSchema.Child> getChildren();

   boolean isQueryable();

   boolean isUpdatable();

   boolean isCreatable();

   boolean isDeletable();

   interface Child extends Nameable, Serializable {
      @Override
      String getName();

      String getObjectType();
   }

   interface Field extends SchemaEntity, Serializable {
      @Override
      String getName();

      @Override
      String getLabel();

      SObjectSchema.Field.Type getType();

      SObjectSchema.Field.SoapType getSoapType();

      boolean isExternalId();

      boolean isIdLookup();

      boolean isCreateable();

      boolean isUpdateable();

      boolean isFilterable();

      List<String> getReferenceTo();

      String getRelationshipName();

      Picklist getPicklist();

      @Override
      boolean equals(Object var1);

      @Override
      int hashCode();

      enum SoapType {
         ID,
         ANY_TYPE,
         BASE64_BINARY,
         BOOLEAN,
         DATE,
         DATETIME,
         TIME,
         DOUBLE,
         INT,
         STRING;

         public String toXsdLocalTypeName() {
            switch (this) {
               case STRING:
               case ID:
               case ANY_TYPE:
                  return "string";
               case BASE64_BINARY:
                  return "base64Binary";
               case BOOLEAN:
                  return "boolean";
               case DATE:
                  return "date";
               case DATETIME:
                  return "dateTime";
               case TIME:
                  return "time";
               case DOUBLE:
                  return "double";
               case INT:
                  return "long";
               default:
                  throw new RuntimeException("Unexpected SOAP type: " + this);
            }
         }

         public String toETLForgeTypeName() {
            switch (this) {
               case STRING:
               case ID:
               case ANY_TYPE:
               case BASE64_BINARY:
                  return "String";
               case BOOLEAN:
                  return "Boolean";
               case DATE:
               case DATETIME:
               case TIME:
                  return "Date";
               case DOUBLE:
                  return "Double";
               case INT:
                  return "Long";
               default:
                  throw new RuntimeException("Unexpected SOAP type: " + this);
            }
         }

         public String toETLForgeTypeFormat() {
            switch (this) {
               case DATE:
                  return "date";
               case DATETIME:
                  return "dateTime";
               case TIME:
                  return "time";
               default:
                  return null;
            }
         }
      }

      enum Type {
         STRING,
         PICKLIST,
         MULTIPICKLIST,
         COMBOBOX,
         REFERENCE,
         BOOLEAN,
         CURRENCY,
         TEXTAREA,
         INT,
         DOUBLE,
         PHONE,
         PERCENT,
         ID,
         DATE,
         DATETIME,
         TIME,
         URL,
         EMAIL,
         ENCRYPTED_STRING,
         DATA_CATEGORY_GROUP_REF,
         BASE64,
         ANY;
      }
   }

   interface Parent extends Nameable, Serializable {
      @Override
      String getName();

      ImmutableSet<String> getObjectTypes();

      String getObjectTypeInQuery();
   }
}
