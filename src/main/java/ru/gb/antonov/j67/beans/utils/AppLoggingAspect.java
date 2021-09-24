package ru.gb.antonov.j67.beans.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import ru.gb.antonov.j67.entities.dtos.AopStatisticDto;

@Aspect
@Component
public class AppLoggingAspect
{
    private long userServiceDuration = 0L;
    private long productServiceDuration = 0L;
    private long roleServiceDuration = 0L;

    public AopStatisticDto getStatisticsAsDto ()
    {   return new AopStatisticDto (userServiceDuration, productServiceDuration, roleServiceDuration);
    }
//------------------------------------------ OurUserDetailsService AOP

    private Object inlineCalcUserServiceAround (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        long   begin    = System.currentTimeMillis ();
        Object out      = proceedingJoinPoint.proceed ();
        long   end      = System.currentTimeMillis ();
        userServiceDuration += end - begin;
        return out;
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.OurUserDetailsService.loadUserByUsername (..))")
    public Object aroundOurUserService_loadUserByUsername (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcUserServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.OurUserDetailsService.createNewOurUser (..))")
    public Object aroundOurUserService_createNewOurUser (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcUserServiceAround (proceedingJoinPoint);
    }
//------------------------------------------ RoleService AOP

    private Object inlineCalcRoleServiceAround (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        long   begin    = System.currentTimeMillis ();
        Object out      = proceedingJoinPoint.proceed ();
        long   end      = System.currentTimeMillis ();
        roleServiceDuration += end - begin;
        return out;
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.findByName (..))")
    public Object aroundProductService_findByName (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcRoleServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.getRoleUser (..))")
    public Object aroundProductService_getRoleUser (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcRoleServiceAround (proceedingJoinPoint);
    }
//------------------------------------------ ProductService AOP

    private Object inlineCalcProductServiceAround (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        long   begin    = System.currentTimeMillis ();
        Object out      = proceedingJoinPoint.proceed ();
        long   end      = System.currentTimeMillis ();
        productServiceDuration += end - begin;
        return out;
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.findById (..))")
    public Object aroundProductService_findById (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.findAll (..))")
    public Object aroundProductService_findAll (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.getCartPage (..))")
    public Object aroundProductService_getCartPage (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.deleteById (..))")
    public Object aroundProductService_deleteById (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.createProduct (..))")
    public Object aroundProductService_createProduct (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.updateProduct (..))")
    public Object aroundProductService_updateProduct (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.getProductsByPriceRange (..))")
    public Object aroundProductService_getProductsByPriceRange (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.addToCart (..))")
    public Object aroundProductService_addToCart (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.removeFromCart (..))")
    public Object aroundProductService_removeFromCart (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.getCartItemsCount (..))")
    public Object aroundProductService_getCartItemsCount (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
    }

    @Around ("execution(public * ru.gb.antonov.j67.beans.services.ProductService.productListToDtoList (..))")
    public Object aroundProductService_productListToDtoList (ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        return inlineCalcProductServiceAround (proceedingJoinPoint);
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
