/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
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
 * DISCLAIMED. IN NO EVENT SHALL DataFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.core.concurrent;

/**
 * A interface that adds support for <tt>TaskStateHandler</tt>
 *
 * @author Hendrik Ebbers
 * @see TaskStateHandler
 */
public interface TaskWithStateHandler {
    /**
     * This method will be called by DataFX to inject a
     * <code>TaskStateHandler</code> in this callable. The handler can be used
     * to provide some more feedback of this callable.
     *
     * @param stateHandler the injected TaskStateHandler
     */
    default void injectStateHandler(TaskStateHandler stateHandler) {
        TaskStateHandlerManager.add(this, stateHandler);
    }

    /**
     * Returns the <tt>TaskStateHandler</tt> for this instance
     * @return the <tt>TaskStateHandler</tt> for this instance
     */
    default TaskStateHandler getStateHandler() {
        return TaskStateHandlerManager.get(this);
    }

    public default void updateTaskTitle(String title) {
        getStateHandler().updateTaskTitle(title);
    }

    default void updateTaskMessage(String message){
        getStateHandler().updateTaskMessage(message);
    }

    default void updateTaskProgress(double workDone, double max){
        getStateHandler().updateTaskProgress(workDone, max);
    }

    default void updateTaskProgress(long workDone, long max){
        getStateHandler().updateTaskProgress(workDone, max);
    }

    default void setCancelable(boolean cancelable){
        getStateHandler().setCancelable(cancelable);
    }
}
