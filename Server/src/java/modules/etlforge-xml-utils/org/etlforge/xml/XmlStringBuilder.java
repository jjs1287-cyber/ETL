package org.etlforge.xml;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.collections.Pair;
import org.etlforge.system.SystemUtils;

public final class XmlStringBuilder {
   private static final String INDENT = "    ";
   private XmlStringBuilder.Element root;
   private final Stack<XmlStringBuilder.Element> elements = new Stack<>();
   private String processingInstruction = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

   public void setProcessingInstruction(String var1) {
      this.processingInstruction = var1;
   }

   public void addElement(String var1) {
      XmlStringBuilder.Element var2 = new XmlStringBuilder.Element(var1);
      if (this.elements.isEmpty() && this.root != null) {
         throw new XmlStringBuilderException("Illegal XML: only one root is allowed.");
      }

      if (this.root == null) {
         this.root = var2;
      }

      if (!this.elements.isEmpty()) {
         this.elements.peek().addChild(var2);
      }

      this.elements.add(var2);
   }

   public void addEmptyElement(String var1) {
      this.addElement(var1);
      this.closeElement();
   }

   public void closeElement() {
      if (this.elements.isEmpty()) {
         throw new XmlStringBuilderException("There is no element to close.");
      }

      this.elements.pop();
   }

   public void closeAllElements() {
      if (this.elements.isEmpty()) {
         throw new XmlStringBuilderException("There are no elements to close.");
      }

      this.elements.removeAllElements();
   }

   public void addAttribute(String var1, String var2) {
      Preconditions.checkNotNull(var1, "name");
      if (var2 == null) {
         var2 = "";
      }

      this.addAttribute(Pair.of(var1, var2));
   }

   public void addAttribute(Pair<String, String> var1) {
      Preconditions.checkNotNull(var1, "attribute");
      if (this.elements.isEmpty()) {
         throw new XmlStringBuilderException("There is no element to add an attribute to.");
      }

      this.elements.peek().addAttribute(var1);
   }

   public void setElementValue(String var1) {
      if (this.elements.isEmpty()) {
         throw new XmlStringBuilderException("There is no element to set a value for.");
      }

      this.elements.peek().setValue(var1);
   }

   public String getXml() {
      if (!this.elements.isEmpty()) {
         throw new XmlStringBuilderException("There are still open elements.");
      }

      StringBuilder var1 = new StringBuilder();
      if (this.processingInstruction != null) {
         var1.append(this.processingInstruction);
         var1.append(SystemUtils.LINE_SEPARATOR);
      }

      if (this.root != null) {
         var1.append(this.root.getXml(""));
      }

      return var1.toString();
   }

   private static class Element {
      private final String name;
      private String value;
      private Set<Pair<String, String>> attributes;
      private List<XmlStringBuilder.Element> children;

      public Element(String var1) {
         this.name = var1;
      }

      public void setValue(String var1) {
         this.value = var1;
      }

      public void addAttribute(Pair<String, String> var1) {
         if (this.attributes == null) {
            this.attributes = new TreeSet<>(Pair.compareFirst());
         }

         this.attributes.add(var1);
      }

      public void addChild(XmlStringBuilder.Element var1) {
         if (this.children == null) {
            this.children = new ArrayList<>();
         }

         this.children.add(var1);
      }

      public String getXml(String var1) {
         StringBuilder var2 = new StringBuilder(var1);
         var2.append("<").append(this.name);
         this.appendAttributes(var2);
         if (this.hasContent()) {
            var2.append(">");
            this.appendValue(var2);
            this.appendChildren(var2, var1);
            if (this.hasChildren()) {
               var2.append(var1);
            }

            var2.append("</").append(this.name).append(">");
         } else {
            var2.append(" />");
         }

         var2.append(SystemUtils.LINE_SEPARATOR);
         return var2.toString();
      }

      private void appendAttributes(StringBuilder var1) {
         if (this.attributes != null) {
            for (Pair var3 : this.attributes) {
               var1.append(" ").append((String)var3.first).append("=\"");
               var1.append(StringEscapeUtils.escapeXml((String)var3.second)).append("\"");
            }
         }
      }

      private boolean hasContent() {
         return this.value != null || this.children != null;
      }

      private boolean hasChildren() {
         return this.children != null;
      }

      private void appendValue(StringBuilder var1) {
         if (this.value != null) {
            var1.append(StringEscapeUtils.escapeXml(this.value));
         }
      }

      private void appendChildren(StringBuilder var1, String var2) {
         if (this.children != null) {
            String var3 = var2 + "    ";
            var1.append(SystemUtils.LINE_SEPARATOR);

            for (XmlStringBuilder.Element var5 : this.children) {
               var1.append(var5.getXml(var3));
            }
         }
      }

      @Override
      public String toString() {
         return this.name;
      }
   }
}
