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
 * This interface is for autonomous direction of vehicles that operate in 
 * (effectively) two dimensions: ALVs (Autonomous Land Vehicles) and ASVs 
 * (Autonomous Sea/Surface Vehicles).
 */
public interface A4jBrain2D extends A4jBrain {

    A4jBrain2D forward(long distance);
    A4jBrain2D backward(long distance);
    A4jBrain2D left(long degrees);
    A4jBrain2D right(long degrees);
    
}
