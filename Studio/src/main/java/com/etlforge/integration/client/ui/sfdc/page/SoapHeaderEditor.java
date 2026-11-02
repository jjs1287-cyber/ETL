package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.etlforge.integration.client.sfdc.data.SoapHeaderTranslator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.salesforce.soap.DefaultSoapHeaderLookup;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUi;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUiFactory;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class SoapHeaderEditor {
   private final SalesforceWizardEntity entity;
   private final SoapHeaderTranslator translator;
   private final SoapHeaderEditor.SoapHeaderMap headers;
   private final LinkedHashMap<String, SoapHeaderUi> editors;

   public SoapHeaderEditor(SalesforceWizardEntity var1, SoapHeaderTranslator var2, SoapHeaderUiFactory var3) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "()");
      this.entity = var1;
      this.translator = var2;
      this.headers = new SoapHeaderEditor.SoapHeaderMap(var1, var2);
      this.editors = this.createEditors(var3);
   }

   private LinkedHashMap<String, SoapHeaderUi> createEditors(SoapHeaderUiFactory var1) {
      LinkedHashMap var2 = Maps.newLinkedHashMap();
      Comparator var3 = var1.getPreferredOrder();

      for (Object __etlItem42 : this.headers.getHeadersInOrder(var3)) {
         SoapHeader var5 = (SoapHeader) __etlItem42;
         SoapHeaderUi var6 = var1.createUi(var5);
         if (var6 != null) {
            var2.put(var5.getName(), var6);
         }
      }

      return var2;
   }

   public Set<SoapHeader> getOriginalHeaders() {
      return this.headers.getIncomingHeaders();
   }

   public List<SoapHeaderUi> getEditors() {
      return Lists.newArrayList(this.editors.values());
   }

   public Set<SoapHeader> getEditedHeaders() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "getEditors");
      HashMap var1 = Maps.newHashMap();

      for (Entry var3 : this.editors.entrySet()) {
         String var4 = (String)var3.getKey();
         SoapHeader var5 = ((SoapHeaderUi)var3.getValue()).getDefinedSoapHeader();
         var1.put(var4, var5);
      }

      this.headers.fillInNotEditedHeaders(var1);
      return Sets.newHashSet(var1.values());
   }

   public void applyToItem() {
      HashSet var1 = Sets.newHashSet();

      for (SoapHeader var3 : this.getEditedHeaders()) {
         var1.add(this.translator.toDataStoreRepresentation(var3));
      }

      this.entity.setSoapHeaders(var1);
   }

   private static class SoapHeaderMap {
      private final Map<String, SoapHeader> headers = Maps.newHashMap();
      private final Set<SoapHeader> incoming = Sets.newHashSet();

      public SoapHeaderMap(SalesforceWizardEntity var1, SoapHeaderTranslator var2) {
         for (SalesforceWizardEntity.Header var4 : var1.getSoapHeaders()) {
            SoapHeader var5 = var2.toSoapHeader(var4);
            this.incoming.add(var5);
            this.add(var5);
         }

         this.addMissingHeaders(var1);
      }

      private void add(SoapHeader var1) {
         this.headers.put(var1.getName(), var1);
      }

      private void addMissingHeaders(SalesforceWizardEntity var1) {
         for (SoapHeader var4 : DefaultSoapHeaderLookup.getDefaultHeaders(var1.getMethod())) {
            if (!this.headers.containsKey(var4.getName())) {
               this.add(var4);
            }
         }
      }

      public List<SoapHeader> getHeadersInOrder(Comparator<SoapHeader> var1) {
         ArrayList var2 = Lists.newArrayList(this.headers.values());
         Collections.sort(var2, var1);
         return var2;
      }

      public Set<SoapHeader> getIncomingHeaders() {
         return Sets.newHashSet(this.incoming);
      }

      public void fillInNotEditedHeaders(Map<String, SoapHeader> var1) {
         for (Entry<String,SoapHeader> var3 : this.headers.entrySet()) {
            if (!var1.containsKey(var3.getKey())) {
               var1.put(var3.getKey(), var3.getValue());
            }
         }
      }
   }
}

