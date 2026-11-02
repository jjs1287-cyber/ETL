package org.etlforge.integration.structure.mapping;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Attr;
import org.etlforge.integration.structure.crom.CROM_DB;
import org.etlforge.integration.structure.crom.CROM_Text;
import org.etlforge.integration.structure.crom.CROM_Xml;
import org.etlforge.integration.structure.crom.CromTreeException;
import org.etlforge.integration.structure.crom.CromType;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public final class MappingStructureToCromConverter extends DefaultHandler {
   public void convert(MappingTreeStructure var1, MappingStructureReadingHandler var2) throws CromTreeException {
      Preconditions.checkNotNull(var2, "handler");

      try {
         SAXParser var3 = SAXParserFactory.newInstance().newSAXParser();
         MappingStructureToCromConverter.StringIndexMapReader var9 = new MappingStructureToCromConverter.StringIndexMapReader();
         var3.parse(new InputSource(var1.getReader()), var9);
         var3.parse(new InputSource(var1.getReader()), new MappingStructureToCromConverter.XmlHandler(var2, var9.stringMap));
      } catch (ParserConfigurationException var6) {
         throw new CromTreeException(
            "Failed to create the XML parser. This indicates a serious problem with the Java platform. Reported error: " + var6.getMessage(), var6
         );
      } catch (SAXException var7) {
         String var4 = "The XML representation of the mapping structure is incorrect. Reported error: " + var7.getMessage();
         if (var7 instanceof SAXParseException) {
            SAXParseException var5 = (SAXParseException)var7;
            var4 = var4 + " The error occurred at line " + var5.getLineNumber() + ", column " + var5.getColumnNumber() + ".";
         }

         throw new CromTreeException(var4, var7);
      } catch (IOException var8) {
         throw new CromTreeException("An I/O error occurred when reading mapping structure information. Reported error: " + var8.getMessage(), var8);
      }
   }

   private static class StringIndexMapReader extends DefaultHandler {
      final Map<Integer, String> stringMap = Maps.newHashMap();

      public StringIndexMapReader() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) {
         if (var3.equals("String")) {
            this.stringMap.put(Integer.valueOf(var4.getValue("idx")), var4.getValue("val"));
         }
      }
   }

   private static class XmlHandler extends DefaultHandler {
      private static final int t_Element = 1;
      private static final int t_ElementType = 2;
      private static final int t_ElementTypeCROM = 3;
      private static final int t_CROM = 4;
      private static final int t_ending = 0;
      private final MappingStructureReadingHandler handler;
      private final Map<Integer, String> stringMap;
      private int status = 4;
      private String ElementType_TypeName;
      private final Stack<String> qNames;
      private final Stack<CROM> crom_stack;
      private CROM current;
      private CROM_Xml crom_xml;
      private CromType crom_type;

      public XmlHandler(MappingStructureReadingHandler var1, Map<Integer, String> var2) {
         this.handler = var1;
         this.stringMap = var2;
         this.qNames = new Stack<>();
         this.crom_stack = new Stack<>();
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) {
         if (this.status != 0) {
            if (var3.equals("JTR")) {
               String var5 = JtrXmlFormatUtils.getString(var4, "Type");
               String var6 = JtrXmlFormatUtils.getString(var4, "BeginQuote");
               String var7 = JtrXmlFormatUtils.getString(var4, "EndQuote");
               this.handler.setCromType(var5);
               this.crom_type = CromType.fromName(var5);
               this.handler.setDBQuotes(var6, var7);
            } else if (var3.equals("Elements")) {
               this.status = 1;
            } else if (var3.equals("ElementTypes")) {
               this.status = 2;
            } else if (var3.equals("ElementType")) {
               this.ElementType_TypeName = JtrXmlFormatUtils.lookupIndexedString(var4, "Name", this.stringMap);
               this.status = 3;
            } else if (var3.equals("CROM")) {
               CROM var8 = this.crom_stack.size() > 0 ? this.crom_stack.peek() : null;
               this.current = new CROM(var8, var4, this.stringMap, this.crom_type);
               if (this.current.isPrecondition()) {
                  String var10 = this.current.getName();
                  if (var10 == null || var10.length() == 0) {
                     this.current.setName("/PRECONDITION/");
                  }
               }

               this.crom_stack.push(this.current);
               switch (this.status) {
                  case 1:
                  case 2:
                  default:
                     break;
                  case 3:
                     if (var8 == null && this.ElementType_TypeName != null) {
                        this.handler.addType(this.ElementType_TypeName, this.current);
                        this.ElementType_TypeName = null;
                     }
                     break;
                  case 4:
                     if (var8 == null) {
                        this.current.addType(32768L);
                        this.handler.setRoot(this.current);
                     }
               }
            } else if (var3.equals("Text") || var3.equals("Ext")) {
               this.crom_stack.peek().setExtension(new CROM_Text(var4));
            } else if (var3.equals("DB") || var3.equals("Ext")) {
               this.crom_stack.peek().setExtension(new CROM_DB(var4));
            } else if (var3.equals("Xml")) {
               this.crom_xml = new CROM_Xml(var4, this.stringMap);
               this.crom_stack.peek().setExtension(this.crom_xml);
               if (this.status == 1 && this.crom_stack.size() == 1) {
                  this.handler.addElement(this.crom_stack.peek());
               }
            } else if (var3.equals("Attr")) {
               CROM_Attr var9 = new CROM_Attr(var4, this.stringMap);
               CROM var11 = this.crom_stack.peek();
               var11.setExtension(var9);
               String var12 = var9.getValueType();
               if (var11.cromType == CromType.CROM_t_Text && var12 != null && !var12.equals("Identifier")) {
                  var11.addType(32L);
               }
            } else if (var3.equals("Extensions")) {
               this.parseXmlTypeExtensions(var4);
            } else if (var3.equals("Substitutions")) {
               this.parseXmlSubstitutionGroups(var4);
            }

            this.qNames.push(var3);
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) {
         if (this.qNames.pop().equals(var3)) {
            if (var3.equals("Elements")) {
               this.status = 4;
            } else if (var3.equals("ElementTypes")) {
               this.status = 4;
            } else if (var3.equals("ElementType")) {
               this.status = 2;
            } else if (var3.equals("CROM")) {
               CROM var4 = this.crom_stack.pop();
               if (this.status == 4 && !var4.isSubstitutionGroup()) {
                  this.handler.checkDerivedTypeHolder(var4);
               }

               this.current = this.crom_stack.size() == 0 ? null : this.crom_stack.peek();
               if (this.qNames.peek().equals("Jtr") && this.crom_stack.size() == 0) {
                  this.status = 0;
               }
            } else if (var3.equals("Xml")) {
               this.crom_xml = null;
            }
         }
      }

      private void parseXmlTypeExtensions(Attributes var1) {
         String var2 = var1.getValue("Types");

         for (String var4 : this.translateIndexList(var2)) {
            this.crom_xml.addTypeExtension(var4);
         }
      }

      private void parseXmlSubstitutionGroups(Attributes var1) {
         String var2 = var1.getValue("Names");

         for (String var4 : this.translateIndexList(var2)) {
            this.crom_xml.addSubstitutionGroup(var4);
         }
      }

      private List<String> translateIndexList(String var1) {
         ArrayList var2 = new ArrayList();
         if (StringUtils.isNotEmpty(var1)) {
            for (String var6 : var1.split(",")) {
               String var7 = this.stringMap.get(Integer.valueOf(var6));
               if (StringUtils.isNotEmpty(var7)) {
                  var2.add(var7);
               }
            }
         }

         return var2;
      }
   }
}
