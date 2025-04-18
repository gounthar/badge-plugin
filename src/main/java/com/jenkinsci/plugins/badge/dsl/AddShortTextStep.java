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

import com.jenkinsci.plugins.badge.action.AbstractBadgeAction;
import com.jenkinsci.plugins.badge.action.BadgeAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.PrintStream;
import java.io.Serial;
import java.io.Serializable;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Create a short text.
 *
 * @deprecated replaced by {@link AddBadgeStep}.
 */
@Deprecated(since = "2.0", forRemoval = true)
public class AddShortTextStep extends Step {

    private final ShortText shortText;

    @DataBoundConstructor
    public AddShortTextStep(String text) {
        this.shortText = new ShortText(text);
    }

    public String getText() {
        return shortText.getText();
    }

    public String getColor() {
        return shortText.getColor();
    }

    @DataBoundSetter
    public void setColor(String color) {
        this.shortText.setColor(color);
    }

    public String getBackground() {
        return shortText.getBackground();
    }

    @DataBoundSetter
    public void setBackground(String background) {
        this.shortText.setBackground(background);
    }

    public Integer getBorder() {
        return shortText.getBorder();
    }

    @DataBoundSetter
    public void setBorder(Integer border) {
        this.shortText.setBorder(border);
    }

    public String getBorderColor() {
        return shortText.getBorderColor();
    }

    @DataBoundSetter
    public void setBorderColor(String borderColor) {
        this.shortText.setBorderColor(borderColor);
    }

    @DataBoundSetter
    public void setLink(String link) {
        this.shortText.setLink(link);
    }

    public String getLink() {
        return this.shortText.getLink();
    }

    @Override
    public StepExecution start(StepContext context) {
        return new Execution(shortText, context);
    }

    @Extension
    @Deprecated(since = "2.0", forRemoval = true)
    public static class DescriptorImpl extends AbstractTaskListenerDescriptor {

        @Override
        public String getFunctionName() {
            return "addShortText";
        }

        @NonNull
        @Override
        public String getDisplayName() {
            return "Add Short Text";
        }

        @Override
        public boolean isAdvanced() {
            return true;
        }
    }

    @Deprecated(since = "2.0", forRemoval = true)
    private static class ShortText implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private final String text;
        private String color;
        private String background;
        private Integer border;
        private String borderColor;
        private String link;

        public ShortText(String text) {
            this.text = text;
        }

        private String getText() {
            return text;
        }

        private String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        private String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        private Integer getBorder() {
            return border;
        }

        public void setBorder(Integer border) {
            this.border = border;
        }

        private String getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(String borderColor) {
            this.borderColor = borderColor;
        }

        private String getBorderString() {
            return border == null ? null : border + "px";
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    @Deprecated(since = "2.0", forRemoval = true)
    public static class Execution extends SynchronousStepExecution<Void> {

        @Serial
        private static final long serialVersionUID = 1L;

        @SuppressFBWarnings(value = "SE_TRANSIENT_FIELD_NOT_RESTORED", justification = "Only used when starting.")
        private final transient ShortText shortText;

        Execution(ShortText shortText, StepContext context) {
            super(context);
            this.shortText = shortText;
        }

        @Override
        protected Void run() throws Exception {
            // translate old styling to new field
            String style = "border: " + (shortText.getBorderString() != null ? shortText.getBorderString() : "")
                    + " solid " + (shortText.getBorderColor() != null ? shortText.getBorderColor() : "") + ";";
            if (shortText.getBackground() != null) {
                style += "background: " + shortText.getBackground() + ";";
            }
            if (shortText.getColor() != null) {
                if (shortText.getColor().startsWith("jenkins-!-color")) {
                    style += "color: var(--" + shortText.getColor().replaceFirst("jenkins-!-color", "") + ");";
                } else if (shortText.getColor().startsWith("jenkins-!-")) {
                    style += "color: var(--" + shortText.getColor().replaceFirst("jenkins-!-", "") + ");";
                } else {
                    style += "color: " + AbstractBadgeAction.getJenkinsColorStyle(shortText.getColor()) + ";";
                }
            }

            BadgeAction action =
                    new BadgeAction(null, null, shortText.getText(), null, style, shortText.getLink(), null);
            getContext().get(Run.class).addAction(action);

            TaskListener listener = getContext().get(TaskListener.class);
            PrintStream logger = listener.getLogger();
            logger.println("Step 'addShortText' is deprecated - please consider using 'addBadge' instead.");

            return null;
        }
    }
}
