/*
 * Copyright (c) 2012 Guillaume Mazoyer
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.respawner.minecheater.math.unsigned;

public final class UShort extends Number implements Comparable<UShort> {
    private static final long serialVersionUID = -7986013314682784760L;

    public static final int MIN_VALUE = 0x0000;
    public static final int MAX_VALUE = 0xFFFF;

    private final int value;

    public UShort(short value) {
        this.value = (short) (value & MAX_VALUE);
    }

    public UShort(int value) throws NumberFormatException {
        this.rangeCheck(value);
        this.value = value;
    }

    private void rangeCheck(int value) throws NumberFormatException {
        if ((value < MIN_VALUE) || (value > MAX_VALUE)) {
            throw new NumberFormatException("Value is out of range: " + value);
        }
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return this.value;
    }

    @Override
    public int intValue() {
        return this.value;
    }

    @Override
    public long longValue() {
        return this.value;
    }

    @Override
    public int compareTo(UShort o) {
        return (this.value < o.value ? -1 : (this.value == o.value ? 0 : 1));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UShort) {
            return (this.value == ((UShort) obj).value);
        }

        return false;

    }

    @Override
    public String toString() {
        return Integer.valueOf(this.value).toString();
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(this.value).hashCode();
    }
}
