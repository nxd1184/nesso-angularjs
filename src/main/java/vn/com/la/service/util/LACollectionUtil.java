package vn.com.la.service.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author dungnguyen
 */
public final class LACollectionUtil {

    private LACollectionUtil() {
    }

    /**
     * Get the first element of collection if have, otherwise return null.
     *
     * @param collection
     * @return
     */
    public static <E, T extends Collection<E>> E firstOrNull(T collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        return collection.iterator().next();
    }

    public static <E, T extends Collection<E>> E first(T collection) {
        return collection.iterator().next();
    }

    /**
     * result the given list if it's not null, else return empty array list.
     *
     * @param l
     * @return
     */
    public static <E> List<E> emptyIfNull(List<E> l) {
        if (l == null) {
            return new ArrayList<>();
        }
        return l;
    }

    /**
     * Put a single element to a list that can be modified later. Unlike
     * Arrays.asList(...) which returns a readonly list.
     *
     * @param e
     * @return
     */
    public static <E> List<E> toModifiableList(E e) {
        List<E> result = new ArrayList<>();
        if (e != null) {
            result.add(e);
        }
        return result;
    }

    /**
     * Copy a list to an modifiable list.
     *
     * @param l
     * @return
     */
    public static <E> List<E> asModifiableList(List<E> l) {
        return (l == null) ? new ArrayList<E>() : new ArrayList<>(l);
    }

    public static String[] toArray(Collection<String> l) {
        return l.toArray(new String[l.size()]);
    }

    public static List<String> toStringList(Object[] arr) {
        if (ArrayUtils.isEmpty(arr)) {
            return new ArrayList<>();
        }
        return toStringList(Arrays.asList(arr));
    }

    public static List<String> toStringList(Collection<?> arr) {
        if (CollectionUtils.isEmpty(arr)) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>(arr.size());
        for (Object obj : arr) {
            if (obj != null) {
                result.add(obj.toString());
            }
        }
        return result;
    }

    public static <E, T> Map<E, T> initMap(int listSize) {
        return new HashMap<>(Double.valueOf(listSize / 0.75).intValue() + 1);
    }

    public static <T> void addIfNotNull(Collection<T> c, T item) {
        if (item != null) {
            c.add(item);
        }
    }

    public static void addIfNotBlank(Collection<String> c, String item) {
        if (StringUtils.isNotBlank(item)) {
            c.add(item);
        }
    }

    public static <E, T> Map<E, List<T>> group(Collection<T> items, MapBy<E, T> mapBy) {
        if (CollectionUtils.isEmpty(items)) {
            return new HashMap<>();
        }
        Map<E, List<T>> map = new HashMap<>();
        for (T item : items) {
            E key = mapBy.by(item);
            List<T> values = map.get(key);
            if (values == null) {
                values = new LinkedList<>();
                map.put(key, values);
            }
            values.add(item);
        }
        return map;
    }

    public static <E, T> Map<E, T> map(Collection<T> items, MapBy<E, T> mapBy) {
        if (CollectionUtils.isEmpty(items)) {
            return new HashMap<>();
        }
        Map<E, T> result = new HashMap<>(items.size());
        for (T item : items) {
            E by = mapBy.by(item);
            if (by != null) {
                result.put(by, item);
            }
        }
        return result;
    }

    public static <E, T> List<E> list(Collection<T> items, MapBy<E, T> mapBy) {
        if (CollectionUtils.isEmpty(items)) {
            return new ArrayList<>();
        }
        List<E> result = new ArrayList<>(items.size());
        for (T item : items) {
            result.add(mapBy.by(item));
        }
        return result;
    }

    public static <T> void executeBulkAction(List<T> entityList, BulkAction<T> action) {
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        int fromIdx = 0;
        int toIdx = Math.min(entityList.size(), action.bulkSize());
        while (fromIdx < toIdx) {
            List<T> segment = entityList.subList(fromIdx, toIdx);
            action.execute(segment);
            fromIdx = toIdx;
            toIdx = Math.min(entityList.size(), toIdx + action.bulkSize());
        }
    }

    public static <T, E> List<E> extract(Collection<T> items, Extractor<T, E> extractor) {
        if (CollectionUtils.isEmpty(items)) {
            return new ArrayList<>();
        }
        List<E> result = new LinkedList<>();
        for (T item : items) {
            result.add(extractor.extract(item));
        }
        return result;
    }

    public static <T> List<T> filter(Collection<T> items, Filter<T> filter) {
        if (CollectionUtils.isEmpty(items)) {
            return new ArrayList<>();
        }
        List<T> result = new LinkedList<>();
        for (T item : items) {
            if (filter.accept(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     *
     * @param originalSet: original collection want to split
     * @param size: the size of the new collection
     * @param <T>: any object type
     * @return the list of split data
     */
    public static <T> List<Set<T>> split(Collection<T> originalSet, int size) {
        if (CollectionUtils.isEmpty(originalSet)) {
            return new ArrayList<>();
        }
        int PARTITIONS_COUNT = (originalSet.size() / size) + 1;

        List<Set<T>> result = new ArrayList<Set<T>>(PARTITIONS_COUNT);
        for (int i = 0; i < PARTITIONS_COUNT; i++) {
            result.add(new HashSet<T>(size));
        }

        int index = 0;
        for (T object : originalSet) {
            result.get(index++ % PARTITIONS_COUNT).add(object);
        }
        return result;
    }

    public static <T> Set<T> asSet(T... a) {
        return new HashSet<>(Arrays.asList(a));
    }



    public static Object readMap(Map<?, ?> map, String key) {
        return getRecursiceInternal(map, key);
    }

    public static String readMapString(Map<?, ?> map, String key) {
        String result = null;
        Object value = getRecursiceInternal(map, key);
        if (value != null) {
            if (value instanceof String) {
                result = (String) value;
            } else {
                result = value.toString();
            }
        }
        return result;

    }



    public static Double readMapDouble(Map<?, ?> map, String key) {
        Double result = null;
        Object value = getRecursiceInternal(map, key);
        if (value != null) {
            if (value instanceof Double) {
                result = (Double) value;
            } else {
                result = Double.valueOf(value.toString());
            }
        }
        return result;
    }



    private static Object getRecursiceInternal(Object theMap, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        String[] parts = StringUtils.split(key, ".");
        if (parts.length == 1) {
            String currentKey = parts[0];
            // getting current value
            if (theMap instanceof Map) {
                Map<?,?> map = (Map<?, ?>) theMap;
                return map.get(currentKey);
            } else {
                // expected a map but it's not
                return null;
            }
        } else {
            String currentKey = parts[0];
            String nextKey = StringUtils.join(ArrayUtils.subarray(parts, 1, parts.length), ".");
            if (theMap instanceof Map) {
                Map<?,?> map = (Map<?, ?>) theMap;
                return getRecursiceInternal(map.get(currentKey), nextKey);
            } else {
                // expected a map but it's not
                return null;
            }
        }
    }

    public interface BulkAction<T> {
        void execute(List<T> entityList);

        int bulkSize();
    }

    public interface MapBy<E, T> {
        E by(T item);
    }

    public interface Filter<T> {
        boolean accept(T item);
    }

    public interface Extractor<T, E> {
        E extract(T item);
    }

}
