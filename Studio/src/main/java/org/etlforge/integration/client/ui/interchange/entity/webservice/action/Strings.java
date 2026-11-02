package org.etlforge.integration.client.ui.interchange.entity.webservice.action;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;

public final class Strings extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.name("action.ic.CreateWsRequest"), "Create Request Transformation..."},
      {ActionDictionaryKeys.tooltip("action.ic.CreateWsRequest"), "Create a request transformation for this Web Service Call"},
      {ActionDictionaryKeys.name("action.ic.CreateWsResponse"), "Create Response Transformation..."},
      {ActionDictionaryKeys.tooltip("action.ic.CreateWsResponse"), "Create a response transformation for this Web Service Call"},
      {ActionDictionaryKeys.name(GenerateSoapSchemasAction.ID), "Generate Request and Response XSD..."},
      {ActionDictionaryKeys.tooltip(GenerateSoapSchemasAction.ID), "Generate XML schemas representing the request and response"}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
