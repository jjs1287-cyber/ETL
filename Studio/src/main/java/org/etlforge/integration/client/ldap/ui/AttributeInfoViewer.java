package org.etlforge.integration.client.ldap.ui;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.etlforge.application.ui.widget.BasicTwoColumnLabelDisplayer;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.text.KongaTextArea;

public class AttributeInfoViewer implements UiProvider {
   private final BasicTwoColumnLabelDisplayer ui = new BasicTwoColumnLabelDisplayer();

   public AttributeInfoViewer(LdapStructureAttribute var1) {
      this.ui.addRow(PackageResources.AttributeViewer.NAME, var1.getName());
      this.ui.addRow(PackageResources.AttributeViewer.OID, var1.getSyntaxInfo().getOid());
      this.ui.addRow(PackageResources.AttributeViewer.USE, var1.getUse().name());
      this.ui.addRow(PackageResources.AttributeViewer.CARDINALITY, var1.getCardinality().name());
      this.ui.addRow(PackageResources.AttributeViewer.READ_ONLY, PackageResources.AttributeViewer.getReadOnlyValue(var1));
      this.ui.addRow(PackageResources.AttributeViewer.DESC, this.getDescriptionField(var1), true);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   private JComponent getDescriptionField(LdapStructureAttribute var1) {
      KongaTextArea var2 = KongaTextArea.viewer(var1.getDescription(), Size.rows(5), Size.columns(30));
      return new JScrollPane(var2);
   }
}
