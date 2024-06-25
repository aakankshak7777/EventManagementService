package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtils {

    public static <T> List<T> nullList(int size) {
        var list = new ArrayList<T>(size);
        for (int i = 0; i < size; ++i) {
            list.add((T) null);
        }
        return list;
    }

    public static <T> void forEach(Iterable<T> it, Consumer<? super T> action) {
        if (it != null) {
            it.forEach(action);
        }
    }

    public static <T, U> Optional<U> mapNullable(T value, Function<? super T, ? extends U> mapper) {
        return Optional.ofNullable(value).map(mapper);
    }

    /**
     * Split input map in smaller maps, size of small maps is based on
     * provided batch size.
     *
     * @param map input map to split
     * @param batchsize number of max entries in result map
     * @return list of maps
     */
    public static <K, V> List<Map<K, V>> splitMapDataInBatch(Map<K, V> map, int batchsize) {
        var entriesAdded = 0L;
        var smallerMapsList = new ArrayList<Map<K, V>>();

        while (entriesAdded < map.size()) {
            var batch = map.entrySet().stream()
                    .skip(entriesAdded)
                    .limit(batchsize)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            smallerMapsList.add(batch);
            entriesAdded = entriesAdded + batchsize;
        }
        return smallerMapsList;
    }
}
