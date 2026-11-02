/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                            
  
                                                                      

#if !defined(AFX_DOCSCM1_H__691A47FC_7DA8_4FF3_93AA_16CF11D6AB22__INCLUDED_)
#define AFX_DOCSCM1_H__691A47FC_7DA8_4FF3_93AA_16CF11D6AB22__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif                   

#include "resource.h"                      

                                                                             
         

class DocScm : 
	public IDispatchImpl<IDocScm, &IID_IDocScm, &LIBID_DOCSCMLib>, 
	public ISupportErrorInfo,
	public CComObjectRoot,
	public CComCoClass<DocScm,&CLSID_DocScm>
{
public:
	DocScm() {}
BEGIN_COM_MAP(DocScm)
	COM_INTERFACE_ENTRY(IDispatch)
	COM_INTERFACE_ENTRY(IDocScm)
	COM_INTERFACE_ENTRY(ISupportErrorInfo)
END_COM_MAP()
                                   
                                                                           
                        

DECLARE_REGISTRY_RESOURCEID(IDR_DocScm)
                     
	STDMETHOD(InterfaceSupportsErrorInfo)(REFIID riid);

	CString m_lastErr;

          
public:
	STDMETHOD(get_lastError)(                  BSTR *pVal);
	STDMETHOD(CreateDocScmFile)(BSTR dsn, int docID, BSTR fileName);
	STDMETHOD(CreateDocScmFile2)(BSTR dsn, int docID, int tranID, BSTR fileName);
};

#endif                                                                            
