/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef JB_WEBSERVICEHANDLER_H
#define JB_WEBSERVICEHANDLER_H

#include <string>
#include <vector>
#include <map>

#include <boost/noncopyable.hpp>
#include <konga/Soap/SoapUtils.h>
#include <konga/Soap/SoapFault_11.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>

#include "WsResponseTransactionStructure.h"
#include "WebServiceUtils.h"

class CErrorLog;
class CViewLog;
class CROM;
class DTDParser;
class XsdNamespaceSupport;
class TreeMapper;

namespace konga
{
    class SoapFault;
	class HttpOptions;
}

class WebServiceInfo : boost::noncopyable
{
public:
	WebServiceInfo(const std::string& stUrl,
				   const std::string& stSoapAction,
				   const std::string& stTargetNamespace,
				   const std::string& stOtherNamespaces,
				   const std::string& stSoapStyle,
				   const std::string& stSoapBodyUse,
				   long webServiceId,
				   int numberOfRetries, int retryWaitSeconds, int maxRedirs,
				   long timeOutSeconds,
   				   const std::string& stHttpUserName,
				   const std::string& stHttpPasswordEncrypted) :
	m_lWebServiceId(webServiceId),
	m_numberOfRetries(numberOfRetries), m_retryWaitTimeInSeconds(retryWaitSeconds), m_maxRedirs(maxRedirs),
	m_timeOutInSeconds(timeOutSeconds),
	m_stUrl( stUrl ),
	m_stSoapAction( stSoapAction ),
	m_stHttpUserName( stHttpUserName ),
	m_stHttpPasswordEncrypted( stHttpPasswordEncrypted ),
	m_stTargetNamespace( stTargetNamespace ),
	m_stOtherNamespaces( stOtherNamespaces ),
	m_soapStyle( konga::GetSoapStyle(stSoapStyle) ),
	m_soapBodyUse( konga::GetSoapBodyUse(stSoapBodyUse) )
	{
		if( m_stOtherNamespaces.length() > 0 && 
			m_stOtherNamespaces.find_last_of(WebServiceUtils::NAMESPACE_SEPARATOR) != (m_stOtherNamespaces.length()-1) )
		{
			m_stOtherNamespaces += WebServiceUtils::NAMESPACE_SEPARATOR;
		}
	};
	~WebServiceInfo() {};

	long GetWebServiceId() const { return m_lWebServiceId; }
	std::string	GetUrl() const { return m_stUrl; }
	std::string	GetSoapAction() const { return m_stSoapAction; }
	std::string	GetTargetNamespace() const { return m_stTargetNamespace; }
	std::string	GetOtherNamespaces() const { return m_stOtherNamespaces; }
	konga::SoapConstants::SoapStyle GetSoapStyle() const { return m_soapStyle; }
	konga::SoapConstants::SoapBodyUse GetSoapBodyUse() const { return m_soapBodyUse; }
	std::string	GetHttpUserName() const { return m_stHttpUserName; }
	std::string	GetHttpPasswordEncrypted() const { return m_stHttpPasswordEncrypted; }

	int GetNumberOfRetries() const { return m_numberOfRetries; }
	int GetRetryWaitTimeInSeconds() const { return m_retryWaitTimeInSeconds; }
	int GetMaxRedirects() const { return m_maxRedirs; }
	long GetTimeOutSeconds() const { return m_timeOutInSeconds; }

	void	ParseOtherNamespaces(std::map<std::string, std::string>& mapOtherNamespaces) const;

private:
	long						m_lWebServiceId;
	int							m_numberOfRetries, m_retryWaitTimeInSeconds, m_maxRedirs;
	long						m_timeOutInSeconds;
	std::string					m_stUrl;
	std::string					m_stSoapAction;
	std::string					m_stHttpUserName;
	std::string					m_stHttpPasswordEncrypted;
	std::string					m_stTargetNamespace;
	std::string					m_stOtherNamespaces;
	konga::SoapConstants::SoapStyle	m_soapStyle;
	konga::SoapConstants::SoapBodyUse	m_soapBodyUse;
};

class WebServiceStructure : boost::noncopyable
{

public:
	WebServiceStructure(TreeMapper* p,
						const std::string& stRequestXsd,
						const std::string& stRequestJtr,
						const std::string& stResponseXsd,
						const std::string& stResponseJtr);
	~WebServiceStructure();

	TreeMapper* getTreeMapper() const {
		return m_pTreeMapper;
	}
	std::string	GetRequestJtrFilename() const { 
		return m_stRequestJtr; 
	}
	std::string	GetResponseJtrFilename() const { 
		return m_stResponseJtr; 
	}
	XsdNamespaceSupport* GetResponseSchemaNamespaces() const {
		return m_responseSchemaNamespaces;
	}
	bool			RequestHasValueContent() const { 
		return m_requestHasValueContent; 
	}
	const WsResponseTransactionStructure* GetTransactionStructure() const { 
		return &m_responseTransactionStructure; 
	}

