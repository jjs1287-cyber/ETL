from pathlib import Path
path=Path('C:/ETL/Dependencies/dtl-3.7/work/dtl/nullable.h')
text=path.read_bytes().decode('latin-1')
old='''      BYTE *offset = val->src_class;
      offset += reinterpret_cast<size_t>(bios.GetWorkingAddr());
      nullable<T> *px = reinterpret_cast<nullable<T> *>(offset);
'''
assert text.count(old)==2
text=text.replace(old,'')
anchor='\t\t   BoundIO &b = it->second;'
assert text.count(anchor)==1
text=text.replace(anchor,anchor+'''
           // SQL iterators share one collection for parameters and result rows.
           // Resolve this nullable owner from its own binding, not the row base.
           nullable<T> *px = reinterpret_cast<nullable<T> *>(
               b.GetAddr() + reinterpret_cast<ptrdiff_t>(val->src_class));''')
anchor='\t   BoundIO &b = bios[val->bios[0]];'
assert text.count(anchor)==1
text=text.replace(anchor,anchor+'''
       nullable<T> *px = reinterpret_cast<nullable<T> *>(
           b.GetAddr() + reinterpret_cast<ptrdiff_t>(val->src_class));''')
old='ptrdiff_t offset = reinterpret_cast<BYTE*>(&x) - b.GetBoundIOsPtr()->GetWorkingAddr();'
assert text.count(old)==1
text=text.replace(old,'ptrdiff_t offset = reinterpret_cast<BYTE*>(&x) - reinterpret_cast<BYTE*>(x.get_ptr());')
path.write_bytes(text.encode('latin-1'))
print('Nullable validation now resolves its owner from the bound member address.')
