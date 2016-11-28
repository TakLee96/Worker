Worker
======

A very simple MapReduce interface written in Java 8.

Example Usage:

```java
List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
List result =
  Worker.of(data)
        .map((x) -> (Integer) x * (Integer) x)
        .map((x) -> (Integer) x - 1)
        .reduce((x, y) -> (Integer) x + (Integer) y, 0)
        .exec();
```

Quick View on API:

```java
/* Construct a Worker with the given dataset */
public static Worker of(List dataset);
/* Map a Function to each item in dataset */
public Worker map(Function func);
/* Left-to-Right Reduce */
public Worker reduce(BiFunction func, Object initialValue);
/* Execute the MapReduce functions and return resulting List */
public List exec();
```

Future:

- How could Java generics work?
- How to achieve parallel processing like actual MapReduce framework?