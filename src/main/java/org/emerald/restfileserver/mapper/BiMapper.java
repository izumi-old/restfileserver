package org.emerald.restfileserver.mapper;

public interface BiMapper<First, Second> {
    First mapToFirst(Second s);
    Second mapToSecond(First f);
}
