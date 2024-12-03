package com.example.study_org_server.controller.advice;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import org.openapitools.example.model.BadRequestError;
import org.openapitools.example.model.InvalidParam;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.StreamSupport;

public class BadRequestErrorCreater {
    //@Valid アノテーションが付けられた引数の検証が失敗した場合にスローされる例外
    //引数Error -> BadRequestError
    public static BadRequestError from(MethodArgumentNotValidException ex) {
        var invalidParamList= ex.getFieldErrors()
                .stream()
                .map(BadRequestErrorCreater::createInvalidParam).toList();
        var error= new BadRequestError();
        error.setInvalidParams(invalidParamList);
        return error;
    }

    private static InvalidParam createInvalidParam(FieldError fieldError) {
        var invalidParam= new InvalidParam();
        invalidParam.setName(fieldError.getField());
        invalidParam.setReason(fieldError.getDefaultMessage());
        return invalidParam;
    }

    //制約違反の結果 -> BadRequestError
    public static BadRequestError from(ConstraintViolationException ex){
        var invalidParamList= ex.getConstraintViolations()
                .stream()
                //以下むずい
                .map(violation -> {

                    var parameterOpt= StreamSupport.stream(violation.getPropertyPath().spliterator(),false)
                            .filter(node -> node.getKind().equals(ElementKind.PARAMETER))
                            .findFirst();
                    var invalidParam= new InvalidParam();
                    parameterOpt.ifPresent(p -> invalidParam.setName(p.getName()));
                    invalidParam.setReason(violation.getMessage());

                    return invalidParam;
                })
                .toList();
        var error=new BadRequestError();
        error.setInvalidParams(invalidParamList);
        return error;
    }
}
