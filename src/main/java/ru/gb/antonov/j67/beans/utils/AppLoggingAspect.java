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
}
