/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.controller.flow.action;

import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.core.DataFXUtils;
import io.datafx.core.concurrent.Async;
import io.datafx.core.concurrent.ObservableExecutor;

import java.lang.reflect.Method;

/**
 * Implementation of a {@link FlowAction} that calls a method in the current view controller instance.
 */
public class FlowMethodAction implements FlowAction {

    private Method actionMethod;

    /**
     * Default constructor
     *
     * @param actionMethod defines the method that should be called whenever the action is triggered.
     */
    public FlowMethodAction(Method actionMethod) {
        this.actionMethod = actionMethod;
    }

    @Override
    public void handle(FlowHandler flowHandler, String actionId) throws FlowException {
        Object controller = flowHandler.getCurrentViewContext().getController();
        try {
            if (actionMethod.isAnnotationPresent(Async.class)) {
                ObservableExecutor.getDefaultInstance().execute(() -> {
                    try {
                        DataFXUtils.callPrivileged(actionMethod, controller);
                    } catch (Exception e) {
                        flowHandler.getExceptionHandler().setException(e);
                    }
                });
            } else {
                DataFXUtils.callPrivileged(actionMethod, controller);
            }
        } catch (Exception e) {
            throw new FlowException(e);
        }

    }
}
