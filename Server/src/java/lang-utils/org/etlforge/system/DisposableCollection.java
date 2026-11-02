  
                             
   
package org.etlforge.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.system.Disposable;
import org.etlforge.system.Disposables;

public final class DisposableCollection
implements Disposable {
    private final List<Disposable> elements = new ArrayList<Disposable>();

    public DisposableCollection() {
    }

    public DisposableCollection(Disposable ... disposableArray) {
        this();
        this.add(disposableArray);
    }

    public DisposableCollection(Iterable<? extends Disposable> iterable) {
        this();
        this.add(iterable);
    }

    public void add(Disposable ... disposableArray) {
        Collections.addAll(this.elements, disposableArray);
    }

    public void add(Iterable<? extends Disposable> iterable) {
        for (Disposable disposable : iterable) {
            this.elements.add(disposable);
        }
    }

    @Override
    public void dispose() {
        Disposables.disposeAll(this.elements);
    }
}

