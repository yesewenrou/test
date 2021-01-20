package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.constant.CommonConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.BusinessExceptionEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception.BusinessException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.MenuService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author LHY
 */
@Aspect
@Component
@Slf4j
public class AuthAspect {

    @Autowired
    private MenuService menuService;

    /**
     * 切入点（1. 只会对加了Auth注解的方法，进行权限校验；2. 对starter中日志管理单独校验）
     */
    @Pointcut("@annotation(net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth)")
    public void entryPoint() {
    }

    @Before("entryPoint()")
    public void before(JoinPoint joinPoint) {
        log.info("=====================开始执行前置通知==================");
    }

    /**
     * 环绕通知处理
     *
     * @throws Throwable
     */
    @Around("entryPoint()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        handleAround(point);
        return point.proceed();
    }

    /**
     * Around权限校验
     */
    private void handleAround(ProceedingJoinPoint point) {
        String permission = null;
        try {
            Signature signature = point.getSignature();
            MethodSignature methodSignature = null;
            methodSignature = (MethodSignature) signature;
            Object target = point.getTarget();
            Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
            // 获取注解对象
            Auth auth = currentMethod.getAnnotation(Auth.class);
            permission = auth.value();
            // 校验权限
            log.info("===正在校验的权限是===：" + permission);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = menuService.checkAuth(permission);
        if (CommonConst.FAIL.equals(result)) {
            throw new BusinessException(BusinessExceptionEnum.NO_PERMISSION);
        }
    }

    @After("entryPoint()")
    public void after(JoinPoint joinPoint) {
        log.info("=====================开始执行后置通知==================");
    }

    @AfterThrowing(pointcut = "entryPoint()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class<?> targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String value = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class<?>[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        value = method.getAnnotation(Auth.class).value();
                        break;
                    }
                }
            }
            StringBuilder paramsBuf = new StringBuilder();
            for (Object arg : arguments) {
                paramsBuf.append(arg);
                paramsBuf.append("&");
            }
            log.info("异常方法:" + className + "." + methodName + "();参数:" + paramsBuf.toString() + ",处理了:" + value);
            log.info("异常信息:" + e.getMessage());
        } catch (Exception ex) {
            log.error("异常信息:{}", ex.getMessage());
        }
    }
}
