package org.etlforge.integration.structure.crom;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.structure.mapping.JtrXmlFormatUtils;
import org.etlforge.integration.structure.mapping.StringIndexMap;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;
import org.xml.sax.Attributes;

public class CROM_Xml extends CROM_ext {
   public static final String ELEMENT = "Xml";
   public static final String IS_ABSTRACT = "IsAbstract";
   public static final String NAMESPACE_QUALIFIED = "NSQual";
   public static final String MIN_USE = "Min";
   public static final String MAX_USE = "Max";
   public static final String NAMESPACE = "NS";
   public static final String SCHEMA_TYPE = "Type";
   public static final String SCHEMA_TYPE_NAMESPACE = "TypeNS";
   public static final String ARRAY_TYPE = "ArrayType";
   public static final String ANY_ELEMENT_NAMESPACE = "AnyNS";
   private static final XmlNameFactory XML_NAME_FACTORY = XmlNameFactory.nonConforming();
   private boolean isAbstract;
   private int minUse = 1;
   private int maxUse = 1;
   private String arrayType;
   private String anyElementNamespace;
   private Set<String> typeExtensionContainer;
   private Set<String> substitutionGroupContainer;
   private ExtraAttribute extra;

   public CROM_Xml() {
   }

   public CROM_Xml(Attributes var1, Map<Integer, String> var2) {
      this.setAbstract(JtrXmlFormatUtils.getBool(var1, "IsAbstract", false));
      this.setNSQualify(JtrXmlFormatUtils.getBool(var1, "NSQual", false));
      this.setMinUse(JtrXmlFormatUtils.getInt(var1, "Min", 1));
      this.setMaxUse(JtrXmlFormatUtils.getInt(var1, "Max", 1));
      this.setArrayType(JtrXmlFormatUtils.lookupIndexedString(var1, "ArrayType", var2));
      this.setNamespace(JtrXmlFormatUtils.lookupIndexedString(var1, "NS", var2));
      this.setSchemaType(JtrXmlFormatUtils.lookupIndexedString(var1, "Type", var2));
      this.setSchemaTypeNamespace(JtrXmlFormatUtils.lookupIndexedString(var1, "TypeNS", var2));
      this.anyElementNamespace = JtrXmlFormatUtils.lookupIndexedString(var1, "AnyNS", var2);
   }

   @Override
   public void addTypeExtension(String var1) {
      if (this.typeExtensionContainer == null) {
         this.typeExtensionContainer = new HashSet<>();
      }

      this.typeExtensionContainer.add(var1);
   }

   @Override
   public void addSubstitutionGroup(String var1) {
      if (this.substitutionGroupContainer == null) {
         this.substitutionGroupContainer = new HashSet<>();
      }

      this.substitutionGroupContainer.add(var1);
   }

   @Override
   public String getNamespace() {
      return this.extra == null ? null : this.extra.m_Namespace;
   }

   @Override
   public boolean isAbstract() {
      return this.isAbstract;
   }

   private void setAbstract(boolean var1) {
      this.isAbstract = var1;
   }

   public int getMinUse() {
      return this.minUse;
   }

   public int getMaxUse() {
      return this.maxUse;
   }

   public String getArrayType() {
      return this.arrayType;
   }

   public String getAnyElementNamespace() {
      return this.anyElementNamespace;
   }

   public void setMinUse(int var1) {
      this.minUse = var1;
   }

   public void setMaxUse(int var1) {
      this.maxUse = var1;
   }

   public void setArrayType(String var1) {
      this.arrayType = var1;
   }

   public void setAnyElementNamespace(String var1) {
      this.anyElementNamespace = var1;
   }

   @Override
   public boolean isXml() {
      return true;
   }

   private boolean hasSubElements() {
      return this.typeExtensionContainer != null && this.typeExtensionContainer.size() > 0
         || this.substitutionGroupContainer != null && this.substitutionGroupContainer.size() > 0;
   }

   @Override
   public String getSchemaType() {
      return this.extra == null ? null : this.extra.m_SchemaType;
   }

   @Override
   public String getSchemaTypeExpandedName() {
      if (this.extra == null) {
         return null;
      } else {
         String var1 = this.getSchemaType();
         if (var1 != null && var1.length() != 0) {
            ExpandedName var2 = XML_NAME_FACTORY.newExpandedName(this.getSchemaTypeNamespace(), this.getSchemaType());
            return var2.toString();
         } else {
            return null;
         }
      }
   }

   @Override
   public boolean hasDerivedTypes() {
      return this.typeExtensionContainer != null && this.typeExtensionContainer.size() > 0;
   }

   @Override
   public boolean hasSubstitutionGroup() {
      return this.substitutionGroupContainer != null && this.substitutionGroupContainer.size() > 0;
   }

   @Override
   public Set<String> getTypeExtensions() {
      return this.typeExtensionContainer;
   }

