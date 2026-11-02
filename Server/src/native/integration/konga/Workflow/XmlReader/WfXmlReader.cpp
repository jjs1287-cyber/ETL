/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <vector>
#include <stack>
#include <sstream>	               
#include <memory>	           

#include <boost/algorithm/string/case_conv.hpp>		           

#include <konga/Utils/CrException.h>	                    
#include <konga/Utils/StringUtils.h>	       

#include <konga/Workflow/Activity.h>
#include <konga/Workflow/ActivityItem.h>
#include <konga/Workflow/DataElement.h>
#include <konga/Workflow/Process.h>
#include <konga/Workflow/Route.h>
#include <konga/Workflow/RouteRule.h>
#include <konga/Workflow/Task.h>
#include <konga/Workflow/Document.h>
#include <konga/Workflow/RuleTree.h>
#include <konga/Workflow/Escalation.h>

#include <konga/Xml/XmlIncludes.h>
#include <konga/Xml/XmlUtils.h>

#include <konga/MailClient/SmtpPriority.h>	                                 

#include "WfXmlReader.h"

using namespace std;

using namespace konga;
using namespace konga::workflow;
using namespace konga::WfXmlReader;
using namespace konga::xml;

                    
namespace {
	const string ELEM_DESCRIPTION("Description");
	const string ELEM_DATA("Data");
	const string ELEM_TASK("Task");
	const string ELEM_ROUTE("Route");
	const string ELEM_DOCUMENT("Document");

	const string ATT_NAME("Name");
	const string ATT_VALUE("Value");
	const string ATT_TYPE("Type");
	const string ATT_IO("IO");
	const string ATT_ORIGIN("Origin");
	const string ATT_PATH("Path");
	const string ATT_TEXT("Text");
	const string ATT_PROCESSPRIORITY("ProcessPriority");
	const string ATT_REQUIRED("Required");
	const string ATT_URL("URL");
}

enum ScriptType { ST_PRE_DOC, ST_POST_DOC };

                                        
Process* HandleProcess(const xml::DOMElementPtr& pProcessElement);
template<class T> void SetDescription(T& t, const xml::DOMElementPtr& pDescElement);
void AddProcessDataElements(Process& process, xml::DOMNodeListPtr& pDataNodes);
void AddTaskDataElements(Task& task, const xml::DOMNodeListPtr& pDataElementList);

void AddTasks(Process& process, xml::DOMNodeListPtr& pTaskNodes);
void AddRoutes(Process& process);
void SetSpecialTaskAttributes(Task& task, const std::string& stSpecial);
void AddScript(Task& task, xml::DOMNodeListPtr& pScriptNodes, ScriptType scriptType);
void AddRouteRules(Process& process, const xml::DOMNodeListPtr& pRouteNodes);
void AddDocument(Task& task, xml::DOMElementPtr& pXmlDocumentElement);
void AddEscalation(Task& task, const xml::DOMElementPtr& pXmlEsclationElement);

Document* HandleEmailDocument(Task& task, xml::DOMElementPtr& pXmlDocumentElement, bool bRequiresResponse);
void HandleOperation(Task& task, xml::DOMElementPtr& pXmlDocumentElement);

const ActivityItem* HandleActivityItem(xml::DOMNodeListPtr& pScriptNodes, const xml::DOMElementPtr& pActivityElement, Task& task);
const ActivityItem* HandleSimpleActionActivity(const xml::DOMElementPtr& pActivityElement, Task& task);
const ActivityItem* HandleInternalFunctionActivity(const xml::DOMElementPtr& pActivityElement, Task& task);
const ActivityItem* HandleRaiseError(const xml::DOMElementPtr& pActivityElement, Task& task);
const ActivityItem* HandleScheduledEvent(const xml::DOMElementPtr& pActivityElement, Task& task);

const ActivityItem* HandleDefaultInternalFunctionActivity(const xml::DOMElementPtr& pActivityElement, Task& task);
const ActivityItem* HandleFileRetrieval(const xml::DOMElementPtr& pActivityElement, Task& task);
const ActivityItem* HandleOdbcActivity(const xml::DOMElementPtr& pActivityElement, Task& task);

const ActivityItem* HandleTrueActivityRule(xml::DOMNodeListPtr& pScriptNodes, const xml::DOMElementPtr& pActivityElement, Task& task);
const ActivityItem* HandleRoute(const xml::DOMElementPtr& pRouteElement, Task& task);

void HandleRouteRule(const xml::DOMNodeListPtr& pXmlRouteItemNodes, Task& destinationTask);

                                                                                      
                              
             
                          
                                      
                                            
string FormatDataElementValue(Task& task, const string& stValue);

void SubstituteTaskNameWithId(string& stText, const Process& process);

void SubstituteTaskNameWithId(string& stText, const Process& process) {
	                                                                                           
	static const string stTASKPat("[TASK:");
	string::size_type ipos = 0, ibegin = 0;
	while( (ipos = stText.find(stTASKPat, ibegin)) != string::npos ) {
		const string::size_type iFirstOfName = ipos + stTASKPat.size();
		const string::size_type idot = stText.find('.', iFirstOfName);
		if ( idot == string::npos )
			throw CrException("Syntax error in Text attribute. Missing '.' separator. " + stText + " is not a valid Text attribute.");
		const string stTaskName( stText.substr(iFirstOfName, idot-iFirstOfName) );
		const Task& task = process.GetTaskByName(stTaskName);
		const string stNewTaskName( "T" + toString<task_id_t>(task.GetId()) );
		stText.replace(ipos+1, stTASKPat.size()+stTaskName.size()-1, stNewTaskName);
		ibegin = stText.find(']', ipos+stNewTaskName.size()+1);
		if ( ibegin == string::npos )
			throw CrException("Syntax error in Text attribute. no closing ']'. " + stText + " is not a valid Text attribute.");
		++ibegin;
	}
}

