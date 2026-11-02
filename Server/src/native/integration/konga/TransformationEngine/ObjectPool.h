/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                    
  
                                                                      

#if !defined(AFX_OBJECTPOOL_H__4EDDC55B_F804_4E67_BA89_867EFDB0E0A9__INCLUDED_)
#define AFX_OBJECTPOOL_H__4EDDC55B_F804_4E67_BA89_867EFDB0E0A9__INCLUDED_

#include <map>

template<typename T>
class ObjectPool
{
public:
	ObjectPool() : m_lTokenCounter(0)
	{
		::InitializeCriticalSection(&m_InstanceContainerLock);
	}
	virtual ~ObjectPool()
	{
		::DeleteCriticalSection(&m_InstanceContainerLock);
	}

public:
	class NoSuchInstance {};

	typedef std::map< unsigned long, T > InstanceContainer;
	typedef typename InstanceContainer::iterator InstanceContainerIt;
	typedef typename InstanceContainer::const_iterator InstanceContainerConstIt;
	typedef typename InstanceContainerIt iterator;
	typedef typename InstanceContainerConstIt const_iterator;

	InstanceContainerIt begin() { return m_InstanceContainer.begin(); }
	InstanceContainerConstIt begin() const { return m_InstanceContainer.begin(); }
	InstanceContainerIt end() { return m_InstanceContainer.end(); }
	InstanceContainerConstIt end() const { return m_InstanceContainer.end(); }

	T GetInstance(unsigned long lToken) const;
	unsigned long AddInstance(T instance);
	void AddInstance(T instance, unsigned long lToken);
	void RemoveInstance(unsigned long lToken);
	unsigned long FindInstance(T instance) const;

protected:
	typedef std::map< T, unsigned long > InstanceToTokenMap;
	typedef typename InstanceToTokenMap::iterator InstanceToTokenMapIt;
	typedef typename InstanceToTokenMap::const_iterator InstanceToTokenMapConstIt;

private:
	InstanceContainer m_InstanceContainer;
	InstanceToTokenMap m_InstanceToTokenMap;
	mutable CRITICAL_SECTION m_InstanceContainerLock;
	mutable unsigned long m_lTokenCounter;

};

template<typename T> T ObjectPool<T>::GetInstance(unsigned long lToken) const
{
	AutoCriticalSection auto_lock(m_InstanceContainerLock);
	InstanceContainerConstIt instIt = m_InstanceContainer.find(lToken);
	if( instIt == m_InstanceContainer.end() )
		throw NoSuchInstance();
	else
		return instIt->second;
}

template<typename T> unsigned long ObjectPool<T>::AddInstance(T instance)
{
	AutoCriticalSection auto_lock(m_InstanceContainerLock);
	m_InstanceContainer[ ++m_lTokenCounter ] = instance;
	m_InstanceToTokenMap[ instance ] = m_lTokenCounter;
	return m_lTokenCounter;
}

  
                                                                       
                
  
template<typename T> void ObjectPool<T>::AddInstance(T instance, unsigned long lToken)
{
	AutoCriticalSection auto_lock(m_InstanceContainerLock);
	m_InstanceContainer[ lToken ] = instance;
	m_InstanceToTokenMap[ instance ] = lToken;
}

template<typename T> void ObjectPool<T>::RemoveInstance(unsigned long lToken)
{
	AutoCriticalSection auto_lock(m_InstanceContainerLock);
	InstanceContainerIt instIt = m_InstanceContainer.find(lToken);
	if( instIt == m_InstanceContainer.end() )
		throw NoSuchInstance();
	else
	{
		InstanceToTokenMapIt it = m_InstanceToTokenMap.find( instIt->second );
		if( it != m_InstanceToTokenMap.end() )
			m_InstanceToTokenMap.erase(it);
		m_InstanceContainer.erase(instIt);
	}
}

template<typename T> unsigned long ObjectPool<T>::FindInstance(T instance) const
{
	InstanceToTokenMapIt it = m_InstanceToTokenMap.find( instance );
	if( it == m_InstanceToTokenMap.end() )
		throw NoSuchInstance();
	else
		return *it;
}

#endif                                                                               
