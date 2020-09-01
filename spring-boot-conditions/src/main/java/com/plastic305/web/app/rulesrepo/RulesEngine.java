package com.plastic305.web.app.rulesrepo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.plastic305.web.app.services.IAttributeService;

@Component
public class RulesEngine {
	@Autowired
	IAttributeService attService;

	public static final String msg = "Lo sentimos esa solución no se encuentra en el sistema";

	private ArrayList<IRules> rules;

	public RulesEngine() {
		rules = new ArrayList<IRules>();
		rules.add(new Rule1());
        rules.add(new Rule2());
        rules.add(new Rule9());
        rules.add(new Rule3());
        rules.add(new Rule4());
        rules.add(new Rule5());
        rules.add(new Rule6());
        rules.add(new Rule7());
        rules.add(new Rule8());
        rules.add(new Rule10());
        rules.add(new Rule11());
	}

	public void addRule(IRules rule) {
		rules.add(rule);
	}

	public String executeRules() {
		for (IRules rule : rules) {
			String ruleResult = rule.evaluate(attService);
			switch (ruleResult.charAt(0)) {
				case '_':
					return "A" + ruleResult/*.substring(1, ruleResult.length() - 1)*/;

				case 'T':
					return "R_" + rule.getAction();
				}
		}
		return msg;
	}
}
