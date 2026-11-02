package org.etlforge.integration.structure.crom;

import org.etlforge.integration.structure.mapping.JtrXmlFormatUtils;
import org.xml.sax.Attributes;

public class CROM_Text_DB extends CROM_ext {
   private static final String el = "CROM_Text_DB";
   private static final String att_StrQual = "StrQual";
   private static final String att_NumQual = "NumQual";
   private static final String att_Delimiter = "Delimiter";
   private static final String att_FKCounter = "FKCounter";
   private static final String att_SQLFilter = "SQLFilter";
   private String m_StrQual;
   private String m_NumQual;
   private String m_Delimiter;
   private int m_FKCounter;
   private String m_SQLFilter;

   public CROM_Text_DB() {
   }

   public CROM_Text_DB(Attributes var1) {
      this.m_StrQual = var1.getValue("StrQual");
      this.m_NumQual = var1.getValue("NumQual");
      this.m_Delimiter = var1.getValue("Delimiter");
      this.m_FKCounter = JtrXmlFormatUtils.getShort(var1, "FKCounter", 0);
      this.m_SQLFilter = var1.getValue("SQLFilter");
   }

   public String toString(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append('<').append("CROM_Text_DB");
      JtrXmlFormatUtils.addAttribute(var2, "StrQual", this.m_StrQual);
      JtrXmlFormatUtils.addAttribute(var2, "NumQual", this.m_NumQual);
      JtrXmlFormatUtils.addAttribute(var2, "Delimiter", this.m_Delimiter);
      JtrXmlFormatUtils.addAttribute(var2, "FKCounter", this.m_FKCounter, 0);
      JtrXmlFormatUtils.addAttribute(var2, "SQLFilter", this.m_SQLFilter);
      var2.append("/>\n");
      return var2.toString();
   }
}
