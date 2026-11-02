package org.etlforge.integration.structure.crom;

import java.io.Writer;
import org.etlforge.integration.structure.mapping.JtrXmlFormatUtils;
import org.etlforge.integration.structure.mapping.StringIndexMap;
import org.xml.sax.Attributes;

public class CROM_Text extends CROM_ext {
   public static final String ELEMENT = "Text";
   public static final String ELEMENT_ = "Ext";
   private static final String att_StrQual = "StrQual";
   private static final String att_NumQual = "NumQual";
   private static final String att_Delimiter = "Delimiter";
   private static final String att_IdentPos = "IdentPos";
   private static final String att_IdentLength = "IdentLength";
   private String m_StrQual = "";
   private String m_NumQual = "";
   private String m_Delimiter = "";
   private int m_IdentPos = 0;
   private int m_IdentLength = 0;

   public CROM_Text() {
   }

   public CROM_Text(Attributes var1) {
      this.m_StrQual = var1.getValue("StrQual");
      this.m_NumQual = var1.getValue("NumQual");
      this.m_Delimiter = var1.getValue("Delimiter");
      String var2 = var1.getValue("IdentPos");
      if (var2 != null && var2.length() > 0) {
         this.m_IdentPos = Integer.parseInt(var2);
      }

      var2 = var1.getValue("IdentLength");
      if (var2 != null && var2.length() > 0) {
         this.m_IdentLength = Integer.parseInt(var2);
      }
   }

   public String toString(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append('<').append("Text");
      JtrXmlFormatUtils.addAttribute(var2, "StrQual", this.m_StrQual);
      JtrXmlFormatUtils.addAttribute(var2, "NumQual", this.m_NumQual);
      JtrXmlFormatUtils.addAttribute(var2, "Delimiter", this.m_Delimiter);
      JtrXmlFormatUtils.addAttribute(var2, "IdentPos", this.m_IdentPos, 0);
      JtrXmlFormatUtils.addAttribute(var2, "IdentLength", this.m_IdentLength, 0);
      var2.append("/>\n");
      return var2.toString();
   }

   @Override
   public void setStrQual(String var1) {
      this.m_StrQual = var1;
   }

   @Override
   public void setNumQual(String var1) {
      this.m_NumQual = var1;
   }

   @Override
   public void setDelimiter(String var1) {
      this.m_Delimiter = var1;
   }

   @Override
   public void setIdentPos(int var1) {
      this.m_IdentPos = var1;
   }

   @Override
   public void setIdentLength(int var1) {
      this.m_IdentLength = var1;
   }

   @Override
   public String getStrQual() {
      return this.m_StrQual;
   }

   @Override
   public String getNumQual() {
      return this.m_NumQual;
   }

   @Override
   public String getDelimiter() {
      return this.m_Delimiter;
   }

   @Override
   public int getIdentPos() {
      return this.m_IdentPos;
   }

   @Override
   public int getIdentLength() {
      return this.m_IdentLength;
   }

   @Override
   public void writeXml(Writer var1, StringIndexMap var2) {
      StringBuilder var3 = new StringBuilder();

      try {
         var3.append('<').append("Text");
         JtrXmlFormatUtils.addAttribute(var3, "StrQual", this.getStrQual());
         JtrXmlFormatUtils.addAttribute(var3, "NumQual", this.getNumQual());
         JtrXmlFormatUtils.addAttribute(var3, "Delimiter", this.getDelimiter());
         JtrXmlFormatUtils.addAttribute(var3, "IdentPos", this.m_IdentPos, 0);
         JtrXmlFormatUtils.addAttribute(var3, "IdentLength", this.m_IdentLength, 0);
         var3.append("/>\n");
         var1.write(var3.toString());
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public CROM_Text clone() {
      return (CROM_Text)super.clone();
   }
}