string FormatDataElementValue(Task& task, const string& stValue_in) {
	string stValue(stValue_in);
	if ( strncmp(stValue.c_str(), "[TASK:", 6) == 0 ) {
		SubstituteTaskNameWithId(stValue, task.GetProcess() );
	}
	else if ( stValue.size() > 1 && stValue.at(0) == '"' ) {
		stValue.erase(0,1);
		stValue.erase(stValue.size()-1);
	}
	else {
		if ( task.GetProcess().IsProcessDataElement(stValue) )
			stValue = "[" + stValue + "]";
		else
			stValue = "[T0." + stValue + "]";
	}
	return stValue;
}

Process* WfXmlReader::ParseXml(const string& stXmlFilePath) {
	string stErr;
                                                                                                                             
	DOMDocumentPtr pDoc( XmlUtils::LoadDoc(stXmlFilePath, stErr, true) );                                  
	if ( pDoc == 0 )
		throw CrException(stErr);

	DOMElementPtr pElement( pDoc->selectSingleNode("/Process") );
	if( pElement == 0 )
		throw CrException("Could not find any workflow processes in the file '" + stXmlFilePath + "'.");

	return HandleProcess( pElement );
}

const char* WfXmlReader::type_str_long[] = {
	"integer","float","string","boolean","date",
	"short_integer","double","long_string","long_integer","object","collection", 
	"integer_byRef","float_byRef","string_byRef","boolean_byRef","date_byRef",
	"short_integer_byRef","double_byRef","long_string_byRef","Long_integer_byRef",
	"object_byRef","collection_byRef",
	0};
const char* WfXmlReader::type_str_short[] = {
	"int","flt","str","bol","dte","srt","dbl","txt","lng","obj","col",
	"Int","Flt","Str","Bol","Dte","Srt","Dbl","Txt","Lng","Obj","Col",
	0};

std::string WfXmlReader::DataTypeName_LongToShort(const std::string& stLongName) {
	                                               
	for(int i=0; type_str_long[i]; ++i)
		if( stLongName == type_str_long[i] )
			return type_str_short[i];
	throw CrException("No such data type with long name '" + stLongName + "'.");
}

std::string WfXmlReader::DataTypeName_ShortToLong(const std::string& stShortName) {
	                                               
	for(int i=0; type_str_short[i]; ++i)
		if( stShortName == type_str_short[i] )
			return type_str_long[i];
	throw CrException("No such data type with short name '" + stShortName + "'.");
}

            
            
            
             
             

Process* HandleProcess(const DOMElementPtr& pProcessElement) {
	auto_ptr<Process> pProcess( new Process() );

	       
	pProcess->SetName( pProcessElement->getAttribute(ATT_NAME) );

	           
	int iPriority = atoi( pProcessElement->getAttribute(ATT_PROCESSPRIORITY).c_str() );
	pProcess->SetPriority( MSG_PRIORITY(iPriority) );

	              
	SetDescription<Process>( *pProcess, pProcessElement->selectSingleNode(ELEM_DESCRIPTION) );

	                
	DOMNodeListPtr pNodes = pProcessElement->selectNodes(ELEM_DATA);
	AddProcessDataElements( *pProcess, pNodes );

	        
	pNodes = pProcessElement->selectNodes(ELEM_TASK);
	AddTasks( *pProcess, pNodes );

	         
	AddRoutes( *pProcess );

	AddRouteRules( *pProcess, pProcessElement->selectNodes(ELEM_ROUTE) );

	pProcess->CompleteScheduledEvents();

	return pProcess.release();
}

template<class T>
void SetDescription(T& t, const xml::DOMElementPtr& pDescElement) {
	if( pDescElement != 0 ) {
		const string stDescription = pDescElement->getTextContent();
		if( !stDescription.empty() )
			t.SetDescription(stDescription);
	}
}

                   
namespace {
	void FillDataElement(DataElement& de, const DOMElementPtr& pDataXmlElement) {
		de.SetName( pDataXmlElement->getAttribute(ATT_NAME) );
		de.SetType( WfXmlReader::DataTypeName_LongToShort( pDataXmlElement->getAttribute(ATT_TYPE) ) );
		de.SetRequired( pDataXmlElement->getAttribute(ATT_REQUIRED) == "1" );
		de.SetOriginType( pDataXmlElement->getAttribute(ATT_ORIGIN) );
		de.SetInitialValue( pDataXmlElement->getAttribute("InitValue") );
	}
}

void AddProcessDataElements(Process& process, xml::DOMNodeListPtr& pDataNodes) {
	if( pDataNodes!=0 ) {
		DOMElementPtr pDataXmlElement;
		while( (pDataXmlElement = pDataNodes->nextNode()) != 0 ) {
			auto_ptr<DataElement> pDe( new DataElement(process.IncrementDataElementId()) );
			FillDataElement(*pDe, pDataXmlElement);
			process.AddProcessDataElement( pDe.release() );
		}
	}
}

