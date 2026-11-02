/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTLDBUTILS_CUSTOMBCA_H
#define KONGA_DTLDBUTILS_CUSTOMBCA_H

#include <assert.h>
#include <dtl/DTL.h>

#include "CustomBinder.h"

template <class T>
class CustomBCA
{
public:
    CustomBCA<T>(CustomBinder<T>* pBinder = 0) : m_pBinder(pBinder)
    {}
    CustomBCA<T>(const CustomBCA<T>& c) : m_pBinder(c.m_pBinder)
    {}
    void operator()(dtl::BoundIOs &cols, T &rowbuf)
    {
        assert(m_pBinder != 0);
        m_pBinder->SetBoundIOs(&cols);
        m_pBinder->SetRow(&rowbuf);
        m_pBinder->Bind();
    }
    void SetBinder(CustomBinder<T>* pBinder)
    {
        m_pBinder = pBinder;
    }

protected:
    CustomBinder<T>* m_pBinder;
};


#endif

