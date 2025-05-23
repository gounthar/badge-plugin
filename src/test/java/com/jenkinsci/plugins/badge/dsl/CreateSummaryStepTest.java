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

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jenkinsci.plugins.badge.action.BadgeSummaryAction;
import hudson.markup.RawHtmlMarkupFormatter;
import java.util.List;
import java.util.UUID;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@Deprecated(since = "2.0", forRemoval = true)
class CreateSummaryStepTest {

    private static JenkinsRule r;

    @BeforeAll
    static void setUp(JenkinsRule rule) {
        r = rule;
    }

    @Test
    void id() {
        CreateSummaryStep step = new CreateSummaryStep(null);
        assertNull(step.getId());

        String id = UUID.randomUUID().toString();
        step.setId(id);
        assertEquals(id, step.getId());
    }

    @Test
    void icon() {
        CreateSummaryStep step = new CreateSummaryStep(null);
        assertNull(step.getIcon());

        String icon = UUID.randomUUID().toString();
        step = new CreateSummaryStep(icon);
        assertEquals(icon, step.getIcon());
    }

    @Test
    void text() {
        CreateSummaryStep step = new CreateSummaryStep(null);
        assertNull(step.getText());

        String text = UUID.randomUUID().toString();
        step.setText(text);
        assertEquals(text, step.getText());
    }

    @Test
    void deprecated() {
        CreateSummaryStep step = new CreateSummaryStep(null);
        assertTrue(step.getDescriptor().isAdvanced());
    }

    @Test
    void createSummary_plain() throws Exception {
        String text = randomUUID().toString();
        BadgeSummaryAction action = createSummary("summary.appendText('" + text + "')");
        assertEquals(text, action.getText());
    }

    @Test
    void createSummary_html_unescaped() throws Exception {
        r.jenkins.setMarkupFormatter(RawHtmlMarkupFormatter.INSTANCE);
        String text = randomUUID().toString();
        BadgeSummaryAction action = createSummary("summary.appendText('<ul><li>" + text + "</li></ul>', false)");
        assertEquals("<ul><li>" + text + "</li></ul>", action.getText());
    }

    @Test
    void createSummary_html_unescaped_remove_script() throws Exception {
        r.jenkins.setMarkupFormatter(RawHtmlMarkupFormatter.INSTANCE);
        String text = randomUUID().toString();
        String html = "<ul><li>" + text + "</li></ul><script>alert(\"exploit!\");</script>";
        BadgeSummaryAction action = createSummary("summary.appendText('" + html + "', false);");
        assertEquals("<ul><li>" + text + "</li></ul>", action.getText());
    }

    @Test
    void createSummary_html_escaped() throws Exception {
        r.jenkins.setMarkupFormatter(RawHtmlMarkupFormatter.INSTANCE);
        String text = randomUUID().toString();
        BadgeSummaryAction action = createSummary("summary.appendText('<ul><li>" + text + "</li></ul>', true)");
        assertEquals("&lt;ul&gt;&lt;li&gt;" + text + "&lt;/li&gt;&lt;/ul&gt;", action.getText());
    }

    @Test
    void createSummary_all() throws Exception {
        r.jenkins.setMarkupFormatter(RawHtmlMarkupFormatter.INSTANCE);
        String text = randomUUID().toString();
        BadgeSummaryAction action = createSummary("summary.appendText('" + text + "', false, true, true, 'grey')");
        assertEquals("<b><i>" + text + "</i></b>", action.getText());
    }

    @Test
    void createSummary_with_text() throws Exception {
        String icon = randomUUID().toString();
        String text = randomUUID().toString();

        WorkflowJob p = r.createProject(WorkflowJob.class);
        p.setDefinition(new CpsFlowDefinition(
                "def summary = createSummary(icon:\"" + icon + "\", text:\"" + text + "\")", true));
        WorkflowRun b = r.assertBuildStatusSuccess(p.scheduleBuild2(0));
        List<BadgeSummaryAction> summaryActions = b.getActions(BadgeSummaryAction.class);
        assertEquals(1, summaryActions.size());

        BadgeSummaryAction action = summaryActions.get(0);
        assertTrue(action.getIcon().endsWith(icon));
        assertEquals(text, action.getText());
    }

    private static BadgeSummaryAction createSummary(String script) throws Exception {
        String icon = randomUUID().toString();

        WorkflowJob p = r.createProject(WorkflowJob.class);
        p.setDefinition(new CpsFlowDefinition("def summary = createSummary(\"" + icon + "\")\n" + script, true));
        WorkflowRun b = r.assertBuildStatusSuccess(p.scheduleBuild2(0));
        List<BadgeSummaryAction> summaryActions = b.getActions(BadgeSummaryAction.class);
        assertEquals(1, summaryActions.size());

        BadgeSummaryAction action = summaryActions.get(0);
        assertTrue(action.getIcon().endsWith(icon));
        return action;
    }
}
