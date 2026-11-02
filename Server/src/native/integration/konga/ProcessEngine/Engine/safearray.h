/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
              
 
#ifndef _SAFEARRAY_H_
#define _SAFEARRAY_H_

                                            
class Dim : public SAFEARRAYBOUND
{
public:
	Dim(const long iLo, const long iHi)
	{ cElements = abs(iHi - iLo + 1); lLbound = iLo; }
	Dim(const long c)
	{ cElements = c; lLbound = 0; }
	const Dim & operator=(const Dim & dim) 
	{ cElements = dim.cElements; lLbound = dim.lLbound; return *this; }
	const Dim & operator=(const long c) 
	{ cElements = c; lLbound = 0; return *this; }
	~Dim() {}
	long Elements() { return cElements; }
	long LBound() { return lLbound; }
	long UBound() { return lLbound + cElements - 1; }
};

                                                                 
const WORD VTA_CONSTRUCTED = 0x80;

                                          
template<class T, VARTYPE vt> 
class SafeArray 
{
public:
	               
	SafeArray();
	SafeArray(SAFEARRAY * psaSrc, bool bAttach=false);
	SafeArray(Dim & dim);
                       
	SafeArray(const SafeArray & saSrc);
	void Init() { m_bIsLocked = false; }

	             
	~SafeArray(); 

	                 
	const SafeArray & operator=(const SafeArray & saSrc);

	           
	T & Get(long i);
	T & Set(T & t, long i);
	T & operator[](const long i);                            
	T & operator()(const long i);                                   

	             
	operator SAFEARRAY(); 
	operator SAFEARRAY() const; 
	                      
	                           

	SAFEARRAY* ReleaseArray();
	T* AccessData();
	void UnAccessData();

	             
	bool ReDim(Dim & dim);
	long LBound();
	long UBound();
	long Elements();
	long Dimensions();
    bool IsSizable();
	void Lock();
	void Unlock();
	bool IsLocked() { return m_bIsLocked; }

private:
	bool m_bIsLocked;

	SAFEARRAY * psa;

    void Destroy();
    bool IsConstructed();
    void Constructed(bool f);
};
               

                  

template<class T, VARTYPE vt> 
inline void SafeArray<T,vt>::Destroy()
{
    Constructed(false);
	HRESULT hres = SafeArrayDestroy(psa);
    if (hres) throw hres;
}

template<class T, VARTYPE vt> 
inline bool SafeArray<T,vt>::IsConstructed()
{
    return (psa->fFeatures & VTA_CONSTRUCTED) ? true : false;
}

template<class T, VARTYPE vt> 
inline void SafeArray<T,vt>::Constructed(bool f)
{
    if (f) {
        psa->fFeatures |= VTA_CONSTRUCTED;
    } else {
        psa->fFeatures &= ~VTA_CONSTRUCTED;
    }
}

               
template<class T, VARTYPE vt> 
inline SafeArray<T,vt>::SafeArray() 
{ 
    Dim dim(0);
	psa = SafeArrayCreate(vt, 1, &dim); 
    Constructed(true);
    if (psa == NULL) throw E_OUTOFMEMORY;
	Init();
}


template<class T, VARTYPE vt> 
inline SafeArray<T,vt>::SafeArray(SAFEARRAY * psaSrc, bool bAttach          ) 
{ 
    if (SafeArrayGetDim(psaSrc) != 1) throw E_INVALIDARG;
    psa = psaSrc; 
    Constructed(bAttach);
	Init();
}

template<class T, VARTYPE vt> 
inline SafeArray<T,vt>::SafeArray(const SafeArray & saSrc) 
{
    HRESULT hres = SafeArrayCopy(saSrc.psa, &psa);
    Constructed(true);
    if (hres) throw hres;
	Init();
}


template<class T, VARTYPE vt> 
inline SafeArray<T,vt>::SafeArray(Dim & dim) 
{
	psa = SafeArrayCreate(vt, 1, &dim); 
    Constructed(true);
    if (psa == NULL) throw E_OUTOFMEMORY;
	Init();
} 

             
template<class T, VARTYPE vt> 
inline SafeArray<T,vt>::~SafeArray()
{
	if( IsLocked() )
		Unlock();

	if (IsConstructed()) {
        Destroy();
    }
} 
	
              
template<class T, VARTYPE vt> 
const SafeArray<T,vt> & SafeArray<T,vt>::operator=(const SafeArray & saSrc)
{
    if (psa) {
        
        SafeArrayDestroy(psa);
    }
    HRESULT hres = SafeArrayCopy(saSrc.psa, &psa);
    Constructed(true);
    if (hres) throw hres;
    return *this;
}

             
template<class T, VARTYPE vt> 
inline SafeArray<T,vt>::operator SAFEARRAY()
{
    return *psa; 
}

