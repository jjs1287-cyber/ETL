/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef WS_RESPONSE_TRANSFORMATION_STRUCTURE_H
#define WS_RESPONSE_TRANSFORMATION_STRUCTURE_H

#include <string>

class CROM;
class XSDParser;

                                          
  
                      
             
         
              
           
         
            
                       
  
                               
class TransactionPartStructure
{
public:
	TransactionPartStructure() : m_pCromRoot(0), m_pHeaderNode(0), m_pBodyNode(0) {};
	~TransactionPartStructure() {};

	void	Load(CROM* pCromRoot);
	CROM*	GetRoot() const { return m_pCromRoot; }
	CROM*	GetHeader() const { return m_pHeaderNode; }
	CROM*	GetBody() const { return m_pBodyNode; }
	std::string	GetTargetNamespace() const { return m_stTargetNamespace; }

private:
	CROM*	m_pCromRoot;
	CROM*	m_pHeaderNode;
	CROM*	m_pBodyNode;

	std::string	m_stTargetNamespace;
};


                                                                                        
class WsResponseTransactionStructure
{
public:
	WsResponseTransactionStructure(void) : m_pTransactionNode(0), m_bHasRequest(false) {};
	virtual ~WsResponseTransactionStructure(void) {};

	void Load(CROM* root);
	
	const TransactionPartStructure* GetRequestStructure() const { return m_bHasRequest ? &m_requestStructure : 0; }
	const TransactionPartStructure* GetResponseStructure() const { return &m_responseStructure; }

	std::string GetTargetNamespace() const { return m_responseStructure.GetTargetNamespace(); }

private:
	CROM*	m_pTransactionNode;
	bool	m_bHasRequest;

	TransactionPartStructure	m_requestStructure;
	TransactionPartStructure	m_responseStructure;

};

#endif                                          
