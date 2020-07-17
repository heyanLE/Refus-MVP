package cn.heyanle.viewbinder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by HeYanLe on 2020/5/4 0004.
 * https://github.com/heyanLE
 */
@Retention(RetentionPolicy.RUNTIME) //保存在内存中的字节码，JVM将在运行时也保留注解，因此可以通过反射机制读取注解的信息
@Target(ElementType.FIELD)
public @interface BindView {
    public int value();
}