void AddTaskDataElements(Task& task, const xml::DOMNodeListPtr& pDataNodes) {
	Process& process = task.GetProcess();
	for( unsigned int i = 0, n = pDataNodes.getLength(); i < n; ++i ) {
		xml::DOMElementPtr pDataXmlElement = pDataNodes->item[i];
		if ( pDataXmlElement != 0 ) {
			const string stIO( pDataXmlElement.getAttribute(ATT_IO) );
			                                                                       
			                                                                                    
			                                                                                     
			if ( stIO == "O" || stIO == "B" ) {
				const string stName( pDataXmlElement.getAttribute(ATT_NAME) );
				if ( !task.HasDataElement(stName) &&
					 !process.IsProcessDataElement(stName) &&
					 strncmp(stName.c_str(), "[TASK:", 6) != 0 ) {
						auto_ptr<DataElement> pDe(new DataElement(task.GetProcess().IncrementDataElementId()) );
						FillDataElement(*pDe, pDataXmlElement);
						task.AddDataElement( pDe.release() );
				}
			}
		}
	}
}

void AddTasks(Process& process, xml::DOMNodeListPtr& pTaskNodes) {
	if( pTaskNodes != 0 ) {
		DOMElementPtr pTaskElement;
		task_id_t id = 0;
		while( (pTaskElement = pTaskNodes->nextNode()) != 0 ) {
			auto_ptr<Task> pTask( new Task(process, ++id) );
			pTask->SetName( pTaskElement->getAttribute("Name") );
			pTask->SetPriority( MSG_PRIORITY( atoi(pTaskElement->getAttribute("TaskPriority").c_str()) ) );
			SetSpecialTaskAttributes( *pTask, pTaskElement->getAttribute("Special") );                           

			              
			SetDescription<Task>( *pTask, pTaskElement->selectSingleNode("Description") );

			                    
			if ( pTaskElement->getAttribute("Terminate") == "True" )
				pTask->AddTaskType(ttTERMINATING);

			                 
			xml::DOMNodeListPtr pXmlScriptItems = pTaskElement->selectNodes("PreDocActivityItem");
			AddScript(*pTask, pXmlScriptItems, ST_PRE_DOC );

			           
			xml::DOMElementPtr pXmlDocument( pTaskElement->selectSingleNode(ELEM_DOCUMENT) );
			AddDocument(*pTask, pXmlDocument);

			                                
			pXmlScriptItems = pTaskElement->selectNodes("ActivityItem");
			AddScript( *pTask, pXmlScriptItems, ST_POST_DOC );

			AddEscalation(*pTask, pTaskElement->selectSingleNode("Escalation") );

			process.AddTask(pTask.release());
		}
	}
}

void SetSpecialTaskAttributes(Task& task, const string& stSpecial) {
	if( stSpecial.length() == 0 ) return;
	                                          
	if ( stSpecial.compare("IsEscalationTask") == 0 ) {
		task.AddTaskType(ttESCALATION);
	}
}

void AddDocument(Task& task, xml::DOMElementPtr& pXmlDocumentElement) {
	if ( pXmlDocumentElement == 0 ) return;

	const string stType( pXmlDocumentElement->getAttribute(ATT_TYPE) );
	Document* pDocument = 0;
	if ( stType == "Unknown" ) {
		         
		return;
	}
	else if ( stType == "InitTask" ) {
		throw NotImplementedYet("Document Type InitTask is not implemented.");
	}
	else if ( stType == "Email" || stType == "EmailNotificationOnly" ) {
		pDocument = HandleEmailDocument(task, pXmlDocumentElement, stType == "Email");
	}
	else if ( stType == "Operation" ) {
		                           
		HandleOperation(task, pXmlDocumentElement);
		return;	                                             
	}
	else {
		throw CrException("Unknown Document Type attribute \"" + stType + "\".");
	}

	task.GetProcess().AddDocument(pDocument);
	task.SetTaskRole(pDocument->To());
	task.AddDocument(pDocument->GetId());
}

