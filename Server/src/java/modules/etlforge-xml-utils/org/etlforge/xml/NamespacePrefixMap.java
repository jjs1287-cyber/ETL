package org.etlforge.xml;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;

public final class NamespacePrefixMap {
   private final Multimap<String, String> uriToPrefix = HashMultimap.create();
   private final Map<String, String> prefixToUri = Maps.newHashMap();
   private String defaultNamespace;

   public void add(String var1, String var2) {
      boolean var3 = false;
      if (var1 == null || var1.length() == 0) {
         var1 = "";
         this.defaultNamespace = var2;
         var3 = true;
      }

      if (var2 != null && var2.length() > 0) {
         this.prefixToUri.put(var1, var2);
         this.uriToPrefix.put(var2, var1);
      } else if (!var3) {
         throw new IllegalArgumentException("A prefix cannot be mapped to an empty namespace URI.");
      }
   }

   public String getNamespaceURI(String var1) {
      return this.prefixToUri.get(var1);
   }

   public Collection<String> getPrefixes(String var1) {
      return this.uriToPrefix.get(var1);
   }

   public boolean isDefaultNamespace(String var1) {
      return Objects.equal(var1, this.defaultNamespace);
   }

   public String getDefaultNamespace() {
      return this.defaultNamespace;
   }

   public ExpandedName expand(QualifiedName var1, XmlNameFactory var2) {
      String var3 = var1.getPrefix();
      String var4 = null;
      if (var3.length() == 0) {
         var4 = this.defaultNamespace;
      } else {
         var4 = this.getNamespaceURI(var3);
      }

      return var2.newExpandedName(var4, var1.getLocalName());
   }

   public QualifiedName collapse(ExpandedName var1, XmlNameFactory var2) {
      String var3 = var1.getNamespace();
      String var4 = this.getPrefix(var3);
      return var2.newQualifiedName(var4, var1.getLocalName());
   }

   private String getPrefix(String var1) {
      return var1.length() != 0 && !var1.equals(this.defaultNamespace) ? this.aPrefix(var1) : "";
   }

   private String aPrefix(String var1) {
      Collection var2 = this.uriToPrefix.get(var1);
      return var2.isEmpty() ? "" : (String)var2.iterator().next();
   }
}
