/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SMARTDBPTR_H
#define KONGA_SMARTDBPTR_H

#include <boost/noncopyable.hpp>
#include "DbException.h"

namespace konga
{

template<typename T>
class SmartDbPtr : boost::noncopyable
{
public:
	SmartDbPtr(T* pObj = 0) : m_pObj(pObj) {}
	~SmartDbPtr() { CloseAndDelete(); }

	void Attach(T* p) {
		if ( p == m_pObj )
			return;	                                                                            
		CloseAndDelete(); 
		m_pObj = p;
	}

	T* Release() { 
		T* p = m_pObj;
		m_pObj = 0;
		return p;
	}
    T* operator -> () { return m_pObj; }
    operator T*() const { return m_pObj; }

	void CloseAndDelete() {
		if( m_pObj != 0 ) {
			try {
				m_pObj->Close();
			}
			catch( const konga::DbException& ) {
				                           
				delete m_pObj;
				m_pObj = 0;
                throw;
			}
			delete m_pObj;
			m_pObj = 0;
		}
	}

private:
	T*	m_pObj;
};

}
#endif

