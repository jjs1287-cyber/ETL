package org.etlforge.integration.salesforce.wsdl;

import java.util.List;

public interface RelatedObjectsOwner {
   void setRelatedObjects(List<RelatedObject> var1);

   List<RelatedObject> getRelatedObjects();
}
