package com.developer.webflux.util;

import com.developer.webflux.constant.ResponseEntityStatusConstant;
import com.developer.webflux.dto.EmployeeAPIResponseEntity;
import com.developer.webflux.dto.ErrorDetails;
import com.developer.webflux.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Subscription;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractEndpoint {

    private String resolveBindingResultErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(fr -> StringUtils.isBlank(fr.getDefaultMessage()) ? "" : fr.getDefaultMessage())
                .findFirst().orElse(bindingResult.getAllErrors().stream()
                        .map(x -> StringUtils.isBlank(x.getDefaultMessage()) ? "" :
                                x.getDefaultMessage()).collect(Collectors.joining(", ")));
    }

    @SuppressWarnings("unchecked")
    protected <P, T, B extends BindingResult> DeferredResult<P> toDeferredResult(DeferredResult<P> deferredResult,
                                                                                 Flux<T> details,
                                                                                 B bindingResult) {
        details.subscribe(new CoreSubscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(T t) {
                ResponseEntity<EmployeeAPIResponseEntity<T>> response = new ResponseEntity<>(
                        new EmployeeAPIResponseEntity<>(ResponseEntityStatusConstant.SUCCESS.getCode(),
                                ResponseEntityStatusConstant.SUCCESS.getMessage(), t), HttpStatus.OK);
                deferredResult.setResult((P) response);
            }

            @Override
            public void onError(Throwable t) {
                handlingExceptions(deferredResult, t, bindingResult);
            }

            @Override
            public void onComplete() {
                deferredResult.onCompletion(() -> {

                });
            }

        });
        return deferredResult;
    }

    @SuppressWarnings("unchecked")
    protected <P, T, B extends BindingResult> DeferredResult<P> toDeferredResult(DeferredResult<P> deferredResult,
                                                                                 Mono<T> details,
                                                                                 B bindingResult) {
        details.subscribe(new BaseSubscriber<T>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            protected void hookOnNext(T t) {
                ResponseEntity<EmployeeAPIResponseEntity<T>> response = new ResponseEntity<>(
                        new EmployeeAPIResponseEntity<>(ResponseEntityStatusConstant.SUCCESS.getCode(),
                                ResponseEntityStatusConstant.SUCCESS.getMessage(), t), HttpStatus.OK);
                deferredResult.setResult((P) response);
            }

            @Override
            protected void hookOnComplete() {
                deferredResult.onCompletion(() -> {

                });
            }

            @Override
            protected void hookOnError(Throwable error) {
                handlingExceptions(deferredResult, error, bindingResult);
            }
        });
        return deferredResult;
    }

    private <T> void handlingExceptions(DeferredResult<T> deferredResult, Throwable error, BindingResult bindingResult) {
        String validationMessage = StringUtils.isBlank(error.getMessage()) ? "" : error.getMessage();
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(),
                bindingResult.hasErrors() ? resolveBindingResultErrors(bindingResult) : validationMessage);
        ResponseEntity<ErrorDetails> response = new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        deferredResult.setErrorResult(response);
    }

    @SuppressWarnings("unchecked")
    protected <P, T> DeferredResult<P> toDeferredResult(DeferredResult<P> deferredResult,
                                                        Flux<T> details) {
        details.subscribe(new BaseSubscriber<T>() {

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            protected void hookOnNext(T t) {
                ResponseEntity<EmployeeAPIResponseEntity<T>> response = new ResponseEntity<>(
                        new EmployeeAPIResponseEntity<>(ResponseEntityStatusConstant.SUCCESS.getCode(),
                                ResponseEntityStatusConstant.SUCCESS.getMessage(), t), HttpStatus.OK);
                deferredResult.setResult((P) response);
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                handleExceptionFluxAndMono(throwable, deferredResult);
            }

            @Override
            protected void hookOnComplete() {
                deferredResult.onCompletion(() -> {

                });
            }

        });
        return deferredResult;
    }

    private <P> void handleExceptionFluxAndMono(Throwable throwable, DeferredResult<P> deferredResult) {
        String validationMessage = StringUtils.isBlank(throwable.getMessage()) ? "" : throwable.getMessage();
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), validationMessage);
        ResponseEntity<ErrorDetails> response = new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        deferredResult.setErrorResult(response);
    }

    @SuppressWarnings("unchecked")
    protected <P, T> DeferredResult<P> toDeferredResult(DeferredResult<P> deferredResult,
                                                        Mono<T> details) {
        details.subscribe(new CoreSubscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(T t) {
                ResponseEntity<EmployeeAPIResponseEntity<T>> response = new ResponseEntity<>(
                        new EmployeeAPIResponseEntity<>(ResponseEntityStatusConstant.SUCCESS.getCode(),
                                ResponseEntityStatusConstant.SUCCESS.getMessage(), t), HttpStatus.OK);
                deferredResult.setResult((P) response);
            }

            @Override
            public void onError(Throwable t) {
                handleExceptionFluxAndMono(t, deferredResult);
            }

            @Override
            public void onComplete() {
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
                                throw new CustomException(resolveBindingResultErrors(v));
                            } else {
                                return Mono.just(v);
                            }
                        }
                ).subscribeOn(Schedulers.single());
    }
}
