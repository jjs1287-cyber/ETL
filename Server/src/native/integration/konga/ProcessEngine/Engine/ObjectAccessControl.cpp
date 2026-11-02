/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
  
                                                                            
  
                                                                      
#include <boost/thread/mutex.hpp>

#include "ObjectAccessControl.h"

void ObjectAccessControl::AddCondition(const std::string& stName, bool bAllowAccess)
{
	boost::mutex::scoped_lock auto_lock(m_Lock);
	m_Access[ stName ] = bAllowAccess;
}

bool ObjectAccessControl::IsAllowed(const std::string& stName) const
{
	boost::mutex::scoped_lock auto_lock(m_Lock);
	ObjAccessCont::const_iterator cit = m_Access.begin();
	if( cit == m_Access.end() )
		return m_bDefaultAllow;
	else
		return cit->second;
}
