package ru.gb.antonov.j67.beans.utils;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import ru.gb.antonov.j67.entities.dtos.AopStatisticDto;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
@RequiredArgsConstructor
public class AppLoggingAspect
{
    private Map<Class<?>, Map<String, Method>> markedClasses;
    private Map<Class<?>, Long> classDurations;

    private final String[] classNames =
    {   "ru.gb.antonov.j67.beans.services.OurUserDetailsService",
        "ru.gb.antonov.j67.beans.services.ProductService",
        "ru.gb.antonov.j67.beans.services.RoleService"
    };
/*  Для того, чтобы методы в каком-то классе-бине обрабатывались нашим классом AppLoggingAspect,
    нужно полное имя этого класса добавить (руками) в AppLoggingAspect.classNames, и нужные методы
    добавленного класса пометить аннотацией @AopMark.

    Кажется, работает только с публичными методами.     */
//-----------------------------------------------------------------------------

    @PostConstruct
    public void init() throws ClassNotFoundException
    {
        markedClasses = new HashMap<>(classNames.length);
        classDurations = new HashMap<>(classNames.length);
        for (String cName : classNames)
        {
            Class<?> c = Class.forName (cName);
            //if (c.isAnnotationPresent (AopMark.class))
            {
                markedClasses.put (c, inlineGetAopMarkedMethods (c));
                classDurations.put (c, 0L);
            }
        }
    }

    private Map<String, Method> inlineGetAopMarkedMethods (Class<?> serviceClass)
    {
        Method[] methods = serviceClass.getDeclaredMethods();
        HashMap<String, Method> hmap = new HashMap<>(methods.length);
        for (Method m : methods)
        {
            if (m.isAnnotationPresent (AopMark.class))
                hmap.put (m.getName(), m);
        }
        return hmap;
    }

    public AopStatisticDto getStatisticsAsDto ()
    {
        List<String> list = new ArrayList<>(classNames.length);
        Set<Map.Entry<Class<?>, Long>> eSet = classDurations.entrySet();

        for (Map.Entry<Class<?>, Long> e : eSet)
        {
            list.add (String.format ("%s = %d мс", e.getKey().getName(), e.getValue()));
        }
        return new AopStatisticDto (list.toArray(new String[0]));
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.*.*(..))")
    public Object aroundPublicMethod (ProceedingJoinPoint pjp) throws Throwable
    {
        Signature signature = pjp.getSignature();
        Class<?> c = signature.getDeclaringType();

        Map<String, Method> markedMethods = markedClasses.get(c);
        if (markedMethods != null && markedMethods.containsKey (signature.getName()))
        {
            return inlineCalcMethodRunTime (pjp, c);
        }
        return pjp.proceed();
    }

    private Object inlineCalcMethodRunTime (ProceedingJoinPoint pjp, Class<?> c) throws Throwable
    {
        long   millis = System.currentTimeMillis();
        Object out = pjp.proceed();
        millis = System.currentTimeMillis() - millis;

        classDurations.replace (c, classDurations.get(c) + millis);
        return out;
    }

/*
    @Before ("execution(public void ru.gb.antonov.j67.beans.services.addUser())") // pointcut expression
    public void beforeAddUserInUserDAOClass ()
    {
        System.out.println ("AOP: Поймали добавление пользователя");
    }

    @Before ("execution(public void ru.gb.antonov.j67.beans.services.*User())") // pointcut expression
    public void beforeUserModifyInUserDAOClass ()
    {
        System.out.println ("AOP: работа с пользователем в UserDAO");
    }

    @Before ("execution(public void ru.gb.antonov.j67.beans.services.*())") // pointcut expression
    public void beforeAnyMethodWithoutArgsInUserDAOClass ()
    {
        System.out.println ("AOP: любой void метод без аргументов из UserDAO");
    }

    @Before ("execution(public void ru.gb.antonov.j67.aop.*.*(..))") // pointcut expression
    public void beforeAnyMethodInUserDAOClass ()
    {
        System.out.println ("AOP: любой метод без аргументов из UserDAO");
    }

    @Before ("execution(public void ru.gb.antonov.j67.beans.services.*(..))") // pointcut expression
    public void beforeAnyMethodInUserDAOClassWithDetails (JoinPoint joinPoint)
    {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature ();
        System.out.println ("В UserDAO был вызван метод: " + methodSignature);
        Object[] args = joinPoint.getArgs ();
        if (args.length > 0)
        {
            System.out.println ("Аргументы:");
            for (Object o : args)
            {
                System.out.println (o);
            }
        }
    }

    @AfterReturning (pointcut = "execution(public * ru.gb.antonov.j67.beans.services.getAllUsers(..))", returning = "result")
    public void afterGetBobInfo (JoinPoint joinPoint, List<String> result)
    {
        result.set (0, "Donald Duck");
    }

    @AfterThrowing (pointcut = "execution(public * ru.gb.antonov.j67.beans.services.*(..))", throwing = "exc")
    public void afterThrowing (JoinPoint joinPoint, Throwable exc)
    {
        System.out.println (exc); // logging
    }

    @After ("execution(public * ru.gb.antonov.j67.beans.services.*(..))")
    public void afterMethod ()
    {
        System.out.println ("After");
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.*(..))")
    public Object methodProfiling (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        System.out.println ("start profiling");
        long   begin    = System.currentTimeMillis ();
        Object out      = proceedingJoinPoint.proceed ();
        long   end      = System.currentTimeMillis ();
        long   duration = end - begin;
        System.out.println ((MethodSignature) proceedingJoinPoint.getSignature () + " duration: " + duration);
        System.out.println ("end profiling");
        return out;
    }//*/
}