template<class T, VARTYPE vt> 
SafeArray<T,vt>::operator SAFEARRAY() const
{
    static SAFEARRAY * psaT;
    SafeArrayCopy(psa, &psaT);
    psaT->fFeatures &= ~VTA_CONSTRUCTED;
    return *psaT;
}

template<class T, VARTYPE vt> 
inline SAFEARRAY* SafeArray<T,vt>::ReleaseArray()
{
	Constructed(false);
    return psa; 
}

                                
                                    
 
                        
 

                              
                                         
 
                          
             
 
  
           
template<class T, VARTYPE vt> 
T & SafeArray<T,vt>::Get(long i)
{
	static T tRes;
	HRESULT hres = SafeArrayGetElement(psa, &i, &tRes);
	if (hres) throw hres;
	return tRes;
}

             
template<class T, VARTYPE vt> 
inline T & SafeArray<T,vt>::Set(T & t, long i)
{
	HRESULT hres = SafeArrayPutElement(psa, &i, (T *)&t);
	if (hres) throw hres;
    return t;
}

template<class T, VARTYPE vt> 
inline T & SafeArray<T,vt>::operator[](const long i)
{
    if (i < 0 || i > Elements() - 1) throw DISP_E_BADINDEX;
	return ((T*)psa->pvData)[i];
}

template<class T, VARTYPE vt> 
T & SafeArray<T,vt>::operator()(const long i)
{
    if (i < LBound() || i > UBound()) throw DISP_E_BADINDEX;
	return ((T*)psa->pvData)[i - LBound()];
}
             

             
template<class T, VARTYPE vt> 
bool SafeArray<T,vt>::ReDim(Dim &dim)
{
    if (!IsSizable()) {
        return false;
    }
	HRESULT hres = SafeArrayRedim(psa, &dim);
	if (hres) throw hres;
    return true;
}

template<class T, VARTYPE vt> 
long SafeArray<T,vt>::LBound()
{
	long iRes;
	HRESULT hres = SafeArrayGetLBound(psa, 1, &iRes);
	if (hres) throw hres;
	return iRes;
}

template<class T, VARTYPE vt> 
inline long SafeArray<T,vt>::Elements()
{
	return psa->rgsabound[0].cElements;
}

template<class T, VARTYPE vt> 
long SafeArray<T,vt>::UBound()
{
	long iRes;
	HRESULT hres = SafeArrayGetUBound(psa, 1, &iRes);
	if (hres) throw hres;
	return iRes;
}

template<class T, VARTYPE vt> 
inline long SafeArray<T,vt>::Dimensions()
{
	return 1;
}

template<class T, VARTYPE vt> 
inline bool SafeArray<T,vt>::IsSizable()
{
    return (psa->fFeatures & FADF_FIXEDSIZE) ? false : true;
}

template<class T, VARTYPE vt>
inline void SafeArray<T,vt>::Lock()
{
	HRESULT hres = SafeArrayLock(psa);
	if (hres) throw hres;
	m_bIsLocked = true;
}

template<class T, VARTYPE vt>
inline void SafeArray<T,vt>::Unlock()
{
	HRESULT hres = SafeArrayUnlock(psa);
	if (hres) throw hres;
	m_bIsLocked = false;
}

template<class T, VARTYPE vt>
inline T* SafeArray<T,vt>::AccessData()
{
	if( !IsLocked() )
		Lock();
	T* pData;
	HRESULT hres = ::SafeArrayAccessData(psa, reinterpret_cast<void**>(&pData));
	if (hres) throw hres;
	return pData;
}

template<class T, VARTYPE vt>
inline void SafeArray<T,vt>::UnAccessData()
{
	HRESULT hres = ::SafeArrayUnaccessData(psa);
	if (hres) throw hres;
}

#endif                 
