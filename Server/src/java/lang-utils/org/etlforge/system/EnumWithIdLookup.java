  
                             
   
                                        
                                   
                                        
   
package org.etlforge.system;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.etlforge.system.EnumWithId;

public final class EnumWithIdLookup {
    public static <T, E extends Enum<E>> E lookup(Class<E> clazz, T t) {
        Optional<E> optional = EnumWithIdLookup.tryLookup(clazz, t);
        if (optional.isPresent()) {
            return (E)((Enum)optional.get());
        }
        throw new IllegalArgumentException("Unknown ID: " + t);
    }

    public static <T, E extends Enum<E>> Optional<E> tryLookup(Class<E> clazz, T t) {
        Preconditions.checkNotNull(t);
        for (E enum_ : clazz.getEnumConstants()) {
            if (!((EnumWithId)((Object)enum_)).getId().equals(t)) continue;
            return Optional.of(enum_);
        }
        return Optional.absent();
    }

    public static <T, E extends Enum<E>> E lookupOr(T t, E e) {
        return (E)((Enum)EnumWithIdLookup.tryLookup(e.getClass(), t).or(e));
    }

    private EnumWithIdLookup() {
    }
}