Document* HandleEmailDocument(Task& task, xml::DOMElementPtr& pXmlDocumentElement, bool bRequiresResponse) {
	Process& process = task.GetProcess();
	auto_ptr<EmailDocument> pEmailDocument(
		new EmailDocument(process, process.IncrementDocumentId(), task.GetId(), bRequiresResponse)
	);

	xml::DOMElementPtr pXmlElement = pXmlDocumentElement.selectSingleNode("Message");
	pEmailDocument->SetMsgTemplateName(pXmlElement.getTextContent());
	pEmailDocument->SetMsgTemplateLocation("Inline");
	string stSendFormat( pXmlElement->getAttribute(ATT_TYPE) );
	boost::algorithm::to_lower( stSendFormat );
	pEmailDocument->SetSendFormat(stSendFormat);

	const string stUrl( pXmlDocumentElement->getAttribute("URL") );
	pEmailDocument->SetUrl(stUrl);

	pXmlElement = pXmlDocumentElement->selectSingleNode("Subject");
	const string stSubject( pXmlElement->getAttribute(ATT_TEXT) );
	pXmlElement = pXmlDocumentElement->selectSingleNode("Cc");
	const string stCC( pXmlElement->getAttribute(ATT_TEXT) );
	pXmlElement = pXmlDocumentElement->selectSingleNode("Bcc");
	const string stBCC( pXmlElement->getAttribute(ATT_TEXT) );
	pXmlElement = pXmlDocumentElement->selectSingleNode("ReplyTo");
	const string stReplyTo( pXmlElement->getAttribute(ATT_TEXT) );
	pXmlElement = pXmlDocumentElement->selectSingleNode("From");
	const string stFrom( pXmlElement->getAttribute(ATT_TEXT) );
	                                                                                         
	                                                                  
	pXmlElement = pXmlDocumentElement->selectSingleNode("To");
	const string stTo( pXmlElement->getAttribute(ATT_TEXT) );
	pEmailDocument->SetEmailFields(stFrom, stTo, stCC, stBCC, stSubject, stReplyTo);

	pXmlElement = pXmlDocumentElement->selectSingleNode("Priority");
	const string stLevel( pXmlElement->getAttribute("Level") );
	pEmailDocument->SetPriority( GetPriorityFromLevel(stLevel) );

	if ( bRequiresResponse ) {
		                                                               
		DOMNodeListPtr dataNodes = pXmlDocumentElement->selectNodes(ELEM_DATA);
		AddTaskDataElements(task, dataNodes);

		                                                  
		for( unsigned long i = 0, n = dataNodes.getLength(); i < n; ++i ) {
			DOMElementPtr pXmlData( dataNodes->item[i] );
			const string stOrigin( pXmlData->getAttribute(ATT_ORIGIN) );
			if ( stOrigin == "Doc" ) {
				const string stIO( pXmlData->getAttribute("IO") );
				const string stName( pXmlData->getAttribute("Name") );
				if ( stIO == "I" ) {
					const string stPath( pXmlData->getAttribute(ATT_PATH) );
					pEmailDocument->AddToDocDataElement(
						ToDocDataElement(stName, process.GetDataElementByName(stPath).GetId())
						);
				}
				else if ( stIO == "B" ) {
					pEmailDocument->AddFromDocDataElement(FromDocDataElement(stName));
				}
			}
		}
	}

	return pEmailDocument.release();
}

void HandleOperation(Task& task, xml::DOMElementPtr& pXmlDocumentElement) {
	const DOMElementPtr pXmlOperation( pXmlDocumentElement->selectSingleNode("Operation") );
	const string stType( pXmlOperation->getAttribute(ATT_TYPE) );
                                                   
	const string stId( pXmlOperation->getAttribute("Id") );
	const string stServer( pXmlOperation->getAttribute("Server") );
	const string stLogin( pXmlOperation->getAttribute("Login") );

	                                           
	                                                                        
	                                                           
	Process& process = task.GetProcess();
	activity_rule_id_t activityRuleId = task.GetPreRoleActivityRule();
	ActivityRule* pActivityRule = 0;
	if ( activityRuleId > 0 ) {
		ActivityRule& taskActivityRule = process.GetActivityRule(activityRuleId);
		pActivityRule = &taskActivityRule;
	}
	else {
		pActivityRule = new ActivityRule(task.GetProcess(), 0, process.IncrementActivityRuleId());
		process.AddActivityRule(pActivityRule);
		task.SetPreRoleActivityRule(pActivityRule->GetId());
	}
	Activity* pActivity = new SimpleActionActivity(process, process.IncrementActivityId());
	task.AddActivity(pActivity);
	pActivity->SetName("__OperationID;" + stId + ";=;");
	pActivityRule->AddToTrueList(pActivity);

	                                                    
	                                                  
	stringstream sOverrideParams;
	DOMNodeListPtr pXmlOperationProperties = pXmlDocumentElement->selectNodes("OperationProperty");
	for( unsigned long i = 0, n = pXmlOperationProperties.getLength(); i < n; ++i ) {
		DOMElementPtr pPropertyElement( pXmlOperationProperties.item[i] );
		if ( pPropertyElement == 0 )
			continue;

		const string stName( pPropertyElement.getAttribute(ATT_NAME) );
		string stValue( pPropertyElement.getAttribute(ATT_VALUE) );
		                                                                                                     
		                                        

		stValue = FormatDataElementValue(task, stValue);

		if ( i > 0 ) sOverrideParams << "^";
		sOverrideParams << stName << "=" << stValue;
	}

	pActivity = new IntegrationActivity(process, process.IncrementActivityId());
	task.AddActivity(pActivity);
	pActivity->SetName(sOverrideParams.str());
	pActivity->SetLocation(stLogin);	                                                                       
	pActivityRule->AddToTrueList(pActivity);

	                                                      
	DataElement* pDataElement = new DataElement(process.IncrementDataElementId());
	task.AddDataElement(pDataElement);
	pDataElement->SetName("__OperationId");
	pDataElement->SetDescription("Hidden data element for WDI operation");
	pDataElement->SetDisplayName("__OperationId");
	pDataElement->SetType("int");
	pDataElement->SetOriginType("Task");

	                                                  
	AddTaskDataElements(task, pXmlDocumentElement.selectNodes("Data"));
}

void AddEscalation(Task& task, const xml::DOMElementPtr& pXmlEsclationElement) {
	if( pXmlEsclationElement == 0 ) return;                                            
	auto_ptr<Escalation> pEscalation( new Escalation(task.GetProcess().IncrementEscalationId()) );
	pEscalation->SetEscalatedTask(task.GetId());
	pEscalation->SetEscalationTaskName( pXmlEsclationElement->getAttribute("TaskName", true) );
	pEscalation->SetMinutesToEscalation( pXmlEsclationElement->getAttribute("RemindMinutes", true) );
	task.SetEscalationId(pEscalation->GetId());
	task.GetProcess().AddEscalation(pEscalation.release());                                           
}

