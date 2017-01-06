package com.jelohazi.ihasthetoken.jdbc;

import java.util.List;

public interface TypedRepository<T, I> {

    public void createOrUpdate(T object);
    public T get(I id);
    public List<T> list();
}
