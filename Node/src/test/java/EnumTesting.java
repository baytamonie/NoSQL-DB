import org.junit.Test;

public class EnumTesting {

  private enum Types {
    create("create"),
    write("write");
    String name;

    Types(String name) {
      this.name = name;
    }
  }

  @Test
  public void test1() {

    for(Types type: Types.values() ){
      System.out.println(type.name);
    }
  }
}