	void		    Load(bool loadRequestStructure, bool loadResponseStructure);
	bool			IsLoaded() const { return m_isLoaded; }
	void			CreateEmptyRequest(const std::string& stOutputFile);
	CROM*			GetRequestRoot() const;
	std::string		GetRequestTargetNamespace() const;
	CROM*			GetResponseRoot() const;
	std::string		GetResponseTargetNamespace() const;
	bool			ResponseStructureHasRequestNode() const;

private:
	TreeMapper*	m_pTreeMapper;
	std::string				m_stRequestXsd;
	std::string				m_stRequestJtr;
	std::string				m_stResponseXsd;
	std::string				m_stResponseJtr;
	bool					m_isLoaded;
	bool					m_requestHasValueContent;

	DTDParser*				m_requestParser;
	DTDParser*				m_responseParser;
	XsdNamespaceSupport*	m_responseSchemaNamespaces;

	WsResponseTransactionStructure	m_responseTransactionStructure;

	void LoadRequestStructure();
	void LoadResponseStructure();
};

class WebServiceIO : boost::noncopyable
{
public:
	WebServiceIO(const std::string& stInputFile,
				 const std::string& stOutputFile,
				 const std::string& stRequestFile,
				 const std::string& stResponseFile) : m_stInputFilename(stInputFile),
												 m_stOutputFilename(stOutputFile),
												 m_stRequestFilename(stRequestFile),
												 m_stResponseFilename(stResponseFile)
	{
		if( stInputFile.empty() && !m_stRequestFilename.empty() ) {
			                         
			                                                                          
			                                                                     
			m_stInputFilename = m_stRequestFilename + "_input.xml";
			FILE* f = fopen( m_stInputFilename.c_str(), "w+" );
			fclose(f);
		}
	};
	~WebServiceIO() {};

	std::string GetInputFilename() const { return m_stInputFilename; }
	std::string GetOutputFilename() const { return m_stOutputFilename; }
	std::string GetRequestFilename() const { return m_stRequestFilename; }
	std::string GetResponseFilename() const { return m_stResponseFilename; }

private:
	std::string	m_stInputFilename;
	std::string	m_stOutputFilename;
	std::string	m_stRequestFilename;
	std::string	m_stResponseFilename;
};

class WebServiceRestrictions : boost::noncopyable
{
public:
	WebServiceRestrictions(long lDelay = -1L, long lMaxSizeBytes = -1L) : m_lDelay(lDelay),
																		  m_lMaxSizeBytes(lMaxSizeBytes)
	{};
	~WebServiceRestrictions() {};

	long	GetDelay() const { return m_lDelay; }
	long	GetMaxSizeBytes() const { return m_lMaxSizeBytes; }
private:
	long	m_lDelay;
	long	m_lMaxSizeBytes;
};

class WebServiceHandler : boost::noncopyable
{
	TreeMapper*m_pTreeMapper;
public:
	WebServiceHandler(TreeMapper*p,
						WebServiceInfo& info, 
						WebServiceIO& io,
						WebServiceStructure& structure,
						WebServiceRestrictions& restrictions,
						const konga::HttpOptions& httpOptions,
						CErrorLog& errorLog,
						CViewLog& viewLog);
	virtual ~WebServiceHandler(void);

	TreeMapper*getTreeMapper() const {return m_pTreeMapper;}

	enum	Mode	{ Request, Response };

	unsigned long CallWebService();
	unsigned long ScaleDownSoapEnvelope(Mode mode, bool isHostedWebService, const std::string& stNewFilename);
	unsigned long CreateSoapEnvelopeForHostedResponse(const std::string& stNewFilename);

	std::string	GetLastError() const { return m_stLastError; }
	void	SetCreateSoapFaultOnError(bool bCreateSoapFaultOnError) { m_bCreateSoapFaultOnError = bCreateSoapFaultOnError; }
	                                                                                                       
	                                                                              
	konga::SoapFault*	GetSoapFault(bool bAdopt = false);

	bool HasEmptyInput() const { return m_bEmptyInput; }
	
protected:
	std::string replace_de(const std::string& value);
	std::string GetCookiejarFileName();

	void RunPipelinePlugin(const Mode mode, const std::string& sFileName);

private:
	WebServiceInfo&			m_info;
	WebServiceIO&			m_io;
	WebServiceStructure&	m_structure;
	WebServiceRestrictions&	m_restrictions;

	bool		m_bCreateSoapFaultOnError;
	bool		m_bEmptyInput;
	bool		m_syncResponse;

	                                                                                             
	                                 
	konga::SoapFault*	m_pSoapFault;

	std::string	m_stSoapEnvelopePrefix;

