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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class AddErrorBadgeStepTest extends AddBadgeStepTest {

    @Override
    @Test
    void defaultConstructor() {
        AbstractAddBadgeStep step = new AddErrorBadgeStep();
        assertNull(step.getId());
        assertEquals("symbol-remove-circle plugin-ionicons-api", step.getIcon());
        assertNull(step.getText());
        assertNull(step.getCssClass());
        assertEquals("color: var(--error-color)", step.getStyle());
        assertNull(step.getLink());
        assertNull(step.getTarget());
    }

    @Override
    @Test
    void icon() {
        AbstractAddBadgeStep step = createStep("id", null, "text", "cssClass", "style", "link", "target");
        assertEquals("symbol-remove-circle plugin-ionicons-api", step.getIcon());

        step = createStep("id", "", "text", "cssClass", "style", "link", "target");
        assertEquals("symbol-remove-circle plugin-ionicons-api", step.getIcon());

        step = createStep("id", "icon", "text", "cssClass", "style", "link", "target");
        assertEquals("symbol-remove-circle plugin-ionicons-api", step.getIcon());
    }

    @Override
    @Test
    void cssClass() {
        AbstractAddBadgeStep step = createStep("id", "icon", "text", null, "style", "link", "target");
        assertNull(step.getCssClass());

        step = createStep("id", "icon", "text", "", "style", "link", "target");
        assertNull(step.getCssClass());

        step = createStep("id", "icon", "text", "cssClass", "style", "link", "target");
        assertNull(step.getCssClass());
    }

    @Override
    @Test
    void style() {
        AbstractAddBadgeStep step = createStep("id", "icon", "text", "cssClass", null, "link", "target");
        assertEquals("color: var(--error-color)", step.getStyle());

        step = createStep("id", "icon", "text", "cssClass", "", "link", "target");
        assertEquals("color: var(--error-color)", step.getStyle());

        step = createStep("id", "icon", "text", "cssClass", "style", "link", "target");
        assertEquals("color: var(--error-color)", step.getStyle());
    }

    @Override
    protected AbstractAddBadgeStep createStep(
            String id, String icon, String text, String cssClass, String style, String link, String target) {
        return new AddErrorBadgeStep(id, text, link, target);
    }
}
