package com.choonchernlim.testHibernate.util;

import com.choonchernlim.testHibernate.example.Example;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Utils {

    public static void runExample(Class exampleClass) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Example example = (Example) context.getBean(exampleClass);

        example.run();
    }
}
