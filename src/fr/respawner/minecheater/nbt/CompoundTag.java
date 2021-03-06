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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import fr.respawner.minecheater.Config;

public final class CompoundTag extends Tag {
    private final Map<String, Tag> value;

    public CompoundTag(String name, Map<String, Tag> value) {
        super(name);

        this.value = Collections.unmodifiableMap(value);
    }

    public List<short[]> toEnchantList() {
        final List<short[]> list;
        final ListTag listTag;

        list = new ArrayList<>();
        listTag = (ListTag) this.value.get("ench");

        for (Tag tag : listTag.getValue()) {
            final CompoundTag compound;
            final short[] enchant;

            compound = (CompoundTag) tag;
            enchant = new short[2];

            enchant[0] = ((ShortTag) compound.value.get("id")).getValue();
            enchant[1] = ((ShortTag) compound.value.get("lvl")).getValue();

            list.add(enchant);
        }

        return list;
    }

    @Override
    public Map<String, Tag> getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        final StringBuilder builder;

        builder = new StringBuilder();

        builder.append("TAG_Compound");

        if ((this.name != null) && !this.name.equals("")) {
            builder.append("(\"");
            builder.append(this.name);
            builder.append("\")");
        }

        builder.append(": ");
        builder.append(this.value.size());
        builder.append(" entries");
        builder.append(Config.LINE_SEPARATOR);
        builder.append("{");
        builder.append(Config.LINE_SEPARATOR);

        for (Map.Entry<String, Tag> entry : this.value.entrySet()) {
            builder.append("   ");
            builder.append(entry
                    .getValue()
                    .toString()
                    .replaceAll(Config.LINE_SEPARATOR,
                            Config.LINE_SEPARATOR + "   "));
            builder.append(Config.LINE_SEPARATOR);
        }

        builder.append("}");

        return builder.toString();
    }
}
