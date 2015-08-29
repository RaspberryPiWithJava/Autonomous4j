/*
 * The MIT License
 *
 * Copyright 2015 Mark A. Heckler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.autonomous4j.interfaces;

import java.util.List;
import org.autonomous4j.tracking.A4jBlackBox;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 * 
 * This interface is an abstraction for autonomous direction of vehicles, 
 * regardless of medium.
 */
public interface A4jBrain {

    boolean connect();
    void disconnect();

    A4jBrain doFor(long ms);
    A4jBrain hold(long ms);
    A4jBrain stay();
    
    A4jBrain goHome();
    A4jBrain replay();   

    void processRecordedMovements(List<A4jBlackBox.Movement> moves);
    
}
