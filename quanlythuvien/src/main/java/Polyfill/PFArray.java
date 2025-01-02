package Polyfill;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PFArray<T> implements Iterable<T> {
    public PFArray() {
        this(1);
    }

    public PFArray(int initialLength) {
        if (initialLength < 1) {
            throw new IllegalArgumentException("This implementation requires initialLength of at least 1");
        }
        size = 0;
        reserve(initialLength);
    }

    public PFArray(T[] primitiveArray) {
        elements = primitiveArray.clone();
        size = elements.length;
    }

    public T at(int index) {
        indexCheck(index);
        return elements[index];
    }

    public void set(int index, T element) {
        indexCheck(index);
        elements[index] = element;
    }

    public void insert(int index, T element) {
        indexCheck(index);
        updateSize(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index - 1);
        set(index, element);
    }

    public T erase(int index) {
        indexCheck(index);
        T toRet = at(index);
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        updateSize(size - 1);
        return toRet;
    }

    public void push_front(T element) {
        insert(0, element);
    }

    public T pop_front() {
        return erase(0);
    }

    public void push_back(T element) {
        updateSize(size + 1);
        set(size - 1, element);
    }

    public T pop_back() {
        return erase(size - 1);
    }

    public T front() {
        return at(0);
    }

    public T back() {
        return at(size - 1);
    }

    public boolean empty() {
        return size == 0;
    }

    public void resize(int newSize) {
        updateSize(newSize);
    }

    public void clear() {
        updateSize(0);
    }

    public int capacity() {
        return elements.length;
    }

    public int size() {
        return size;
    }

    public T[] toArray() {
        T[] array = initOne4Me(size());
        System.arraycopy(elements, 0, array, 0, size());
        return array;
    }

    private void reserve(int reservedCapacity) {
        if (reservedCapacity > capacity()) {
            if (capacity() >= maxElements) {
                throw new UnsupportedOperationException("Max elements limit reached");
            }
            long newCapacity = Math.round(Math.ceil(capacity() * growthFactor));
            // T[] newArray = (T[]) new Object[newCapacity > maxElements ? maxElements :
            // (int) newCapacity];
            T[] newArray = initOne4Me(newCapacity > maxElements ? maxElements : (int) newCapacity);
            System.arraycopy(elements, 0, newArray, 0, size);
            elements = newArray;
        }
    }

    private void updateSize(int newSize) {
        reserve(newSize);
        size = newSize;
    }

    private void indexCheck(int index) {
        if (index < 0 || index > size - 1) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    public void fillCapacity(T element) {
        for (int i = size(); i < capacity(); i++) {
            set(i, element);
        }
    }

    @SuppressWarnings("unchecked")
    private T[] initOne4Me(int n) {
        return (T[]) new Object[n];
    }

    // Iterable implementation
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return index < size && elements[index] != null;
            }

            @Override
            public T next() {
                return at(index++);
            }

            private int index = 0;
        };
    }

    // Spliterator implementation
    public Spliterator<T> spliterator() {
        return new TaggedSpliterator<T>(elements, 0, size);
    }

    private static class TaggedSpliterator<T> implements Spliterator<T> {
        public TaggedSpliterator(T[] elements, int start, int fence) {
            this.elements = elements;
            this.start = start;
            this.fence = fence;
        }

        public boolean tryAdvance(Consumer<? super T> action) {
            if (start < fence) {
                action.accept(elements[start++]);
                return true;
            }
            return false;
        }

        public Spliterator<T> trySplit() {
            int mid = (start + fence) / 2;
            if (mid - start < splitLimit) { // not worth splitting
                return null;
            }
            int childStart = start; // new child starts from where I am
            start = mid; // I jumps to second half
            return new TaggedSpliterator<>(elements, childStart, mid);
        }

        public long estimateSize() {
            return fence - start;
        }

        public int characteristics() {
            return SIZED | SUBSIZED | NONNULL | IMMUTABLE;
        }

        private final T[] elements;
        private int start;
        private final int fence;
        private static final int splitLimit = 16;
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    private int size = 0;
    private static final float growthFactor = 2;
    private static final int maxElements = 2_000_000_000;
    private T elements[] = initOne4Me(1);
}