void AddScript(Task& task, xml::DOMNodeListPtr& pScriptNodes, ScriptType scriptType) {
	if( pScriptNodes != 0 ) {
		DOMElementPtr pActivityElement;
		const ActivityItem* pActivityItem = 0;

		                                                                                               
		                                                                                                                 

		                                                                                                 
		                                   

		auto_ptr<ActivityRule> pActivityRule( new ActivityRule(task.GetProcess(), 0, 0) );
		while( (pActivityElement = pScriptNodes->nextNode()) != 0 ) {
			pActivityItem = HandleActivityItem(pScriptNodes, pActivityElement, task);
			if( pActivityItem )
				pActivityRule->AddToTrueList(pActivityItem);
		}

		if( pActivityRule->GetTrueActivityListAsString().empty() ) return;

		pActivityRule->SetId( task.GetProcess().IncrementActivityRuleId() );
		if( scriptType == ST_PRE_DOC ) task.SetPreRoleActivityRule(pActivityRule->GetId());
		else task.SetActivityRule(pActivityRule->GetId());
		task.GetProcess().AddActivityRule(pActivityRule.release());
	}
}

                                                                                    
                                                                                           
const ActivityItem* HandleActivityItem(xml::DOMNodeListPtr& pScriptNodes, const xml::DOMElementPtr& pActivityElement, Task& task) {
	if ( pActivityElement->getAttribute("IsCommentedOut") == "Yes" )
		return 0;

	const string stActivityType( pActivityElement->getAttribute(ATT_TYPE) );
	                                 
	if( stActivityType == "BeginItem" ||
		stActivityType == "Comment" ||
		stActivityType == "RemovedLink" ||
		stActivityType == "UnknownItemType" )
		return 0;

	                                                          
	                                                                                                    
	                                                                                                  
	                                                                     
                           
	if( stActivityType == "SimpleAction" )
		return HandleSimpleActionActivity(pActivityElement, task);
	else if( stActivityType == "TrueAction" )
		return HandleTrueActivityRule(pScriptNodes, pActivityElement, task);
	else if( stActivityType == "SimpleInFcnObject" )
		return HandleInternalFunctionActivity(pActivityElement, task);
	else if( stActivityType == "ODBCItem" )
		return HandleOdbcActivity(pActivityElement, task);
	else if( stActivityType == "FileRetrievalItem" )
		return HandleFileRetrieval(pActivityElement, task);
	else if( stActivityType == "TargetTask" )                 
		return HandleRoute(pActivityElement, task);
	else if( stActivityType == "FalseAction" )
		throw CrException("FalseAction encountered where it was not expected.");	                                             
	else if ( stActivityType == "EndifAction" )
		throw CrException("EndifAction encountered where it was not expected.");	                                             
	else if( stActivityType == "SimpleComObject" )
		throw NotImplementedYet("COM call encountered in script. Not yet implemented.");
	else if( stActivityType == "CORBAObject" )
		throw NotImplementedYet("CORBA call encountered in script. Not yet implemented.");
	else if( stActivityType == "EJBObject" )
		throw NotImplementedYet("EJB call encountered in script. Not yet implemented.");
	else
		throw CrException("Unknown script activity type \"" + stActivityType + "\" encountered in script.");
}

                
           
const ActivityItem* HandleSimpleActionActivity(const xml::DOMElementPtr& pActivityElement, Task& task) {
	Process& process = task.GetProcess();
	auto_ptr<SimpleActionActivity> pActivity( new SimpleActionActivity(process, process.IncrementActivityId()) );

	                       
	RuleTree rtree;
	string stText( pActivityElement->getAttribute(ATT_TEXT) );
	SubstituteTaskNameWithId(stText, process);

	rtree.parseExpression(stText);
	const string stStack( rtree.stackFormat() );
	pActivity->SetName(stStack);

	AddTaskDataElements(task, pActivityElement->selectNodes(ELEM_DATA));
	const ActivityItem* pItem = pActivity.get();
	task.AddActivity(pActivity.release());
	return pItem;
}

const ActivityItem* HandleInternalFunctionActivity(const xml::DOMElementPtr& pActivityElement, Task& task) {

	                                                
	                                                       
	const string stText(pActivityElement->getAttribute(ATT_TEXT));
	if ( stText == "FileXfer" )
		return HandleFileRetrieval(pActivityElement, task);
	else if ( stText == "RaiseError" )
		return HandleRaiseError(pActivityElement, task);
	else if ( stText == "CreateScheduledEvent" )
		return HandleScheduledEvent(pActivityElement, task);
	else if ( stText == "ReportDataElements" ||
			  stText == "TerminateProcess" ||
			  stText == "TerminateTask" ||
			  stText == "ResetLastError" ||
			  stText == "BlockEscalatedTask" )
		return HandleDefaultInternalFunctionActivity(pActivityElement, task);
	else
		throw CrException("The internal function \"" + stText + "\" is unknown or has not been implemented.");
}

