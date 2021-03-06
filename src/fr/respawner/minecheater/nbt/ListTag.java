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
package fr.respawner.minecheater.nbt;

import java.util.Collections;
import java.util.List;

import fr.respawner.minecheater.Config;

public final class ListTag extends Tag {
    private final Class<? extends Tag> type;
    private final List<Tag> value;

    public ListTag(String name, Class<? extends Tag> type, List<Tag> value) {
        super(name);

        this.type = type;
        this.value = Collections.unmodifiableList(value);
    }

    public Class<? extends Tag> getType() {
        return this.type;
    }

    @Override
    public List<Tag> getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        final StringBuilder builder;

        builder = new StringBuilder();

        builder.append("TAG_List");

        if ((this.name != null) && !this.name.equals("")) {
            builder.append("(\"");
            builder.append(this.name);
            builder.append("\")");
        }

        builder.append(": ");
        builder.append(this.value.size());
        builder.append(" entries of type ");
        builder.append(Utils.getTypeName(this.type));
        builder.append(Config.LINE_SEPARATOR);
        builder.append("{");
        builder.append(Config.LINE_SEPARATOR);

        for (Tag tag : this.value) {
            builder.append("   ");
            builder.append(tag.toString().replaceAll(Config.LINE_SEPARATOR,
                    Config.LINE_SEPARATOR + "   "));
            builder.append(Config.LINE_SEPARATOR);
        }

        builder.append("}");

        return builder.toString();
    }
}
