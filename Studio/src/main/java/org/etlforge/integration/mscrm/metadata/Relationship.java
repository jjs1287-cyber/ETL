package org.etlforge.integration.mscrm.metadata;

public interface Relationship {
   String getLinkEntityName();

   boolean isIntersect();

   String getFromAttribute();

   String getToAttribute();
}
