package org.etlforge.integration.client.ui.entity.search;

import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.FocusUtils;

public final class EntitySearchView extends AbstractWindowSectionContent {
   public static final String CONTENT_ID = EntitySearchView.class.getName();
   private final EntitySearchUi searchUi;

   public EntitySearchView(WindowSection var1, EntitySearchUi var2) {
      super(CONTENT_ID, var1);
      this.searchUi = var2;
   }

   @Override
   public String getTitle() {
      return "Find";
   }

   @Override
   public Icon getIcon() {
      return CommonIcons.FIND_16;
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder().north(this.getTitlePanel()).center(this.searchUi);
      JComponent var3 = var2.container();
      UiUtils.setPanelBackgrounds(var3, Colors.NEUTRAL_BACKGROUND);
      BorderLayoutBuilder.from(var1).center(var3);
   }

   @Override
   protected void activated() {
      this.requestFocus();
   }

   public void requestFocus() {
      this.searchUi.requestFocus();
   }

   @Override
   protected void attached() {
      FocusUtils.requestFocusLater(this.searchUi, 100);
   }

   @Override
   protected void detached() {
      FocusUtils.requestFocusLater(this.searchUi, 100);
   }
}
