package org.etlforge.integration.salesforce.wsdl;

import java.io.Serializable;
import java.util.Comparator;

public final class RelatedObjectsByRelationshipName implements Comparator<RelatedObject>, Serializable {
   public int compare(RelatedObject var1, RelatedObject var2) {
      return var1.getRelationshipName().compareTo(var2.getRelationshipName());
   }

   @Override
   public String toString() {
      return "By RelationshipName";
   }
}
