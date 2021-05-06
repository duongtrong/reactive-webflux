package com.developer.webflux.util;

import com.developer.webflux.constant.EmployeeConstant;
import com.developer.webflux.dto.EmployeeAPIResponseEntity;
import com.developer.webflux.dto.ErrorDetails;
import com.developer.webflux.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractEndpoint {

    @Autowired
    private MessageSource messageSource;

    private String getMessageLocalization(String message, Locale locale) {
        try {
            return messageSource.getMessage(message.replaceAll("[\\{|}|']", ""), null, locale);
        } catch (NoSuchMessageException noSuchMessageException) {
            noSuchMessageException.printStackTrace();
            return message;
        }
    }

    private String resolveBindingResultErrors(BindingResult bindingResult, Locale locale) {
        return bindingResult.getFieldErrors().stream()
                .map(fr -> {
                    String validationMessage = StringUtils.isBlank(fr.getDefaultMessage()) ? "" : fr.getDefaultMessage();
                    return getMessageLocalization(validationMessage, locale);
                }).findFirst().orElse(bindingResult.getAllErrors().stream().map(x -> {
                    String validationMessage = StringUtils.isBlank(x.getDefaultMessage()) ? "" : x.getDefaultMessage();
                    return getMessageLocalization(validationMessage, locale);
                }).collect(Collectors.joining(", ")));
    }

    @SuppressWarnings("unchecked")
    protected <P, T, B extends BindingResult, L extends Locale> DeferredResult<P> toDeferredResult(DeferredResult<P> deferredResult,
                                                                                                   Flux<T> details,
                                                                                                   B bindingResult,
                                                                                                   L locale) {
        details.subscribe(new BaseSubscriber<T>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(1);
            }

            @Override
            protected void hookOnNext(T t) {
                ResponseEntity<EmployeeAPIResponseEntity<T>> response = new ResponseEntity<>(
                        new EmployeeAPIResponseEntity<>(EmployeeConstant.SUCCESS.getCode(), EmployeeConstant.SUCCESS.getMessage(), t), HttpStatus.OK);
                deferredResult.setResult((P) response);
            }

            @Override
            protected void hookOnComplete() {
                deferredResult.onCompletion(() -> {

                });
            }

            @Override
            protected void hookOnError(Throwable error) {
                handlingExceptions(deferredResult, error, bindingResult, locale);
            }
        });
        return deferredResult;
    }

    protected <P, T, B extends BindingResult, L extends Locale> DeferredResult<P> toDeferredResult(DeferredResult<P> deferredResult,
                                                                                                   Mono<T> details,
                                                                                                   B bindingResult,
                                                                                                   L locale) {
        details.subscribe(new BaseSubscriber<T>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(1);
            }

            @Override
            protected void hookOnNext(T t) {
                ResponseEntity<EmployeeAPIResponseEntity<T>> response = new ResponseEntity<>(
                        new EmployeeAPIResponseEntity<>(EmployeeConstant.SUCCESS.getCode(), EmployeeConstant.SUCCESS.getMessage(), t), HttpStatus.OK);
                deferredResult.setResult((P) response);
            }

            @Override
            protected void hookOnComplete() {
                deferredResult.onCompletion(() -> {

                });
            }

            @Override
            protected void hookOnError(Throwable error) {
                handlingExceptions(deferredResult, error, bindingResult, locale);
            }
        });
        return deferredResult;
    }

    private <T> void handlingExceptions(DeferredResult<T> deferredResult, Throwable error, BindingResult bindingResult, Locale locale) {
        String validationMessage = StringUtils.isBlank(error.getMessage()) ? "" : error.getMessage();
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(),
                bindingResult.hasErrors() ? resolveBindingResultErrors(bindingResult, locale) :
                        getMessageLocalization(validationMessage, locale));
        ResponseEntity<ErrorDetails> response = new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        deferredResult.setErrorResult(response);
    }

    @SuppressWarnings("unchecked")
    protected <P, T, L extends Locale> DeferredResult<P> toDeferredResult(DeferredResult<P> deferredResult,
                                                                          Flux<T> details,
                                                                          L locale) {
        details.subscribe(new BaseSubscriber<T>() {

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(1);
            }

            @Override
            protected void hookOnNext(T t) {
                ResponseEntity<EmployeeAPIResponseEntity<T>> response = new ResponseEntity<>(
                        new EmployeeAPIResponseEntity<>(EmployeeConstant.SUCCESS.getCode(), EmployeeConstant.SUCCESS.getMessage(), t), HttpStatus.OK);
                deferredResult.setResult((P) response);
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                throwable.printStackTrace();
                String validationMessage = StringUtils.isBlank(throwable.getMessage()) ? "" : throwable.getMessage();
                ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(),
                        getMessageLocalization(validationMessage, locale));
                ResponseEntity<ErrorDetails> response = new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
                deferredResult.setErrorResult(response);
            }

            @Override
            protected void hookOnComplete() {
                deferredResult.onCompletion(() -> {

                });
            }

        });
        return deferredResult;
    }

    protected <B extends BindingResult> Mono<B> toObservable(B bindingResult) {
        return Mono.fromCallable(() -> bindingResult)
                .flatMap(
                        v -> {
                            if (v.hasErrors()) {
                                throw new CustomException(resolveBindingResultErrors(v, Locale.getDefault()));
                            } else {
                                return Mono.just(v);
                            }
                        }
                ).subscribeOn(Schedulers.boundedElastic());
    }
}
