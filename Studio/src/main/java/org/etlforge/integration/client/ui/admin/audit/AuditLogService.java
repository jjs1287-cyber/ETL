package org.etlforge.integration.client.ui.admin.audit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.xml.namespace.QName;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.encoding.XMLType;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;

public final class AuditLogService implements AuditLogPanel.Service {
    private static final String NS="urn:org.etlforge.integration.server.implementation.webservice.admin.Admin";
    private static final ObjectMapper JSON=new ObjectMapper().enable(com.fasterxml.jackson.core.JsonGenerator.Feature.ESCAPE_NON_ASCII);
    private final CppWebServiceConfiguration configuration;
    public AuditLogService(CppWebServiceConfiguration configuration){this.configuration=configuration;}
    @Override public JsonNode request(ObjectNode request)throws Exception {
        if(configuration.getServerManager().getConnectionStatus()!=org.etlforge.integration.client.server.ConnectionStatus.CONNECTED)
            throw new IllegalStateException(org.etlforge.integration.client.server.ServerConnectionService.NOT_CONNECTED_MESSAGE);
        org.etlforge.integration.client.server.login.LoginConfiguration.ConnectionSnapshot session=configuration.getServerManager().getLoginService().getConfiguration().getConnectionSnapshot();
        String host=session.serverName;
        if(host==null || host.isEmpty())throw new IllegalStateException(org.etlforge.integration.client.server.ServerConnectionService.NOT_CONNECTED_MESSAGE);
        String endpoint=(session.useHttps?"https://":"http://")+host+configuration.getWsPath()+"konga_admin";
        return invokeRequest(endpoint,session.userName,session.passwordHash,request);
    }
    static JsonNode invokeRequest(String endpoint,String user,String passwordHash,ObjectNode request)throws Exception {
        Service service=new Service();
        service.getEngine().setOption("sendMultiRefs",Boolean.FALSE);
        Call call=(Call)service.createCall();
        call.setTargetEndpointAddress(endpoint);
        call.setOperationName(new QName(NS,"auditRequest"));
        call.setOperationStyle(Style.WRAPPED);call.setOperationUse(Use.LITERAL);
        call.setUseSOAPAction(true);call.setSOAPActionURI("");call.setTimeout(30000);
        for(String field:new String[]{"in0","in1","in2"})call.addParameter(new QName("",field),XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
        call.setReturnType(XMLType.XSD_STRING);
        String result=(String)call.invoke(new Object[]{user,passwordHash,JSON.writeValueAsString(request)});
        return JSON.readTree(result);
    }
}
