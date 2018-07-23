package com.vi.drds;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DrdsTransaction {

    boolean transactionCheck() default true;//spring 事务检查
}
