package org.etlforge.integration.dataelement;

public interface DataElement {
   DataElementTemplate getTemplate();

   Object getValue();

   void setValue(Object var1);
}
