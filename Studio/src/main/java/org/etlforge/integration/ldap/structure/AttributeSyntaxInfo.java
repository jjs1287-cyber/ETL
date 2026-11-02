package org.etlforge.integration.ldap.structure;

public interface AttributeSyntaxInfo {
   String getOid();

   String getDescription();

   AttributeDataType getDataType();
}
