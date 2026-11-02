package com.etlforge.integration.client.ui.sfdc;

import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import javax.swing.event.UndoableEditListener;
import org.etlforge.ui.layout.TwoColumnLayout;

public interface RetryPolicyEditor {
   RetryPolicy getPolicy();

   void layoutIn(TwoColumnLayout var1);

   void addUndoableEditListener(UndoableEditListener var1);

   void removeUndoableEditListener(UndoableEditListener var1);
}
