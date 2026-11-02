package org.etlforge.xml;

import java.io.Serializable;

public interface QualifiedName extends Serializable {
   String getPrefix();

   String getLocalName();

   @Override
   String toString();
}
