package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import org.etlforge.xml.XmlStringBuilder;

interface FilterEntry {
   void appendToXml(XmlStringBuilder var1);

   @Override
   boolean equals(Object var1);

   @Override
   int hashCode();
}
