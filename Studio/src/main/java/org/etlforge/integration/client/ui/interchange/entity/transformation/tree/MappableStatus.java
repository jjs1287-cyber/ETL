package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

public enum MappableStatus {
   OK {
      @Override
      public String explainWhyNotMappable() {
         throw new RuntimeException("I am mappable, so this method should be have been called. This is a bug.");
      }
   },
   FOLDER {
      @Override
      public String explainWhyNotMappable() {
         return "The selected target data elements is a folder, and cannot be mapped.";
      }
   },
   LINK_KEY {
      @Override
      public String explainWhyNotMappable() {
         return "This target data element is linked to a data element of its parent. You cannot map a value to this element.";
      }
   },
   IDENTIFIER {
      @Override
      public String explainWhyNotMappable() {
         return "You cannot map the selected target data element because it is the segment Identifier in a Complex Text definition.";
      }
   },
   UNDEFINED_DATA_TYPE {
      @Override
      public String explainWhyNotMappable() {
         return "You cannot map the selected target data element because its data type is undefined";
      }
   },
   POPULATED_AUTOMATICALLY {
      @Override
      public String explainWhyNotMappable() {
         return "You cannot map the selected target data element because the value is set automatically.";
      }
   },
   FIXED_VALUE {
      @Override
      public String explainWhyNotMappable() {
         return "You cannot map the selected target data element because this attribute has fixed value.";
      }
   },
   SFDC_CREATE_REQUEST_ID {
      @Override
      public String explainWhyNotMappable() {
         return "The ID is created by Salesforce. You cannot map this field.";
      }
   };

   MappableStatus() {
   }

   public final boolean isMappable() {
      return this == OK;
   }

   public static MappableStatus of(Node var0) {
      if (var0.isFolder()) {
         return FOLDER;
      } else if (var0.isLinkKey()) {
         return LINK_KEY;
      } else if (var0.isIdentifier()) {
         return IDENTIFIER;
      } else if (var0.isUndefinedDataType()) {
         return UNDEFINED_DATA_TYPE;
      } else if (var0.isPopulatedAutomatically()) {
         return POPULATED_AUTOMATICALLY;
      } else if (var0.isFixedValueAttribute()) {
         return FIXED_VALUE;
      } else {
         return var0.isSfdcCreateId() ? SFDC_CREATE_REQUEST_ID : OK;
      }
   }

   public abstract String explainWhyNotMappable();
}
