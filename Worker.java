import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Worker {

  private List dataset;
  private List<Function> mappers;
  private BiFunction reducer;
  private Object initialValue;
  private boolean duplicated;
  public Worker(List dataset) {
    this.dataset = dataset;
    this.mappers = new ArrayList<>();
    this.duplicated = false;
  }

  public static Worker of(List dataset) {
    return new Worker(dataset);
  }

  public Worker map(Function func) {
    this.mappers.add(func);
    return this;
  }

  public Worker reduce(BiFunction func, Object initialValue) {
    if (this.duplicated)
      throw new RuntimeException("cannot have multiple reducers");
    this.reducer = func;
    this.initialValue = initialValue;
    this.duplicated = true;
    return this;
  }

  public List exec() {
    List from = dataset;
    List result = new ArrayList();
    System.out.println("[Worker] begin " + from);
    for (Function func : this.mappers) {
      List to = new ArrayList();
      for (Object item : from) {
        to.add(func.apply(item));
      }
      System.out.println("[Worker] map " + func);
      System.out.println("[Worker] get " + to);
      from = to;
    }
    if (this.reducer != null) {
      Object value = this.initialValue;
      for (Object item : from) {
        value = this.reducer.apply(value, item);
      }
      result.add(value);
      System.out.println("[Worker] reduce " + this.reducer);
      System.out.println("[Worker] get " + result);
    } else {
      result.addAll(from);
    }
    System.out.println("[Worker] done " + result);
    return result;
  }

  public static void main(String[] args) {
    List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
    List result =
      Worker.of(data)
            .map((x) -> (Integer) x * (Integer) x)
            .map((x) -> (Integer) x - 1)
            .reduce((x, y) -> (Integer) x + (Integer) y, 0)
            .exec();
  }

}