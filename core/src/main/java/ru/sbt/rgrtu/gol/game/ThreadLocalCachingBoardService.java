package ru.sbt.rgrtu.gol.game;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ThreadLocalCachingBoardService implements BoardService {

    private final BoardService delegate;

    private final int size;

    private ThreadLocal<Cache> cache = new ThreadLocal<>();

    public ThreadLocalCachingBoardService(BoardService delegate, int size) {
        this.delegate = delegate;
        this.size = size;
    }

    @Override
    public int getSizeX() {
        return delegate.getSizeX();
    }

    @Override
    public int getSizeY() {
        return delegate.getSizeY();
    }

    @Override
    public void setPoint(int x, int y, boolean alive) {
        delegate.setPoint(x, y, alive);
    }

    @Override
    public boolean getPoint(int x, int y) {
        if(cache.get() == null) {
            cache.set(new Cache(size));
        }
        return cache.get().computeIfAbsent(new XY(x, y), p -> delegate.getPoint(p.x, p.y));
    }

    @Override
    public void applyNewValues() {
        delegate.applyNewValues();
        cache = new ThreadLocal<>();
    }

    @Override
    public long getGeneration() {
        return delegate.getGeneration();
    }

    private static class XY {
        int x, y;

        XY(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            XY xy = (XY) o;
            return x == xy.x &&
                    y == xy.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static class Cache extends LinkedHashMap<XY, Boolean> {

        private final int size;

        private Cache(int size) {
            super(size * 2, 1, true);
            this.size = size;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<XY, Boolean> eldest) {
            return size() > size;
        }
    }

}
