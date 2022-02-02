package com.rcore.domain.commons.usecase;

import java.util.function.Function;

/**
 * Выполнение useCase.
 */
public interface UseCaseExecutor {

    /**
     * UseCase executor with result
     *
     * @param useCase - use case
     * @param input - входящие данные
     * @param outputMapper - мапер, преобхазующий исходящие данные use case в модель результата
     * @param <ResultModel> - конечные данные. Например, данные, аозвращаемые по RestAPI
     * @param <Input> - входящие данные use case
     * @param <Output> - исходящие данные use case
     * @return
     */
    <ResultModel, Input extends UseCase.InputValues, Output extends UseCase.OutputValues> ResultModel execute(
            UseCase<Input, Output> useCase,
            Input input,
            Function<Output, ResultModel> outputMapper
    );

    /**
     * UseCase executor without result
     *
     * @param useCase
     * @param input
     * @param <Input>
     * @param <Output>
     */
    <Input extends UseCase.InputValues, Output extends UseCase.OutputValues> Output execute(
            UseCase<Input, Output> useCase,
            Input input
    );

}