const ActivityItem* HandleScheduledEvent(const xml::DOMElementPtr& pActivityElement, Task& task) {

                                                                                      

	string stTaskName, stTime, stDataElementName;
	DOMNodeListPtr pXmlDataElements( pActivityElement->selectNodes("Data") );
	for( unsigned long i = 0, n = pXmlDataElements->getLength(); i < n; ++i ) {
		DOMElementPtr pXmlData( pXmlDataElements->item[i] );
		switch(i) {
			case 0:
				stTaskName = pXmlData->getAttribute(ATT_NAME);
				break;
			case 1:
				{
					const string stOrigin( pXmlData->getAttribute(ATT_ORIGIN) );
					if ( stOrigin == "Constant" ) {
						int iTime = Escalation::GetTimeFromString(pXmlData->getAttribute(ATT_PATH));
						stTime = toString(iTime);
					}
					else {
						stTime = FormatDataElementValue(task, pXmlData->getAttribute(ATT_NAME));
					}
				}
				break;
			case 2:
				stDataElementName = pXmlData->getAttribute(ATT_NAME);
				break;
			default:
				throw CrException("Too many Data Elements for ScheduledEvent ActivityItem.");
		}
	}

	Process& process = task.GetProcess();
	AddTaskDataElements(task, pXmlDataElements);
	CreateScheduledEventActivity* pActivity = new CreateScheduledEventActivity(process, process.IncrementActivityId());
	task.AddActivity(pActivity);
	pActivity->SetTaskName(stTaskName);
	pActivity->SetTime(stTime);
	pActivity->SetDataElementName(stDataElementName);

	                                                                                                          
	                                  
	process.AddPendingScheduledEvent(pActivity);
	return pActivity;
}

const ActivityItem* HandleRaiseError(const xml::DOMElementPtr& pActivityElement, Task& task) {
	DOMNodeListPtr pXmlDataElements( pActivityElement->selectNodes("Data") );
	unsigned long n = pXmlDataElements->getLength();
	if ( n != 2 )
		throw CrException("Unknown number of Data elements for a RaiseError ActivityItem. Should be 2, was " + toString(n) + ".");

	bool bTerminateTask = false;
	string stArgument;
	for( unsigned long i = 0; i < 2; ++i ) {
		DOMElementPtr pXmlDataElement( pXmlDataElements->item[i] );
		const string stType( pXmlDataElement.getAttribute(ATT_TYPE) );
		const string stName( pXmlDataElement.getAttribute(ATT_NAME) );
		if ( stType == "boolean" ) {
			bTerminateTask = (stName != "0" );
		}
		else if ( stType == "string" ) {
			const string stOrigin( pXmlDataElement.getAttribute(ATT_ORIGIN) );
			if ( stOrigin == "Constant" )
				stArgument = "C." + stName;
			else if ( stOrigin == "InFcn" ) {
				stArgument = FormatDataElementValue(task, stName);
			}
			else
				throw CrException("Unexpected Origin of string argument for RaiseError. Only \"Constant\" or \"InFcn\" allowed. Found \"" + stOrigin + "\".");
		}
		else
			throw CrException("Unexpected data type for a RaiseError ActivityItem. Valid values are \"boolean\" or \"string\". Found \"" + stType + "\".");
	}

	Process& process = task.GetProcess();
	RaiseErrorActivity* pActivity = new RaiseErrorActivity(process, process.IncrementActivityId(), bTerminateTask, stArgument);
	task.AddActivity(pActivity);
	return pActivity;
}

const ActivityItem* HandleOdbcActivity(const xml::DOMElementPtr& pActivityElement, Task& task) {
	Process& process = task.GetProcess();
	auto_ptr<OdbcActivity> pActivity(new OdbcActivity(process, process.IncrementActivityId()));

	                             
	DOMElementPtr pXmlDBData( pActivityElement->selectSingleNode("DBData") );
	bool bIsDsn = (pXmlDBData->getAttribute("IsDSN") == "Yes");
	pActivity->SetConnectString(pXmlDBData->getAttribute("ConnectString"), bIsDsn);
	const string stEntityType( pXmlDBData->getAttribute("EntityType") );
	if ( stEntityType == "StoredProcedure" )
		throw NotImplementedYet("Stored procedures has not been implemented yet.");
	pActivity->SetEntityType(stEntityType);
	pActivity->SetEntityName(pXmlDBData->getAttribute("EntityName"));
	pActivity->SetStatementType(pXmlDBData->getAttribute("StatementType"));


	if ( pXmlDBData->getAttribute("IsAdvancedWhere") == "Yes" ) {
		pActivity->SetAdvancedWhere(pXmlDBData->getAttribute("AdvancedWhereClause"));
	}
	                                                                                                

	DOMNodeListPtr pXmlDataNodes( pActivityElement->selectNodes(ELEM_DATA) );
	AddTaskDataElements(task, pXmlDataNodes);
	for( unsigned long i = 0, n = pXmlDataNodes->getLength(); i < n; ++i ) {
		DOMElementPtr pXmlData( pXmlDataNodes->item[i] );
		if ( pXmlData->getAttribute(ATT_IO) == "O" )
			pActivity->AddOutputDataElement(process.GetDataElementByName(pXmlData->getAttribute(ATT_NAME)).GetId());
	}

	                        
	DOMNodeListPtr pXmlDBAttributeNode( pActivityElement->selectNodes("DBAttribute") );
	static const string stValueTypeAttr("ValueType"), stFilterAttr("Filter");
	static const string stDataTypeAttr("DataType"), stFilterTypeAttr("FilterType");
	for( unsigned long i = 0, n = pXmlDBAttributeNode->getLength(); i < n; ++i ) {
		DOMElementPtr pDBAttribute(pXmlDBAttributeNode->item[i]);
		DBAttribute dbAttr;
		dbAttr.m_stName = pDBAttribute.getAttribute(ATT_NAME);
		dbAttr.m_stDataType = pDBAttribute.getAttribute(stDataTypeAttr);
		dbAttr.m_stIO = pDBAttribute.getAttribute(ATT_IO);
		dbAttr.m_bIsConstantValue = (pDBAttribute.getAttribute(stValueTypeAttr) == "Constant");
		dbAttr.m_stValue = pDBAttribute.getAttribute(ATT_VALUE);
		if ( !dbAttr.m_stValue.empty() && !dbAttr.m_bIsConstantValue )
			dbAttr.m_stValue = FormatDataElementValue(task, dbAttr.m_stValue);
		dbAttr.m_stFilter = pDBAttribute.getAttribute(stFilterAttr);
		dbAttr.m_bIsConstantFilter = (pDBAttribute.getAttribute(stFilterTypeAttr) == "Constant");
		if ( !dbAttr.m_stFilter.empty() && !dbAttr.m_bIsConstantFilter )
			dbAttr.m_stFilter = FormatDataElementValue(task, dbAttr.m_stFilter);
		pActivity->AddDBAttribute(dbAttr);
	}

	                                           

	const ActivityItem* pItem = pActivity.get();
	task.AddActivity(pActivity.release());
	return pItem;
}

