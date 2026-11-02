/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_AUTO_CLEANUP_GENERIC
#define KONGA_AUTO_CLEANUP_GENERIC

#include <boost/function.hpp>		                  
#include <boost/noncopyable.hpp>

namespace konga {

template<class T>
class AutoCleanupGeneric : boost::noncopyable
{
public:
	AutoCleanupGeneric(T& cleanMe, boost::function<void (T&)> cleanUp) : 
		m_cancel(false), m_cleanMe(cleanMe), m_cleanUp(cleanUp)
	{
	}
	~AutoCleanupGeneric() { 
		if(!m_cancel) m_cleanUp();
	}

	T& Release() { 
		m_cancel = true;
		return m_cleanMe;
	}

private:
	bool m_cancel;
	T& m_cleanMe;
	boost::function<void (T&)> m_cleanUp;
};

}                   

#endif                              

