/*
 * Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.greenrobot.eventbus;

import java.lang.reflect.Method;

/**
 * Used internally by EventBus and generated subscriber indexes.
 * 存储器的作用
 * 保存了带有EventBus规定注解的方法，方法的参数类型(即发送的event类型)， 注解的处理类型， 优先级，是否粘性
 */
public class SubscriberMethod {
    final Method method;
    /*** 事件处理的线程模式 */
    final ThreadMode threadMode;
    /*** 方法的参数类型(即发送的event事件类型)*/
    final Class<?> eventType;
    /*** 优先级 */
    final int priority;
    /*** 是否为粘性 */
    final boolean sticky;
    /** Used for efficient comparison */
    String methodString;

    /**
     *
     * @param method 带有EventBus规定注解的方法
     * @param eventType 方法的参数类型(即发送的event事件类型)
     * @param threadMode 注解的处理类型
     * @param priority 优先级
     * @param sticky 是否粘性
     */
    public SubscriberMethod(Method method, Class<?> eventType, ThreadMode threadMode, int priority, boolean sticky) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.priority = priority;
        this.sticky = sticky;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof SubscriberMethod) {
            checkMethodString();
            SubscriberMethod otherSubscriberMethod = (SubscriberMethod)other;
            otherSubscriberMethod.checkMethodString();
            // Don't use method.equals because of http://code.google.com/p/android/issues/detail?id=7811#c6
            return methodString.equals(otherSubscriberMethod.methodString);
        } else {
            return false;
        }
    }

    private synchronized void checkMethodString() {
        if (methodString == null) {
            // Method.toString has more overhead, just take relevant parts of the method
            StringBuilder builder = new StringBuilder(64);
            builder.append(method.getDeclaringClass().getName());
            builder.append('#').append(method.getName());
            builder.append('(').append(eventType.getName());
            methodString = builder.toString();
        }
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}