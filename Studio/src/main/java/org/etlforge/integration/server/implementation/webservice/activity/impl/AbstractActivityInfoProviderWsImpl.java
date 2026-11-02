package org.etlforge.integration.server.implementation.webservice.activity.impl;

import com.google.common.base.Function;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.activity.ActivitySubjectType;
import org.etlforge.integration.activity.client.filter.TimeWindowFilter;
import org.etlforge.integration.activity.client.filter.TransferSizeFilter;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.serverapi.ActivityRequest;
import org.etlforge.integration.activity.serverapi.ActivitySubjectId;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.common.server.ServerConversionUtils;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.server.implementation.webservice.activity.client.ActivityInfoProvider;
import org.etlforge.integration.server.implementation.webservice.activity.client.ActivityInfoProviderServiceLocator;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationInstanceFilter;
import org.etlforge.lang.Arrays2;
import org.etlforge.util.SortOrder;

public abstract class AbstractActivityInfoProviderWsImpl extends CppWebServiceServerCall {
   private static final String WS_NAME = "konga_activity_infoprovider";
   protected String userName;
   protected String password;

   protected AbstractActivityInfoProviderWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   protected final ActivityInfoProvider getServerProxy(ServerCallback var1) {
      ActivityInfoProviderServiceLocator var2 = new ActivityInfoProviderServiceLocator();
      StringBuilder var3 = new StringBuilder();
      if (this.getConfiguration().configureWebservice(var2, "konga_activity_infoprovider", var3, var1)) {
         try {
            var2.setkonga_activity_infoproviderEndpointAddress(var3.toString());
            this.userName = this.getConfiguration().getUserName();
            this.password = this.getConfiguration().getPasswordHash();
            return var2.getkonga_activity_infoprovider();
         } catch (ServiceException var5) {
            var1.caught(new IntegrationServerException("Failed to call webservice: " + var5.getMessage(), var5));
         }
      }

      return null;
   }

   protected static final WsOperationInstanceFilter startFilter(ActivityRequest var0) {
      WsOperationInstanceFilter var1 = new WsOperationInstanceFilter();
      var1.setGuids(new String[]{getIdString(var0.subjectId)});
      var1.setGuidTypes(new int[]{toIdTypeInteger(var0)});
      var1.setOperationInstances(new long[]{getOpInstanceId(var0)});
      var1.setOperationName(getNameFilter(var0));
      var1.setMaxRecords(getMaxEntries(var0));
      return var1;
   }

   protected static final String getIdString(ActivitySubjectId var0) {
      if (var0 == null) {
         return "";
      } else {
         return var0.getType() == ActivitySubjectType.SERVER ? "" : var0.getGuid().toString();
      }
   }

   private static int toIdTypeInteger(ActivityRequest var0) {
      if (var0.subjectId == null) {
         return 0;
      } else {
         return var0.subjectId.getType() == ActivitySubjectType.SERVER ? 0 : ((IntegrationEntityId)var0.subjectId.getGuid()).getEntityType().getId();
      }
   }

   private static long getOpInstanceId(ActivityRequest var0) {
      return var0.opInstanceId == null ? -1L : var0.opInstanceId;
   }

   private static String getNameFilter(ActivityRequest var0) {
      return var0.nameFilter == null ? "" : var0.nameFilter.getFilterText();
   }

   private static int getMaxEntries(ActivityRequest var0) {
      return var0.maxNumberOfEntriesFilter == null ? -1 : var0.maxNumberOfEntriesFilter.getMaxEntriesToGet();
   }

   protected static final String[] getStartAndEndDates(TimeWindowFilter var0) {
      return new String[]{
         var0 == null ? "" : ServerConversionUtils.toServerDateFormat(var0.getStartDate()),
         var0 == null ? "" : ServerConversionUtils.toServerDateFormat(var0.getEndDate())
      };
   }

   protected static final String getSizeFilter(TransferSizeFilter var0) {
      return var0 == null ? "" : var0.getOperator().toString() + " " + var0.getValueInBytes();
   }

   protected static final int getSortOrder(SortOrder var0) {
      return var0 == null ? SortOrder.ASCENDING.getSortValue() : var0.getSortValue();
   }

   protected static final int[] translateLogStatuses(EnumSet<LogStatus> var0) {
      int[] var1 = new int[var0.size()];
      int var2 = 0;

      for (LogStatus var4 : var0) {
         var1[var2++] = var4.getServerId();
      }

      return var1;
   }

   protected static final <S, C> List<C> createClientSideResponse(S[] var0, Class<C> var1, Function<S, C> var2) {
      return var0 == null ? Collections.emptyList() : Arrays2.toList(var0, var1, var2);
   }
}
