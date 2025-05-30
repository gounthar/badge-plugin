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
import static org.junit.jupiter.api.Assertions.assertTrue;

import hudson.model.BuildBadgeAction;
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
class RemoveHtmlBadgesStepTest {

    private static JenkinsRule r;

    @BeforeAll
    static void setUp(JenkinsRule rule) {
        r = rule;
    }

    @Test
    void removeById() throws Exception {
        String badgeId = UUID.randomUUID().toString();
        AddHtmlBadgeStep addStep = createAddStep(badgeId);
        RemoveHtmlBadgesStep removeStep = createRemoveStep(badgeId);
        runRemoveJob(addStep, removeStep, 0);
    }

    @Test
    void removeAll() throws Exception {
        String badgeId = UUID.randomUUID().toString();
        AddHtmlBadgeStep addStep = createAddStep(badgeId);
        RemoveHtmlBadgesStep removeStep = createRemoveStep(null);
        runRemoveJob(addStep, removeStep, 0);
    }

    @Test
    void removeInvalidId() throws Exception {
        String badgeId = UUID.randomUUID().toString();
        AddHtmlBadgeStep addStep = createAddStep(badgeId);
        RemoveHtmlBadgesStep removeStep = createRemoveStep(UUID.randomUUID().toString());
        runRemoveJob(addStep, removeStep, 1);
    }

    @Test
    void deprecated() {
        RemoveHtmlBadgesStep removeStep = createRemoveStep(null);
        assertTrue(removeStep.getDescriptor().isAdvanced());
    }

    private static void runRemoveJob(AddHtmlBadgeStep addStep, RemoveHtmlBadgesStep removeStep, int expected)
            throws Exception {
        WorkflowJob project = r.createProject(WorkflowJob.class);

        String script = addStep.getDescriptor().getFunctionName() + "("
                + (addStep.getId() != null ? "id: '" + addStep.getId() + "', " : "") + "html: '" + addStep.getHtml()
                + "')\n";
        script += removeStep.getDescriptor().getFunctionName() + "("
                + (removeStep.getId() != null ? "id: '" + removeStep.getId() + "'" : "") + ")";

        project.setDefinition(new CpsFlowDefinition(script, true));
        WorkflowRun run = r.assertBuildStatusSuccess(project.scheduleBuild2(0));

        assertActionExists(run, expected);
    }

    private static void assertActionExists(WorkflowRun run, int expected) {
        List<BuildBadgeAction> badgeActions = run.getBadgeActions();
        assertEquals(expected, badgeActions.size());
    }

    private static AddHtmlBadgeStep createAddStep(String id) {
        AddHtmlBadgeStep step = new AddHtmlBadgeStep("Test Text");
        step.setId(id);
        return step;
    }

    private static RemoveHtmlBadgesStep createRemoveStep(String id) {
        RemoveHtmlBadgesStep step = new RemoveHtmlBadgesStep();
        step.setId(id);
        return step;
    }
}
