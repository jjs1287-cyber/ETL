package org.etlforge.integration.client.ui.interchange.connect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.client.ui.interchange.TopLevelEntityCategory;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.focus.FocusUtils;

public final class LaunchConnectDialog {
   private final EntityConfigLookup configLookup;
   private final ImmutableList<TopLevelEntityCategory> categories;
   private final String title;
   private final Icon icon;

   public LaunchConnectDialog(EntityConfigLookup var1, Iterable<TopLevelEntityCategory> var2, String var3, Icon var4) {
      this.configLookup = Preconditions.checkNotNull(var1);
      this.categories = ImmutableList.copyOf(var2);
      this.title = Preconditions.checkNotNull(var3);
      this.icon = Preconditions.checkNotNull(var4);
   }

   public void open(Window var1) {
      KongaDialog var2 = this.createDialog(var1);
      var2.setLocationRelativeTo(null);
      var2.setVisible(true);
   }

   private KongaDialog createDialog(Window var1) {
      ConnectLaunchScreen var2 = new ConnectLaunchScreen(this.configLookup, this.categories, this.title, this.icon);
      final KongaDialog var3 = new KongaDialog(var1, this.title);
      BorderLayoutBuilder var4 = this.layoutDialog(var3, var2);
      var4.asContentPaneFor(var3);
      var3.setCancelOnEscape(true);
      var2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            var3.dispose();
         }
      });
      this.installFocusTraversalKeys(var3);
      var3.pack();
      var3.setResizable(false);
      return var3;
   }

   private BorderLayoutBuilder layoutDialog(KongaDialog var1, ConnectLaunchScreen var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder();
      JPanel var4 = BorderLayoutBuilder.fillWith(var2);
      var3.center(var4);
      JPanel var5 = var1.getButtonPanel(KongaDialog.CLOSE_ONLY, -1, 0, -1);
      var5.setBorder(Empty.border(10, 12, 16, 10));
      var5.setBackground(Colors.ETLFORGE_BLUE);
      var3.south(var5);
      return var3;
   }

   private void installFocusTraversalKeys(KongaDialog var1) {
      JRootPane var2 = var1.getRootPane();
      FocusUtils.installArrowDownAndUpAsFocusTraversalKeys(var2);
   }
}