   @Override
   public Set<String> getSubsitutionGroup() {
      return this.substitutionGroupContainer;
   }

   @Override
   public boolean getNSQualify() {
      return this.extra == null ? false : this.extra.m_NSQualify;
   }

   @Override
   public String getSchemaTypeNamespace() {
      return this.extra == null ? null : this.extra.m_SchemaTypeNamespace;
   }

   @Override
   public void setNamespace(String var1) {
      if (var1 != null || this.extra != null) {
         if (this.extra == null) {
            this.extra = new ExtraAttribute();
         }

         this.extra.m_Namespace = var1;
      }
   }

   @Override
   public void setNSQualify(boolean var1) {
      if (this.extra != null || var1) {
         if (this.extra == null) {
            this.extra = new ExtraAttribute();
         }

         this.extra.m_NSQualify = var1;
      }
   }

   @Override
   public void setSchemaType(String var1) {
      if (var1 != null || this.extra != null) {
         if (this.extra == null) {
            this.extra = new ExtraAttribute();
         }

         this.extra.m_SchemaType = var1;
      }
   }

   @Override
   public void setSchemaTypeNamespace(String var1) {
      if (var1 != null || this.extra != null) {
         if (this.extra == null) {
            this.extra = new ExtraAttribute();
         }

         this.extra.m_SchemaTypeNamespace = var1;
      }
   }

   @Override
   public void clearSubstitutionGroupContainer() {
      if (this.substitutionGroupContainer != null) {
         this.substitutionGroupContainer.clear();
      }

      if (this.typeExtensionContainer != null) {
         this.typeExtensionContainer.clear();
      }
   }

   @Override
   public void writeXml(Writer var1, StringIndexMap var2) throws IOException {
      StringBuilder var3 = new StringBuilder();
      var3.append('<').append("Xml");
      JtrXmlFormatUtils.addAttribute(var3, "IsAbstract", this.isAbstract(), false);
      JtrXmlFormatUtils.addAttribute(var3, "NSQual", this.getNSQualify(), false);
      JtrXmlFormatUtils.addAttribute(var3, "Min", this.getMinUse(), 1);
      int var4 = this.getMaxUse();
      if (var4 == Integer.MAX_VALUE) {
         var4 = -1;
      }

      JtrXmlFormatUtils.addAttribute(var3, "Max", var4, 1);
      JtrXmlFormatUtils.addAttribute(var3, "ArrayType", var2.store(this.getArrayType()));
      JtrXmlFormatUtils.addAttribute(var3, "NS", var2.store(this.getNamespace()));
      JtrXmlFormatUtils.addAttribute(var3, "Type", var2.store(this.getSchemaType()));
      JtrXmlFormatUtils.addAttribute(var3, "TypeNS", var2.store(this.getSchemaTypeNamespace()));
      JtrXmlFormatUtils.addAttribute(var3, "AnyNS", var2.store(this.getAnyElementNamespace()));
      var1.write(var3.toString());
      boolean var5 = this.hasSubElements();
      if (var5) {
         var1.write(">\n");
      }

      if (this.typeExtensionContainer != null && this.typeExtensionContainer.size() > 0) {
         StringBuilder var6 = new StringBuilder();
         var6.append('<').append("Extensions");
         StringBuilder var7 = new StringBuilder();

         for (String var9 : this.typeExtensionContainer) {
            if (var7.length() > 0) {
               var7.append(",");
            }

            var7.append(var2.store(var9));
         }

         JtrXmlFormatUtils.addAttribute(var6, "Types", var7.toString());
         var6.append("/>\n");
         var1.write(var6.toString());
      }

      if (this.substitutionGroupContainer != null && this.substitutionGroupContainer.size() > 0) {
         StringBuilder var10 = new StringBuilder();
         var10.append('<').append("Substitutions");
         StringBuilder var11 = new StringBuilder();

         for (String var13 : this.substitutionGroupContainer) {
            if (var11.length() > 0) {
               var11.append(",");
            }

            var11.append(var2.store(var13));
         }

         JtrXmlFormatUtils.addAttribute(var10, "Names", var11.toString());
         var10.append("/>\n");
         var1.write(var10.toString());
      }

      if (var5) {
         var1.write("</" + "Xml" + ">\n");
      } else {
         var1.write("/>\n");
      }
   }

   public CROM_Xml clone() {
      CROM_Xml var1 = (CROM_Xml)super.clone();
      if (this.typeExtensionContainer != null) {
         var1.typeExtensionContainer = new HashSet<>(this.typeExtensionContainer);
      }

      if (this.substitutionGroupContainer != null) {
         var1.substitutionGroupContainer = new HashSet<>(this.substitutionGroupContainer);
      }

      if (this.extra != null) {
         var1.extra = this.extra.clone();
      }

      return var1;
   }
}
