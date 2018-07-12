package com.xiaowei.accountweb.ini;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import org.hibernate.engine.query.spi.EntityGraphQueryHint;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class InitCommandLineRunner {
    static {
        try {
            ClassPool pool = ClassPool.getDefault();
            //获取一个Student类的CtClass对象
            CtClass ctClass = pool.get("org.hibernate.engine.query.spi.HQLQueryPlan");
            CtClass[] paramTypes1 = {pool.get(String.class.getName()),
                    pool.get(boolean.class.getName()),
                    pool.get(Map.class.getName()),
                    pool.get(SessionFactoryImplementor.class.getName())};
            CtClass[] paramTypes2 = {pool.get(String.class.getName()),
                    pool.get(boolean.class.getName()),
                    pool.get(Map.class.getName()),
                    pool.get(SessionFactoryImplementor.class.getName()),
                    pool.get(EntityGraphQueryHint.class.getName())};
            CtConstructor constructor1 = ctClass.getDeclaredConstructor(paramTypes1);
            CtConstructor constructor2 = ctClass.getDeclaredConstructor(paramTypes2);

            constructor1.insertBefore("System.out.println(\"执行hql替换\");\n" +
                    "        if(hql.contains(\"count\")){\n" +
                    "            System.out.println(\"替换成功\");\n" +
                    "            hql = hql.replace(\"fetch\",\"\");\n" +
                    "        }");
            constructor2.insertBefore("System.out.println(\"执行hql替换\");\n" +
                    "        if(hql.contains(\"count\")){\n" +
                    "            System.out.println(\"替换成功\");\n" +
                    "            hql = hql.replace(\"fetch\",\"\");\n" +
                    "        }");
            pool.toClass(ctClass);



//        constructor1.setBody("{if(hql.contains(\"count\")){\n" +
//                "                hql = hql.replace(\"fetch\",\"\");\n" +
//                "            }\n" +
//                "            this( hql, null, shallow, enabledFilters, factory, null );}");
//        constructor2.setBody("{if(hql.contains(\"count\")){\n" +
//                "                hql = hql.replace(\"fetch\",\"\");\n" +
//                "            }\n" +
//                "            this( hql, null, shallow, enabledFilters, factory, entityGraphQueryHint );}");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
