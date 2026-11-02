package com.etlforge.integration.server.engine.bulkload.rest;

import org.etlforge.xml.XmlStringBuilder;

final class RequestBuilder {
   private final XmlStringBuilder xml = new XmlStringBuilder();

   public RequestBuilder() {
      this.xml.addElement("jobInfo");
      this.xml.addAttribute("xmlns", "http://www.force.com/2009/06/asyncapi/dataload");
   }

   public void addElement(String var1, String var2) {
      this.xml.addElement(var1);
      this.xml.setElementValue(var2);
      this.xml.closeElement();
   }

   public String close() {
      this.xml.closeAllElements();
      return this.xml.getXml().trim();
   }
}
