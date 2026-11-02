/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef OBJECT_ACCESS_CONTROL_H
#define OBJECT_ACCESS_CONTROL_H

#include <map>
#include <string>

#include <boost/thread/mutex.hpp>

namespace konga {

class ObjectAccessControl  
{
public:
	ObjectAccessControl(bool bDefaultAllow = true) : m_bDefaultAllow(bDefaultAllow) {}
	virtual ~ObjectAccessControl() {}

	void AddCondition(const std::string& stName, bool bAllowAccess);
	bool IsAllowed(const std::string& stName) const;
	void SetDefaultAccess(bool bDefaultAllow) { m_bDefaultAllow = bDefaultAllow; }
	bool GetDefaultAccess() const { return m_bDefaultAllow; }

protected:

private:
	typedef std::map<std::string, bool> ObjAccessCont;
	ObjAccessCont m_Access;

	                                   
	mutable boost::mutex m_Lock;
	bool m_bDefaultAllow;
};

}                   

#endif                           

