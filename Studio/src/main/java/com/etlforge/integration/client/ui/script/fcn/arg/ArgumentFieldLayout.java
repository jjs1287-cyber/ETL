package com.etlforge.integration.client.ui.script.fcn.arg;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

interface ArgumentFieldLayout {
   void addField(JLabel var1, JComponent var2, JComponent var3);

   void addButton(JButton var1);

   JComponent getContainer();
}
