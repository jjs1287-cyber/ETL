/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ENTITY_ID_CONSTANTS_H
#define KONGA_ENTITY_ID_CONSTANTS_H

namespace {
	const int InvalidEntityId = 0;
	const int FolderEntityId = 1;
	const int SourceEntityId = 2;
	const int TargetEntityId = 3;
	const int TransformationEntityId = 4;
	const int ScheduleEntityId = 5;
	const int OperationEntityId = 6;
	const int ProcessEntityId = 7;
	                                                                               
	                                                                               
                                  
                                 
	const int WebServiceEntityId = 10;
	const int CallRestrictionEntityId = 11;
	const int GroupEntityId = 12;
	const int UserEntityId = 13;
                                   
                                   
	const int EmailMessageEntityId = 16;
	const int DocumentEntityId = 17;
                                  
	const int XmlEntityId = 19;
	const int InterchangeProjectEntityId = 20;
	const int WsdlFileEntityId = 21;
	const int JmsMessageEntityId = 22;
	const int ScriptEntityId = 23;
	const int HttpEndpointEntityId = 24;
	const int SalesforceQueryEntityId = 25;
	const int SalesforceUpsertEntityId = 26;
	const int SalesforceCreateEntityId = 27;
	const int SalesforceUpdateEntityId = 28;
	const int SalesforceBulkLoadEntityId = 29;
	const int SalesforceDeleteEntityId = 30;
	const int SalesforceConnectorEntityId = 31; const int XsltTransformEntityId = 34; const int ProjectVariableEntityId = 35; const int MaxEntityId = ProjectVariableEntityId;
}

#endif                               

