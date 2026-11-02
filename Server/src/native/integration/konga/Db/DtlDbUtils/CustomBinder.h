/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTLDBUTILS_CUSTOMBINDER_H
#define KONGA_DTLDBUTILS_CUSTOMBINDER_H

#include <dtl/DTL.h>

template<class T>
class CustomBinder
{
public:
    virtual void Bind() {}
    void SetBoundIOs(dtl::BoundIOs* pIOs) {
        m_pBoundIOs = pIOs;
    }
    void SetRow(T* pRow) {
        m_pRow = pRow;
    }
protected:
    dtl::BoundIOs*  m_pBoundIOs;
    T*              m_pRow;
};
          

#endif

