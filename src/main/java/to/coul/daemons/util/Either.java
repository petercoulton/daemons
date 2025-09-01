/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package to.coul.daemons.util;

import static to.coul.daemons.util.Either.*;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This is based on Either from vavr, but it's simplified and doesn't have
 * any dependencies on the other vavr constructs.
 *
 * @See <a href="https://github.com/vavr-io/vavr/blob/main/vavr/src/main/java/io/vavr/control/Either.java">Either.java</a>
 *
 */
public sealed interface Either<L, R> permits Left, Right {

    static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    /**
     * cond
     *
     */
    static <L, R> Either<L, R> cond(boolean test,
                                    Supplier<? extends R> right,
                                    Supplier<? extends L> left) {
        Objects.requireNonNull(right, "right is null");
        Objects.requireNonNull(right, "left is null");

        return test
               ? Either.right(right.get())
               : Either.left(left.get());
    }

    /**
     * cond
     *
     */
    static <L, R> Either<L, R> cond(boolean test,
                                    R right,
                                    L left) {
        Objects.requireNonNull(right, "right is null");
        Objects.requireNonNull(right, "left is null");

        return cond(test, () -> right, () -> left);
    }

    default L left() {
        throw new NoSuchElementException("Cannot get left value from right");
    }

    default R right() {
        throw new NoSuchElementException("Cannot get right value from left");
    }

    default boolean isLeft() {
        return this instanceof Left;
    }

    default boolean isRight() {
        return this instanceof Right;
    }

    /**
     * orElse
     *
     * @param other
     * @return
     */
    @SuppressWarnings("unchecked")
    default Either<L, R> orElse(Either<? extends L, ? extends R> other) {
        Objects.requireNonNull(other, "other is null");
        return orElse(() -> other);
    }

    /**
     * orElse
     *
     * @param other
     * @return
     */
    @SuppressWarnings("unchecked")
    default Either<L, R> orElse(Supplier<Either<? extends L, ? extends R>> other) {
        Objects.requireNonNull(other, "other is null");
        return isRight() ? this : (Either<L, R>) other.get();
    }

    /**
     * getOrElseGet
     *
     * @param other
     * @return
     */
    default R getOrElseGet(Function<? super L, ? extends R> other) {
        Objects.requireNonNull(other, "other is null");
        return isRight() ? right() : other.apply(left());
    }

    /**
     * orElseRun
     *
     * @param action
     */
    default void orElseRun(Consumer<? super L> action) {
        Objects.requireNonNull(action, "action is null");
        if (isLeft()) {
            action.accept(left());
        }
    }

    /**
     * orElseThrow
     *
     * @param exceptionFunction
     * @return
     * @param <X>
     * @throws X
     */
    default <X extends Throwable> R orElseThrow(Function<? super L, X> exceptionFunction) throws X {
        Objects.requireNonNull(exceptionFunction, "exceptionFunction is null");
        if (isLeft()) {
            throw exceptionFunction.apply(left());
        } else {
            return get();
        }
    }

    /**
     * get
     *
     * @return
     */
    default R get() {
        if (isLeft()) {
            throw new NoSuchElementException("get() called on Left");
        }

        return right();
    }

    /**
     * flatMap
     *
     * @param mapper
     * @return
     * @param <U>
     */
    @SuppressWarnings("unchecked")
    default <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isRight()) {
            return (Either<L, U>) mapper.apply(get());
        } else {
            return (Either<L, U>) this;
        }
    }

    /**
     * map
     *
     * @param mapper
     * @return
     * @param <U>
     */
    @SuppressWarnings("unchecked")
    default <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isRight()) {
            return Either.right(mapper.apply(get()));
        } else {
            return (Either<L, U>) this;
        }
    }

    /**
     * mapLeft
     *
     * @param mapper
     * @return
     * @param <U>
     */
    @SuppressWarnings("unchecked")
    default <U> Either<U, R> mapLeft(Function<? super L, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isLeft()) {
            return Either.left(mapper.apply(left()));
        } else {
            return (Either<U, R>) this;
        }
    }

    /**
     * filter
     *
     * @param predicate
     * @return
     */
    default Optional<Either<L, R>> filter(Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return isLeft() || predicate.test(get())
               ? Optional.of(this)
               : Optional.empty();
    }

    /**
     * filterOrElse
     *
     * @param predicate
     * @param zero
     * @return
     */
    default Either<L, R> filterOrElse(Predicate<? super R> predicate,
                                      Function<? super R, ? extends L> zero) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(zero, "zero is null");
        return isLeft() || predicate.test(get())
               ? this
               : Either.left(zero.apply(get()));
    }


    record Left<L, R>(L left) implements Either<L, R> {
        public Left {
            Objects.requireNonNull(left, "left is null");
        }
    }

    record Right<L, R>(R right) implements Either<L, R> {
        public Right {
            Objects.requireNonNull(right, "right is null");
        }
    }
}
