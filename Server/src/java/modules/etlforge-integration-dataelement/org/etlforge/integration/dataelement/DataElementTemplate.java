package org.etlforge.integration.dataelement;

import org.etlforge.util.name.Nameable;

public interface DataElementTemplate extends Nameable {
   @Override
   String getName();

   DataElementType getType();

   Object getDefaultValue();

   String getDescription();
}
