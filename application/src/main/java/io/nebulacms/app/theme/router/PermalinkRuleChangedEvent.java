package io.nebulacms.app.theme.router;

import io.nebulacms.app.theme.DefaultTemplateEnum;

import org.springframework.context.ApplicationEvent;

public class PermalinkRuleChangedEvent extends ApplicationEvent {
    private final DefaultTemplateEnum template;
    private final String oldRule;
    private final String rule;

    public PermalinkRuleChangedEvent(Object source, DefaultTemplateEnum template,
        String oldRule, String rule) {
        super(source);
        this.template = template;
        this.oldRule = oldRule;
        this.rule = rule;
    }

    public DefaultTemplateEnum getTemplate() {
        return template;
    }

    public String getOldRule() {
        return oldRule;
    }

    public String getRule() {
        return rule;
    }
}
