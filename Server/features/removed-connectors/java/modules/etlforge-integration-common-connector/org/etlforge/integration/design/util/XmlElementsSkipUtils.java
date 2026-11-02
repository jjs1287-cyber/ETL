package org.etlforge.integration.design.util;

import org.etlforge.integration.design.exception.ConnectorNetSuiteDesignRuntimeException;

public class XmlElementsSkipUtils {
   private static char xmlStartAngle = '<';
   private static char xmlEndAngle = '>';
   private static char xmlslash = '/';
   private static char questionmark = '?';
   private boolean stop;
   private boolean isShortElement;
   private final char[] xml;
   private final int len;
   private int currentPosition;
   private int startIndex;
   private int endIndex;

   XmlElementsSkipUtils(char[] var1, int var2) {
      this.xml = var1;
      this.len = var2;
      this.setStartIndex(0);
      this.setEndIndex(var2);
      this.stop = false;
   }

   void skipXmlHeaders() {
      while (this.skipXmlHeader()) {
      }
   }

   void skipElement() {
      this.skipElementStart();
      this.skipElementEnd();
   }

   boolean skipElementEnd() {
      if (this.stop) {
         return false;
      }

      if (!this.locateEndTag()) {
         return false;
      }

      boolean var1 = this.getStartIndex() + 2 < this.getEndIndex();
      if (var1) {
         var1 = this.locateTagReverse(xmlStartAngle, this.getEndIndex() - 2);
         if (var1) {
            var1 = this.xml[this.currentPosition + 1] == xmlslash;
         }
      }

      if (var1) {
         this.setEndIndex(this.currentPosition);
         return var1;
      } else {
         this.stop = true;
         String var2 = "Missing start tag < in " + this.getData(0, this.getEndIndex());
         var2 = var2 + "\n possible reason the internal cache is too small";
         throw new ConnectorNetSuiteDesignRuntimeException(var2);
      }
   }

   boolean skipElementStart() {
      if (this.stop) {
         return false;
      }

      this.isShortElement = true;
      if (!this.locateStartTag()) {
         return false;
      }

      boolean var1 = this.getStartIndex() + 2 < this.getEndIndex();
      if (var1) {
         var1 = this.locateTag(xmlEndAngle, this.getStartIndex() + 2);
      }

      if (var1) {
         this.setStartIndex(this.currentPosition + 1);
         this.isShortElement = this.xml[this.currentPosition - 1] == xmlslash;
         if (this.isShortElement) {
            this.stop = true;
         }

         return var1;
      } else {
         this.stop = true;
         String var2 = "Missing end tag > in " + this.getData(this.getStartIndex(), this.len);
         var2 = var2 + "\n possible reason the internal cache is too small";
         throw new ConnectorNetSuiteDesignRuntimeException(var2);
      }
   }

   private String getData(int var1, int var2) {
      return new String(this.xml, var1, var2 - var1);
   }

   private boolean skipXmlHeader() {
      this.stop = false;
      if (!this.locateStartTag()) {
         return false;
      }

      boolean var1 = this.getStartIndex() + 2 < this.getEndIndex();
      if (var1) {
         boolean var2 = questionmark == this.xml[this.getStartIndex() + 1];
         if (!var2) {
            return false;
         }

         var1 = this.locateTag(xmlEndAngle, this.getStartIndex() + 2);
         if (var1) {
            var1 = questionmark == this.xml[this.currentPosition - 1];
         }
      }

      if (var1) {
         this.setStartIndex(this.currentPosition + 1);
         return var1;
      } else {
         this.stop = true;
         String var3 = "Missing end tag !> in " + this.getData(this.getStartIndex(), this.len);
         var3 = var3 + "\n possible reason the internal cache is too small";
         throw new ConnectorNetSuiteDesignRuntimeException(var3);
      }
   }

   private boolean locateEndTag() {
      if (this.locateTagReverse(xmlEndAngle, this.getEndIndex() - 1)) {
         this.setEndIndex(this.currentPosition + 1);
         return true;
      } else {
         this.setStartIndex(this.setEndIndex(0));
         return false;
      }
   }

   private boolean locateStartTag() {
      if (this.locateTag(xmlStartAngle, this.getStartIndex())) {
         this.setStartIndex(this.currentPosition);
         return true;
      } else {
         this.setStartIndex(this.setEndIndex(0));
         return false;
      }
   }

   private boolean locateTag(char var1, int var2) {
      for (this.currentPosition = var2; this.currentPosition < this.getEndIndex(); this.currentPosition++) {
         if (var1 == this.xml[this.currentPosition]) {
            return true;
         }
      }

      return false;
   }

   private boolean locateTagReverse(char var1, int var2) {
      for (this.currentPosition = var2; this.currentPosition >= this.getStartIndex(); this.currentPosition--) {
         if (var1 == this.xml[this.currentPosition]) {
            return true;
         }
      }

      return false;
   }

   public int getStartIndex() {
      return this.startIndex;
   }

   public void setStartIndex(int var1) {
      this.startIndex = var1;
   }

   public int getEndIndex() {
      return this.endIndex;
   }

   public int setEndIndex(int var1) {
      this.endIndex = var1;
      return var1;
   }
}
