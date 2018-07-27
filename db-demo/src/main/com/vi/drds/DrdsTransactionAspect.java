package com.vi.drds;

import com.vi.common.utils.TimeUtil;
import com.vi.dao.UserDao;
import com.vi.mapper.UserMapper;
import com.vi.vo.Person;
import org.aspectj.lang.ProceedingJoinPoint;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.*;

import javax.annotation.Resource;

@Component
public class DrdsTransactionAspect {

    @Autowired
    private UserDao userDao;

    @Resource
    private PlatformTransactionManager transactionManager;

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 开启DRDS柔性事务
     * @param joinPoint
     */
    public void openFlexibleTransaction(JoinPoint joinPoint) {

        Class[] parameterTypes = ((MethodSignature)joinPoint.getSignature()).getMethod().getParameterTypes();
        Method method = null;
        String methodName = joinPoint.getSignature().getName();
        Class targetClass = joinPoint.getTarget().getClass();
        try {
            method = targetClass.getMethod(methodName,parameterTypes);
            boolean hasAnnotation = method.isAnnotationPresent(com.vi.drds.DrdsTransaction.class);
            if(hasAnnotation){
                DrdsTransaction drdsTransaction =  method.getAnnotation(DrdsTransaction.class);
                boolean transaction = drdsTransaction.transactionCheck();
                if(transaction){
                    if(!TransactionSynchronizationManager.isSynchronizationActive()){
                        throw new RuntimeException("没有通过Spring的事务管控，不允许开启DRDS柔性事务!");
                    }
                }
                System.out.println("transaction:"+transaction);
                Person person = new Person();
                person.setName("flexible" + TimeUtil.getNow(TimeUtil.DATE_TIME_FORMAT_14));
                person.setAge("flex");
                userDao.insertPerson(person);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启DRDS柔性事务（不需要增加spring事务，使用事务模板执行方法）
     * @param proceedingJoinPoint
     * @return
     */
    public Object openFlexibleTransactionExt(final ProceedingJoinPoint proceedingJoinPoint){
        Class[] parameterTypes = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod().getParameterTypes();
        Method method = null;
        String methodName = proceedingJoinPoint.getSignature().getName();
        Class targetClass = proceedingJoinPoint.getTarget().getClass();
        Object object = null;
        try {
            method = targetClass.getMethod(methodName,parameterTypes);
            boolean hasAnnotation = method.isAnnotationPresent(com.vi.drds.DrdsTransaction.class);
            if(hasAnnotation){
                DrdsTransaction drdsTransaction =  method.getAnnotation(DrdsTransaction.class);
                boolean transaction = drdsTransaction.transactionCheck();
                if(transaction){
                    if(!TransactionSynchronizationManager.isSynchronizationActive()){
                        throw new RuntimeException("没有通过Spring的事务管控，不允许开启DRDS柔性事务!");
                    }
                    this.flexibleSet();
                    Object[] args = proceedingJoinPoint.getArgs();
                    try {
                        object = proceedingJoinPoint.proceed(args);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }else{
                    DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
                    definition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_NESTED);
                    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, definition);
                    object = transactionTemplate.execute(new TransactionCallback() {
                        @Override
                        public Object doInTransaction(TransactionStatus transactionStatus) {
                            try {
                                flexibleSet();
                                Object[] args = proceedingJoinPoint.getArgs();
                                return proceedingJoinPoint.proceed(args);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                            return null;
                        }
                    });
                }
            }else{
                Object[] args = proceedingJoinPoint.getArgs();
                try {
                    object = proceedingJoinPoint.proceed(args);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return object;
    }

    private void flexibleSet(){
        Person person = new Person();
        person.setName("flexible" + TimeUtil.getNow(TimeUtil.DATE_TIME_FORMAT_14));
        person.setAge("flex");
        sqlSessionTemplate.update(UserMapper.class.getName() + ".insertPerson",person);
    }

}
