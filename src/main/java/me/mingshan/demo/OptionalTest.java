package me.mingshan.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Optional
 * 
  *  不应该用作类的字段。主要用作返回类型， 进行链式调用。
 *  
 * @author mingshan
 *
 */
public class OptionalTest {

    /**
     * Create Optional
     */
    @Test(expected = NoSuchElementException.class)
    public void test1() {
        Optional<User> emptyOpt = Optional.empty();
        emptyOpt.get();
    }

    /**
     * If the object is null or not null, you need to use {@code Optional.ofNullable},
     * {@code orElse} If the object is not null, return the origin object, otherwise 
     * return the given object.
     */
    @Test
    public void test2() {
        User user = null;
        User u = createNewUser();
        User result = Optional.ofNullable(user).orElse(u);
        assertEquals(u, result);
    }

    /**
     * orElse
     * orElseGet
     * 
     * {@code other}.
     */
    @Test
    public void test3() {
        User user = new User(22, "walker");
        User result = Optional.ofNullable(user).orElse(createNewUser());
        assertEquals(user, result);
        User result2 = Optional.ofNullable(user).orElseGet(() -> createNewUser());
        assertEquals(user, result2);
    }

    /**
     * map 转换值
     */
    @Test
    public void test4() {
        User user = new User(22, "walker");
        String name = Optional.ofNullable(user)
                        .map(u -> u.getName()).orElse("zz");
        assertEquals(user.getName(), name);
    }

    /**
     *  filter 过滤值
     */
    @Test
    public void test5() {
        User user = new User(22, "walker");
        Optional<User> result = Optional.ofNullable(user)
                .filter(u -> u.getName().contains("w") && u.getAge() > 20);

        assertTrue(result.isPresent());
    }

    /**
     *  or 
     *  or() 方法与 orElse() 和 orElseGet() 类似，它们都在对象为空的时候提供了替代情况。
     *  or() 的返回值是由 Supplier 参数产生的另一个 Optional 对象。
     */
    @Test
    public void test6() {
        User user = null;
        User result = Optional.ofNullable(user)
            .or(() -> Optional.of(new User(23, "xcc"))).get();
        assertEquals(23, result.getAge());
    }

    /**
     *  ifPresentOrElse
     *  ifPresentOrElse() 方法需要两个参数：一个 Consumer 和一个 Runnable。
     *    如果对象包含值，会执行 Consumer 的动作，否则运行 Runnable。
     */
    @Test
    public void test7() {
        User user = null;

        Optional.ofNullable(user)
        .ifPresentOrElse(u -> System.out.print(u), () -> System.out.println("User not found"));
    }

    /**
     * stream 把Optional 转为stream，然后即可以用其api了。
     *   
     */
    @Test
    public void test8() {
        User user = new User(22, "walker");
        List<String> names = Optional.ofNullable(user)
            .stream()
            .filter(u -> u.getName().contains("w") && u.getAge() > 20)
            .map(u -> u.getName())
            .collect(Collectors.toList());

        assertTrue(names.size() == 1);
        assertEquals(user.getName(), names.get(0));
    }

    private User createNewUser() {
        System.out.println("Creating New User");
        return new User(23, "zccc");
    }
}

class User {
    private int age;
    private String name;

    public User() {}

    public User(int age, String name) {
        super();
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User [age=" + age + ", name=" + name + "]";
    }

}
