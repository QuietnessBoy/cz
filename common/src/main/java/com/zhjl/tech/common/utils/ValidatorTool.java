package com.zhjl.tech.common.utils;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Author: wildfist
 * Email: windsp@foxmail.com
 * Date: 2017/2/22
 * Time: 11:29
 * FileStreamDesc:
 */
public class ValidatorTool {

    public static Validator validator;

    public static Validator getValidator(){
        if( validator != null ){
            return validator;
        }else{
            synchronized (ValidatorTool.class){
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                validator = factory.getValidator();
            }
            return validator;
        }
    }
}
