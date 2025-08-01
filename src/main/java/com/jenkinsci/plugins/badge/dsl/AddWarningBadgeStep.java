/*
 * The MIT License
 *
 * Copyright (c) 2025, Badge Plugin Authors
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
package com.jenkinsci.plugins.badge.dsl;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import io.jenkins.plugins.ionicons.Ionicons;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Add an error badge.
 */
public class AddWarningBadgeStep extends AddBadgeStep {

    @DataBoundConstructor
    public AddWarningBadgeStep() {
        this(null, null, null, null);
    }

    protected AddWarningBadgeStep(String id, String text, String link, String target) {
        super(id, Ionicons.getIconClassName("warning"), text, null, "color: var(--warning-color)", link, target);
    }

    @Override
    public String toString() {
        List<String> fields = new ArrayList<>();

        if (getId() != null) {
            fields.add("id: '" + getId() + "'");
        }
        if (getText() != null) {
            fields.add("text: '" + getText() + "'");
        }
        if (getLink() != null) {
            fields.add("link: '" + getLink() + "'");
        }
        if (getTarget() != null) {
            fields.add("target: '" + getTarget() + "'");
        }

        return getDescriptor().getFunctionName() + "(" + StringUtils.join(fields, ", ") + ")";
    }

    @Extension
    public static class DescriptorImpl extends AbstractTaskListenerDescriptor {

        @Override
        public String getFunctionName() {
            return "addWarningBadge";
        }

        @NonNull
        @Override
        public String getDisplayName() {
            return "Add Warning Badge";
        }
    }
}
