package org.etlforge.integration.client.ui.interchange.util;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.entity.IntegrationEntityNameInputField;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.dataelement.ui.DeTemplateTransferSupport;
import org.etlforge.system.callback.Acceptor;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.widget.ComponentTraverser;
import org.etlforge.ui.widget.text.KongaTextField;

public final class InterchangeViewUtils {
   public static TextInputField2 getIntegrationEntityNameInputField(IntegrationEntity var0, String var1, JLabel var2, int var3) {
      KongaTextField var4 = InputFieldFactory.getTextField(var3, true);
      var4.setText(var0.getName());
      return new IntegrationEntityNameInputField(var0, var1, var4, var2);
   }

   public static void configurePropertyFields(Container var0) {
      ComponentTraverser.traverse(var0, new Acceptor<Component>() {
         public boolean accept(Component var1) {
            if (var1 instanceof JTextComponent) {
               JTextComponent var2 = (JTextComponent)var1;
               if (!DeTemplateTransferSupport.isRejecting(var2)) {
                  InterchangeViewUtils.configurePropertyField(var2);
               }
            }

            return true;
         }
      });
   }

   public static void configurePropertyField(JTextComponent var0) {
      DeTemplateTransferSupport.installDropHandler(var0);
   }

   private InterchangeViewUtils() {
   }
}
