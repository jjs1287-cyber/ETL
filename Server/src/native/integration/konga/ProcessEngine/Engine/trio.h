/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#if !defined(AFX_TRIO_H__318AF344_A5CB_11D3_9B44_FEBA4A000000__INCLUDED_)
#define AFX_TRIO_H__318AF344_A5CB_11D3_9B44_FEBA4A000000__INCLUDED_

                       
template<class _T1, class _T2, class _T3> struct trio {
	typedef _T1 first_type;
	typedef _T2 second_type;
	typedef _T3 third_type;
	trio()
		: first(_T1()), second(_T2()), third(_T3()) {}
	trio(const _T1& _V1, const _T2& _V2, const _T3& _V3)
		: first(_V1), second(_V2), third(_V3) {}
	template<class U, class V, class S> trio(const trio<U, V, S> &p)
		: first(p.first), second(p.second), third(p.third) {}
	_T1 first;
	_T2 second;
	_T3 third;
	};
template<class _T1, class _T2, class _T3> inline
	bool __cdecl operator==(const trio<_T1, _T2, _T3>& _X,
		const trio<_T1, _T2, _T3>& _Y)
	{return (_X.first == _Y.first && _X.second == _Y.second && _X.third == _Y.third); }
template<class _T1, class _T2, class _T3> inline
	bool __cdecl operator!=(const trio<_T1, _T2, _T3>& _X,
		const trio<_T1, _T2, _T3>& _Y)
	{return (!(_X == _Y)); }
template<class _T1, class _T2, class _T3> inline
	bool __cdecl operator<(const trio<_T1, _T2, _T3>& _X,
		const trio<_T1, _T2, _T3>& _Y)
	{return (_X.first < _Y.first ||
		 ( !(_Y.first < _X.first) &&   _X.second < _Y.second ) ||
		 ( !(_Y.first < _X.first) && !(_Y.second < _X.second) && _X.third < _Y.third) ); }
template<class _T1, class _T2, class _T3> inline
	bool __cdecl operator>(const trio<_T1, _T2, _T3>& _X,
		const trio<_T1, _T2, _T3>& _Y)
	{return (_Y < _X); }
template<class _T1, class _T2, class _T3> inline
	bool __cdecl operator<=(const trio<_T1, _T2, _T3>& _X,
		const trio<_T1, _T2, _T3>& _Y)
	{return (!(_Y < _X)); }
template<class _T1, class _T2, class _T3> inline
	bool __cdecl operator>=(const trio<_T1, _T2, _T3>& _X,
		const trio<_T1, _T2, _T3>& _Y)
	{return (!(_X < _Y)); }
template<class _T1, class _T2, class _T3> inline
	trio<_T1, _T2, _T3> __cdecl make_trio(const _T1& _X, const _T2& _Y, const _T3& _Z)
	{return (trio<_T1, _T2, _T3>(_X, _Y, _Z)); }

#endif                                                                         