const ActivityItem* HandleDefaultInternalFunctionActivity(const xml::DOMElementPtr& pActivityElement, Task& task) {
	Process& process = task.GetProcess();
	auto_ptr<InternalFunctionActivity> pActivity(new InternalFunctionActivity(process, process.IncrementActivityId()));
	stringstream sName;
	sName << pActivityElement->getAttribute(ATT_TEXT) << "(";
	                         
	xml::DOMNodeListPtr pDataElementList = pActivityElement->selectNodes("Data");
	for( unsigned int i=0, n=pDataElementList.getLength(); i<n; ++i ) {
		xml::DOMElementPtr pDataElement = pDataElementList->item[i];
		const string stName( pDataElement.getAttribute(ATT_NAME) );
		const string stOrigin( pDataElement.getAttribute(ATT_ORIGIN) );
		sName << stName;
		if ( i < n-1 )
			sName << ",";
	}
	sName << ")";
	pActivity->SetName(sName.str());

	const ActivityItem* pItem = pActivity.get();
	task.AddActivity(pActivity.release());
	return pItem;
}

const ActivityItem* HandleFileRetrieval(const xml::DOMElementPtr& pActivityElement, Task& task) {
	static const string stNodeName("FileXferObject");
	static const string stAttrUrl("URL"), stAttrSource("FileOrString");
	static const string stAttrTransfer("Transfer"), stAttrOperation("Operation");
	static const string stAttrResponseName("ResponseDE");
	static const string stAttrUserName("UID"), stAttrPassword;
	static const string stAttrCertificate("Certificate"), stAttrTimeOut("Timeout");

	Process& process = task.GetProcess();
	auto_ptr<FileRetrievalActivity>
		pActivity( new FileRetrievalActivity(process, process.IncrementActivityId()) );
	xml::DOMElementPtr pFileXferNode( pActivityElement->selectSingleNode(stNodeName) );
	if ( pFileXferNode != 0 ) {
		pActivity->m_stUrl = pFileXferNode.getAttribute(stAttrUrl);
		pActivity->m_stFileOrString = pFileXferNode.getAttribute(stAttrSource);

		                                                                                          
		int iTransfer = atoi(pFileXferNode->getAttribute(stAttrTransfer).c_str());
		int iOperation = atoi(pFileXferNode->getAttribute(stAttrOperation).c_str());
		int& iFileMode = pActivity->m_iFileMode;
		int& iResponseMode = pActivity->m_iResponseMode;
		switch(iTransfer) {
		case 1:
			switch(iOperation) {
			case 1:
				iFileMode = 1;
				iResponseMode = 0;
				break;
			case 2:
				iFileMode = 5;
				iResponseMode = 2;
				break;
			}
			break;
		case 2:
			switch(iOperation) {
			case 1:
				iFileMode = 2;
				iResponseMode = 0;
				break;
			case 2:
				iFileMode = 6;
				iResponseMode = 1;
				break;
			}
			break;
		}

		pActivity->m_stResponseName = pFileXferNode->getAttribute(stAttrResponseName);
		pActivity->m_stUserName = pFileXferNode->getAttribute(stAttrUserName);
		pActivity->m_stPassword = pFileXferNode->getAttribute(stAttrPassword);
		pActivity->m_stCertName = pFileXferNode->getAttribute(stAttrCertificate);
		pActivity->m_lTimeOut = atoi(pFileXferNode->getAttribute(stAttrTimeOut).c_str());
	}

	const ActivityItem* pItem = pActivity.get();
	task.AddActivity(pActivity.release());
	return pItem;
}

