package org.etlforge.integration.client.ui.interchange.entity.webservice.util;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.ui.layout.TwoColumnLayout;

public class WebServiceOperationInfoViewer {
   private static final String[] INFO_PROPERTIES = new String[]{"Service", "Port", "Binding", "Transport", "SoapAction", "SoapVersion"};
   private static final String[] INFO_LABEL_NAMES = new String[]{
      PackageResources.InfoViewer.SERVICE_NAME,
      PackageResources.InfoViewer.PORT_NAME,
      PackageResources.InfoViewer.BINDING_NAME,
      PackageResources.InfoViewer.TRANSPORT_TYPE,
      PackageResources.InfoViewer.SOAP_ACTION,
      PackageResources.InfoViewer.SOAP_VERSION
   };
   private final String[] properties;
   private final List<JLabel> nameLabels = Lists.newArrayList();
   private final JLabel[] values;

   public WebServiceOperationInfoViewer() {
      this.properties = this.getPropertiesToInclude();
      this.values = new JLabel[this.properties.length];

      for (int var1 = 0; var1 < this.values.length; var1++) {
         this.values[var1] = new JLabel();
      }
   }

   private String[] getPropertiesToInclude() {
      ArrayList var1 = Lists.newArrayList();

      for (int var2 = 0; var2 < INFO_PROPERTIES.length; var2++) {
         String var3 = INFO_PROPERTIES[var2];
         if (this.isIncluded(var3)) {
            var1.add(var3);
            this.nameLabels.add(TextStyles.CaptionText.makeLabel(INFO_LABEL_NAMES[var2] + ":"));
         }
      }

      return (String[]) var1.toArray(new String[var1.size()]);
   }

   protected boolean isIncluded(String var1) {
      return true;
   }

   public void displayOperationInfo(final WebServiceOperationInfo var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (var1 == null) {
               for (JLabel var4 : WebServiceOperationInfoViewer.this.values) {
                  var4.setText("");
               }
            } else {
               for (int var5 = 0; var5 < WebServiceOperationInfoViewer.this.values.length; var5++) {
                  WebServiceOperationInfoViewer.this.values[var5].setText(var1.getProperty(WebServiceOperationInfoViewer.this.properties[var5]));
               }
            }
         }
      });
   }

   public void layoutIn(TwoColumnLayout var1) {
      for (int var2 = 0; var2 < this.values.length; var2++) {
         var1.addRow(this.nameLabels.get(var2), this.values[var2]);
      }
   }
}
