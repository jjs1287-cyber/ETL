/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	#include <windows.h>
#endif         

#include <konga/Db/PDb/ObjectPermissionsTab.h>                                      

#include <konga/Server/Server.h>				              

#include "EngineCore.h"
#include "KeyValues.h"			                       
#include "IncomingMsg.h"		                                                
#include "KeyValues.h"			                       

void EngineCore::ReadAccessControledObjectList()
{
	konga::SmartDbPtr<PDb::ObjectPermissionsTab> pObjectPermissionsTab( PDb::ObjectPermissionsTab::MakeInstance(GetPDB()));
	try
	{
		pObjectPermissionsTab->Select();
		while(pObjectPermissionsTab->Next())
			m_ObjectAccessControl.AddCondition( pObjectPermissionsTab->GetObjectName(),
												pObjectPermissionsTab->GetAllowAccess()!=0 );
	}
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 357] Database error while reading access control list for object calls. "
			"All object calls will be disabled. (PDBObjectPermissionsTab) " + e.GetErrorString() +
			" [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		m_ObjectAccessControl.SetDefaultAccess(false);                                      
	}
	pObjectPermissionsTab->Close();
}