const ActivityItem* HandleTrueActivityRule(xml::DOMNodeListPtr& pScriptNodes, const xml::DOMElementPtr& pXmlTrueActionActivity, Task& task) {
	Process& process = task.GetProcess();
	                                                                                  
	auto_ptr<Activity> pActivity( new TrueActionActivity(process, process.IncrementActivityId()) );

	const string stText( pXmlTrueActionActivity->getAttribute(ATT_TEXT) );
	RuleTree rtree;
	rtree.parseExpression(stText);
	pActivity->SetName(rtree.stackFormat());

	                                                                          
	auto_ptr<ActivityRule> pActivityRule( new ActivityRule(process, pActivity->GetId(), process.IncrementActivityRuleId()) );

	                                                                                                                    
	process.AddActivity( pActivity.release() );	                                                                     

	                                                                                           
	                                                                                         
	bool bIsPartOfTrueAction = true;
	while(true) {
		xml::DOMElementPtr pXmlActivity( pScriptNodes.nextNode() );
		if ( pXmlActivity == 0 ) {
			throw CrException("No \"FalseAction\" or \"EndifAction\" was found after the opening \"TrueAction\" Activity. The document is invalid.");
		}
		const string stType(pXmlActivity->getAttribute("Type"));
		if ( stType == "FalseAction" ) {
			bIsPartOfTrueAction = false;
			continue;	                                    
		}
		else if ( stType == "EndifAction" ) {
			break;	                        
		}
		if ( bIsPartOfTrueAction ) {
			const ActivityItem* pItem = HandleActivityItem(pScriptNodes, pXmlActivity, task);
			if ( pItem ) pActivityRule->AddToTrueList(pItem);
		}
		else {
			const ActivityItem* pItem = HandleActivityItem(pScriptNodes, pXmlActivity, task);
			if ( pItem ) pActivityRule->AddToFalseList(pItem);
		}
	}
	const ActivityItem* pItem = pActivityRule.get();
	process.AddActivityRule(pActivityRule.release());
	return pItem;
}

const ActivityItem* HandleRoute(const xml::DOMElementPtr& pRouteElement, Task& originTask) {
	Process& process = originTask.GetProcess();

	         
                                                                                

	static const string stAttrDestinationTask("Text");
	const string stDestinationTaskName( pRouteElement->getAttribute(stAttrDestinationTask) );
	                                                                                                                                            
	Route* pRoute = 0;
	try {
		Task& destinationTask = process.GetTaskByName(stDestinationTaskName);
		if( process.IsRoutePresent(originTask.GetId(), destinationTask.GetId()) )                                                                                    
			return 0;
		pRoute = new Route(process, process.IncrementRouteId(), originTask.GetId(), destinationTask.GetId());
	}
	catch( const CrException& ) {
		if( process.IsRoutePresent(originTask.GetId(), stDestinationTaskName) )
			return 0;
		                                                                                       
		pRoute = new Route(process, process.IncrementRouteId(), originTask.GetId(), stDestinationTaskName);
	}
	process.AddRoute(pRoute);
	return pRoute;
}

void AddRoutes(Process& process) {
	process.ConnectRoutes();
}

void AddRouteRules(Process& process, const xml::DOMNodeListPtr& pRouteNodes) {
	static const string stAttrTarget("Target");
	for( unsigned int i = 0, n = pRouteNodes.getLength(); i < n; ++i ) {
		xml::DOMElementPtr pXmlRouteElement = pRouteNodes.item[i];
		const string stTarget( pXmlRouteElement.getAttribute(stAttrTarget) );
		Task& destinationTask = process.GetTaskByName(stTarget);
		HandleRouteRule(pXmlRouteElement.selectNodes("RouteItem"), destinationTask);
	}
}

void HandleRouteRule(const xml::DOMNodeListPtr& pXmlRouteItemNodes, Task& destinationTask) {
	Process& process = destinationTask.GetProcess();
	static const char* opOr = "|";
	static const char* opAnd = "&";
	stack<const char*> operatorStack;
	string stSeqStack;
	for( unsigned int i = 0, n = pXmlRouteItemNodes.getLength(); i < n; ++i ) {
		xml::DOMElementPtr pRouteItemElement( pXmlRouteItemNodes.item[i] );
		const string stType( pRouteItemElement.getAttribute(ATT_TYPE) );
		if ( stType == "BeginItem" ) {
			         
		}
		else if ( stType == "OrItem" ) {
			operatorStack.push(opOr);
		}
		else if ( stType == "AndItem" ) {
			operatorStack.push(opAnd);
		}
		else if ( stType == "SourceTask" ) {
			const string stText(pRouteItemElement.getAttribute(ATT_TEXT));
			Task& originTask = process.GetTaskByName(stText);
			if ( !stSeqStack.empty() )
				stSeqStack += ";";
			stSeqStack += toString(originTask.GetId());
			
		}
		else if ( stType == "EndItem" ) {
			if ( !operatorStack.empty() ) {
				stSeqStack += ";";
				stSeqStack += operatorStack.top();
				operatorStack.pop();
			}
			else
				throw CrException("EndItem found without corresponding AndItem/OrItem");
		}
		else
			throw CrException("Unknown RouteItem type: " + stType + " encountered for Route to Task " + destinationTask.GetName());
	}
	if ( stSeqStack.find(";") == string::npos )
		stSeqStack.erase();
	else
		stSeqStack += ";";

	auto_ptr<RouteRule> pRouteRule( new RouteRule(process.IncrementRouteRuleId(), destinationTask.GetId(), stSeqStack) );

	                                                         
	escalation_id_t iEscalation = destinationTask.GetEscalationId();
	if( iEscalation > 0 ) {
		Escalation& escalation = process.GetEscalation(iEscalation);
		string stEscalationTaskName = escalation.GetEscalationTaskName();
		Task& escalationTask = process.GetTaskByName(stEscalationTaskName);
		escalation.SetEscalationTask( escalationTask.GetId() );

		pRouteRule->SetEscalationDestinationTaskId(escalationTask.GetId());
		pRouteRule->SetMinutesToEscalation(escalation.GetMinutesToEscalationAsString());
	}

	destinationTask.SetRouteRule( pRouteRule->GetId() );
	process.AddRouteRule( pRouteRule.release() );                                          
}

