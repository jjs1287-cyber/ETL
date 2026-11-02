package org.etlforge.integration.client.ui.script.builder.docs;

import java.util.ListResourceBundle;

public class ProjectItemTreeHtmlDocumentation extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {"ProjectItemTree.Help.Databases.Title", "Databases"},
      {
            "ProjectItemTree.Help.Databases",
            "<p>This node contains all the database Sources and Targets that have been defined in the project.<br><br><p>These can be used as input argument to Database functions, such as <code>DbLookup</code> and <code>DbExecute</code>."
      },
      {"ProjectItemTree.Help.FileLocations.Title", "File Locations"},
      {
            "ProjectItemTree.Help.FileLocations",
            "<p>This node contains all the file-based Sources and Targets that have been defined in the project.<br><br><p>These can be used as input argument to File functions, such as <code>ReadFile</code> and <code>WriteFile</code>."
      },
      {"ProjectItemTree.Help.LDAP.Title", "LDAP Locations"},
      {
            "ProjectItemTree.Help.LDAP",
            "<p>This node contains all the LDAP Sources and Targets that have been defined in the project.<br><br><p>These can be used as input argument to the <code>LdapConnect<code> function."
      },
      {"ProjectItemTree.Help.Operations.Title", "Operations"},
      {
            "ProjectItemTree.Help.Operations",
            "<p>This node contains those Operations that can be started through the <code>RunOperation</code> function.<br><br><p>The following types of operations cannot be used in <code>RunOperation</code>, and are therefore not included in this tree:<br><br><ul><li>Hosted Web Service Calls</li><li>Operations that use Target From Previous Operation as source</li></ul>"
      },
      {"ProjectItemTree.Help.Scripts.Title", "Scripts"},
      {
            "ProjectItemTree.Help.Scripts",
            "<p>This node contains all the Scripts that have been defined in the project.<br><br><p>The Scripts can be used as input argument to the <code>RunScript<code> function."
      },
      {"ProjectItemTree.Help.EmailMessages.Title", "Email Messages"},
      {
            "ProjectItemTree.Help.EmailMessages",
            "<p>This node contains all the Email Messages that have been defined in the project.<br><br><p>The Email Messages can be used as input argument to the <code>SendEmailMessage<code> function."
      },
      {"ProjectItemTree.Help.SalesforceOrgs.Title", "Salesforce Orgs"},
      {
            "ProjectItemTree.Help.SalesforceOrgs",
            "<p>This node contains all the Salesforce Orgs that have been defined in the project.<br><br><p>The Salesforce Orgs can be used as input argument to the <code>SalesforceLogin<code> function."
      },
      {"ProjectItemTree.Help.PLM360Endpoints.Title", "PLM360 Endpoints"},
      {
            "ProjectItemTree.Help.PLM360Endpoints",
            "<p>This node contains all the PLM360 Endpoints that have been defined in the project.<br><br><p>The PLM360 Endpoints can be used as input argument to the <code>PLM360Login<code> function."
      },
      {"ProjectItemTree.Help.NetSuiteEndpoints.Title", "NetSuite Endpoints"},
      {
            "ProjectItemTree.Help.NetSuiteEndpoints",
            "<p>This node contains all the NetSuite Endpoints that have been defined in the project.<br><br><p>The NetSuite Endpoints can be used as input argument to the <code>NetSuiteGetSerVerTime<code> function."
      }
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
