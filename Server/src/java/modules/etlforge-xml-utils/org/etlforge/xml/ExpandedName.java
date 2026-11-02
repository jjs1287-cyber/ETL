package org.etlforge.xml;

import java.io.Serializable;

public interface ExpandedName extends Serializable {
   String getNamespace();

   String getLocalName();

   @Override
   boolean equals(Object var1);

   @Override
   String toString();
}