	const konga::HttpOptions& m_httpOptions;

	std::map<std::string, std::string>	m_mapOtherNamespaces;
	std::map<std::string, std::string>  m_arrayConvertedFiles;
	std::vector<std::string>			m_vstRequestFileNames;
	std::vector<std::string>			m_vstOutputFileNames;

	std::string		m_stLastError;
	CErrorLog&	m_errorLog;
	CViewLog&	m_viewLog;

	unsigned long	CreateRequestFiles();
	unsigned long	PrepareRequest(int nReq, const std::string& stRequestFileName);
	std::string		GetResponseFileName(int nReq);
	unsigned long	SendRequest(const std::string& stRequestFile, 
							    const std::string& stResponseFile, 
								const std::string& stCookiejarFileName, 
								const std::string& stPassword);
	unsigned long	SendSalesforceQueryMoreRequest(const std::string& stRequestFileName, const std::string& stResponseFileName, const std::string& stOutputFileName, const std::string& stCookiejarFileName, const std::string& stDecryptedPassword);
	unsigned long	ProcessResponse(const std::string& stRequestFileName, const std::string& stResponseFileName, const std::string& stOutputFileName);
	unsigned long	CheckForEmptyInput();
	unsigned long	CreateSoapContent(WebServiceHandler::Mode mode, const std::string& stSoapEnvelopeFilename = "");
	unsigned long	CreateSoapHeader(konga::xml::DOMDocumentPtr& pDoc,
							 konga::xml::DOMElementPtr& pEnvelopeNode,
							 const konga::xml::DOMElementPtr& pOriginalHeader,
							 const std::string& stSchemaTypeAttributeName,
							 CROM* pTransactionCromNode);
	unsigned long CreateSoapBody(konga::xml::DOMDocument2Ptr& pDoc,
								 konga::xml::DOMElementPtr& pEnvelopeNode,
								 const konga::xml::DOMElementPtr& pOriginalBody,
								 const std::string& stSchemaTypeAttributeName,
								 CROM* pTransactionCromNode,
								 const std::string& stFilename);
	unsigned long	EnvelopeToXsdStructure(const std::string& stOriginalXmlFilename,
								   const std::string& stOutputFilename,
								   const std::string& stRequestFilename,
								   WebServiceHandler::Mode mode,
								   bool bIncludeRequestInResponse,
								   bool bWrapBodyAndHeader);

	unsigned long CreateTransactionStructure(konga::xml::DOMDocument2Ptr& pOutputDoc,
									   konga::xml::DOMElementPtr& pTransactionNode,
									   const std::string& stXmlFilename,
									   WebServiceHandler::Mode mode,
									   bool bWrapBodyAndHeader);
	void MoveChildNodes(konga::xml::DOMElementPtr& pOriginalParent,
						konga::xml::DOMElementPtr& pNewParent,
						konga::xml::DOMDocumentPtr& pTargetDocument,
						WebServiceHandler::Mode mode);

	unsigned long MergeOutputDocuments();

	std::string	GetSoapEnvelopeNamespacePrefix();
	std::string	DeclareSchemaInstanceNamespace(konga::xml::DOMElementPtr& pEnvelopeNode, 
										   konga::xml::XmlNamespaceDeclarations& envelopeNamespaces) const;

	std::string GetIndexedFileName(const std::string&stOldFileName, unsigned int i);

	CROM* GetTransactionNode(WebServiceHandler::Mode mode);

	void	SetSoapFault(konga::SoapFault* pSoapFault);
	std::string GetNameOfRequestFileToAttachToResponse(const std::string& stNameOfRequestSentToServer) const;
	unsigned long LoadXmlRequestFromTransformation(const std::string stOriginalXmlFile, konga::xml::DOMElementPtr& pOriginalRoot);
	unsigned long CreateSoapContentForTransactionNode(konga::xml::DOMElementPtr pTransactionNode, 
					CROM* pTransactionCromNode,
					konga::xml::DOMElementPtr pOriginalRoot,
					const std::string& stEnvelope,
					const std::string& stSoapEnvelopeFilename,
					WebServiceHandler::Mode mode,
					unsigned int fileCounter);
	konga::xml::DOMElementPtr CreateRequestEnvelopeElement(konga::xml::DOMDocument2Ptr pRequestDoc, 
					konga::xml::DOMElementPtr pOriginalRoot, 
					const std::string& stEnvelope, 
					std::string& stSchemaTypeAttributeName) const;

	void LoadMappingStructures();
};

std::string SetSpecificSoapFault(const konga::SoapFault& soapFault);
std::string SetSpecificSoapFault(konga::FaultCodeValue_11 faultCode, const std::string& stFaultString);
std::string SetGenericSoapFault(konga::FaultCodeValue_11 faultCode, long lErrorCode);

#endif                          
