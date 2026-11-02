package org.etlforge.ui.xml;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import javax.swing.JTextPane;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.collections.Pair;
import org.etlforge.lang.exception.ExceptionUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.text.ColorTextPane;
import org.etlforge.ui.widget.text.KongaTextPane;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class XmlTextViewer implements UiProvider {
   private static final String INDENT = "    ";
   private final ColorTextPane textPane;

   public XmlTextViewer() {
      this(new KongaTextPane());
   }

   public XmlTextViewer(JTextPane var1) {
      this.textPane = new ColorTextPane(var1);
      this.textPane.setEditable(false);
      this.textPane.setText("");
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.textPane.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.textPane.requestFocus();
   }

   public void setPreferredSize(Dimension var1) {
      this.textPane.setPreferredSize(var1);
   }

   public void view(String var1) {
      try {
         XmlTextViewer.Element var2 = this.getRoot(var1);
         this.renderXml(var2);
      } catch (Exception var3) {
         this.textPane.setText(UiResourceBundle.getString("XmlTextViewer.Error.InvalidXmlString"), Color.RED);
         this.textPane.append(ExceptionUtils.getStackTrace(var3), Color.RED);
      }
   }

   public void view(File var1) {
      try {
         XmlTextViewer.Element var2 = this.getRoot(var1);
         this.renderXml(var2);
      } catch (Exception var3) {
         this.textPane.setText(UiResourceBundle.getString("XmlTextViewer.Error.InvalidXmlFile"), Color.RED);
         this.textPane.append(ExceptionUtils.getStackTrace(var3), Color.RED);
      }
   }

   public String getXml() {
      return this.textPane.getText();
   }

   private XmlTextViewer.Element getRoot(String var1) throws Exception {
      XmlTextViewer.RootFactory var2 = new XmlTextViewer.RootFactory();
      SaxParsers.parseString(var1, var2, true);
      return var2.getRoot();
   }

   private XmlTextViewer.Element getRoot(File var1) throws Exception {
      XmlTextViewer.RootFactory var2 = new XmlTextViewer.RootFactory();
      SaxParsers.parseFile(var1, var2, true);
      return var2.getRoot();
   }

   private void renderXml(final XmlTextViewer.Element var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            XmlTextViewer.this.textPane.clear();
            var1.render(XmlTextViewer.this.textPane);
            XmlTextViewer.this.textPane.setCaretPosition(0);
         }
      });
   }

   private static class Element {
      private final String name;
      private String value;
      private Set<Pair<String, String>> attributes;
      private List<XmlTextViewer.Element> children;

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

      public void addChild(XmlTextViewer.Element var1) {
         if (this.children == null) {
            this.children = new ArrayList<>();
         }

         this.children.add(var1);
      }

      public void render(ColorTextPane var1) {
         this.renderImpl(var1, "");
      }

      private void renderImpl(ColorTextPane var1, String var2) {
         var1.append(var2);
         var1.append("<", XmlTextViewer.Element.Colors.DELIMITER);
         var1.append(this.name, XmlTextViewer.Element.Colors.ELEMENT);
         this.renderAttributes(var1);
         if (this.hasContent()) {
            if (this.hasValue() || this.hasChildren()) {
               var1.append(">", XmlTextViewer.Element.Colors.DELIMITER);
            }

            this.appendValue(var1);
            this.appendChildren(var1, var2);
            if (this.hasChildren()) {
               var1.append(var2);
            }
         }

         this.renderClosingTag(var1);
      }

      private void renderClosingTag(ColorTextPane var1) {
         if (!this.hasValue() && !this.hasChildren()) {
            var1.append(" />", XmlTextViewer.Element.Colors.DELIMITER);
         } else {
            var1.append("</", XmlTextViewer.Element.Colors.DELIMITER);
            var1.append(this.name, XmlTextViewer.Element.Colors.ELEMENT);
            var1.append(">", XmlTextViewer.Element.Colors.DELIMITER);
         }

         var1.append("\n");
      }

      private boolean hasValue() {
         return this.value != null && this.value.length() > 0;
      }

      private void renderAttributes(ColorTextPane var1) {
         if (this.attributes != null) {
            for (Pair var3 : this.attributes) {
               var1.append(" ");
               var1.append((String)var3.first, XmlTextViewer.Element.Colors.ATTRIBUTE);
               var1.append("=\"", XmlTextViewer.Element.Colors.DELIMITER);
               var1.append(StringEscapeUtils.escapeXml((String)var3.second), XmlTextViewer.Element.Colors.TEXT);
               var1.append("\"", XmlTextViewer.Element.Colors.DELIMITER);
            }
         }
      }

      private boolean hasContent() {
         return this.value != null || this.children != null;
      }

      private boolean hasChildren() {
         return this.children != null;
      }

      private void appendValue(ColorTextPane var1) {
         if (this.hasValue()) {
            var1.append(StringEscapeUtils.unescapeXml(this.value), XmlTextViewer.Element.Colors.TEXT);
         }
      }

      private void appendChildren(ColorTextPane var1, String var2) {
         if (this.children != null) {
            String var3 = var2 + "    ";
            var1.append("\n");

            for (XmlTextViewer.Element var5 : this.children) {
               var5.renderImpl(var1, var3);
            }
         }
      }

      private static final class Colors {
         public static final Color ELEMENT = new Color(139, 69, 19);
         public static final Color ATTRIBUTE = new Color(255, 50, 35);
         public static final Color DELIMITER = Color.BLUE;
         public static final Color TEXT = Color.BLACK;
      }
   }

   private static class RootFactory extends DefaultHandler {
      private StringBuilder value;
      private XmlTextViewer.Element root;
      private final Stack<XmlTextViewer.Element> elements = new Stack<>();

      private RootFactory() {
      }

      public XmlTextViewer.Element getRoot() {
         return this.root;
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         this.elements.peek().setValue(this.value.toString().trim());
         this.elements.pop();
         super.endElement(var1, var2, var3);
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.value = new StringBuilder();
         super.startElement(var1, var2, var3, var4);
         this.addElement(var3);
         int var5 = 0;

         for (int var6 = var4.getLength(); var5 < var6; var5++) {
            this.addAttribute(var4.getQName(var5), var4.getValue(var5));
         }
      }

      private void addElement(String var1) {
         XmlTextViewer.Element var2 = new XmlTextViewer.Element(var1);
         if (this.root == null) {
            this.root = var2;
         }

         if (!this.elements.isEmpty()) {
            this.elements.peek().addChild(var2);
         }

         this.elements.add(var2);
      }

      private void addAttribute(String var1, String var2) {
         Pair var3 = Pair.of(var1, var2);
         this.elements.peek().addAttribute(var3);
      }

      @Override
      public void characters(char[] var1, int var2, int var3) {
         this.value.append(new String(var1, var2, var3));
      }
   }
}
