package org.etlforge.xml.xsd;

import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.xml.Elements;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.QualifiedName;
import org.etlforge.xml.SimpleNamespaceContext;
import org.etlforge.xml.XmlNameFactory;
import org.w3c.dom.Element;

public final class DerivedTypesCollector {
   private final Iterable<Element> schemas;
   private String targetNamespace;
   private Set<DerivedTypesCollector.TypeDef> derivedTypes;
   private ExpandedName baseType;
   private String currentTargetNamespace;
   private final XmlNameFactory nameFactory;

   public DerivedTypesCollector(Iterable<Element> var1) {
      this.schemas = var1;
      this.nameFactory = XmlNameFactory.conforming();
   }

   public void setTargetNamespace(String var1) {
      this.targetNamespace = var1;
   }

   public Set<DerivedTypesCollector.TypeDef> collectDerivedTypes(ExpandedName var1) {
      this.baseType = var1;
      this.derivedTypes = Sets.newHashSet();
      SimpleNamespaceContext var2 = new SimpleNamespaceContext();
      var2.add("xs", "http://www.w3.org/2001/XMLSchema");
      String var3 = "./xs:complexType/xs:complexContent/xs:extension";

      for (Element var5 : this.schemas) {
         this.setCurrentTargetNamespace(var5);

         for (Element var7 : Elements.matching(var3, var5, var2)) {
            this.processPossibleMatch(var7);
         }
      }

      return this.derivedTypes;
   }

   private void setCurrentTargetNamespace(Element var1) {
      this.currentTargetNamespace = var1.getAttribute("targetNamespace");
      if (StringUtils.isEmpty(this.currentTargetNamespace)) {
         this.currentTargetNamespace = this.targetNamespace;
      }

      if (StringUtils.isEmpty(this.currentTargetNamespace)) {
         this.currentTargetNamespace = "";
      }
   }

   private void processPossibleMatch(Element var1) {
      if (this.isCorrectExtension(var1)) {
         ExpandedName var2 = this.getTypeName(var1);
         this.derivedTypes.add(new DerivedTypesCollector.TypeDef(var2, var1));
      }
   }

   private boolean isCorrectExtension(Element var1) {
      String var2 = var1.getAttribute("base");
      if (StringUtils.isNotBlank(var2)) {
         QualifiedName var3 = this.qname(var2);
         String var4 = var1.lookupNamespaceURI(var3.getPrefix());
         ExpandedName var5 = this.nameFactory.newExpandedName(var4, var3.getLocalName());
         return var5.equals(this.baseType);
      } else {
         return false;
      }
   }

   private ExpandedName getTypeName(Element var1) {
      Element var2 = (Element)var1.getParentNode().getParentNode();
      String var3 = var2.getAttribute("name");
      return this.nameFactory.newExpandedName(this.currentTargetNamespace, var3);
   }

   private QualifiedName qname(String var1) {
      return this.nameFactory.newQualifiedName(var1);
   }

   public static final class TypeDef {
      public final ExpandedName name;
      public final Element definition;

      public TypeDef(ExpandedName var1, Element var2) {
         this.name = var1;
         this.definition = var2;
      }
   }
}
