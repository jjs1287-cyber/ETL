package org.etlforge.integration.client.ui.interchange.component.page.entity;

import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderHelper;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.integration.client.ui.interchange.core.action.InterchangeActionDictionary;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ActiveObjectPageAction extends AbstractApplicationAction implements ActiveObjectProvider {
   private final IntegrationEntityPage page;
   private final ApplicationAction encapsulatedAction;
   private final ActiveObjectProviderHelper helper;
   private final ImmutableList<IntegrationEntity> activeObjectList;
   private boolean saveIfDirty;
   private String tooltip;

   public ActiveObjectPageAction(IntegrationEntityPage var1, ApplicationAction var2) {
      this(var1, var2, InterchangeActionDictionary.DICTIONARY);
   }

   public ActiveObjectPageAction(IntegrationEntityPage var1, ApplicationAction var2, ActionDictionary var3) {
      super(var2.getID(), var3);
      this.page = var1;
      this.helper = new ActiveObjectProviderHelper(this);
      this.encapsulatedAction = var2;
      this.activeObjectList = ImmutableList.of(var1.getObject());
   }

   public void setSaveIfDirty(boolean var1) {
      this.saveIfDirty = var1;
   }

   @Override
   public boolean isActive() {
      return true;
   }

   @Override
   public String getProviderId() {
      return this.encapsulatedAction.getID();
   }

   @Override
   public List<?> getActiveObjects() {
      return this.activeObjectList;
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.helper.add(var1);
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.helper.remove(var1);
   }

   @Override
   public void execute(ActionEvent var1) {
      this.helper.notifyListeners(this.activeObjectList);
      final ActionEvent var2 = var1;
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ActiveObjectPageAction.this.runEncapsulatedAction(var2);
         }
      });
   }

   private void runEncapsulatedAction(ActionEvent var1) {
      if (this.isClearedToRun()) {
         this.encapsulatedAction.actionPerformed(var1);
      }
   }

   private boolean isClearedToRun() {
      return this.saveIfDirty ? this.savePageIfDirty() : true;
   }

   private boolean savePageIfDirty() {
      if (this.page.isDirty()) {
         try {
            this.page.save();
         } catch (SaveEditorException var2) {
            ErrorLog.attention(this.page.getClass(), null, var2);
            return false;
         }
      }

      return true;
   }

   @Override
   public Object getValue(String var1) {
      return "ShortDescription".equals(var1) && StringUtils.isNotEmpty(this.tooltip) ? this.tooltip : this.encapsulatedAction.getValue(var1);
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

   public void setToolTip(String var1) {
      this.tooltip = var1;
   }
}
