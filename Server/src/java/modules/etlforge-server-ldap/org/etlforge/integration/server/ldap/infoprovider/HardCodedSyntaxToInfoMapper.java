package org.etlforge.integration.server.ldap.infoprovider;

import java.util.HashMap;
import java.util.Map;
import org.etlforge.integration.ldap.structure.AttributeDataType;
import org.etlforge.integration.ldap.structure.AttributeSyntaxInfo;

public class HardCodedSyntaxToInfoMapper implements SyntaxToInfoMapper {
   private static final Map<String, AttributeSyntaxInfo> infos = new HashMap<String, AttributeSyntaxInfo>() {
      {
         this.add("1.3.6.1.4.1.1466.115.121.1.4", "Audio", AttributeDataType.BINARY);
         this.add("1.3.6.1.4.1.1466.115.121.1.5", "Binary", AttributeDataType.BINARY);
         this.add("1.3.6.1.4.1.1466.115.121.1.6", "Bit String", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.7", "Boolean", AttributeDataType.BOOLEAN);
         this.add("1.3.6.1.4.1.1466.115.121.1.8", "Certificate", AttributeDataType.BINARY);
         this.add("1.3.6.1.4.1.1466.115.121.1.9", "Certificate List", AttributeDataType.BINARY);
         this.add("1.3.6.1.4.1.1466.115.121.1.10", "Certificate Pair", AttributeDataType.BINARY);
         this.add("1.3.6.1.4.1.1466.115.121.1.11", "Country String", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.12", "Distinguished Name", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.15", "Directory String", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.22", "Facsimile Telephone Number", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.24", "Generalized Time", AttributeDataType.DATE);
         this.add("1.3.6.1.4.1.1466.115.121.1.26", "IA5 String", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.27", "Integer", AttributeDataType.INTEGER);
         this.add("1.3.6.1.4.1.1466.115.121.1.28", "JPEG", AttributeDataType.BINARY);
         this.add("1.3.6.1.4.1.1466.115.121.1.34", "Name And Optional UID", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.36", "Numeric String", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.38", "OID", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.39", "Other Mailbox", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.40", "Octet String", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.41", "Postal Address", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.44", "Printable String", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.49", "Supported Algorithm", AttributeDataType.BINARY);
         this.add("1.3.6.1.4.1.1466.115.121.1.50", "Telephone Number", AttributeDataType.STRING);
         this.add("1.3.6.1.4.1.1466.115.121.1.52", "Telex Number", AttributeDataType.STRING);
         this.add("1.3.6.1.1.1.0.0", "RFC2307 NIS Netgroup Triple", AttributeDataType.STRING);
         this.add("1.3.6.1.1.1.0.1", "RFC2307 Boot Parameter", AttributeDataType.STRING);
         this.add("1.2.826.0.1.3344810.7.1", "Serial Number and Issuer", AttributeDataType.STRING);
         this.add("1.2.840.113556.1.4.906", "Large-Integer", AttributeDataType.LONG);
         this.add("1.2.840.113556.1.4.907", "Object-Security-Descriptor", AttributeDataType.STRING);
         this.add("1.2.840.113556.1.4.903", "DNWithOctetString", AttributeDataType.STRING);
         this.add("1.2.840.113556.1.4.905", "CaseIgnoreString", AttributeDataType.STRING);
         this.add("OctetString", "Octet String", AttributeDataType.STRING);
      }

      private void add(String var1, String var2, AttributeDataType var3) {
         HardCodedSyntaxToInfoMapper.SyntaxInfoImpl var4 = new HardCodedSyntaxToInfoMapper.SyntaxInfoImpl(var1, var2, var3);
         this.put(var4.getOid(), var4);
      }
   };

   @Override
   public AttributeSyntaxInfo fromSyntax(String var1) {
      AttributeSyntaxInfo var2 = infos.get(var1);
      return var2 != null ? var2 : new HardCodedSyntaxToInfoMapper.SyntaxInfoImpl(var1, "", AttributeDataType.UNKNOWN);
   }

   private static class SyntaxInfoImpl implements AttributeSyntaxInfo {
      private final String oid;
      private final AttributeDataType dataType;
      private final String description;

      public SyntaxInfoImpl(String var1, String var2, AttributeDataType var3) {
         this.oid = var1;
         this.dataType = var3;
         this.description = var2;
      }

      @Override
      public AttributeDataType getDataType() {
         return this.dataType;
      }

      @Override
      public String getDescription() {
         return this.description;
      }

      @Override
      public String getOid() {
         return this.oid;
      }
   }
}
