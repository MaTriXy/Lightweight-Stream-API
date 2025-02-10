package com.annimon.stream.operator;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.Function;
import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

public class ObjFlatMapToInt<T> extends PrimitiveExtIterator.OfInt {

    private final Iterator<? extends T> iterator;
    private final Function<? super T, ? extends IntStream> mapper;
    private PrimitiveIterator.OfInt inner;

    public ObjFlatMapToInt(
            @NotNull Iterator<? extends T> iterator,
            @NotNull Function<? super T, ? extends IntStream> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    protected void nextIteration() {
        if ((inner != null) && inner.hasNext()) {
            next = inner.next();
            hasNext = true;
            return;
        }
        while (iterator.hasNext()) {
            if (inner == null || !inner.hasNext()) {
                final T arg = iterator.next();
                final IntStream result = mapper.apply(arg);
                if (result != null) {
                    inner = result.iterator();
                }
            }
            if ((inner != null) && inner.hasNext()) {
                next = inner.next();
                hasNext = true;
                return;
            }
        }
        hasNext = false;
    }
}
