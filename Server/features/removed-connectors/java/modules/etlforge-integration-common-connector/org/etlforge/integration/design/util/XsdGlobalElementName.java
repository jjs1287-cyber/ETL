package org.etlforge.integration.design.util;

import com.google.common.base.Strings;
import java.io.Serializable;
import org.etlforge.integration.design.netsuite.definition.NetSuiteConstants;

public class XsdGlobalElementName implements Serializable {
   private final String namespace;
   private final String localName;
   private final boolean isElement;

   private XsdGlobalElementName(String var1, String var2, boolean var3) {
      this.namespace = var1;
      this.localName = var2;
      this.isElement = var3;
   }

   public static XsdGlobalElementName newInstance(String var0, String var1, boolean var2) {
      return new XsdGlobalElementName(var0, var1, var2);
   }

   public static XsdGlobalElementName newType(String var0, String var1) {
      return new XsdGlobalElementName(var0, var1, false);
   }

   public static XsdGlobalElementName newElement(String var0, String var1) {
      return new XsdGlobalElementName(var0, var1, true);
   }

   public String toExpandName() {
      return Strings.isNullOrEmpty(this.namespace) ? this.localName : "{" + this.namespace + "}" + this.localName;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public String getLocalName() {
      return this.localName;
   }

   public boolean isElement() {
      return this.isElement;
   }

   public boolean isStandard() {
      return NetSuiteConstants.instance.isStandardNamespace(this.namespace);
   }

   @Override
   public int hashCode() {
      return this.toString().hashCode();
   }

   @Override
   public String toString() {
      return this.toExpandName() + '(' + this.isElement + ')';
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 instanceof XsdGlobalElementName) {
         XsdGlobalElementName var2 = (XsdGlobalElementName)var1;
         if (!var2.localName.equals(this.localName)) {
            return false;
         } else {
            return var2.isElement != this.isElement ? false : var2.namespace.equals(this.namespace);
         }
      } else {
         return false;
      }
   }
}
