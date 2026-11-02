package org.etlforge.integration.salesforce.soap.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnFacade;
import org.etlforge.ui.layout.TwoColumnLayout;

public final class SoapHeaderUiCollection {
   private final ImmutableList<SoapHeaderUi> collection;

   public static SoapHeaderUiCollection of(Collection<? extends SoapHeaderUi> var0) {
      return new SoapHeaderUiCollection(var0);
   }

   private SoapHeaderUiCollection(Collection<? extends SoapHeaderUi> var1) {
      this.collection = ImmutableList.copyOf(var1);
   }

   public void registerAsChangeSource(InputPanel var1) {
      for (SoapHeaderUi var3 : this.collection) {
         var3.addToParent(var1);
      }
   }

   public void layoutIn(TwoColumnLayout var1) {
      this.layoutIn(var1, false);
   }

   private void layoutIn(TwoColumnLayout var1, boolean var2) {
      for (SoapHeaderUi var4 : this.collection) {
         if (!var2 || !(var4 instanceof FourColumnSoapHeaderUi)) {
            var4.layoutIn(var1);
         }
      }
   }

   public void layoutInTwoColumnFacade(TwoColumnFacade var1) {
      this.layoutIn(var1, true);
      var1.addLastRow();

      for (SoapHeaderUi var3 : this.collection) {
         if (var3 instanceof FourColumnSoapHeaderUi) {
            ((FourColumnSoapHeaderUi)var3).layoutInFourColumns(var1.getFourColumnGrid());
         }
      }
   }

   public void requestFocus() {
      if (!this.collection.isEmpty()) {
         this.collection.get(0).requestFocus();
      }
   }

   public List<SoapHeader> getSoapHeaders() {
      ArrayList var1 = Lists.newArrayList();

      for (SoapHeaderUi var3 : this.collection) {
         var1.add(var3.getDefinedSoapHeader());
      }

      return var1;
   }
}
